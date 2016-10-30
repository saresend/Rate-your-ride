package com.lannbox.rfduinotest;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

public class viewMetricsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_metrics);

        new getMetricsTask().execute();


        CharSequence text = "Please wait while we load your latest metrics!";
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,text,duration);
        toast.show();

    }

}