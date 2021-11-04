package com.gdomhid.practicavinos.util;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.gdomhid.practicavinos.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TrabajandoArchivos extends AppCompatActivity {
    public String nombreArchivo;

    public TrabajandoArchivos() {}

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public boolean writeFile(File file,String string){
        File f = new File(file, this.nombreArchivo);
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
}
