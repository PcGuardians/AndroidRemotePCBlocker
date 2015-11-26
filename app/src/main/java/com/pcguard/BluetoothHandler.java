package com.pcguard;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class BluetoothHandler extends Activity {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    //    Thread workerThread;
//    byte[] readBuffer;
//    int readBufferPosition;
//    int counter;
    volatile boolean stopWorker;

    public BluetoothHandler() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    void findBT() {
        if (mBluetoothAdapter == null) {
            Log.d("BluetoothHandler", "No bluetooth adapter available");
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("HC-05")) {
                    mmDevice = device;
                    break;
                }
            }
        }
        Log.d("BluetoothHandler", "Bluetooth Device Found");
    }

    void openBT() throws IOException {
        Log.d("openBT", "Trying to open socket connection");
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID

        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        if (mmSocket.isConnected()) {
            Log.d("Connection", " is installed");
        }

        Log.d("aka", "1");
        mmSocket.connect();
        Log.d("aka", "2");
        mmOutputStream = mmSocket.getOutputStream();
        Log.d("aka", "3");
        mmInputStream = mmSocket.getInputStream();
        Log.d("aka", "4");

        Log.d("BluetoothHandler","Bluetooth Opened");
    }


//        beginListenForData();



    /*void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = mmInputStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {
                                            Log.d("BluetoothHandler", data);
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }*/

    void sendData() throws IOException {
//        mmOutputStream.write(msg.getBytes()); - String to send
        String lockMessage = "trnf";

        mmOutputStream.write(lockMessage.getBytes());
        Log.d("BluetoothHandler", "Data Sent " + lockMessage);
    }

    void closeBT() throws IOException {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        Log.d("BluetoothHandler", "Bluetooth Closed");
    }
}
