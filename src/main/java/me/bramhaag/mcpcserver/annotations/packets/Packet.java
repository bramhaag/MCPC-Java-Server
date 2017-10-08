package me.bramhaag.mcpcserver.annotations.packets;

import me.bramhaag.mcpcserver.protocol.packets.PacketState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Packet {
    byte id();
    PacketState state();
}
