package com.example.wycliffenyakemwa.mqttchatapp;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Chat extends AppCompatActivity {

    Button publish;
    MainActivity mqttInstance;
    TextInputEditText topic, theMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        }

    }

