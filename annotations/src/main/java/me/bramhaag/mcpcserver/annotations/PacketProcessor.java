package me.bramhaag.mcpcserver.annotations;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import me.bramhaag.mcpcserver.annotations.packets.Packet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedSourceVersion(SourceVersion.RELEASE_9)
@SupportedAnnotationTypes("me.bramhaag.mcpcserver.annotations.packets.Packet")
@AutoService(Processor.class)
public class PacketProcessor extends AbstractProcessor {

    private static final ClassName CLASSNAME_BYTEBUF = ClassName.get("io.netty.buffer", "ByteBuf");
    private static final ClassName CLASSNAME_TYPE = ClassName.get("me.bramhaag.mcpcserver.server.protocol.type", "Type");

    private Map<Class<?>, Packet> packets;

    private Messager messager;
    private Elements elementsUtil;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        elementsUtil = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element e : roundEnv.getElementsAnnotatedWith(Packet.class)) {
            if (e.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        String.format("Cannot annotate '%s' with annotation %s, only classes can be annotated with this annotation",
                                e.getSimpleName(), Packet.class.getSimpleName()), e);

                return true;
            }

            try {
                write(e.getAnnotation(Packet.class), e);
            } catch (IOException ex) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Cannot write " + e.getSimpleName(), e);
                ex.printStackTrace();

                return true;
            }
        }

        return false;
    }

    private void write(Packet annotation, Element element) throws IOException {
        String className = element.getSimpleName().toString();
        String packageName = elementsUtil.getPackageOf(element).getQualifiedName().toString();

        String classNameImpl = className + "Impl";

        TypeSpec.Builder implClass = TypeSpec.classBuilder(classNameImpl)
                .addModifiers(Modifier.PUBLIC)
                .superclass(ClassName.get(packageName, className));

        MethodSpec.Builder encode = generateMethodBuilder("encode");
        MethodSpec.Builder decode = generateMethodBuilder("decode");

        List<Element> fields = element.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .peek(e -> implClass.addMethod(createGetter(e)))
                .collect(Collectors.toList());

        if(annotation.type() == Packet.Type.IN) { //decode
            encode.addStatement("throw new $T($S)",
                    UnsupportedOperationException.class, "Cannot encode incoming packet!");

            fields.forEach(e -> generateFieldDecoder(e, decode));
        } else if (annotation.type() == Packet.Type.OUT) { //encode
            decode.addStatement("throw new $T($S)",
                    UnsupportedOperationException.class, "Cannot decode outgoing packet!");

            MethodSpec.Builder constructor = MethodSpec.constructorBuilder();

            fields.forEach(e -> {
                String name = e.getSimpleName().toString();
                constructor.addParameter(TypeName.get(e.asType()), name);
                constructor.addStatement("this.$N = $N", name, name);

                generateFieldEncoder(e, encode);
            });

            implClass.addMethod(constructor.build());
        }

        implClass.addMethod(decode.build());
        implClass.addMethod(encode.build());

        JavaFile.builder(elementsUtil.getPackageOf(element).getQualifiedName().toString(), implClass.build())
                .build()
                .writeTo(filer);
    }

    private MethodSpec createGetter(Element element) {
        String fieldName = element.getSimpleName().toString();
        String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        return MethodSpec.methodBuilder("get" + capitalizedFieldName)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.get(element.asType()))
                .addStatement("return " + fieldName)
                .build();
    }

    private MethodSpec.Builder generateMethodBuilder(String name) {
        return MethodSpec.methodBuilder(name)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(CLASSNAME_BYTEBUF, "buf");
    }

    private void generateFieldEncoder(Element element, MethodSpec.Builder builder) {
        builder.addStatement("$T.ENCODER.encode($N, buf)", CLASSNAME_TYPE, element.getSimpleName());
    }

    private void generateFieldDecoder(Element element, MethodSpec.Builder builder) {
        String fieldName = element.getSimpleName().toString();
        TypeName type = TypeName.get(element.asType());

        builder.addStatement("$N = $T.DECODER.decode($N.class, buf)", fieldName, CLASSNAME_TYPE, type.toString());
    }
}
