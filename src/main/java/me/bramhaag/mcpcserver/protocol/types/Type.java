package me.bramhaag.mcpcserver.protocol.types;

import io.netty.buffer.ByteBuf;
import me.bramhaag.mcpcserver.annotations.packets.Packet;

import java.util.HashMap;
import java.util.Map;

public class Type {
    public static final TypeByteArray BYTE_ARRAY = new TypeByteArray();
    public static final TypeShort SHORT = new TypeShort();
    public static final TypeString STRING = new TypeString();
    public static final TypeVarInt VAR_INT = new TypeVarInt();
    public static final TypeVarLong VAR_LONG = new TypeVarLong();

    public static final Encoder ENCODER = new Encoder();
    public static final Decoder DECODER = new Decoder();

    public static class Encoder {
        private Map<Class<?>, ITypeEncoder<?>> encoders = new HashMap<>();

        private Encoder() {
            encoders.put(int.class, (ITypeEncoder<Integer>) VAR_INT::write);
            encoders.put(long.class, (ITypeEncoder<Long>) VAR_LONG::write);
            encoders.put(short.class, (ITypeEncoder<Short>) SHORT::write);
            encoders.put(String.class, (ITypeEncoder<String>) STRING::write);
            encoders.put(Packet.State.class, (ITypeEncoder<Packet.State>) (buf, value) -> VAR_INT.write(buf, value.getId()));
        }

//        public <T> void encode(Class<T> type, T value, ByteBuf buf) {
//            ((ITypeEncoder<T>)encoders.get(type)).encode(buf, value);
//        }

        public <T> void encode(Object value, ByteBuf buf) {
            ((ITypeEncoder<T>)encoders.get(value.getClass())).encode(buf, ((T) value));
        }


    }

    public static class Decoder {
        private Map<Class<?>, ITypeDecoder<?>> decoders = new HashMap<>();

        private Decoder() {
            decoders.put(int.class, (ITypeDecoder<Integer>) Type.VAR_INT::read);
            decoders.put(long.class, (ITypeDecoder<Long>) Type.VAR_LONG::read);
            decoders.put(short.class, (ITypeDecoder<Short>) Type.SHORT::read);
            decoders.put(String.class, (ITypeDecoder<String>) Type.STRING::read);
            decoders.put(Packet.State.class, (ITypeDecoder<Packet.State>) buf -> Packet.State.getById(Type.VAR_INT.read(buf)));
        }

        public <T> T decode(Class<T> type, ByteBuf buf) {
            return (T)decoders.get(type).decode(buf);
        }
    }
}
