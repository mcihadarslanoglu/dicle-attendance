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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Student extends View {
    String role;
    BluetoothAdapter btAdapter;
    Context context;
    UUID APP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String SERVER_APP_NAME = "ATTENDANCE";
    BluetoothSocket  socket;
    String ID;
    JSONObject user;
    public boolean isSigned() {
        return this.isSigned;
    }

    Boolean signFlag;

    boolean isSigned = false;
    public Student(Context context, String user){
        super(context);

        this.role = "student";
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
        this.APP_UUID = APP_UUID;
        this.isSigned = isSigned;
        try {
            this.user = new JSONObject(user);
            this.ID = this.user.get("id").toString();
            Log.i("Student","Student user id is " + this.user.get("id").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
    private BluetoothSocket createSocket(BluetoothDevice device){


        try {
            Log.i("Student","Socket is creating...");
            this.socket = device.createInsecureRfcommSocketToServiceRecord(this.APP_UUID);
            Log.i("Student","Socket is created");
            this.socket.connect();
            Log.i("Student","Socket is connected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.socket;

    }
    private void sign(BluetoothSocket target){
        //this.signFlag = true;

        Thread signThread = new Thread(()->{
            while (this.signFlag){
                Log.i("Student","Responses are listening");
                String response =  listenData(target);
                Log.i("Student","Obtained message is " + response);

                if(response != null){
                    if (response.equals(this.ID)){

                        Log.i("Student","In if statement" + this.ID );
                        this.isSigned = true;
                        this.signFlag = false;

                        try {
                            target.close();
                            stop();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }

            }
        });

        signThread.start();

        int ct = 0;
        while (!this.isSigned){
            Log.i("Student","Sign is been sending");
            this.sendData(target,this.ID);
            ct = ct + 1;
            Log.i("Student","Sign has been sent "+this.ID+"and ct is "+ct + " is bluetooth connected ? "+target.isConnected());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (ct>3){
                Log.i("Student","ct is bigger than 3");

                try {


                    target.close();
                    Log.i("Student", "getSign socket is closed");
                    this.stop();
                    Thread.sleep(2000);
                    Log.i("Student","Sleep is done");
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("Student","Application is stopped as student");
                this.start();

                Log.i("Student","Application is started as student");
                ct = 0;
                break;
            }

        }
        Log.i("Teacher","Sign loop is broken");



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
    private void disableBT(){

        this.btAdapter.disable();
        Log.i("Student","BT is disabled");
    }

    private boolean sendData(BluetoothSocket client, String msg){
        Log.i("Student","Data is sending");
        OutputStream outputStream;
        byte[] msgByte = msg.getBytes(StandardCharsets.UTF_8);

        if(!client.isConnected()){
            return false;
        }

        try {
            outputStream = client.getOutputStream();
            outputStream.write(msgByte);
            Log.i("Student","Data has been sent");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }



    }

    public String listenData(BluetoothSocket client){
        Log.i("Student","Data is listening");
        InputStream inputStream;
        int numBytes = 10;
        byte[] buffer = new byte[1024];
        String msg = null;
        boolean isConnected = client.isConnected();
        Log.i("Student","is client connected " + isConnected);

        if (isConnected){
            try {
                Log.i("Student","client is connected");
                inputStream = client.getInputStream();
                numBytes = inputStream.read(buffer);
                Log.i("Student","A new data is obtained.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            msg = new String(buffer,0,numBytes, StandardCharsets.UTF_8);
            Log.i("Student","Response will be sent.");
            Log.i("Student", "The obtained data is " + msg);

        }



        return msg;

    }

    public void start(){
        Log.i("Student","Application is started as student");
        this.enableBT();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.signFlag = true;
        this.scanDevices();

    }
    public void stop(){
        Log.i("Student","Application is stopped as student");

        this.signFlag = false;
        this.context.unregisterReceiver(receiver);
        this.disableBT();


    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Student", "in receiver");
            String action = intent.getAction();
            Log.i("Student","The action is "+ action);
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Log.i("Student","Discovery is started");
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                Log.i("Student","Device scanning is started again.");
                btAdapter.startDiscovery();


            }else if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();


                Log.i("Student","A new device found to connect. "+device.getName());
                Log.i("Student", "out SERVER_APP_NAME if statement "+ SERVER_APP_NAME);
                if(device != null && deviceName != null){
                    if (deviceName.equals(SERVER_APP_NAME)){
                        btAdapter.cancelDiscovery();
                        Log.i("Student", "in SERVER_APP_NAME if statement");
                        BluetoothSocket socket = createSocket(device);
                        Log.i("Student","Socket is created");
                        sign(socket);
                        Log.i("Student","Sign is done");

                    }
                    }

            }
        }
    };


}
