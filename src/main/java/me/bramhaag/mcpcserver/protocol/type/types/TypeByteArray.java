package me.bramhaag.mcpcserver.protocol.type.types;

import io.netty.buffer.ByteBuf;
import me.bramhaag.mcpcserver.protocol.type.IType;
import me.bramhaag.mcpcserver.protocol.type.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.IntStream;

public class TypeByteArray implements IType<Byte[]> {
    @Override
    public Byte[] read(@NotNull ByteBuf buf) {
        byte[] temp = new byte[Type.VAR_INT.read(buf)];
        buf.readBytes(temp);

        Byte[] bytes = new Byte[temp.length];
        Arrays.setAll(bytes, i -> temp[i]);

        return bytes;
    }

    @Override
    public void write(@NotNull ByteBuf buf, @NotNull Byte[] value) {
        Type.VAR_INT.write(buf, value.length);

        byte[] bytes = new byte[value.length];
        IntStream.range(0, value.length).forEach(i -> bytes[i] = value[i]);

        buf.writeBytes(bytes);
    }
}
