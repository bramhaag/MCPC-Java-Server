package me.bramhaag.mcpcserver.protocol.types;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import org.jetbrains.annotations.NotNull;

public class TypeString implements IType<String> {

    @Override
    public String read(@NotNull ByteBuf buf) {
        byte[] data = new byte[Type.VAR_INT.read(buf)];
        buf.readBytes(data);
        return new String(data, CharsetUtil.UTF_8);
    }

    @Override
    public void write(@NotNull ByteBuf buf, @NotNull String value) {
        //TODO
    }
}
