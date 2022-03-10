package com.mentalab.packets;

import com.mentalab.packets.command.CommandReceived;
import com.mentalab.packets.command.CommandStatus;
import com.mentalab.packets.sensors.exg.Eeg94;
import com.mentalab.packets.sensors.exg.Eeg98;
import com.mentalab.packets.info.Device;
import com.mentalab.packets.info.Environment;
import com.mentalab.packets.sensors.Orientation;
import com.mentalab.packets.sensors.Marker;

public enum PacketId {
    ORIENTATION(13) {
        @Override
        public Packet createInstance(double timeStamp) {

            return new Orientation(timeStamp);
        }
    },
    ENVIRONMENT(19) {
        @Override
        public Packet createInstance(double timeStamp) {
            return new Environment(timeStamp);
        }
    },
    TIMESTAMP(27) {
        @Override
        public Packet createInstance(double timeStamp) {
            return null;
        }
    },
    DISCONNECT(25) {
        @Override
        public Packet createInstance(double timeStamp) {
            return null;
        }
    },
    INFO(99) {
        @Override
        public Packet createInstance(double timeStamp) {
            return new Device(timeStamp);
        }
    },
    EEG94(144) {
        @Override
        public Packet createInstance(double timeStamp) {
            return new Eeg94(timeStamp);
        }
    },
    EEG98(146) {
        @Override
        public Packet createInstance(double timeStamp) {
            return new Eeg98(timeStamp);
        }
    },
    EEG99S(30) {
        @Override
        public Packet createInstance(double timeStamp) {
            return null;
        }
    },
    EEG99(62) {
        @Override
        public Packet createInstance(double timeStamp) {
            return null;
        }
    },
    EEG94R(208) {
        @Override
        public Packet createInstance(double timeStamp) {
            return null;
        }
    },
    EEG98R(210) {
        @Override
        public Packet createInstance(double timeStamp) {
            return new Eeg98(timeStamp);
        }
    },
    CMDRCV(192) {
        @Override
        public Packet createInstance(double timeStamp) {
            return new CommandReceived(timeStamp);
        }
    },
    CMDSTAT(193) {
        @Override
        public Packet createInstance(double timeStamp) {
            return new CommandStatus(timeStamp);
        }
    },
    MARKER(194) {
        @Override
        public Packet createInstance(double timeStamp) {
            return new Marker(timeStamp);
        }
    },
    CALIBINFO(195) {
        @Override
        public Packet createInstance(double timeStamp) {
            return null;
        }
    };

    private int value;

    PacketId(int value) {
        this.value = value;
    }

    public int getNumVal() {
        return value;
    }

    public abstract Packet createInstance(double timeStamp);
}
