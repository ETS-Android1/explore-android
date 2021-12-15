package com.mentalab.packets.sensors.exg;

public class Eeg94 extends EEGPacket {

    private static final int CHANNEL_NUMBER = 4;


    public Eeg94(double timeStamp) {
        super(timeStamp, CHANNEL_NUMBER);
    }
}