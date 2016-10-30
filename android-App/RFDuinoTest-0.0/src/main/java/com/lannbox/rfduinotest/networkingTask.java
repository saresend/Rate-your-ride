package com.lannbox.rfduinotest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by SamResendez on 10/29/16.
 */

public class networkingTask extends AsyncTask<String,Integer,String> {

    @Override
    protected String doInBackground(String...params) {

        String urlString = params[0];

        InputStream inputStream = null;

        if(params[1] == "1") {
            try {

                String responseString;
                String baseString = "http://ec2-35-161-86-195.us-west-2.compute.amazonaws.com/test_input";
                URL baseUrl = new URL(baseString);
                HttpURLConnection connector = (HttpURLConnection) baseUrl.openConnection();
                InputStream response = connector.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader bufRead = new BufferedReader(new InputStreamReader(response));

                while((responseString = bufRead.readLine()) != null) {
                    sb.append(responseString);
                }
                Log.e("dasf",sb.toString());
            }
            catch(Exception e) {
                Log.e("Errors:", e.toString());
            }
        }

        try {

            String urlParameters = "stringData=HelloWorld";
            byte[] postData = urlParameters.getBytes();

            URL url = new URL(urlString);
            Log.e("URL: ",url.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            //Setting network configurations
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            OutputStream outputPost = urlConnection.getOutputStream();

            outputPost.write(postData);


            outputPost.flush();

            urlConnection.getResponseCode();
            if(urlConnection.getResponseCode() == 200) {
                Log.e("Networking:","My boy, we out here");
            }


        }
        catch(Exception e) {
            Log.e("asdf",e.toString());
        }

       return null;
    }

}
