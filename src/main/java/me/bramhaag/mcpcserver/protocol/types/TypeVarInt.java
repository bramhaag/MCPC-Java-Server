package me.bramhaag.mcpcserver.protocol.types;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class TypeVarInt implements IType<Integer> {

    @Override
    public Integer read(@NotNull ByteBuf buf) {
        int numRead = 0;
        int result = 0;
        byte read;

        do {
            read = buf.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    @Override
    public void write(@NotNull ByteBuf buf, @NotNull Integer value) {
        do {
            byte temp = (byte)(value & 0b01111111);
            value >>>= 7;

            if (value != 0) {
                temp |= 0b10000000;
            }

            buf.writeByte(temp);
        } while (value != 0);
    }
}
