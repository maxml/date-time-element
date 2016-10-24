package com.maxml.datetime.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.maxml.datetime.DateTimeFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartDate = (TextView) findViewById(R.id.exDate);
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DateTimeFactory(MainActivity.this, mStartDate).start();
            }
        });
    }
}
