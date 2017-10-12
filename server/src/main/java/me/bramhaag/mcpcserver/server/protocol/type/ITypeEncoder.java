package me.bramhaag.mcpcserver.server.protocol.type;

import io.netty.buffer.ByteBuf;

@FunctionalInterface
public interface ITypeEncoder<T> {
    void encode(ByteBuf buf, T value);
}
