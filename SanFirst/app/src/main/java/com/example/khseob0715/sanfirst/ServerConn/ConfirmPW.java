package com.example.khseob0715.sanfirst.ServerConn;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.khseob0715.sanfirst.R;
import com.example.khseob0715.sanfirst.UserActivity.UserActivity;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_Account;

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

import static com.example.khseob0715.sanfirst.UserActivity.UserActivity.UserActContext;


/**
 * Created by Kim Jin Hyuk on 2018-02-08.
 */

public class ConfirmPW {
    public static final String url = "http://teama-iot.calit2.net/chkpwdapp";

    OkHttpClient client = new OkHttpClient();

    public static String responseBody = null;

    public void Confirmpw_Asycn(final int usn, final String PW){
        (new AsyncTask<UserActivity, Void, String>(){

            @Override
            protected String doInBackground(UserActivity... mainActivities) {
                ConfirmPW.ConnectServer connectServerPost = new ConfirmPW.ConnectServer();
                connectServerPost.requestPost(url, usn, PW);
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

        return ;
    }

    class ConnectServer {
        //Client 생성

        public int requestPost(String url, final int usn, String password) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("usn", String.valueOf(usn))
                    .add("pwd", password).build();
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

                        if(Message.equals("Success"))   {
                            changePW(usn);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Log.e("aaaa", "Response Body is " + response.body().string());

                }
            });
            return 0;
        }
    }


    public void changePW(int usn) {
        Fragment fragment = null;
        fragment = new Fragment_Account();
        FragmentTransaction ft = UserActContext.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout, fragment);
        ft.commit();
        Log.e("changepw", "Changepw");
    }
}

