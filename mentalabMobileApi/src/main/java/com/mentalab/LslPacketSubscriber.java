package com.mentalab;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import com.mentalab.LslLoader.ChannelFormat;
import com.mentalab.LslLoader.StreamInfo;
import com.mentalab.io.ContentServer;
import com.mentalab.packets.Packet;

import java.io.IOException;
import java.util.ArrayList;

public class LslPacketSubscriber extends Thread {

  private static final String TAG = "EXPLORE_LSL_DEV";
  private static final int nominalSamplingRateOrientation = 20;
  private static final int dataCountOrientation = 9;
  static LslLoader.StreamOutlet lslStreamOutletExg;
  static LslLoader.StreamOutlet lslStreamOutletOrn;
  static LslLoader.StreamOutlet lslStreamOutletMarker;
  static LslLoader lslLoader = new LslLoader();
  private static BluetoothDevice connectedDevice = null;
  private LslLoader.StreamInfo lslStreamInfoExg;
  private LslLoader.StreamInfo lslStreamInfoOrn;
  private LslLoader.StreamInfo lslStreamInfoMarker;

  public LslPacketSubscriber(BluetoothDevice device) {
    connectedDevice = device;
  }

  @Override
  public void run() {
    try {

      lslStreamInfoOrn =
          new StreamInfo(
              connectedDevice.getName() + "_ORN",
              "ORN",
              dataCountOrientation,
              nominalSamplingRateOrientation,
              ChannelFormat.float32,
              connectedDevice.getName() + "_ORN");
      if (lslStreamInfoOrn == null) {
        throw new IOException("Stream Info is Null!!");
      }
      lslStreamOutletOrn = new LslLoader.StreamOutlet(lslStreamInfoOrn);

      lslStreamInfoMarker =
          new StreamInfo(
              connectedDevice.getName() + "_Marker",
              "Markers",
              1,
              0,
              ChannelFormat.int32,
              connectedDevice.getName() + "_Markers");

      if (lslStreamInfoMarker == null) {
        throw new IOException("Stream Info is Null!!");
      }
      lslStreamOutletMarker = new LslLoader.StreamOutlet(lslStreamInfoMarker);
      Log.d(TAG, "Subscribing!!");
      ContentServer.getInstance().subscribe("ExG", this::packetCallbackExG);

      ContentServer.getInstance().subscribe("Orn", this::packetCallbackOrn);

      ContentServer.getInstance().subscribe("Marker", this::packetCallbackMarker);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public void packetCallbackExG(Packet packet) {
    if (lslStreamInfoExg == null) {
      lslStreamInfoExg =
          new StreamInfo(
              connectedDevice + "_ExG",
              "ExG",
              packet.getDataCount(),
              250,
              LslLoader.ChannelFormat.float32,
              connectedDevice + "_ExG");
      if (lslStreamInfoExg != null) {
        try {
          lslStreamOutletExg = new LslLoader.StreamOutlet(lslStreamInfoExg);
        } catch (IOException exception) {
          exception.printStackTrace();
        }
      }
    }
    Log.d("TAG", "packetCallbackExG");
    lslStreamOutletExg.push_chunk(convertArraylistToFloatArray(packet));
  }

  public void packetCallbackOrn(Packet packet) {
    Log.d("TAG", "packetCallbackOrn");
    lslStreamOutletOrn.push_sample(convertArraylistToFloatArray(packet));
  }

  public void packetCallbackMarker(Packet packet) {
    Log.d("TAG", "packetCallbackMarker");
    lslStreamOutletMarker.push_sample(convertArraylistToFloatArray(packet));
  }

  float[] convertArraylistToFloatArray(Packet packet) {
    ArrayList<Float> packetVoltageValues = packet.getData();
    float[] floatArray = new float[packetVoltageValues.size()];
    Object[] array = packetVoltageValues.toArray();
    for (int index = 0; index < packetVoltageValues.size(); index++) {
      floatArray[index] = ((Float) packetVoltageValues.get(index)).floatValue();
    }
    return floatArray;
  }
}
