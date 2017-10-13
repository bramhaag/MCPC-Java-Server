package me.bramhaag.mcpcserver.server.protocol.packets.in.handshake;

import me.bramhaag.mcpcserver.annotations.packets.Packet;
import me.bramhaag.mcpcserver.server.protocol.NetworkManager;
import me.bramhaag.mcpcserver.server.protocol.packets.AbstractPacket;

@Packet(id = 0x00, state = Packet.State.HANDSHAKING, type = Packet.Type.IN)
public abstract class PacketHandshake extends AbstractPacket {

    protected int protocol;
    protected String hostname;
    protected short port;
    protected Packet.State nextState;

    @Override
    public void process() {
        //state == 1 -> ping, else if state == 2 -> login
        System.out.printf("Protocol version: %s", protocol).println();
        System.out.printf("Connecting from: %s:%s", hostname, port).println();
        System.out.printf("Next state: %s", nextState.name()).println();

        NetworkManager.getInstance().setState(getAddress(), Packet.State.LOGIN);
    }
}