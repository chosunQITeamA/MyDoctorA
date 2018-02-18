package com.example.khseob0715.sanfirst.ServerConn;

import android.os.AsyncTask;
import android.util.Log;

import com.example.khseob0715.sanfirst.UserActivity.UserActivity;

import org.json.JSONArray;
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

public class showConnectList {
    OkHttpClient client = new OkHttpClient();

    public String url;
    public static String responseBody = null;

    public void showConnectList_Asycn(int add, final int usn) {

        if(add == 0)    {
            url = "http://teama-iot.calit2.net/userlistapp";
        }   else if(add == 1)   {
            url = "http://teama-iot.calit2.net/doctorlistapp";
        }

        (new AsyncTask<UserActivity, Void, String>() {

            @Override
            protected String doInBackground(UserActivity... mainActivities) {
                showConnectList.ConnectServer connectServerPost = new showConnectList.ConnectServer();
                connectServerPost.requestPost(url, usn);
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

        public void requestPost(String url, int usn) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("usn", String.valueOf(usn))
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
                        Log.e("Response_Error", "Response Body is " + responseBody);

                        JSONObject jsonObject = new JSONObject(responseBody);

                        String Message = jsonObject.getString("message");

                        JSONArray data = jsonObject.getJSONArray("data");

                        for(int i=0; i<data.length(); i++)    {
                            JSONObject userdata = data.getJSONObject(i);
/*
                            int usn = Integer.parseInt(userdata.getString("USN"));
                            String ID = userdata.getString("User_email");
                            int Gender_num = userdata.getInt("Gender");
                            String Birth = userdata.getString("Birth");
                            String Fname = userdata.getString("Fname");
                            String Lname = userdata.getString("Lname");
                            String Gender = null;

                            if (Gender_num == 0) {
                                Gender = "Female";
                            } else if (Gender_num == 1) {
                                Gender = "Male";
                            }

                            Fragment_SearchDoctor.Search_fname[i] = Fname;
                            Fragment_SearchDoctor.Search_lname[i] = Lname;
                            Fragment_SearchDoctor.Search_ID[i] = ID;
                            Fragment_SearchDoctor.Search_Gender[i] = Gender;
                            Fragment_SearchDoctor.Search_old[i] = Birth;

                            Fragment_SearchDoctor.search_count++;
*/
                            Log.e("searchConnectList = ", i +"///"+ userdata);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
