package com.example.khseob0715.sanfirst.udoo_btchat;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by chony on 2018-02-04.
 */
public class BluetoothAQI extends Service{

    private static final String TAG = "BluetoothService";

    public final static String ACTION_SEND_AQI_DATA =
            "com.example.jeonghoon.googlemap.bluetooth.ACTION_SEND_AQI_DATA";


    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    private String address;
    private Intent bluetoothIntent;
    private BluetoothAdapter mBluetoothAdapter=null;
    private BluetoothChatService mChatService=null;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i(TAG,"onCreate");


    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i(TAG, "onStartCommand");
        bluetoothIntent=intent;
        try {
            address = bluetoothIntent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

            Log.i(TAG, "address : "+address);

            mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter==null)onDestroy();

            if(!address.equals(" have been paired")){
                BluetoothDevice device=mBluetoothAdapter.getRemoteDevice(address);

                if (mChatService == null) {
                    setupChat();
                }
                mChatService.connect(device, true);

                if (mChatService != null) {
                    // Only if the state is STATE_NONE, do we know that we haven't started already
                    if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                        Log.i(TAG, "state : "+mChatService.getState());
                        // Start the Bluetooth chat services
                        mChatService.start();
                    }
                }
                setupChat();
            }else{
                Toast.makeText(this, "Please turn on the Bluetooth",Toast.LENGTH_SHORT).show();
            }
        }catch(NullPointerException nex){
            Log.e(TAG, "Exception_onStart : "+nex.getMessage());
        }
/*
        int count=0;
        String sendData="";
        while(count<=3){
            double ran=Math.random()*500;
            sendData=String.valueOf(ran);
            for(int i=0;i<8;i++){
                ran=Math.random()*500;
                sendData=sendData+","+String.valueOf(ran);
            }
            Log.i(TAG,"data : "+sendData);
            broadcastUpdate(ACTION_SEND_AQI_DATA, sendData);
            SystemClock.sleep(1000);
            count++;
        }
*/
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendMessage(String message) {//메시지 전송
        // Check that we're actually connected before trying anything
        // 연결재확인
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Log.d(TAG, "state : "+mChatService.getState());
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            System.getProperty("line.separator");
            byte[] send = (message.replace("[\r\n]","")+"\n").getBytes();
            mChatService.write(send);//메시지 전송

            // Reset out string buffer to zero and clear the edit text field
            //에딧 텍스트 클리어&리스트에 메시지 추가
            mOutStringBuffer.setLength(0);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mChatService!=null){
            mChatService.stop();
        }
        Log.i(TAG,"onDestroy");
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the BluetoothChatService to perform bluetooth connections
        if(mChatService==null)
            mChatService = new BluetoothChatService(getApplicationContext(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");

    }
    public void broadcastUpdate(final String action, String data) {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(ACTION_SEND_AQI_DATA, data);
        sendBroadcast(intent);
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            Log.i(TAG, "Connected!!! state : "+mChatService.getState());

                            String message="Server connected!";
                            String sendData="sensor";

                            /*for(int i=0;i<9;i++){
                                ran=Math.random()*500;
                                sendData=sendData+","+String.valueOf(ran);
                            }*/
                            Log.i(TAG,"data : "+sendData);

                            byte[] send = (sendData.replace("[\r\n]","")+"\n").getBytes();
                            mChatService.write(send);
                            Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            Log.i(TAG, "Connecting!!!");
                            Toast.makeText(getApplicationContext(),"Connecting",Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            Log.i(TAG, "NoState!!!");
                            Toast.makeText(getApplicationContext(),"Can't Connect",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE://쓰기
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Log.i(TAG, "Write!!!"+writeMessage);
                    break;
                case Constants.MESSAGE_READ://읽기
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    broadcastUpdate(ACTION_SEND_AQI_DATA, readMessage);

                    Log.i(TAG, "Read!!!"+readMessage);
                    break;
                default:
                    break;
            }
        }
    };

}