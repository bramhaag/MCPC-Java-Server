package me.bramhaag.mcpcserver.server.protocol.packets;

import me.bramhaag.mcpcserver.annotations.packets.IgnoreVariable;

import java.net.InetSocketAddress;

public abstract class AbstractPacket {

    @IgnoreVariable
    private InetSocketAddress address;

    public void setAddress(InetSocketAddress address) {
        this.address = address;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public abstract void process();
}
