package me.bramhaag.mcpcserver.server.protocol.type;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public interface IType<T> {
    T read(@NotNull ByteBuf buf);
    void write(@NotNull ByteBuf buf, @NotNull T value);
}
