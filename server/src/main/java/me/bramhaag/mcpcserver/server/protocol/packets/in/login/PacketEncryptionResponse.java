package me.bramhaag.mcpcserver.server.protocol.packets.in.login;

import me.bramhaag.mcpcserver.annotations.packets.Packet;
import me.bramhaag.mcpcserver.server.protocol.packets.AbstractPacket;

@Packet(id = 0x01, state = Packet.State.LOGIN, type = Packet.Type.IN)
public class PacketEncryptionResponse extends AbstractPacket {

    private int sharedSecretLength;
    private byte[] sharedSecret;
    private int verifyTokenLength;
    private byte[] verifyToken;

    @Override
    public void process() {
        //TODO
    }
}
