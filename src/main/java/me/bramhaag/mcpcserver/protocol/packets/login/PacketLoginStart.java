package me.bramhaag.mcpcserver.protocol.packets.login;

import me.bramhaag.mcpcserver.annotations.packets.Packet;
import me.bramhaag.mcpcserver.protocol.packets.AbstractPacket;
import me.bramhaag.mcpcserver.protocol.packets.PacketState;

@Packet(id = 0x00, state = PacketState.LOGIN)
public class PacketLoginStart extends AbstractPacket {
    private String name;

    @Override
    public void onReceive() {
        System.out.println("Received login from: " + name);
    }
}
