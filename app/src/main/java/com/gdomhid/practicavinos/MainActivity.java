package com.gdomhid.practicavinos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Vino> listaVinos = new ArrayList<>();
    private String nombreArchivo;
    private TextView tvListaVinos,tvErrorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
        nombreArchivo = getString(R.string.file_name);
        //Si no existe el fichero lo creo
        if (!readFile()) {
            writeFileEmpty();
        }else{
            //Si existe el archivo lo leo y relleno el arraylist de vinos.
            readFile();
        }
        Button bAñadir = findViewById(R.id.bAñadir);
        bAñadir.setOnClickListener((View v) -> {
            openActivityAdd();
        });

        Button bEdit = findViewById(R.id.bEditar);
        bEdit.setOnClickListener((View v) -> {
            EditText edtId = findViewById(R.id.edtId);
            String text = edtId.getText().toString();
            tvErrorId = findViewById(R.id.tvErrorId);
            if (text.equals("")) {
                tvErrorId.setText("Este id no existe.");
            }else{
                boolean comprueba = AddVino.compruebaId(Long.parseLong(text),listaVinos);
                if (comprueba) {
                    tvErrorId.setText("");
                    openActivityEdit();
                }else {
                    tvErrorId.setText("Este id no existe.");
                }
            }
        });
        tvListaVinos = findViewById(R.id.tvListaVinos);
        tvListaVinos.setText(readInternoFile());
    }

    /**
     * Inicializo la otra actividad
     */
    private void openActivityAdd() {
        Intent intent = new Intent(this, AddVino.class);
        startActivity(intent);
    }
    /**
     * Inicializo la actividad de editar
     */
    private void openActivityEdit() {
        Intent intent = new Intent(this, EditVino.class);
        EditText edtId = findViewById(R.id.edtId);
        intent.putExtra("id",edtId.getText().toString());
        startActivity(intent);
    }

    /**
     * Leo el fichero y lo muestro en el scrollview
     */
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

    /**
     *Leo el archivo interno
     */
    public String readInternoFile() {
        return readFile(getFilesDir(), nombreArchivo);
    }

    @Override
    protected void onRestart() {
        tvListaVinos = findViewById(R.id.tvListaVinos);
        tvListaVinos.setText(readInternoFile());
        readFile();
        super.onRestart();
    }

    /**
     * Creo un fichero interno automaticamente con el nombre que se declara al crear el activity
     */
    public void writeFileEmpty(){
        File f = new File(getFilesDir(), nombreArchivo);
        FileWriter fw = null; //FileWriter(File f,boolean append)
        try {
            fw = new FileWriter(f, true);
            fw.write("");
            fw.flush();
            fw.close();
        } catch (IOException e) {}
    }
    /**
     * Leo automaticamente el fichero y convierto en objetos vino cada linea y lo añado al arraylist
     * de vinos de la clase para trabjar con el.
     * Se le podria añadir como parametro el file para seleccionar si interno o externo y el nombre
     * del archivo pero para esta práctica he decidido hacerlo de esta forma.
     */
    public boolean readFile(){
        File f = new File(getFilesDir(), nombreArchivo);
        boolean readok = true;
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String linea;
            while ((linea = br.readLine()) != null) {
                listaVinos.add((Vino) Csv.getVino(linea));
            }
            br.close();
        } catch (Exception e){
            readok = false;
        }
        return readok;
    }
}