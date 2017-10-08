package me.bramhaag.mcpcserver.protocol.types;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import org.jetbrains.annotations.NotNull;

public enum Type {
    SHORT {
        public Short read(@NotNull ByteBuf buf) {
            return (short)buf.readUnsignedShort();
        }

        @Override
        public <T> void write(@NotNull ByteBuf buf, @NotNull T value) {

        }
    },
    STRING {
        @Override
        public String read(@NotNull ByteBuf buf) {
            byte[] data = new byte[(Integer)VAR_INT.read(buf)];
            buf.readBytes(data);
            return new String(data, CharsetUtil.UTF_8);
        }

        @Override
        public <T> void write(@NotNull ByteBuf buf, @NotNull T value) {

        }
    },
    VAR_INT {
        @Override
        public Integer read(@NotNull ByteBuf buf) {
            return this.readNumber(buf, 5).intValue();
        }

        @Override
        public <T> void write(@NotNull ByteBuf buf, @NotNull T value) {
            writeNumber(buf, (Number)value);
        }
    },
    VAR_LONG {
        @Override
        public Long read(@NotNull ByteBuf buf) {
            return this.readNumber(buf, 10).longValue();
        }

        @Override
        public <T> void write(@NotNull ByteBuf buf, @NotNull T value) {
            writeNumber(buf, (Number)value);
        }
    },
    BYTE_ARRAY {
        @Override
        public Object read(@NotNull ByteBuf buf) {
            //return read(buf, (Integer)VAR_INT.read(buf));
            return null;
        }

//        public Object read(@NotNull ByteBuf buf, int length) {
//            byte[] array = new byte[length];
//            buf.readBytes(array);
//            return array;
//        }

        @Override
        public <T> void write(@NotNull ByteBuf buf, @NotNull T value) {
        }
    };

    public abstract Object read(@NotNull ByteBuf buf);
    public abstract <T> void write(@NotNull ByteBuf buf, @NotNull T value);

    protected Number readNumber(ByteBuf buf, int max) {
        int numRead = 0;
        long result = 0;
        byte read;

        do {
            read = buf.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > max) {
                throw new RuntimeException("Number is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    protected void writeNumber(ByteBuf buf, Number value) {
        long number = value.longValue();
        do {
            byte temp = (byte)(number & 0b01111111);
            number >>>= 7;

            if (number != 0) {
                temp |= 0b10000000;
            }

            buf.writeByte(temp);
        } while (number != 0);
    }
}
