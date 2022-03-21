package com.example.dicle_attendance.persons;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Student extends View {
    String role;
    BluetoothAdapter btAdapter;
    Context context;
    UUID APP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String SERVER_APP_NAME = "ATTENDANCE";
    BluetoothSocket  socket;
    String ID = "18354012";
    String firstName;
    String lastName;

    boolean isSigned = false;
    public Student(Context context){
        super(context);

        this.role = "student";
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
        this.APP_UUID = APP_UUID;
        this.ID = ID;
        this.isSigned = isSigned;
    }
    private BluetoothSocket createSocket(BluetoothDevice device){


        try {
            Log.i("Student","Socket is creating...");
            this.socket = device.createInsecureRfcommSocketToServiceRecord(this.APP_UUID);
            this.socket.connect();
            Log.i("Student","Socket is created.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.socket;

    }
    private void sign(BluetoothSocket target){

        new Thread(()->{
            while (true){
                Log.i("Student","Responses are listening");
                String response =  listenData(target);
                if (response == ID){
                    isSigned = true;
                    break;
                }
            }
        }).start();
        Log.i("Student","Sign is been sending");
        this.sendData(target,this.ID);
        Log.i("Student","Sign has been sent "+this.ID);
    }


    private void scanDevices(){
        Log.i("Student","Devices are scanning");

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);



        this.context.registerReceiver(receiver,filter);
        this.btAdapter.startDiscovery();
    }

    private void enableBT(){
        this.btAdapter.enable();
    }

    private void sendData(BluetoothSocket client, String msg){
        Log.i("Student","Data is sending");
        OutputStream outputStream;
        byte[] msgByte = msg.getBytes(StandardCharsets.UTF_8);

        try {
            outputStream = client.getOutputStream();
            outputStream.write(msgByte);
            Log.i("Student","Data has been sent");
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public String listenData(BluetoothSocket client){
        Log.i("Student","Data is listening");
        InputStream inputStream;
        int numBytes = 10;
        byte[] buffer = new byte[1024];
        String msg = null;
        if (client.isConnected()){
            try {
                inputStream = client.getInputStream();
                numBytes = inputStream.read(buffer);
                Log.i("Student","A new data is obtained.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            msg = new String(buffer,0,numBytes, StandardCharsets.UTF_8);
            Log.i("Student","Response will be sent.");
            Log.i("Student", "The obtained data is " + msg);

        }else{
            Log.i("Student","The connection is closed");
        }

        return msg;

    }

    public void start(){
        Log.i("Student","Application is started as student");
        this.enableBT();
        this.scanDevices();

    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Student", "in receiver");
            String action = intent.getAction();
            Log.i("Student","The action is "+ action);
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){

            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                Log.i("Student","Device scanning is started again.");
                enableBT();


            }else if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i("Student","A new device found to connect.");
                if(device.getName().equals(SERVER_APP_NAME)){

                    BluetoothSocket socket = createSocket(device);
                    sign(socket);
                }

            }
        }
    };


}
