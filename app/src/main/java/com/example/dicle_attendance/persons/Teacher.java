package com.example.dicle_attendance.persons;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

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
    ArrayList<String> requests = new ArrayList<String>();
    Context context;
    public Teacher(Context context){
        this.role = "Teacher";
        this.btAdapter = BluetoothAdapter.getDefaultAdapter();
        this.SERVER_APP_NAME = SERVER_APP_NAME;
        this.context = context;


    }

    public String listenData(BluetoothSocket client){
        Log.i("Teacher","Data is listening.");
        InputStream inputStream;
        int numBytes = 0;
        byte[] buffer = new byte[1024];
        String msg = null;
        if(client.isConnected()){
            try {
                inputStream = client.getInputStream();
                numBytes = inputStream.read(buffer);
                Log.i("Teacher","Data received  ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        msg = new String(buffer,0,numBytes, StandardCharsets.UTF_8);
        requests.add(msg);
        Log.i("Teacher","The received message is " + msg);
        Log.i("Teacher","Response will be sent");

        return msg;

    }

    public void sendData(BluetoothSocket client, String msg){
        Log.i("Teacher","Data is sending");
        OutputStream outputStream;
        byte[] msgByte = msg.getBytes(StandardCharsets.UTF_8);

        try {
            outputStream = client.getOutputStream();
            outputStream.write(msgByte);
            Log.i("Teacher","Data has been sent");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void getSign(BluetoothSocket client){
        Log.i("Teacher","in getSign");


        new Thread(()->{
            while (true){

                /*
                 * String response;
                 * A response may be added here to notify the user with a specific message;
                 * */
                Log.i("Student","Sign is been getting");
                String request = listenData(client);
                Log.i("Student","Sigh has been gotten");
                requests.add(request);
                Log.i("Student","Request has been sent");
                sendData(client, request);


            }


        }).start();
    }

    private void discoverable(){
        Log.i("Teacher","The device is discoverable.");
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        this.context.startActivity(discoverableIntent);

    }
    public void acceptDevices(){



        new Thread(()->{
            while (true){
                try {
                    Log.i("Teacher","Socket is been creating...");
                    socket = btAdapter.listenUsingInsecureRfcommWithServiceRecord(SERVER_APP_NAME,APP_UUID);
                    btAdapter.setName(SERVER_APP_NAME);


                    client = socket.accept();
                    Log.i("Teacher","Socket is created");
                    getSign(client);
                    Log.i("Teacher","After getSign");


                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }).start();

    }

    public void start(){
        Log.i("Teacher","Application is started as teacher");
        this.discoverable();
        this.acceptDevices();
    }
}
