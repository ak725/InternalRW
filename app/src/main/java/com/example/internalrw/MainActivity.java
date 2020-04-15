package com.example.internalrw;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button Read;
    Button Write;

    EditText Data;

    private int REQUEST_CODE = 6162;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Read = findViewById(R.id.button_read);
        Write = findViewById(R.id.button_write);
        Data = findViewById(R.id.editText_data);

        Data.setText(loadData());



        Read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.setText(loadData());
            }
        });

        Write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeData(Data.getText().toString());
            }
        });



    }

    @SuppressLint("NewApi")
    private void writeData(String Data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                // Permission is Already is Granted
                try{
                    writeDataHelper(Data);
                }catch (Exception E){
                    Log.e(getApplication().toString(), Objects.requireNonNull(E.getMessage()));
                }
            }else{

                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(getApplicationContext(),"The Permission is necessary",Toast.LENGTH_LONG).show();
                }

                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }

        else{
            try{
                writeDataHelper(Data);
            }catch (Exception E){
                Log.e(getApplication().toString(), Objects.requireNonNull(E.getMessage()));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void writeDataHelper(String Data) throws IOException {

        File path = this.getFilesDir();
        File file = new File(path,"Data.txt");

        if(!file.exists()){
            file.createNewFile();
        }

        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(Data.getBytes());
            System.out.println("Data Writtten");
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String loadData() {

        String Result = "Enter the Data Here";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                // Permission is Already is Granted
                try{
                    Result = loadDataHelper();
                }catch (Exception E){
                    Log.e(getApplication().toString(), Objects.requireNonNull(E.getMessage()));
                }

            }else{
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }

        try{
            Result = loadDataHelper();
        }catch (Exception E){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Log.e(getApplication().toString(), Objects.requireNonNull(E.getMessage()));
            }
        }

        return Result;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String loadDataHelper() throws IOException{
        File path = this.getFilesDir();

        File file = new File(path,"Data.txt");

        if(!file.exists()) {
            file.createNewFile();
        }

        int length = (int) file.length();

        byte[] bytes = new byte[length];

        try (FileInputStream in = new FileInputStream(file)) {
            in.read(bytes);
        }
        String contents = new String(bytes);
        System.out.println("The C"+contents);

        return contents;

    }


    @SuppressLint("ShowToast")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if( permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Cant DO I/0 .. ",Toast.LENGTH_LONG);
            }
        }



    }
}