package com.example.khseob0715.sanfirst.ServerConn;

/**
 * Created by Kim Jin Hyuk on 2018-02-08.
 */

public class ConfirmPW {
    /*
    //public static final String url = "http://teama-iot.calit2.net/signinapp";

    OkHttpClient client = new OkHttpClient();

    public static String responseBody = null;

    public void signin_Asycn(final String ID, final String PW){
        (new AsyncTask<LoginActivity, Void, String>(){
            ProgressDialog Loginloading;

            @Override
            protected String doInBackground(LoginActivity... mainActivities) {
                ConfirmPW.ConnectServer connectServerPost = new ConfirmPW.ConnectServer();
                connectServerPost.requestPost(url, ID, PW);
                return responseBody;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Loginloading = ProgressDialog.show(LoginContext, "Please Wait", "Confirm Password...,", true, true);
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

        public int requestPost(String url, String usn, String password) {

            //Request Body에 서버에 보낼 데이터 작성
            final RequestBody requestBody = new FormBody.Builder().add("usn", usn).add("userpassword", password).build();
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
                        Log.e("message", Message);
                        Log.e("usn", String.valueOf(usn));
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
}
*/
}
