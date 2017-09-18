package com.example.guti.socketclient;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Main5Activity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE3 = "com.example.myfirstapp.MESSAGE3";

    private Button btn_validar_pin;
    private EditText pin;
    private Context context = this;
    private String response;
    private String user_pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        Intent intent = getIntent();
        user_pin = intent.getStringExtra(MainActivity.EXTRA_MESSAGE2);


        btn_validar_pin = ((Button) findViewById(R.id.btn_validar_pin));
        pin = ((EditText) findViewById(R.id.pin));


        btn_validar_pin.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if(pin.getText().toString().length()>0){
                            if (user_pin.compareTo(pin.getText().toString())== 0)
                            {
                                cambiar_main_activity(btn_validar_pin);
                            } else {
                                Toast.makeText(context, "PIN Incorrecto", Toast.LENGTH_LONG).show();
                            }

                        }else{
                            Toast.makeText(context, "Escriba su PIN ", Toast.LENGTH_LONG).show();
                        }


                    }
                });

    }

    private void cambiar_main_activity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE3, "true");
        startActivity(intent);
    }
}
