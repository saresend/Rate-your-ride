package com.lannbox.rfduinotest;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
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
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //Setting network configurations
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");

            urlConnection.setDoOutput(true);

            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());



        }
        catch(Exception e) {
            Log.e("asdf",e.toString());
        }
        try {



        }
        catch(Exception e) {

        }

        return null;
    }

}
