package com.lannbox.rfduinotest;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;


public class viewMetricsActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_metrics);


        new getMetricsTask(this).execute();


        CharSequence text = "Please wait while we load your latest metrics!";
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context,text,duration);
        toast.show();

        WebView graphView = (WebView) findViewById(R.id.graphView);
        graphView.getSettings().setJavaScriptEnabled(true);
        graphView.loadUrl("http://ec2-35-161-86-195.us-west-2.compute.amazonaws.com/chart/");


    }
    private void updateBumpinessUI(String stringToSet) {
        TextView bumpTextValue = (TextView) findViewById(R.id.bumpValue);
        bumpTextValue.setText(stringToSet);
    }
}
