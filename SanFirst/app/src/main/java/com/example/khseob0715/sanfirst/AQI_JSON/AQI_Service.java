package com.example.khseob0715.sanfirst.AQI_JSON;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

// --------------------------------------------------------receive AQI api is success, and now is not using
public class AQI_Service extends Service {

    public static final int LOAD_SUCCESS = 101;

    public AQI_Service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
/*
    private ProgressDialog progressDialog;
    private TextView textviewJSONText;

    //private String REQUEST_URL = SEARCH_URL + Lat + Lon + Key + KeyValue;
    private String REQUEST_URL = "https://api.waqi.info/feed/geo:32.8824;-117.2384/?token=9f64560eb24586831e1167b1c0e8ecddb1193014";
    //private String REQUEST_URL = "https://tiles.breezometer.com/3/27.154478/-127.548775.png?key=c53444784e804d6b8a59a8ba6615f049";
    //private String REQUEST_URL = "https://api.breezometer.com/baqi/?lat=40.7324296&lon=-73.9977264&key=c53444784e804d6b8a59a8ba6615f049";



    @Override
    public void onCreate() {
        super.onCreate();
        //----- 여기 원래 버튼 누르면 실행되는거임
        startgetjson();
        
    }

    private void startgetjson() {
        //작업스레드 생성(매듭 묶는과정)
        getJSON JsonRunnable = new getJSON();
        Thread aqiThread = new Thread(JsonRunnable);
        aqiThread.setDaemon(true);
        aqiThread.start();
    }

    public class getJSON implements Runnable {
        @Override
        public void run() {
            while (true) {
                Log.e("Run", "Run");
                String result;

                try {
                    URL url = new URL(REQUEST_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.connect();


                    int responseStatusCode = httpURLConnection.getResponseCode();

                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();

                    }


                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line;


                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();
                    httpURLConnection.disconnect();

                    result = sb.toString().trim();


                } catch (Exception e) {
                    result = e.toString();
                }

                Log.e("send msg", "send msg");
                Message message = aqihandler.obtainMessage(LOAD_SUCCESS, result);
                aqihandler.sendMessage(message);

                try{
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    android.os.Handler aqihandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == LOAD_SUCCESS) {
                Log.e("JsonString", "JsonString");
                String jsonString = (String)msg.obj;
                textviewJSONText.setText(jsonString);
            }
        }
    };
*/
}
