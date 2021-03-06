package me.bramhaag.mcpcserver.server.protocol.type;

import io.netty.buffer.ByteBuf;

@FunctionalInterface
public interface ITypeDecoder<T> {
    T decode(ByteBuf buf);
}
