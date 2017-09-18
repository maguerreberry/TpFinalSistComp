package com.example.guti.socketclient;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import java.util.concurrent.ExecutionException;

public class Main3Activity extends AppCompatActivity {

    private String key = "Bar12345Bar12345"; // 128 bit key
    private String initVector = "RandomInitVector"; // 16 bytes IV

    private Context context = this;
    private String response;
    private String user_data;

    private TextView estado;

    MyATaskCliente myATaskYW = new MyATaskCliente(context);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Intent intent = getIntent();
        user_data = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        estado = ((TextView) findViewById(R.id.estado_abriendo));

    }//end:onCreate

    public void cerrar_porton (View view)
    {


        try {
            String str_to_Send = Encryptor.encrypt(key,initVector,user_data + "-cerrar");
            response = myATaskYW.execute(str_to_Send).get();
            //Toast.makeText(context, "RESPUESTA SV: " + response, Toast.LENGTH_LONG).show();
            Log.i("SERVIDOR: ", Encryptor.decrypt(key, initVector,response));

            response = Encryptor.decrypt(key, initVector,response);
            if ("cerrando".compareTo(response) == 0) {
                estado.setText("Cerrando Porton");

            } else {
                estado.setText("Error...");
            }
        } catch (InterruptedException e) {
            Toast.makeText(context, "ERROR " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(context, "ERROR " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void abrir_porton (View view)
    {
        MyATaskCliente myATaskYW = new MyATaskCliente(context);

        try {
            String str_to_Send = Encryptor.encrypt(key,initVector,user_data + "-abrir");
            response = myATaskYW.execute(str_to_Send).get();
            //Toast.makeText(context, "RESPUESTA SV: " + response, Toast.LENGTH_LONG).show();
            Log.i("SERVIDOR: ", Encryptor.decrypt(key, initVector,response));

            response = Encryptor.decrypt(key, initVector,response);
            if ("abriendo".compareTo(response) == 0) {
                estado.setText("Abriendo Porton");
            } else {
                estado.setError("Error...");
            }
        } catch (InterruptedException e) {
            Toast.makeText(context, "ERROR " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(context, "ERROR " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void logout (View view)
    {
        String filepath = "MyFileCliente";
        String filename = "test.txt";
        File file = new File(getExternalFilesDir(filepath), filename);

        if (file.exists()){
            file.delete();
            cambiar_main_activity(view);
        }

    }

    private void cambiar_main_activity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra(EXTRA_MESSAGE3, "true");
        startActivity(intent);
    }

}
