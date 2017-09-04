package com.example.guti.socketclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    private Switch switch1;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        switch1 = (Switch) findViewById(R.id.switch1);
        textView = (TextView) findViewById(R.id.textView);

        switch1.setChecked(false);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textView.setText("PRENDIDO");
                } else {
                    textView.setText("APAGADO");
                }
            }
        });

    }
}
