package com.example.khseob0715.sanfirst.ServerConn;

import android.os.AsyncTask;

import com.example.khseob0715.sanfirst.UserActivity.UserActivity;
import com.example.khseob0715.sanfirst.navi_fragment.Fragment_SearchDoctor;

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

                        JSONArray data = jsonObject.getJSONArray("data");

                        Fragment_SearchDoctor.connection_count = 0;
                        Fragment_SearchDoctor.accepatance_count = 0;
                        Fragment_SearchDoctor.waiting_count = 0;

                        for(int i=0; i<data.length(); i++)    {
                            JSONObject userdata = data.getJSONObject(i);

                            int usn = userdata.getInt("usn");

                            String ID = userdata.getString("email");
                            String Fname = userdata.getString("fname");
                            String Lname = userdata.getString("lname");
                            String Birth = userdata.getString("birth");
                            int Gender_num = userdata.getInt("gender");
                            String Phone = userdata.getString("phone");
                            int requestingusn = userdata.getInt("requestingUSN");
                            int requestedusn = userdata.getInt("requestedUSN");
                            int conn_state = userdata.getInt("CONN_state");

                            String Gender = null;

                            if (Gender_num == 0) {
                                Gender = "Female";
                            } else if (Gender_num == 1) {
                                Gender = "Male";
                            }

                            if(conn_state == 1){ // 연결 된 것.
                                Fragment_SearchDoctor.Connect_fname[Fragment_SearchDoctor.connection_count] = Fname;
                                Fragment_SearchDoctor.Connect_lname[Fragment_SearchDoctor.connection_count] = Lname;
                                Fragment_SearchDoctor.Connect_Gender[Fragment_SearchDoctor.connection_count] = Gender;
                                Fragment_SearchDoctor.Connect_Phone[Fragment_SearchDoctor.connection_count] = Phone;
                                Fragment_SearchDoctor.Connect_ID[Fragment_SearchDoctor.connection_count] = ID;
                                Fragment_SearchDoctor.Connect_old[Fragment_SearchDoctor.connection_count] = Birth;
                                Fragment_SearchDoctor.Connect_usn[Fragment_SearchDoctor.connection_count] = String.valueOf(usn);
                                Fragment_SearchDoctor.connection_count++;
                            }else if(conn_state == 0 && requestingusn == usn){ // 내가 기다리는 것.
                                Fragment_SearchDoctor.waiting_fname[Fragment_SearchDoctor.waiting_count] = Fname;
                                Fragment_SearchDoctor.waiting_lname[Fragment_SearchDoctor.waiting_count] = Lname;
                                Fragment_SearchDoctor.waiting_Gender[Fragment_SearchDoctor.waiting_count] = Gender;
                                Fragment_SearchDoctor.waiting_ID[Fragment_SearchDoctor.waiting_count] = ID;
                                Fragment_SearchDoctor.waiting_old[Fragment_SearchDoctor.waiting_count] = Birth;
                                Fragment_SearchDoctor.waiting_usn[Fragment_SearchDoctor.waiting_count] = String.valueOf(usn);
                                Fragment_SearchDoctor.waiting_count++;
                            }else if(conn_state == 0 && requestedusn == usn){  // myadapter 3
                                Fragment_SearchDoctor.accepatance_fname[Fragment_SearchDoctor.accepatance_count] = Fname;
                                Fragment_SearchDoctor.accepatance_lname[Fragment_SearchDoctor.accepatance_count] = Lname;
                                Fragment_SearchDoctor.accepatance_Gender[Fragment_SearchDoctor.accepatance_count] = Gender;
                                Fragment_SearchDoctor.accepatance_Phone[Fragment_SearchDoctor.accepatance_count] = Phone;
                                Fragment_SearchDoctor.accepatance_ID[Fragment_SearchDoctor.accepatance_count] = ID;
                                Fragment_SearchDoctor.accepatance_old[Fragment_SearchDoctor.accepatance_count] = Birth;
                                Fragment_SearchDoctor.accepatance_usn[Fragment_SearchDoctor.accepatance_count] = String.valueOf(usn);
                                Fragment_SearchDoctor.accepatance_count++;
                            }

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
