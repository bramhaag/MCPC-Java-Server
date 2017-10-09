package me.bramhaag.mcpcserver.protocol.type.types;

import io.netty.buffer.ByteBuf;
import me.bramhaag.mcpcserver.protocol.type.IType;
import org.jetbrains.annotations.NotNull;

public class TypeShort implements IType<Short> {
    @Override
    public Short read(@NotNull ByteBuf buf) {
        return (short)buf.readUnsignedShort();
    }

    @Override
    public void write(@NotNull ByteBuf buf, @NotNull Short value) {
        buf.writeShort(value);
    }
}
