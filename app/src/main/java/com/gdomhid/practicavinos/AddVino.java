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

    public static ArrayList<Vino> listaVinos = new ArrayList<>();
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
        //Leo el fichero y relleno el array con los vinos
        nombreArchivo = getString(R.string.file_name);
        readFile();
        Button bCrearVino = findViewById(R.id.bCrearVino);
        bCrearVino.setOnClickListener((View v) -> {
            //Recojo todos los campos
            EditText edtId = findViewById(R.id.edtAddId);
            EditText edtNombre = findViewById(R.id.edtAddNombre);
            EditText edtBodega = findViewById(R.id.edtAddBodega);
            EditText edtColor = findViewById(R.id.edtAddColor);
            EditText edtOrigen = findViewById(R.id.edtAddOrigen);
            EditText edtGraduacion = findViewById(R.id.edtAddGraduacion);
            EditText edtFecha = findViewById(R.id.edtAddFecha);
            Long auxid = Long.parseLong(edtId.getText().toString());
            //Compruebo que no existe ese id
            if (!compruebaId(auxid, listaVinos)) {
                //Como no existe el id creo el vino
                Vino vino = new Vino(auxid, edtNombre.getText().toString(),
                        edtBodega.getText().toString(), edtColor.getText().toString(), edtOrigen.getText().toString(),
                        Double.parseDouble(edtGraduacion.getText().toString()),
                        Integer.parseInt(edtFecha.getText().toString()));
                //Convierto el vino en string
                String vinoCsv = Csv.getCsv(vino);
                //Escribo el vino en el fichero
                writeInternoFile(vinoCsv);
                finish();
            } else {
                //En caso de que exista el vino mostrar error en un textview
                TextView errortv = findViewById(R.id.tvErrores);
                errortv.setText("¡ERROR! \n Esta id ya existe, pruebe otra.");
            }
        });
    }
    /**
     * Comparo el id del edittext con todos los ids de los vinos del arraylist en caso de tener vinos
     * si no lo crea directamente.
     * Me devuelve true si el id ya existe.
     */
    public static boolean compruebaId(long id, ArrayList<Vino> listaVinos){
        boolean x = false;
        if (listaVinos.size() > 0) {
            for (int i = 0; i < listaVinos.size(); i++) {
                Long xId = listaVinos.get(i).getId();
                if (xId.equals(id)) {
                    x = true;
                }
            }
        }
        return x;
    }

    /**
     * Creo un fichero interno automaticamente con el nombre que se declara al crear el activity
     * y escribo en el los vinos
     */
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
    /**
     * Creo el archivo interno con el file y el texto que le paso
     */
    public void writeInternoFile(String texto) {
        writeFile(getFilesDir(),texto);
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
