package com.example.khseob0715.sanfirst.ServerConn;

import android.content.Context;
import android.os.AsyncTask;

import com.example.khseob0715.sanfirst.UserActivity.LoginActivity;

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
 * Created by Kim Jin Hyuk on 2018-02-18.
 */

public class UserConnReq {
    public static String url = null;

    OkHttpClient client = new OkHttpClient();

    public static String responseBody = null;

    private static Context context;

    int addr = 0;

    private int error_message = 0;
    public UserConnReq(){
    }

    public UserConnReq(Context c) {
        this.context = c;
    }

    public void ConnRequest_Asycn(final int add, final int usn, final int dsn) {
        switch (add)    {
            /*
            Connect request -> conrequestbyuserapp
            Disconnect -> disconnectuserapp
            Accepatance -> connectbyuserapp
            Waiting -> disconnectuserapp
            //-----------------------------------------------
            Connect request -> conrequestbydoctorapp
            Disconnect -> disconnectdoctorapp
            Accepatance -> connectbydoctorapp
            Waiting -> disconnectdoctorapp
            */
            case 0 : {
                url = "http://teama-iot.calit2.net/conrequestbyuserapp";
                break;
            }
            case 1 : {
                url = "http://teama-iot.calit2.net/disconnectbyuserapp";
                break;
            }
            case 2 : {
                url = "http://teama-iot.calit2.net/connectbyuserapp";
                break;
            }
            case 3 : {
                url = "http://teama-iot.calit2.net/disconnectbyuserapp";
                break;
            }
        }

        (new AsyncTask<LoginActivity, Void, String>() {

            @Override
            protected String doInBackground(LoginActivity... mainActivities) {
                UserConnReq.ConnectServer connectServerPost = new UserConnReq.ConnectServer();
                connectServerPost.requestPost(url, usn, dsn);
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

        public int requestPost(String url, int usn, int dsn) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("usn", String.valueOf(usn))
                    .add("dsn", String.valueOf(dsn)).build();


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
