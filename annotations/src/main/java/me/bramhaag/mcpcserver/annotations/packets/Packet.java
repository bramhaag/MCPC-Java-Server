package me.bramhaag.mcpcserver.annotations.packets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Packet {
    byte id();
    State state();
    Type type();

    enum State {
        HANDSHAKING(0),
        STATUS(1),
        LOGIN(2),
        PLAY(3);

        private int id;

        State(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static State getById(int id) {
            return Arrays.stream(values()).filter(e -> e.getId() == id).findFirst().orElse(null);
        }
    }

    enum Type {
        IN,
        OUT
    }
}
