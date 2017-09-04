package com.example.guti.socketclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main2Activity extends AppCompatActivity {
    /**
     * Controles
     * */
    private Button button_login;
    private Button siguiente;
    private EditText usuario;
    private EditText password;
    private Context context = this;

    /**
     * Puerto
     * */
    private static final int SERVERPORT = 8089;
    /**
     * HOST
     * */
    private static final String ADDRESS = "10.0.2.2";

    private Encryption encryption = Encryption.getDefault("This is a key123", "This is an IV456", new byte[16]);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button_login = ((Button) findViewById(R.id.button_login));
        siguiente = ((Button) findViewById(R.id.siguiente));
        usuario = ((EditText) findViewById(R.id.usuario));
        password = ((EditText) findViewById(R.id.password));

        button_login.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if(usuario.getText().toString().length()>0 && password.getText().toString().length()>0){
                            MyATaskCliente myATaskYW = new MyATaskCliente(context);
                            //myATaskYW.execute(editText.getText().toString());
                            myATaskYW.execute(usuario.getText().toString(), password.getText().toString());
                        }else{
                            Toast.makeText(context, "Escriba Usuario y Contraseña ", Toast.LENGTH_LONG).show();
                        }


                    }
                });

        siguiente.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        cambiar_actividad(siguiente);

                    }
                });

    }//end:onCreate


    public void cambiar_actividad(View view) {
        Intent intent = new Intent(this, Main3Activity.class);
       /* EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);*/
        startActivity(intent);
    }


}

