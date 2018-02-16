package com.example.khseob0715.sanfirst.ServerConn;

import android.os.AsyncTask;
import android.util.Log;

import com.example.khseob0715.sanfirst.UserActivity.UserActivity;

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

public class SendHR {
    public static final String url = "http://teama-iot.calit2.net/heartdataapp";

    OkHttpClient client = new OkHttpClient();

    public static String responseBody = null;

    public void SendHR_Asycn(final int usn, final String datetime, final Double lat, final Double lon, final Double hr , final Double rr) {
        (new AsyncTask<UserActivity, Void, String>() {

            @Override
            protected String doInBackground(UserActivity... mainActivities) {
                SendHR.ConnectServer connectServerPost = new SendHR.ConnectServer();
                connectServerPost.requestPost(url, usn, datetime, lat, lon, hr, rr);
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

    class ConnectServer {
        //Client 생성

        public void requestPost(String url, int usn, String datetime, Double lat, Double lon, Double hr, Double rr) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("USN", String.valueOf(usn))
                    .add("TS", datetime)
                    .add("LAT", String.valueOf(lat))
                    .add("LNG", String.valueOf(lon))
                    .add("HEART_RATE", String.valueOf(hr))
                    .add("RR_RATE", String.valueOf(rr))
                    .build();
            //RequestBody requestBody = new FormBody.Builder().add("email", id).add("password", password).build();

            Log.e("RequestBody", requestBody.toString());

            //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
            Request request = new Request.Builder().url(url).post(requestBody).build();

            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("error", "Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        responseBody = response.body().string();
                        Log.e("aaaa", "Response Body is " + responseBody);

                        JSONObject jsonObject = new JSONObject(responseBody);
                        String Message = jsonObject.getString("message");

                        if (Message.equals("Success")) {

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Log.e("aaaa", "Response Body is " + response.body().string());

                }
            });
        }
    }
}