package me.bramhaag.mcpcserver.protocol.packets.login;

import me.bramhaag.mcpcserver.annotations.packets.Packet;
import me.bramhaag.mcpcserver.protocol.packets.PacketState;

@Packet(id = 0x01, state = PacketState.LOGIN)
public class PacketEncryptionRequest {
}
