package me.bramhaag.mcpcserver.protocol.packets.handshake;

import me.bramhaag.mcpcserver.annotations.packets.Packet;
import me.bramhaag.mcpcserver.protocol.NetworkManager;
import me.bramhaag.mcpcserver.protocol.packets.AbstractPacket;
import me.bramhaag.mcpcserver.protocol.packets.PacketState;

@Packet(id = 0x00, state = PacketState.HANDSHAKING)
public class PacketHandshake extends AbstractPacket {

    private int protocol;
    private String hostname;
    private short port;
    private PacketState nextState;

    @Override
    public void onReceive() {
        //state == 1 -> ping, else if state == 2 -> login
        System.out.printf("Protocol version: %s", protocol).println();
        System.out.printf("Connecting from: %s:%s", hostname, port).println();
        System.out.printf("Next state: %s", nextState.name()).println();

        NetworkManager.getInstance().setState(getAddress(), PacketState.LOGIN);
    }
}