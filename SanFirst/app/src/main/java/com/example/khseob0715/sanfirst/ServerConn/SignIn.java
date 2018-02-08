package com.example.khseob0715.sanfirst.ServerConn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.khseob0715.sanfirst.UserActivity.LoginActivity;
import com.example.khseob0715.sanfirst.UserActivity.UserMainActivity;

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

import static com.example.khseob0715.sanfirst.UserActivity.LoginActivity.LoginContext;

/**
 * Created by Kim Jin Hyuk on 2018-02-07.
 */

public class Signin {

    public static final String url = "http://teamb-iot.calit2.net/api/auths";
    //public static final String url = "http://teama-iot.calit2.net/api/auths";

    static String responseBody;

    public static void firstAction(final String ID, final String PW){
        (new AsyncTask<LoginActivity, Void, Void>(){
            ProgressDialog Loginloading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Loginloading = ProgressDialog.show(LoginContext, "Please Wait", "Sign In...,", true, true);
            }

            @Override
            protected Void doInBackground(LoginActivity... params) {
                ConnectServer connectServerPost = new ConnectServer();
                //connectServerPost.requestPost("user id 값 입력", "user password 값 입력");
                connectServerPost.requestPost(ID, PW);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                Loginloading.dismiss();
                //super.onPostExecute(result);
            }
        }).execute();

        return;
    }

    static class ConnectServer {
        //Client 생성
        OkHttpClient client = new OkHttpClient();

        int requestPost(String email, String password) {
            Log.e("IDPW", email + " / " + password);
            int result = 0;
            //Request Body에 서버에 보낼 데이터 작성
            try {

            /*byte[] emailBytes = email.getBytes("UTF-8");
            byte[] passwordBytes = password.getBytes("UTF-8");*/

                RequestBody requestBody = new FormBody.Builder().add("email", email)
                        .add("password", password).build();

                //작성한 Request Body와 데이터를 보낼 url을 Request에 붙임
                final Request request = new Request.Builder().url(url).post(requestBody)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded").build();

                //request를 Client에 세팅하고 Server로 부터 온 Response를 처리할 Callback 작
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        responseBody = response.body().string();

                        Log.d("aaaa", "Response Body is " + responseBody);

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(1000);
                JSONObject json = new JSONObject(responseBody);

                if (json.has("meta")) {
                    // Error
                    result = 2;
                } else if (json.has("data")) {
                    JSONObject jjson = json.getJSONObject("data");
                    result = 1;
                    Log.e("JsonObj", String.valueOf(jjson));
                    /*
                    api = jjson.getString(TAG_API);
                    setapi(api);
                    */
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //LoginActivity.ActivityChange(result);
            ActivityChange(result);
            return 0;
        }
    }

    public static void ActivityChange(int result)  {
        if(result == 1) {
            Log.e("Login status", "LoginSuccess!!");
            Intent intent = new Intent(LoginContext.getApplicationContext(), UserMainActivity.class);
            LoginContext.startActivity(intent);
        }   else if (result == 2)    {
            Log.e("Login status", "LoginFail!!");
        }
    }
}
