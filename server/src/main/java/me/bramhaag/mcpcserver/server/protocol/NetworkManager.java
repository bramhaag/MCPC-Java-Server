package me.bramhaag.mcpcserver.server.protocol;

import me.bramhaag.mcpcserver.annotations.packets.Packet;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class NetworkManager {

    private Map<InetSocketAddress, Packet.State> states = new HashMap<>();

    private NetworkManager() {}

    public void setState(InetSocketAddress address, Packet.State state) {
        this.states.put(address, state);
    }

    public Packet.State getState(InetSocketAddress address) {
        if(!states.containsKey(address)) {
            states.put(address, Packet.State.HANDSHAKING);
        }

        return states.get(address);
    }

    private static class LazyHolder {
        private static final NetworkManager INSTANCE = new NetworkManager();
    }

    public static NetworkManager getInstance() {
        return NetworkManager.LazyHolder.INSTANCE;
    }
}
