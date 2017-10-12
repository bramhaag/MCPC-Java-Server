package me.bramhaag.mcpcserver.server.protocol.packets.out.login;

import me.bramhaag.mcpcserver.annotations.packets.Packet;
import me.bramhaag.mcpcserver.server.protocol.packets.AbstractPacket;

import java.util.Arrays;

@Packet(id = 0x01, state = Packet.State.LOGIN, type = Packet.Type.OUT)
public class PacketEncryptionRequest extends AbstractPacket {

    private String serverId;
    private byte[] publicKey;
    private byte[] verifyToken;

    public PacketEncryptionRequest(String serverId, byte[] publicKey, byte[] verifyToken) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    @Override
    public void process() {
        System.out.println("SERVER ID: " + serverId);
        System.out.println("PUBLIC KEY: " + Arrays.toString(publicKey));
        System.out.println("VERIFY KEY: " + Arrays.toString(verifyToken));
        //TODO
    }
}
