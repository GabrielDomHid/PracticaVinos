package com.gdomhid.practicavinos;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EditVino extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) { // este bundle me avisa cuando se borra y se crea una actividad
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initialize();
    }
    private void initialize() {
        String sId;
        Bundle bundle = getIntent().getExtras(); // Este intent es el que me ha llamado, entonces me da la informacion de la actividad q me ha llamado.
        sId = bundle.getString("id");
        TextView tvtext = findViewById(R.id.editTextId);
        Button btback = findViewById(R.id.btback);
        tvtext.setText(sId);
        btback.setOnClickListener((View v) ->{
            finish();
        });
    }
}
