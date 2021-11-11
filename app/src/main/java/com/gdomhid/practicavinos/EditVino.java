package com.gdomhid.practicavinos;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gdomhid.practicavinos.util.Csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class EditVino extends AppCompatActivity {
    public static ArrayList<Vino> listaVinos = new ArrayList<>();
    private String nombreArchivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // este bundle me avisa cuando se borra y se crea una actividad
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initialize();
    }

    /**
     * Código que se ejecuta en el oncreate
     */
    private void initialize() {
        nombreArchivo = getString(R.string.file_name);
        readFile();
        Bundle bundle = getIntent().getExtras();
        String sId = bundle.getString("id");// Este intent es el que me ha llamado, entonces me da la informacion de la actividad q me ha llamado.
        rellenarCampos(bundle);

        Button btback = findViewById(R.id.btback);
        btback.setOnClickListener((View v) ->{
            finish();
        });
        Button btEditarVino = findViewById(R.id.bEditarVino);
        btEditarVino.setOnClickListener((View v) ->{
            reemplazaVino(Integer.parseInt(sId), obtenerCampos());
            if (writeFile()) {
                finish();
            }
        });
        Button btDelete = findViewById(R.id.btDelete);
        btDelete.setOnClickListener((View v) ->{
            if (borraVino(Integer.parseInt(sId))) {
                if (writeFile()) {
                    finish();
                }
            }
        });
    }

    /**
     * Leo el fichero y relleno el array con cada uno de los vinos para trabajar con ellos
     * @return si se ha podido leer el fichero y rellenado el array
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

    /**
     * Selecciono todos los campos e inserto sus tados para luego ser modificados.
     * @param bundle Recibo el id del vino que voy a editar
     */
    public void rellenarCampos(Bundle bundle){
        String sId = bundle.getString("id");
        EditText tvtext = findViewById(R.id.editTextId);
        tvtext.setText(sId);
        //Con el id obtengo el vino y con eso ya relleno los campos.
        Vino vino = buscaVino(Integer.parseInt(sId));
        EditText editTextNombre = findViewById(R.id.editTextNombre);
        EditText editTextBodega = findViewById(R.id.editTextBodega);
        EditText editTextColor = findViewById(R.id.editTextColor);
        EditText editTextOrigen = findViewById(R.id.editTextOrigen);
        EditText editTextGraduacion = findViewById(R.id.editTextGraduacion);
        EditText editTextFecha = findViewById(R.id.editTextFecha);
        editTextNombre.setText(vino.getNombre());
        editTextBodega.setText(vino.getBodega());
        editTextColor.setText(vino.getColor());
        editTextOrigen.setText(vino.getOrigen());
        editTextGraduacion.setText(String.valueOf(vino.getGraduacion()));
        editTextFecha.setText(String.valueOf(vino.getFecha()));
    }

    /**
     * Busca en el arraylist el vino para borrarlo.
     * @param id id del vino que se va a borrar
     * @return si se ha borrado del arraylist o no
     */
    private boolean borraVino(int id){
        boolean ok = false;
        for (int i = 0; i < listaVinos.size(); i++) {
            if (listaVinos.get(i).getId() == (long) id){
                listaVinos.remove(i);
                ok = true;
            }
        }
        return ok;
    }
    /**
     * profe aprueba a mi novio que es guapo y listo pliss carmelo i love you teacher
     * @param id
     * @return
     */
    private Vino buscaVino(int id){
        Vino vv = new Vino();
        for (Vino v: listaVinos) {
            int idaux = (int) v.getId();
            if (idaux == id){
                vv = v;
            }
        }
        return vv;
    }

    /**
     * @param id id del vino
     * @param vinito vino con los nuevos campos
     * @return vino actualizado
     */
    private void reemplazaVino(int id, Vino vinito){
        for (int i = 0; i < listaVinos.size(); i++) {
            if (listaVinos.get(i).getId() == (long) id){
                listaVinos.set(i, vinito);
            }
        }
    }
    /**
     * Selecciono todos los campos y creo un vino con los nuevos datos y devuelvo el vino.
     */
    public Vino obtenerCampos(){
        Vino v = new Vino();
        EditText editTextId = findViewById(R.id.editTextId);
        EditText editTextNombre = findViewById(R.id.editTextNombre);
        EditText editTextBodega = findViewById(R.id.editTextBodega);
        EditText editTextColor = findViewById(R.id.editTextColor);
        EditText editTextOrigen = findViewById(R.id.editTextOrigen);
        EditText editTextGraduacion = findViewById(R.id.editTextGraduacion);
        EditText editTextFecha = findViewById(R.id.editTextFecha);
        v.setId(Long.parseLong(editTextId.getText().toString()));
        v.setNombre(editTextNombre.getText().toString());
        v.setBodega(editTextBodega.getText().toString());
        v.setColor(editTextColor.getText().toString());
        v.setOrigen(editTextOrigen.getText().toString());
        v.setGraduacion(Double.parseDouble(editTextGraduacion.getText().toString()));
        v.setFecha(Integer.parseInt(editTextFecha.getText().toString()));
        return v;
    }
    /**
     * Creo un fichero interno automaticamente con el nombre que se declara al crear el activity
     * y escribo en el los vinos
     */
    public boolean writeFile(){
        File f = new File(getFilesDir(), nombreArchivo);
        boolean ok = true;
        if (f.exists()) {
            f.delete();
            f = new File(getFilesDir(), nombreArchivo);
            FileWriter fw = null; //FileWriter(File f,boolean append)
            String vcsv;
            for (int i = 0; i < listaVinos.size(); i++) {
                if (listaVinos.get(i).getId() != 0){
                    vcsv = Csv.getCsv(listaVinos.get(i));
                    try {
                        fw = new FileWriter(f, true);
                        fw.write( vcsv + "\n");
                        fw.flush();
                        fw.close();
                    } catch (IOException e) {
                        ok = false;
                    }
                }
            }
        }
        return ok;
    }
}
