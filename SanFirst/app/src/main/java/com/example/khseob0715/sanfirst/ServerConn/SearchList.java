package com.example.khseob0715.sanfirst.ServerConn;

import android.content.Context;
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

/**
 * Created by Kim Jin Hyuk on 2018-02-17.
 */

public class SearchList {
    public static String url = null;

    OkHttpClient client = new OkHttpClient();

    public static String responseBody = null;

    private static Context context;

    private int error_message = 0;
    public SearchList(){
    }

    public SearchList(Context c) {
        this.context = c;
    }

    public void SearchList_Asycn(final int who, final String type, final String value) {
        if(who == 0)    {
            url = "http://teama-iot.calit2.net/usersearchapp";
        }   else if(who == 1)    {
            url = "http://teama-iot.calit2.net/doctorsearchapp";
        }
        (new AsyncTask<UserActivity, Void, String>() {

            @Override
            protected String doInBackground(UserActivity... mainActivities) {
                SearchList.ConnectServer connectServerPost = new SearchList.ConnectServer();
                connectServerPost.requestPost(url, type, value);
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

        public int requestPost(String url, String type, String value) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("type", type)
                    .add("value", value)
                    .build();

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
                        Log.e("Response_Error", "Response Body is " + responseBody);

                        JSONObject jsonObject = new JSONObject(responseBody);

                        String Message = jsonObject.getString("message");

                        int usn = Integer.parseInt(jsonObject.getString("usn"));

                        String ID = jsonObject.getString("email");
                        String Phone = jsonObject.getString("phone");
                        int Gender_num = jsonObject.getInt("gender");
                        String Bitrh = jsonObject.getString("birth");
                        String Fname = jsonObject.getString("fname");
                        String Lname = jsonObject.getString("lname");
                        int Conn_state = jsonObject.getInt("CONN_state");

                        if(Gender_num == 0) {
                            String Gender = "Female";
                        }   else if (Gender_num == 1)   {
                            String Gender = "Male";
                        }

                    } catch (IOException e) {
                        e.printStackTrace();

                    } catch (JSONException e) {
                        // 로그인이 틀렸을 때,
                        e.printStackTrace();
                    }
                }
            });
            return 0;
        }
    }
}
