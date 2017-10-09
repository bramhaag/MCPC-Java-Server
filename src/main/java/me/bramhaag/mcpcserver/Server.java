package me.bramhaag.mcpcserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.bramhaag.mcpcserver.protocol.packets.AbstractPacket;
import me.bramhaag.mcpcserver.protocol.packets.PacketDecoder;
import me.bramhaag.mcpcserver.protocol.packets.PacketEncoder;
import me.bramhaag.mcpcserver.protocol.packets.out.login.PacketEncryptionRequest;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class Server {

    public static final int PROTOCOL = 340;
    public static final KeyPair KEYPAIR;

    private final int port;
    private Logger logger = Logger.getLogger("Minecraft");

    static {
        KEYPAIR = generateKeypair();
    }

    public Server(int port) {
        this.port = port;
        logger.info("Created");
    }

    public void run() throws InterruptedException {
        logger.info("Ready");

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("decoder", new PacketDecoder(ch.remoteAddress()));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private static KeyPair generateKeypair() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        keyPairGenerator.initialize(1024);

        return keyPairGenerator.genKeyPair();
    }
}
