package me.bramhaag.mcpcserver.protocol.resolver;

import io.netty.buffer.ByteBuf;
import me.bramhaag.mcpcserver.annotations.packets.Packet;
import me.bramhaag.mcpcserver.protocol.types.Type;

import java.util.HashMap;
import java.util.Map;

public class TypeResolver {

    private Map<Class<?>, ITypeResolver<?>> resolvers = new HashMap<>();

    public TypeResolver() {
        resolvers.put(int.class, (ITypeResolver<Integer>) Type.VAR_INT::read);
        resolvers.put(long.class, (ITypeResolver<Long>) Type.VAR_LONG::read);
        resolvers.put(short.class, (ITypeResolver<Short>) Type.SHORT::read);
        resolvers.put(String.class, (ITypeResolver<String>) Type.STRING::read);
        resolvers.put(Packet.State.class, (ITypeResolver<Packet.State>) buf -> Packet.State.getById(Type.VAR_INT.read(buf)));
    }

    public <T> T resolve(Class<T> type, ByteBuf buf) {
        return (T)resolvers.get(type).resolve(buf);
    }
}
