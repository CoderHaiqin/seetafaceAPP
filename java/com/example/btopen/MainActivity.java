package com.example.btopen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private EditText passwordWidget;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        passwordWidget = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
    }

    public void clickLogin(View view) {
        String password = passwordWidget.getText().toString();
        if(password.equals("123456")){
            startActivity(new Intent(this, MainActivity2.class));
        }
    }
}