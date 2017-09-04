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

public class MainActivity extends AppCompatActivity {

    /**
     * Controles
     * */
    private Button button;
    private Button button3;
    private EditText editText;
    private EditText editText2;
    private EditText editText3;
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
        setContentView(R.layout.activity_main);

        button = ((Button) findViewById(R.id.button));
        button3 = ((Button) findViewById(R.id.button3));
        editText = ((EditText) findViewById(R.id.editText));
        editText2 = ((EditText) findViewById(R.id.editText2));
        editText3 = ((EditText) findViewById(R.id.editText3));

        button.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if(editText.getText().toString().length()>0){
                            MyATaskCliente myATaskYW = new MyATaskCliente();
                            myATaskYW.execute(editText.getText().toString());
                        }else{
                            //Toast.makeText(context, "Escriba \"frase\" o \"libro\" ", Toast.LENGTH_LONG).show();
                        }


                    }
                });

        button3.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if(editText3.getText().toString().length()>0){
                            if ("1234".equalsIgnoreCase(editText3.getText().toString())) {
                                Toast.makeText(context, "PIN CORRECT!", Toast.LENGTH_LONG).show();
                                cambiar_actividad(button3);
                            } else{
                                Toast.makeText(context, "PIN Incorrecto ", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(context, "Escriba su PIN ", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }//end:onCreate


    public void cambiar_actividad(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
/*        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);*/
        startActivity(intent);
    }

    /**
     * Clase para interactuar con el servidor
     * */
    class MyATaskCliente extends AsyncTask<String,Void,String>{

        /**
         * Ventana que bloqueara la pantalla del movil hasta recibir respuesta del servidor
         * */
        ProgressDialog progressDialog;

        /**
         * muestra una ventana emergente
         * */
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setTitle("Connecting to server");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }

        /**
         * Se conecta al servidor y trata resultado
         * */
        @Override
        protected String doInBackground(String... values){

            try {
                //Se conecta al servidor
                InetAddress serverAddr = InetAddress.getByName(ADDRESS);
                Log.i("I/TCP Client", "Connecting...");
                Socket socket = new Socket(serverAddr, SERVERPORT);
                Log.i("I/TCP Client", "Connected to server");

                //envia peticion de cliente
                Log.i("I/TCP Client", "Send data to server");
                PrintStream output = new PrintStream(socket.getOutputStream());
                String request = values[0];

                //request = encryption.encryptOrNull(request);
                output.println(request);


                //recibe respuesta del servidor y formatea a String
                Log.i("I/TCP Client", "Received data to server");
                InputStream stream = socket.getInputStream();
                byte[] lenBytes = new byte[256];
                stream.read(lenBytes,0,256);
                String received = new String(lenBytes,"UTF-8").trim();
                Log.i("I/TCP Client", "Received " + received);
                //received = encryption.decryptOrNull(received);
                Log.i("I/TCP Client", "");
                //cierra conexion
                socket.close();
                return received;
            }catch (UnknownHostException ex) {
                Log.e("E/TCP Client", "" + ex.getMessage());
                return ex.getMessage();
            } catch (IOException ex) {
                Log.e("E/TCP Client", "" + ex.getMessage());
                return ex.getMessage();
            }
        }

        /**
         * Oculta ventana emergente y muestra resultado en pantalla
         * */
        @Override
        protected void onPostExecute(String value){
            progressDialog.dismiss();
            editText2.setText(value);
        }
    }
}