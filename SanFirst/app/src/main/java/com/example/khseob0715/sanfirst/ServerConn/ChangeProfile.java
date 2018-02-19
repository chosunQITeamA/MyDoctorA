package com.example.khseob0715.sanfirst.ServerConn;

/**
 * Created by Kim Jin Hyuk on 2018-02-12.
 */
import android.os.AsyncTask;

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

/**
 * Created by Kim Jin Hyuk on 2018-02-08.
 */

public class ChangeProfile {
    public static final String url = "http://teama-iot.calit2.net/profileapp";

    OkHttpClient client = new OkHttpClient();

    public static String responseBody = null;

    public void changeprofile_Asycn(final int usn, final String fname, final String lname, final String birth, final String phone) {
        (new AsyncTask<UserActivity, Void, String>() {

            @Override
            protected String doInBackground(UserActivity... mainActivities) {
                ChangeProfile.ConnectServer connectServerPost = new ChangeProfile.ConnectServer();
                connectServerPost.requestPost(url, usn, fname, lname, birth, phone);
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

        public int requestPost(String url, final int usn, String fname, String lname, String birth, String phone) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("usn", String.valueOf(usn))
                    .add("fname", fname)
                    .add("lname", lname)
                    .add("birth", birth)
                    .add("phone", phone).build();
            //RequestBody requestBody = new FormBody.Builder().add("email", id).add("password", password).build();


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

                        if (Message.equals("Success")) {

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