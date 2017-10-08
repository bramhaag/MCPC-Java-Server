package me.bramhaag.mcpcserver.protocol;

import me.bramhaag.mcpcserver.protocol.packets.PacketState;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class NetworkManager {

    private Map<InetSocketAddress, PacketState> states = new HashMap<>();

    private NetworkManager() {}

    public void setState(InetSocketAddress address, PacketState state) {
        this.states.put(address, state);
    }

    public PacketState getState(InetSocketAddress address) {
        if(!states.containsKey(address)) {
            states.put(address, PacketState.HANDSHAKING);
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
