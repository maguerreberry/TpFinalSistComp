package com.example.guti.socketclient;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Guti on 14/09/2017.
 */
public class FileManager {
    private static String filename = "testfile.txt";
    static final int READ_BLOCK_SIZE = 100;
    private File file;
    private Context context;


    public FileManager(Context context){
        this.context = context;
        this.file = new File(filename);
    }

    public String read_file() {
        //reading text from file
        try {
            FileInputStream fileIn= context.openFileInput(filename);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            Toast.makeText(context, "Leí: " + s,
                    Toast.LENGTH_SHORT).show();
            return s;

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }

    // write text to file
    public void write_file(String text_to_Save) {
        // add-write text into file
        try {
            FileOutputStream fileout=context.openFileOutput(filename, context.MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(text_to_Save);
            outputWriter.close();

            //display file saved message
            Toast.makeText(context, "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean file_exists()
    {
        return this.file.exists();
    }
}
