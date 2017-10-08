package me.bramhaag.mcpcserver.protocol.resolver;

import io.netty.buffer.ByteBuf;
import me.bramhaag.mcpcserver.protocol.packets.PacketState;
import me.bramhaag.mcpcserver.protocol.types.Type;

import java.util.HashMap;
import java.util.Map;

public class TypeResolver {

    private Map<Class<?>, ITypeResolver<?>> resolvers = new HashMap<>();

    private TypeResolver() {
        resolvers.put(int.class, (ITypeResolver<Integer>) buf -> (Integer)Type.VAR_INT.read(buf));
        resolvers.put(long.class, (ITypeResolver<Long>) buf -> (Long) Type.VAR_LONG.read(buf));
        resolvers.put(short.class, (ITypeResolver<Short>) buf -> (Short) Type.SHORT.read(buf));
        resolvers.put(String.class, (ITypeResolver<String>) buf -> (String) Type.STRING.read(buf));
        resolvers.put(PacketState.class, (ITypeResolver<PacketState>) buf -> (PacketState) PacketState.getById((Integer)Type.VAR_INT.read(buf)));
    }

    public <T> T resolve(Class<T> type, ByteBuf buf) {
        return (T)resolvers.get(type).resolve(buf);
    }

    private static class LazyHolder {
        private static final TypeResolver INSTANCE = new TypeResolver();
    }

    public static TypeResolver getInstance() {
        return TypeResolver.LazyHolder.INSTANCE;
    }
}
