package com.example.dicle_attendance.persons;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.UUID;


public class Teacher {
    String role;
    BluetoothAdapter btAdapter;
    String SERVER_APP_NAME = "ATTENDANCE";
    UUID APP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothServerSocket socket;
    BluetoothSocket client;
    public ArrayList<String> requests = new ArrayList<>();
    Context context;
    boolean acceptDevicesFlag;
    boolean getSignFlag;
    public JSONObject user;
    Thread acceptDevicesThread;
    public Teacher(Context context, String user){
        this.role = "Teacher";
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        this.SERVER_APP_NAME = SERVER_APP_NAME;
        this.context = context;
        this.acceptDevicesFlag = true;
        this.getSignFlag = true;
        this.requests = requests;
        try {
            this.user = new JSONObject(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String listenData(BluetoothSocket client){
        Log.i("Teacher","Data is listening.");
        InputStream inputStream;
        int numBytes = 0;
        byte[] buffer = new byte[1024];
        String msg = null;
        Log.i("Teacher","is device connected " + client.isConnected());
        if(client != null && client.isConnected()){
            try {
                inputStream = client.getInputStream();
                numBytes = inputStream.read(buffer);
                Log.i("Teacher","Data received, numBytes is " + numBytes);
                msg = new String(buffer,0,numBytes, StandardCharsets.UTF_8);
                //requests.add(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.i("Teacher","The received message is " + msg);
        Log.i("Teacher","Response will be sent");

        return msg;

    }
    public void enableBT(){
        this.btAdapter.enable();
    }
    public boolean sendData(BluetoothSocket client, String msg){
        Log.i("Teacher","Data is sending");
        OutputStream outputStream;
        byte[] msgByte = msg.getBytes(StandardCharsets.UTF_8);
        if(!client.isConnected()){
            return false;
        }

        try {
            outputStream = client.getOutputStream();
            outputStream.write(msgByte);
            Log.i("Teacher","Data has been sent");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }



    }

    public void getSign(BluetoothSocket client){
        Log.i("Teacher","in getSign");


        new Thread(()->{
            while (client.isConnected()){

                Log.i("Student","Sign is been getting");
                String request = listenData(client);
                Log.i("Student","Sigh has been gotten" + request);
                if (request != null){
                    this.requests.add(request);
                    Log.i("Teacher","requests are "+requests.toString());
                    Log.i("Student","Request has been sent");
                    boolean success = sendData(client, request);
                    Log.i("Student","Success is " + success);
                    if (success){
                        try {
                            Log.i("Teacher","Socket being closed");
                            client.close();
                            //this.getSignFlag = false;
                            Log.i("Teacher","Socket was closed "+client.isConnected());
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }


            }
            Log.i("Teacher","getSign loop is broken");


        }).start();
    }

    private void discoverable(){
        Log.i("Teacher","The device is discoverable.");
        this.btAdapter.setName(this.SERVER_APP_NAME);
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        this.context.startActivity(discoverableIntent);

    }
    public void acceptDevices(){



        acceptDevicesThread = new Thread(()->{
            btAdapter.setName(SERVER_APP_NAME);
            try {
                socket = btAdapter.listenUsingInsecureRfcommWithServiceRecord(SERVER_APP_NAME,APP_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (this.acceptDevicesFlag){
                try {
                    Log.i("Teacher","Socket is been creating...");

                    client = socket.accept();
                    Log.i("Teacher","Socket is created");
                    getSign(client);
                    Log.i("Teacher","After getSign");


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            Log.i("Teacher","acceptDevices function is terminated");

        });
        acceptDevicesThread.start();

    }

    public void start(){
        Log.i("Teacher","Application is started as teacher");

        this.acceptDevicesFlag = true;
        this.enableBT();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.discoverable();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("Teacher","sleep time is ended");
        this.acceptDevices();
    }
    public void stop(){
        Log.i("Teacher","Application is stopped as teacher");
        this.acceptDevicesFlag = false;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.btAdapter.disable();
    }


}
