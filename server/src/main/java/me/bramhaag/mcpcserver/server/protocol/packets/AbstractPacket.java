package me.bramhaag.mcpcserver.server.protocol.packets;

import io.netty.buffer.ByteBuf;
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

    public abstract void encode(ByteBuf buf);
    public abstract void decode(ByteBuf buf);

    public abstract void process();
}
