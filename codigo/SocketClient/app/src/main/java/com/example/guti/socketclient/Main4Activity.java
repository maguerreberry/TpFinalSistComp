package com.example.guti.socketclient;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class Main4Activity extends AppCompatActivity {

    private Button btn_add_user;
    private EditText usuario;
    private EditText password;
    private Context context = this;
    private String response;
    private String user_data;

    private String key = "Bar12345Bar12345"; // 128 bit key
    private String initVector = "RandomInitVector"; // 16 bytes IV

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Intent intent = getIntent();
        user_data = intent.getStringExtra(Main2Activity.EXTRA_MESSAGE4);

        btn_add_user = ((Button) findViewById(R.id.btn_add_user));

        usuario = ((EditText) findViewById(R.id.usuario));
        password = ((EditText) findViewById(R.id.password));

        btn_add_user.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {

                        if(usuario.getText().toString().length()>0 && password.getText().toString().length()>0){
                            MyATaskCliente myATaskYW = new MyATaskCliente(context);
                            //myATaskYW.execute(editText.getText().toString());
                            try {
                                String str_to_send = user_data  + "-new_user-" +   usuario.getText().toString()+"-"+ password.getText().toString();
                                str_to_send = Encryptor.encrypt(key,initVector, str_to_send);
                                response = myATaskYW.execute(str_to_send).get();
                                response = Encryptor.decrypt(key, initVector,response);
                                //Toast.makeText(context, "RESPUESTA SV: " + response, Toast.LENGTH_LONG).show();
                                Log.i("SERVIDOR: ", Encryptor.decrypt(key, initVector,response));

                                if ("okay".compareTo(response) == 0)
                                {
                                    Toast.makeText(context, "Usuario Creado Correctamente", Toast.LENGTH_LONG).show();
                                    cambiar_main_activity(btn_add_user);


                                } else if ("existente".compareTo(response) == 0){
                                    Toast.makeText(context, "El usuario " + usuario.getText().toString() + " ya existe" , Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "Error al Crear Usuario", Toast.LENGTH_LONG).show();
                                }
                            } catch (InterruptedException e) {
                                Toast.makeText(context, "ERROR " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                Toast.makeText(context, "ERROR " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                Log.e("ERROR: ", e.getMessage().toString());

                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(context, "Escriba Usuario y Contraseña ", Toast.LENGTH_LONG).show();
                        }


                    }
                });



    }

    private void cambiar_main_activity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra(EXTRA_MESSAGE3, "true");
        startActivity(intent);
    }
}
