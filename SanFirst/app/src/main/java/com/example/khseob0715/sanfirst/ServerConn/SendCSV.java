package com.example.khseob0715.sanfirst.ServerConn;

import android.os.AsyncTask;
import android.util.Log;

import com.example.khseob0715.sanfirst.UserActivity.UserActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Kim Jin Hyuk on 2018-02-16.
 */

public class SendCSV {

    public static final String url = "http://teama-iot.calit2.net/heartcsvapp";
    OkHttpClient client = new OkHttpClient();
    public static String responseBody = null;
    private final MediaType MEDIA_TYPE_CSV = MediaType.parse("text/csv");

    public void SendCSV_Asycn(final File file) {
        (new AsyncTask<UserActivity, Void, String>() {

            @Override
            protected String doInBackground(UserActivity... mainActivities) {
                SendCSV.ConnectServer connectServerPost = new SendCSV.ConnectServer();
                connectServerPost.requestPost(url, file);
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

        public void requestPost(String url, File file) {

            //Request Body에 서버에 보낼 데이터 작성
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", "MyDoctorA.csv", RequestBody.create(MEDIA_TYPE_CSV, file))
                    .build();

            //RequestBody requestBody = new FormBody.Builder().add("email", id).add("password", password).build();

            Log.e("RequestBody", requestBody.toString());

            //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
            //Request request = new Request.Builder().url(url).post(requestBody).build();
            Request request = new Request.Builder().url(url)
                    .post(requestBody).build();

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
            /*
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                Log.e("SendCSV is Fail = ", String.valueOf(response));
                throw new IOException("Unexpected code " + response);
            }*/

            //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작성
            /*
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
            */
        }
    }
}
