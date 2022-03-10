package com.mentalab.packets.command;

import androidx.annotation.NonNull;
import com.mentalab.exception.InvalidDataException;
import com.mentalab.utils.constants.Topic;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CommandStatus extends UtilPacket {

    boolean commandStatus;


    public CommandStatus(double timeStamp) {
        super(timeStamp);
    }


    @Override
    public void convertData(byte[] byteBuffer) throws InvalidDataException {
        double[] convertedRawValues = bytesToDouble(byteBuffer, 2); // TODO: Is this a function with side-effects? Why is it here?
        short status =
                ByteBuffer.wrap(new byte[]{byteBuffer[5], 0}).order(ByteOrder.LITTLE_ENDIAN).getShort();
        commandStatus = status != 0;
    }


    @NonNull
    @Override
    public String toString() {
        return "Command status is " + commandStatus;
    }


    @Override
    public int getDataCount() {
        return 1;
    }

    public boolean getResult() {
        return commandStatus;
    }
}
