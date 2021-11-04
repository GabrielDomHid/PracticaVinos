package com.gdomhid.practicavinos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gdomhid.practicavinos.util.TrabajandoArchivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Long> listaIdVinos = new ArrayList<Long>();
    public List<String> listaDeIds = new ArrayList<>();
    private String nombreArchivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        nombreArchivo = getString(R.string.file_name);
        Button bAñadir = findViewById(R.id.bAñadir);
        bAñadir.setOnClickListener((View v) -> {
            openActivityAdd();
        });
        Button bEdit = findViewById(R.id.bEditar);
        bEdit.setOnClickListener((View v) -> {
            openActivityEdit();
        });
        actualizaIds();
        TextView tvListaVinos = findViewById(R.id.tvListaVinos);
        tvListaVinos.setText(readInternoFile() +"\n Cantidad de id "+ listaIdVinos.size());
    }

    private void openActivityAdd() {
        Intent intent = new Intent(this, AddVino.class);
        startActivity(intent);
    }
    private void openActivityEdit() {
        Intent intent = new Intent(this, EditVino.class);
        EditText edtId = findViewById(R.id.edtId);
        intent.putExtra("id",edtId.getText().toString());
        startActivity(intent);
    }

    public String readFile(File file, String filename){
        File f = new File(file, filename);
        String texto = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea;
            while((linea = br.readLine()) != null) {
                texto += linea + "\n";
            }
            br.close();
        } catch (Exception e){
            texto = null;
        }
        return texto;
    }

    public String readInternoFile() {
        return readFile(getFilesDir(), nombreArchivo);
    }

    public String readExternalFile() {
        return readFile(getExternalFilesDir(null), nombreArchivo);
    }

    public void readFileIds(){
        File f = new File(getFilesDir(), getString(R.string.file_nameIds));
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea;
            while((linea = br.readLine()) != null) {
                listaDeIds = new ArrayList<>(Arrays.asList(linea.split(";")));
            }
            br.close();
        } catch (Exception e){}
    }
    public void actualizaIds(){
        readFileIds();
        if (listaDeIds.size() > 0) {
            for (String ids : listaDeIds) {
                listaIdVinos.add(Long.parseLong(ids));
            }
        }
    }
}