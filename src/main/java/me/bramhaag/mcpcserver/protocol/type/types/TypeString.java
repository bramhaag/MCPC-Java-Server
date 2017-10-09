package me.bramhaag.mcpcserver.protocol.type.types;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import me.bramhaag.mcpcserver.protocol.type.IType;
import me.bramhaag.mcpcserver.protocol.type.Type;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class TypeString implements IType<String> {

    @Override
    public String read(@NotNull ByteBuf buf) {
        byte[] data = new byte[Type.VAR_INT.read(buf)];
        buf.readBytes(data);
        return new String(data, CharsetUtil.UTF_8);
    }

    @Override
    public void write(@NotNull ByteBuf buf, @NotNull String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        Type.VAR_INT.write(buf, bytes.length);
        buf.writeBytes(bytes);
    }
}
