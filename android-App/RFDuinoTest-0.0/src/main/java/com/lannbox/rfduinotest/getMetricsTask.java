package com.lannbox.rfduinotest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SamResendez on 10/29/16.
 */

public class getMetricsTask extends AsyncTask {

    public String bumpinessFactor;
    @Override
    protected String doInBackground(Object[] params) {
        try {
        String urlString = "Set-URL";
        String responseString;
        URL baseUrl = new URL(urlString);
        HttpURLConnection connector = (HttpURLConnection) baseUrl.openConnection();
        InputStream response = connector.getInputStream();
        StringBuilder sb = new StringBuilder();
        BufferedReader bufRead = new BufferedReader(new InputStreamReader(response));

        while ((responseString = bufRead.readLine()) != null) {
            sb.append(responseString);
        }
        Log.e("dasf", sb.toString());
        bumpinessFactor = sb.toString();
    } catch (Exception e) {
            Log.e("Errors:", e.toString());
        }

        return bumpinessFactor;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.e("onPostExecute:",o.toString());

    }
}
