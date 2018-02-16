package com.example.khseob0715.sanfirst.ServerConn;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.khseob0715.sanfirst.navi_fragment.Fragment_AQIHistory;

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
 * Created by Aiden on 2018-02-15.
 */


public class ReceiveAQI_ChartData {

    public static final String url = "http://teama-iot.calit2.net/aqiapp";

    OkHttpClient client = new OkHttpClient();

    public static String responseBody = null;

    private static Context context;

    private int error_message = 0;

    private double total_PM_value = 0;
    private double total_CO_value = 0;
    private double total_NO_value = 0;
    private double total_SO_value = 0;
    private double total_O3_value = 0;
    private double total_TP_value = 0;

    private int date_count = 0;

    public ReceiveAQI_ChartData() {
    }

    public ReceiveAQI_ChartData(Context c) {
        this.context = c;
    }

    public void ReceiveAQI_ChartData_Asycn(final int usn, final String fdate, final String ldate) {
        (new AsyncTask<Fragment_AQIHistory, Void, String>() {

            @Override
            protected String doInBackground(Fragment_AQIHistory... mainActivities) {
                ConnectServer connectServerPost = new ConnectServer();
                connectServerPost.requestPost(url, usn, fdate, ldate);
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

        public int requestPost(String url, int usn, String fdate, String ldate) {
            Log.e("AQI request", "go");
            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder()
                    .add("usn", String.valueOf(usn))
                    .add("fdate", fdate)
                    .add("ldate", ldate).build();

            Log.e("RequestBody", requestBody.toString());

            total_PM_value = 0;
            total_CO_value = 0;
            total_NO_value = 0;
            total_SO_value = 0;
            total_O3_value = 0;
            total_TP_value = 0;
            date_count = 0;

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
                        Log.e("message", Message + "/" + responseBody);

                        JSONArray AQIData = jsonObject.getJSONArray("data");
                        Log.e("AQIData.length = ", String.valueOf(AQIData.length()));

                        String compare = "1";
                        Fragment_AQIHistory.Air_response_count = 0;


                        for (int i = 0; i < AQIData.length(); i++) {
                            JSONObject getAQIData = AQIData.getJSONObject(i);
                            String TS = getAQIData.getString("TS");
                            Log.i("substring", TS.substring(0, 10));

                            Double CO = getAQIData.getDouble("CO");
                            Double SO2 = getAQIData.getDouble("SO2");
                            Double NO2 = getAQIData.getDouble("NO2");
                            Double O3 = getAQIData.getDouble("O3");
                            Double PM = getAQIData.getDouble("PM2.5");
                            Double TEMP = getAQIData.getDouble("Temperature");

                            Double LAT = getAQIData.getDouble("LAT");
                            Double LNG = getAQIData.getDouble("LNG");

                            total_PM_value += PM;
                            total_CO_value += CO;
                            total_NO_value += NO2;
                            total_SO_value += SO2;
                            total_O3_value += O3;
                            total_TP_value += TEMP;

                            date_count++;
                            Log.i("AQIData = ", i + " / " + TS + " / " + CO + " / " + SO2 + " / " + NO2 + "/" + O3 + "/" + PM + "/" + TEMP + "/" + LAT + " / " + LNG);

                        }

                            Fragment_AQIHistory.AQI_date_items[Fragment_AQIHistory.Air_response_count] = compare;

                            Fragment_AQIHistory.pmValue = (int) total_PM_value / date_count;
                            Fragment_AQIHistory.coValue = (int) total_CO_value / date_count;
                            Fragment_AQIHistory.noValue = (int) total_NO_value / date_count;
                            Fragment_AQIHistory.soValue = (int) total_SO_value / date_count;
                            Fragment_AQIHistory.o3Value = (int) total_O3_value / date_count;
                            Fragment_AQIHistory.tpValue = (int) total_TP_value / date_count;

                            Fragment_AQIHistory.Air_response_count++;

                            total_PM_value = 0;
                            total_CO_value = 0;
                            total_NO_value = 0;
                            total_SO_value = 0;
                            total_O3_value = 0;
                            total_TP_value = 0;

                            date_count = 0;


                        } catch (JSONException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            return 0;
        }
    }
}