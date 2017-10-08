package me.bramhaag.mcpcserver;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        int port = Arrays.stream(args).findFirst().map(Integer::valueOf).orElse(25565);

        new Server(port).run();
    }
}
