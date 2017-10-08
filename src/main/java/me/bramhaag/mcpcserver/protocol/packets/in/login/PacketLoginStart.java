package me.bramhaag.mcpcserver.protocol.packets.in.login;

import me.bramhaag.mcpcserver.annotations.packets.Packet;
import me.bramhaag.mcpcserver.protocol.packets.AbstractPacket;

@Packet(id = 0x00, state = Packet.State.LOGIN, type = Packet.Type.IN)
public class PacketLoginStart extends AbstractPacket {

    private String name;

    @Override
    public void process() {
        System.out.println("Received login from: " + name);
    }
}
