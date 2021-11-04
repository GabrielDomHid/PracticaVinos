package com.gdomhid.practicavinos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gdomhid.practicavinos.util.Csv;
import com.gdomhid.practicavinos.util.TrabajandoArchivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddVino extends AppCompatActivity {
    public static ArrayList<Long> listaIdVinos = new ArrayList<Long>();
    public List<String> listaDeIds = new ArrayList<>();
    private String nombreArchivo;
    @Override
    protected void onCreate(Bundle savedInstanceState) { // este bundle me avisa cuando se borra y se crea una actividad
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initialize();
    }
    private void initialize() {
        Button bCancelar = findViewById(R.id.bCancelar);
        bCancelar.setOnClickListener((View v) -> {
            finish();
        });
        nombreArchivo = getString(R.string.file_name);
        Button bCrearVino = findViewById(R.id.bCrearVino);
        bCrearVino.setOnClickListener((View v) -> {
            actualizaIds();
            EditText edtId = findViewById(R.id.edtAddId);
            EditText edtNombre = findViewById(R.id.edtAddNombre);
            EditText edtBodega = findViewById(R.id.edtAddBodega);
            EditText edtColor = findViewById(R.id.edtAddColor);
            EditText edtOrigen = findViewById(R.id.edtAddOrigen);
            EditText edtGraduacion = findViewById(R.id.edtAddGraduacion);
            EditText edtFecha = findViewById(R.id.edtAddFecha);
            if (!compruebaId(Long.parseLong(edtId.getText().toString()))) {
                Vino vino = new Vino(Long.parseLong(edtId.getText().toString()), edtNombre.getText().toString(),
                        edtBodega.getText().toString(), edtColor.getText().toString(), edtOrigen.getText().toString(),
                        Double.parseDouble(edtGraduacion.getText().toString()),
                        Integer.parseInt(edtFecha.getText().toString()));
                String vinoCsv = Csv.getCsv(vino);
                writeInternoFile(vinoCsv);
                writeExternoFile(vinoCsv);
                listaIdVinos.add(Long.parseLong(edtId.getText().toString()));
                writeStringIds(listaIdVinos);
                finish();
            } else {
                TextView errortv = findViewById(R.id.tvErrores);
                errortv.setText("¡ERROR! \n Esta id ya existe, pruebe otra.");
            }
        });
    }
    public void writeStringIds(ArrayList<Long> listaids){
        String cadenaIds = "";
        if (listaids.size() > 0) {
            for (Long ids : listaids) {
                cadenaIds += String.valueOf(ids) + ";";
            }
            writeFileIds(cadenaIds);
        }
    }

    public boolean writeFileIds(String string){
        File f = new File(getFilesDir(), getString(R.string.file_nameIds));
        FileWriter fw = null; //FileWriter(File f,boolean append)
        boolean ok = true;
        try {
            fw = new FileWriter(f, true);
            fw.write(string + "\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            ok = false;
        }
        return ok;
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

    public static boolean compruebaId(long id){
        boolean x = false;
        if (listaIdVinos.size() > 0) {
            for (Long ids : listaIdVinos) {
                if (ids.equals(id)) {
                    x = true;
                }
            }
        }
        return x;
    }
    public boolean writeFile(File file, String string){
        File f = new File(file, nombreArchivo);
        FileWriter fw = null; //FileWriter(File f,boolean append)
        boolean ok = true;
        try {
            fw = new FileWriter(f, true);
            fw.write(string + "\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            ok = false;
        }
        return ok;
    }
    public void writeInternoFile(String texto) {
        writeFile(getFilesDir(),texto);
    }

    public void writeExternoFile(String texto) {
        writeFile(getExternalFilesDir(null),texto);
    }
}
