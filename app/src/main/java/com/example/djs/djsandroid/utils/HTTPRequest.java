package com.example.djs.djsandroid.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by kien on 3/21/2017.
 */

public class HTTPRequest extends AsyncTask<String, Void, String> {
    static OkHttpClient client = new OkHttpClient();
    public AsyncResponse delegate = null;
    Context context;
    boolean isOnline = true;

    public HTTPRequest(AsyncResponse delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
        if(!AppUltils.isOnline(context)) {
            AppDialogManager.ShowDialogError(context, "Error internet connection", "Please connect to internet and try again");
            isOnline = false;
//            AppTransaction.Toast(context, "Please connect to internet and try again!");
        }
    }

    protected String doInBackground(String... params) {
        String result = "";
//        if(isOnline) {
            try {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                Request request = null;
                if (params.length > 1) {
                    Log.d("kien", "param 1 " + params[1]);
                    RequestBody body = RequestBody.create(JSON, params[1]);
                    request = new Request.Builder()
                            .url(params[0])
                            .post(body)
                            .build();
                } else {
                    Log.d("kien", "param 0 " + params[0]);
                    request = new Request.Builder()
                            .url(params[0])
                            .build();
                }


                Response response = client.newCall(request).execute();

                if (response.body() != null) {
                    result = response.body().string();
                    Log.d("kien", "result: " + response.body().string());
                } else Log.d("kien", "result: null");
            } catch (Exception e) {
                Log.d("kien", "error network: " + e.toString());
            }
//        }

        return result;
    }




    protected void onPostExecute(String s) {
        delegate.processFinish(s);
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }
}

