package me.bramhaag.mcpcserver.protocol.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.bramhaag.mcpcserver.annotations.packets.IgnoreVariable;
import me.bramhaag.mcpcserver.protocol.NetworkManager;
import me.bramhaag.mcpcserver.protocol.resolver.TypeResolver;
import me.bramhaag.mcpcserver.protocol.types.Type;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    private static final TypeResolver resolver = new TypeResolver();
    private InetSocketAddress address;

    public PacketDecoder(InetSocketAddress address) {
        this.address = address;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        int length = Type.VAR_INT.read(in);
        int id = Type.VAR_INT.read(in);

        System.out.println("== Received Packet ==");
        System.out.println("Length: " + length);
        System.out.println("ID: " + id);

        Class<? extends AbstractPacket> packetType = PacketManager.getInstance().getPacket(id, NetworkManager.getInstance().getState(address));
        if(packetType == null) return;

        AbstractPacket packet = packetType.getConstructor().newInstance();

        Arrays.stream(packetType.getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(IgnoreVariable.class))
                .peek(f -> f.setAccessible(true))
                .forEach(f -> setField(f, packet, in));

        packet.setAddress(address);
        packet.process();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }

    private void setField(Field field, Object object, ByteBuf buf) {
        try {
            field.set(object, resolver.resolve(field.getType(), buf));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
