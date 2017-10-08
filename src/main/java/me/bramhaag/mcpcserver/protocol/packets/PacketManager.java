package me.bramhaag.mcpcserver.protocol.packets;

import org.reflections.Reflections;

import java.util.Map;
import java.util.stream.Collectors;

public class PacketManager {

    private Map<Class<? extends AbstractPacket>, me.bramhaag.mcpcserver.annotations.packets.Packet> packets;

    private PacketManager() {
        packets = new Reflections("me.bramhaag.mcpcserver.protocol.packets").getSubTypesOf(AbstractPacket.class).stream()
                .filter(c -> c.isAnnotationPresent(me.bramhaag.mcpcserver.annotations.packets.Packet.class))
                .collect(Collectors.toMap(c -> c, c -> c.getAnnotation(me.bramhaag.mcpcserver.annotations.packets.Packet.class)));
    }

    public Class<? extends AbstractPacket> getPacket(int id, PacketState state) {
        return packets.entrySet().stream()
                .filter(e -> e.getValue().id() == id && e.getValue().state() == state)
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    private static class LazyHolder {
        private static final PacketManager INSTANCE = new PacketManager();
    }

    public static PacketManager getInstance() {
        return LazyHolder.INSTANCE;
    }
}
