package com.example.khseob0715.sanfirst.ServerConn;

/**
 * Created by Kim Jin Hyuk on 2018-02-07.
 */

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignIn {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    String bowlingJson(String ID) {
        /*
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
                */
        String Query = "select USN, Hashed_PW From Users Where User_email Like="+ID;
        return Query;
    }

    public static void signin(String ID) throws IOException {
        SignIn signin = new SignIn();
        String json = signin.bowlingJson(ID);
        String response = signin.post("http://teama-iot@teama-iot.calit2.net", json);
        Log.e("signin-response == ", response);
    }
}