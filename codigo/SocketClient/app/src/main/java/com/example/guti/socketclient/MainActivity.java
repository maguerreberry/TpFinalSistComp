package com.example.guti.socketclient;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public final static String EXTRA_MESSAGE2 = "com.example.myfirstapp.MESSAGE";




    private String key = "Bar12345Bar12345"; // 128 bit key
    private String initVector = "RandomInitVector"; // 16 bytes IV

    /**
     * Controles
     * */
    private Button button_login;
    private EditText usuario;
    private EditText password;
    private EditText pin;
    private String response;
    private Context context = this;

    private Usuario user;


    private File file;
    private EditText textmsg;
    static final int READ_BLOCK_SIZE = 100;
    private String filepath = "MyFileCliente";
    private String filename = "test.txt";
    private String pin_validado = "false";


    private FileManager fileManager;
    String myData = "";



    private Encryption encryption = Encryption.getDefault("This is a key123", "This is an IV456", new byte[16]);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_login = ((Button) findViewById(R.id.button_login));
        usuario = ((EditText) findViewById(R.id.usuario));
        password = ((EditText) findViewById(R.id.password));
        pin = ((EditText) findViewById(R.id.pin));

        Intent intent = getIntent();

        //this.file  = new File(context.getFilesDir(), "testfile.txt");
        this.file = new File(getExternalFilesDir(filepath), filename);
        //Toast.makeText(context, context.getFilesDir().toString(), Toast.LENGTH_LONG).show();
        Log.i("PATH DEL ARCHIVO: ", file.getPath());

        //Toast.makeText(context, file.getPath(), Toast.LENGTH_LONG).show();

        //this.fileManager = new FileManager(context);

        if (file.exists()) {

            String datos_usuario_from_file;
            //Toast.makeText(context, "Existe el archivo " + file.getName(), Toast.LENGTH_LONG).show();
            //datos_usuario_from_file = read_file();
            datos_usuario_from_file = read_ext_file();
            Log.i("DATOS DEL ARCHIVO: ", datos_usuario_from_file);
            //Toast.makeText(context, datos_usuario_from_file, Toast.LENGTH_LONG).show();
            if ("error".compareToIgnoreCase(datos_usuario_from_file) == 0){
                Log.e("ERROR", "Error de lectura archivo");
            } else {
                String[] datos_usuario_parseado = datos_usuario_from_file.split("-");
                user = new Usuario(datos_usuario_parseado[0], datos_usuario_parseado[1],datos_usuario_parseado[2],datos_usuario_parseado[3]);

                pin_validado = intent.getStringExtra(Main5Activity.EXTRA_MESSAGE3);
                if (intent.getStringExtra(Main5Activity.EXTRA_MESSAGE3) == null) {
                    cambiar_actividad_validar_pin(button_login);
                } else if ("true".compareTo(pin_validado) == 0) {
                    if (user.is_admin()) {
                        cambiar_actividad_adminitrador(button_login);
                    } else cambiar_actividad_usuario(button_login);
                } else {
                    Toast.makeText(context, "Error Intercambio en Actividades", Toast.LENGTH_LONG).show();

                }


                //Toast.makeText(context, user.getUsername(), Toast.LENGTH_LONG).show();
            }
        }

        button_login.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if(usuario.getText().toString().length()>0 && password.getText().toString().length()>0 && pin.getText().toString().length()>0){
                            MyATaskCliente myATaskYW = new MyATaskCliente(context);
                            //myATaskYW.execute(editText.getText().toString());
                            try {
                                String str_to_Send = usuario.getText().toString() + "-"+ password.getText().toString()+ "-login";
                                str_to_Send = Encryptor.encrypt(key,initVector,str_to_Send);
                                response = myATaskYW.execute(str_to_Send).get();
                                //Toast.makeText(context, "RESPUESTA SV: " + response, Toast.LENGTH_LONG).show();
                                //set_user_data();
                                //Toast.makeText(context, "RESPUESTA SV: " + Encryptor.decrypt(key, initVector,response), Toast.LENGTH_LONG).show();
                                Log.i("SERVIDOR: ", Encryptor.decrypt(key, initVector,response));
                                response = Encryptor.decrypt(key, initVector,response);
                                // Chequeo que el servidor esté conectado...
                                if (response.contains("failed")){
                                    Toast.makeText(context, "No se pudo conectar al Servidor...", Toast.LENGTH_LONG).show();
                                } else
                                if ("Incorrecto".equalsIgnoreCase(response)) {
                                    Toast.makeText(context, "Usuario o Contraseña Incorrecta", Toast.LENGTH_LONG).show();
                                } else {
                                    /* Guardo en el Archivo datos + pin + si es admin */
                                    write_ext_file(usuario.getText().toString() + "-" + password.getText().toString() + "-" + pin.getText().toString() + "-" + response);
                                    user = new Usuario(usuario.getText().toString(), password.getText().toString(),pin.getText().toString(),response);


                                    // 0 -> admin
                                    if ("0".equalsIgnoreCase(response)) {
                                        cambiar_actividad_adminitrador(button_login);
                                    }

                                    //1 -> Usuario
                                    if ("1".equalsIgnoreCase(response)) {
                                        //Toast.makeText(context, "Estoy en 1", Toast.LENGTH_LONG).show();
                                        cambiar_actividad_usuario(button_login);
                                    }

                                }
                            } catch (InterruptedException e) {
                                Toast.makeText(context, "ERROR " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                Toast.makeText(context, "ERROR " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(context, "Escriba Usuario y Contraseña ", Toast.LENGTH_LONG).show();
                        }


                    }
                });


    }//end:onCreate


    private void cambiar_actividad_adminitrador(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
        //Toast.makeText(context, this.user.toString(), Toast.LENGTH_LONG).show();
        intent.putExtra(EXTRA_MESSAGE, this.user.toString());
        startActivity(intent);
    }

    private void cambiar_actividad_usuario(View view) {
        Intent intent = new Intent(this, Main3Activity.class);
        intent.putExtra(EXTRA_MESSAGE, this.user.toString());

        //intent.putExtra(EXTRA_MESSAGE, this.user.toString());
        startActivity(intent);
    }

    private void cambiar_actividad_validar_pin(View view) {
        Intent intent = new Intent(this, Main5Activity.class);
        Log.i("PIN DE USUARIO: ", this.user.getPin());
        //Toast.makeText(context, this.user.getPin(), Toast.LENGTH_LONG).show();
        intent.putExtra(EXTRA_MESSAGE2, this.user.getPin());
        startActivity(intent);
    }

    public void write_ext_file(String text_to_Save) {
        // add-write text into file
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text_to_Save.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read_ext_file() {

        //reading text from file
        try {
            FileInputStream fis = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();
            return myData;
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }

    }
}