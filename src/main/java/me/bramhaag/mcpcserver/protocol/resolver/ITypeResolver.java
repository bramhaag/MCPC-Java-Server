package me.bramhaag.mcpcserver.protocol.resolver;

import io.netty.buffer.ByteBuf;

@FunctionalInterface
public interface ITypeResolver<T> {
    T resolve(ByteBuf buf);
}
