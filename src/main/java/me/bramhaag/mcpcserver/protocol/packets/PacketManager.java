package me.bramhaag.mcpcserver.protocol.packets;

import me.bramhaag.mcpcserver.annotations.packets.Packet;
import org.reflections.Reflections;

import java.util.Map;
import java.util.stream.Collectors;

public class PacketManager {

    private Map<Class<? extends AbstractPacket>, Packet> packets;

    private PacketManager() {
        packets = new Reflections("me.bramhaag.mcpcserver.protocol.packets").getSubTypesOf(AbstractPacket.class).stream()
                .filter(c -> c.isAnnotationPresent(Packet.class))
                .filter(c -> c.getAnnotation(Packet.class).type() == Packet.Type.IN)
                .collect(Collectors.toMap(c -> c, c -> c.getAnnotation(Packet.class)));
    }

    public Class<? extends AbstractPacket> getPacket(int id, Packet.State state) {
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
