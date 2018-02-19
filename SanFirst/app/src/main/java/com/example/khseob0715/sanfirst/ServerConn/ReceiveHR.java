package com.example.khseob0715.sanfirst.ServerConn;

import android.content.Context;
import android.os.AsyncTask;

import com.example.khseob0715.sanfirst.navi_fragment.Fragment_AQIHistory;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_HRHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Aiden on 2018-02-07.
 */


public class ReceiveHR {

    public static final String url = "http://teama-iot.calit2.net/heartdatasearch";

    OkHttpClient client = new OkHttpClient();

    public static String responseBody = null;

    private static Context context;

    private int error_message = 0;

    private double total_Heart_rate = 0;
    private double total_RR_rate = 0;
    private int date_count = 0;

    public ReceiveHR(){
    }

    public ReceiveHR(Context c) {
        this.context = c;
    }

    public void ReceiveHR_Asycn(final int usn, final String fdate, final String ldate) {
        (new AsyncTask<Fragment_HRHistory, Void, String>() {

            @Override
            protected String doInBackground(Fragment_HRHistory... mainActivities) {
                ConnectServer connectServerPost = new ConnectServer();
                connectServerPost.requestPost(url, usn, fdate, ldate);
                return responseBody;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result) {

            }
        }).execute();

        return;
    }

    class ConnectServer {//Client 생성

        public int requestPost(String url, int usn, String fdate, String ldate) {
            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("usn", String.valueOf(usn))
                    .add("fdate", fdate)
                    .add("ldate", ldate).build();


            total_Heart_rate = 0;
            total_RR_rate = 0;
            date_count = 0;

            //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
            Request request = new Request.Builder().url(url).post(requestBody).build();

            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String Message = jsonObject.getString("message");

                        JSONArray HRData = jsonObject.getJSONArray("data");
                        String compare = "1";
                        Fragment_HRHistory.response_count = 0;

                        for(int i=0; i<HRData.length(); i++)    {

                            JSONObject getHRData = HRData.getJSONObject(i);
                            String TS = getHRData.getString("TS");

                            Double Heart_rate = getHRData.getDouble("Heart_rate");
                            Double RR_rate = getHRData.getDouble("RR_rate");
                            Double LAT = getHRData.getDouble("LAT");
                            Double LNG = getHRData.getDouble("LNG");

                            total_Heart_rate += Heart_rate;
                            total_RR_rate += RR_rate;
                            date_count++;

                            if(!compare.equals(TS.substring(0,10))){
                                compare = TS.substring(0,10);
                                Fragment_HRHistory.items[Fragment_HRHistory.response_count] = compare;
                                Fragment_HRHistory.HeartAvg[Fragment_HRHistory.response_count] = (int)total_Heart_rate / date_count;
                                Fragment_HRHistory.RRAvg[Fragment_HRHistory.response_count] = (int)total_RR_rate / date_count;
                                Fragment_HRHistory.response_count++;

                                total_Heart_rate = 0;
                                total_RR_rate = 0;
                                date_count = 0;
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            return 0;
        }
    }
}