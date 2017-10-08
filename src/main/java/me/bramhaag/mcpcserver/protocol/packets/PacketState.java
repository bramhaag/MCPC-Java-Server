package me.bramhaag.mcpcserver.protocol.packets;

import java.util.Arrays;

public enum PacketState {
    HANDSHAKING(0),
    STATUS(1),
    LOGIN(2),
    PLAY(3);

    private int id;

    PacketState(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static PacketState getById(int id) {
        return Arrays.stream(values()).filter(e -> e.getId() == id).findFirst().orElse(null);
    }
}
