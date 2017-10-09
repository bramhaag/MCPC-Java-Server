package me.bramhaag.mcpcserver.protocol.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.bramhaag.mcpcserver.annotations.packets.IgnoreVariable;
import me.bramhaag.mcpcserver.protocol.type.Type;

import java.lang.reflect.Field;
import java.util.Arrays;

public class PacketEncoder extends MessageToByteEncoder<AbstractPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket packet, ByteBuf out) {
        Arrays.stream(packet.getClass().getDeclaredFields())
                .filter(f -> !f.isAnnotationPresent(IgnoreVariable.class))
                .peek(f -> f.setAccessible(true))
                .forEach(f -> addToBuffer(f, packet, out));
    }

    private void addToBuffer(Field field, Object instance, ByteBuf buf) {
        try {
            Type.ENCODER.encode(field.get(instance), buf);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
