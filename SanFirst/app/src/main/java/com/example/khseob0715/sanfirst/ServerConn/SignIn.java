package com.example.khseob0715.sanfirst.ServerConn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.khseob0715.sanfirst.UserActivity.LoginActivity;
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

import static com.example.khseob0715.sanfirst.UserActivity.LoginActivity.LoginContext;

/**
     * Created by Kim Jin Hyuk on 2018-02-07.
     */

    public class SignIn {
        public static final String url = "http://teama-iot.calit2.net/signinapp";

        OkHttpClient client = new OkHttpClient();

        public static String responseBody = null;

        public void signin_Asycn(final String ID, final String PW){
            (new AsyncTask<LoginActivity, Void, String>(){
                ProgressDialog Loginloading;

                @Override
                protected String doInBackground(LoginActivity... mainActivities) {
                    ConnectServer connectServerPost = new ConnectServer();
                    connectServerPost.requestPost(url, ID, PW);
                    return responseBody;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    Loginloading = ProgressDialog.show(LoginContext, "Please Wait", "Sign In...,", true, true);
                }

                @Override
                protected void onPostExecute(String result) {
                    Loginloading.dismiss();
                }
            }).execute();

            return ;
        }

        class ConnectServer {
            //Client 생성

            public int requestPost(String url, String id, String password) {

                //Request Body에 서버에 보낼 데이터 작성
                final RequestBody requestBody = new FormBody.Builder().add("useremail", id).add("userpassword", password).build();
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
                            int usn = Integer.parseInt(jsonObject.getString("usn"));
                            String ID = jsonObject.getString("email");
                            String Fname = jsonObject.getString("fname");
                            String Lname = jsonObject.getString("lname");
                            Log.e("message", Message +"/"+usn+"/"+ID+"/"+Fname+"/"+Lname);
                            LoginSuccess(Message, usn, ID, Fname, Lname);;
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

    private void LoginSuccess(String message, int usn, String ID, String Fname, String Lname) {
            if(message.equals("Success"))  {
                String name = Fname + Lname;
                Intent intent = new Intent(LoginContext.getApplicationContext(), UserActivity.class);
                intent.putExtra("usn", usn);
                intent.putExtra("ID", ID);
                intent.putExtra("name", name);
                LoginContext.startActivity(intent);
            }   else    {
                Log.e("parentJArray", message);
            }
    }
}