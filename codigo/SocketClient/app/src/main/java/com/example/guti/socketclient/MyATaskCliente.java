package com.example.guti.socketclient;

/**
 * Created by Guti on 04/09/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Clase para interactuar con el servidor
 * */
class MyATaskCliente extends AsyncTask<String,Void,String> {

    /**
     * Puerto
     * */
    private static final int SERVERPORT = 7000;
    /**
     * HOST
     * */
    //private static final String ADDRESS = "10.0.2.2";
    private static final String ADDRESS = "192.168.0.121";

    /**
     * Ventana que bloqueara la pantalla del movil hasta recibir respuesta del servidor
     * */
    ProgressDialog progressDialog;
    private Context context;

    public MyATaskCliente(Context context) {
        this.context = context;
    }
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
            //request.concat(request1);

            Log.i("I/TCP Client", "Sending: " + request);
            //request = encryption.encryptOrNull(request);
            output.println(request);


            //recibe respuesta del servidor y formatea a String
            Log.i("I/TCP Client", "Received data to server");
            InputStream stream = socket.getInputStream();
            byte[] lenBytes = new byte[256];
            stream.read(lenBytes,0,256);
            String received = new String(lenBytes,"UTF-8").trim();
            Log.i("I/TCP Client", "Received " + received);
            //received = encryption.decryptOroNull(received);
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
        //Toast.makeText(context, value, Toast.LENGTH_LONG).show();
        //password.setText(value);
    }
}