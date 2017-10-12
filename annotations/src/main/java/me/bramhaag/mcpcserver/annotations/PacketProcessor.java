package me.bramhaag.mcpcserver.annotations;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.MethodSpec;
import me.bramhaag.mcpcserver.annotations.packets.IgnoreVariable;
import me.bramhaag.mcpcserver.annotations.packets.Packet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedSourceVersion(SourceVersion.RELEASE_9)
@SupportedAnnotationTypes("me.bramhaag.mcpcserver.annotations.packets.Packet")
@AutoService(Processor.class)
public class PacketProcessor extends AbstractProcessor {

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element e : roundEnv.getElementsAnnotatedWith(Packet.class)) {
            if(e.getKind() != ElementKind.CLASS) {
                messager.printMessage(Diagnostic.Kind.ERROR,
                        String.format("Cannot annotate '%s' with annotation %s, only classes can be annotated with this annotation",
                                e.getSimpleName(), Packet.class.getSimpleName()), e);

                return true;
            }

            TypeElement typeElement = (TypeElement) e;
            write(typeElement);
        }

        return false;
    }

    private void write(TypeElement typeElement) {

    }
}
