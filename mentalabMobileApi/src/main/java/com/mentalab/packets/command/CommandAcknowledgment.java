package com.mentalab.packets.command;

import androidx.annotation.NonNull;
import com.mentalab.io.constants.Topic;

/**
 * Acknowledgement packet is sent when a configuration command is successfully executed on the
 * device
 */
public class CommandAcknowledgment extends UtilPacket {


    public CommandAcknowledgment(double timeStamp) {
        super(timeStamp);
    }


    @Override
    public void convertData(byte[] byteBuffer) {
    }


    @NonNull
    @Override
    public String toString() {
        return "AckPacket";
    }


    @Override
    public int getDataCount() {
        return 0; // TODO: Explanation
    }


    @Override
    public Topic getTopic() {
        return Topic.COMMAND;
    }
}
