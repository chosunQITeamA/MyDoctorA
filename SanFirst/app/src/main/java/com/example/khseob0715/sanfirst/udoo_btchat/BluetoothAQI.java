package com.example.khseob0715.sanfirst.udoo_btchat;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.khseob0715.sanfirst.UserActivity.UserMainActivity;

/**
 * Created by enemf on 2018-02-04.
 */

public class BluetoothAQI extends Service{

    private static final String TAG = "BluetoothService";
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

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
        Log.i("BluetoothService","onCreate");

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("BluetoothService", "onBind");
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i("BluetoothService", "onStartCommand");
        bluetoothIntent=intent;
        try {
            address = bluetoothIntent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

            Log.i("BluetoothService", "address : "+address);

            mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
            if(mBluetoothAdapter==null)onDestroy();

            BluetoothDevice device=mBluetoothAdapter.getRemoteDevice(address);

            mChatService = new BluetoothChatService(getApplicationContext(), mHandler);
            mOutStringBuffer = new StringBuffer("");

            mChatService.connect(device, true);

            setupChat();
            if (mChatService != null) {
                // Only if the state is STATE_NONE, do we know that we haven't started already
                if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                    // Start the Bluetooth chat services
                    mChatService.start();
                }
            }
        }catch(NullPointerException nex){
            Log.e(TAG, "Exception : "+nex.getMessage());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mChatService!=null){
            mChatService.stop();
        }
        Log.i("BluetoothService","onDestroy");
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getApplicationContext(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            Log.i(TAG, "Connected!!!"+mChatService.getState());

                            if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
                                Log.e(TAG, "not_connected!!!");
                                return;
                            }

                            System.getProperty("line.separator");

                            byte[] send=("If u recv this msg, plz kakaotalk to me.".replace("[\r\n]", "")+"\n").getBytes();
                            mChatService.write(send);

                            mOutStringBuffer.setLength(0);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            Log.i(TAG, "Connecting!!!");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE://쓰기
                    byte[] writeBuf = (byte[]) msg.obj;
                    Log.i(TAG, "Write!!!");
                    // construct a string from the buffer
                    //String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add(writeMessage);
                    break;
                case Constants.MESSAGE_READ://읽기
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.i(TAG, "Read!!!"+readMessage);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + " : " + readMessage);

                    String[] BTSplit = readMessage.split(",");  // Split Message (split word : ,)
                    int temperatureval = Integer.valueOf(BTSplit[0]);   // receive temperatureval

                    int coval = Integer.valueOf(BTSplit[1]);
                    int so2val = Integer.valueOf(BTSplit[2]);
                    int o3val = Integer.valueOf(BTSplit[3]);
                    int no2val = Integer.valueOf(BTSplit[4]);
                    int pm25val = Integer.valueOf(BTSplit[5]);

                    UserMainActivity.udoo_temperature = temperatureval;
                    UserMainActivity.udoo_co = coval;
                    UserMainActivity.udoo_so2 = so2val;
                    UserMainActivity.udoo_o3 = o3val;
                    UserMainActivity.udoo_no2 = no2val;
                    UserMainActivity.udoo_pm25 = pm25val;

                    break;
                default:
                    break;
            }
        }
    };
}
