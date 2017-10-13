package me.bramhaag.mcpcserver.server.protocol.packets.in.login;

import me.bramhaag.mcpcserver.annotations.packets.Packet;
import me.bramhaag.mcpcserver.server.protocol.packets.AbstractPacket;

@Packet(id = 0x01, state = Packet.State.LOGIN, type = Packet.Type.IN)
public abstract class PacketEncryptionResponse extends AbstractPacket {

    protected int sharedSecretLength;
    protected byte[] sharedSecret;
    protected int verifyTokenLength;
    protected byte[] verifyToken;

    @Override
    public void process() {
        //TODO
    }
}
