package com.example.khseob0715.sanfirst.ServerConn;

import android.content.Intent;
import android.os.AsyncTask;

import com.example.khseob0715.sanfirst.UserActivity.SignUPActivity;
import com.example.khseob0715.sanfirst.UserActivity.SignUPCodeActivity;
import com.example.khseob0715.sanfirst.UserActivity.SignUPEmailActivity;

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

import static com.example.khseob0715.sanfirst.UserActivity.SignUPEmailActivity.SignupEmailContext;

/**
 * Created by Kim Jin Hyuk on 2018-02-08.
 */

public class SignUp {


    OkHttpClient client = new OkHttpClient();

    public static String responseBody = null;

    public void signup_email_Asycn(final String email){
        final String url = "http://teama-iot.calit2.net/mailcheckapp";
        (new AsyncTask<SignUPEmailActivity, Void, String>(){

            @Override
            protected String doInBackground(SignUPEmailActivity... mainActivities) {
                SignUp.ConnectServer connectServerPost = new SignUp.ConnectServer();
                connectServerPost.requestPost(url, email);

                return responseBody;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String result){            }
        }).execute();

        return ;
    }

    public void signup_Asycn(final String email, final String pwd, final String fname, final String lname, final String gender, final String birth, final String phone){
        final String url = "http://teama-iot.calit2.net/signupapp";
        (new AsyncTask<SignUPActivity, Void, String>(){

            @Override
            protected String doInBackground(SignUPActivity... mainActivities) {
                SignUp.ConnectServer connectServerPost = new SignUp.ConnectServer();
                connectServerPost.requestPost(url, email, pwd, fname, lname, gender, birth, phone);
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

        public int requestPost(String url,final String email) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("email", email)
                    .build();
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

                        if(Message.equals("Success")) {
                            String code = jsonObject.getString("code");
                            signup_email_Success(email, code);
                            SignUPEmailActivity.Duplicate_check = 0;

                        }   else    {

                            SignUPEmailActivity aActivity = (SignUPEmailActivity)SignUPEmailActivity.AActivity;

                            SignUPEmailActivity.Duplicate_check = 1;

                            Intent intent = new Intent(SignupEmailContext.getApplicationContext(), SignUPEmailActivity.class);
                            aActivity.finish();

                            SignupEmailContext.startActivity(intent);


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        SignUPEmailActivity aActivity = (SignUPEmailActivity)SignUPEmailActivity.AActivity;

                        SignUPEmailActivity.Duplicate_check = 1;

                        Intent intent = new Intent(SignupEmailContext.getApplicationContext(), SignUPEmailActivity.class);
                        aActivity.finish();

                        SignupEmailContext.startActivity(intent);
                        e.printStackTrace();
                    }
                }
            });
            return 0;
        }



        public int requestPost(String url, String email, String pwd, String fname, String lname, String gender, String birth, String phone) {
            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("email", email)
                    .add("pwd", pwd)
                    .add("fname", fname)
                    .add("lname", lname)
                    .add("gender", gender)
                    .add("birth", birth)
                    .add("phone", phone)
                    .build();
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


    private void signup_email_Success(String email, String code) {
        Intent intent = new Intent(SignupEmailContext.getApplicationContext(), SignUPCodeActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("code", code);

        SignUPEmailActivity aActivity = (SignUPEmailActivity)SignUPEmailActivity.AActivity;
        aActivity.finish();

        SignupEmailContext.startActivity(intent);
    }

}
