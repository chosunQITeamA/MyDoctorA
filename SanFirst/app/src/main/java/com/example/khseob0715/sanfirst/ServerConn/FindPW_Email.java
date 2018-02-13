package com.example.khseob0715.sanfirst.ServerConn;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.khseob0715.sanfirst.UserActivity.FindPWCodeActivity;
import com.example.khseob0715.sanfirst.UserActivity.FindPWEmailActivity;

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

import static com.example.khseob0715.sanfirst.UserActivity.FindPWEmailActivity.FindPWEmailContext;

/**
 * Created by Kim Jin Hyuk on 2018-02-12.
 */

public class FindPW_Email {
    public static final String url = "http://teama-iot.calit2.net/fmailcheckapp";

    OkHttpClient client = new OkHttpClient();

    public static String responseBody = null;

    public void findpw_Asycn(final String email){
        (new AsyncTask<FindPWEmailActivity, Void, String>(){

            @Override
            protected String doInBackground(FindPWEmailActivity... mainActivities) {
                FindPW_Email.ConnectServer connectServerPost = new FindPW_Email.ConnectServer();
                connectServerPost.requestPost(url, email);
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

        public int requestPost(String url, final String email) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("email", email).build();
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
                            int usn = jsonObject.getInt("usn");
                            String code = jsonObject.getString("code");
                            FindPW_email_Success(email, usn, code);


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

    private void FindPW_email_Success(String email, int usn, String code) {
        Intent intent = new Intent(FindPWEmailContext.getApplicationContext(), FindPWCodeActivity.class);
        intent.putExtra("usn", usn);
        intent.putExtra("email", email);
        intent.putExtra("code", code);

        FindPWEmailContext.startActivity(intent);
    }
}
