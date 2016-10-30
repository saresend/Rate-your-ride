package com.lannbox.rfduinotest;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SamResendez on 10/29/16.
 */

public class getMetricsTask extends AsyncTask {

    private Activity myActivity;

    public getMetricsTask(Activity activity) {
        this.myActivity = activity;
    }

    public String bumpinessFactor;
    @Override
    protected String doInBackground(Object[] params) {
        try {
        String urlString = "http://ec2-35-161-86-195.us-west-2.compute.amazonaws.com/bumpiness?sessionId="+globals.getInstance().getSessionID();
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
        globals.getInstance().setBumpinessString(sb.toString());
    } catch (Exception e) {
            Log.e("Errors:", e.toString());
        }

        return bumpinessFactor;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(o != null) {
            super.onPostExecute(o);

            String trunValue = o.toString().substring(0, 3);
            TextView bumpView = (TextView) myActivity.findViewById(R.id.bumpValue);
            bumpView.setText(trunValue);
        }
    }
}
