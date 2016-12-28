package com.example.colorpicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CustomColorPicker.OnColorChangedListener {

    TextView endTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        endTV = (TextView) findViewById(R.id.endTextView);
    }

    @Override
    public void colorChanged(int color) {
        endTV.setTextColor(color);
    }
}
