package com.gdomhid.practicavinos.util;

import com.gdomhid.practicavinos.Vino;

public class Csv {

    public static Vino getVino(String s){
        String[] atributos = s.split(";");
        Vino v = new Vino();
        if (atributos.length >= 7){
            try {
                v.setId(Long.parseLong(atributos[0].trim()));
            } catch(Exception e){}

            v.setNombre(atributos[1].trim());
            v.setBodega(atributos[2].trim());
            v.setColor(atributos[3].trim());
            v.setOrigen(atributos[4].trim());
            try {
                v.setGraduacion(Double.parseDouble(atributos[5].trim()));
            } catch(Exception e){}
            try {
                v.setFecha(Integer.parseInt(atributos[6].trim()));
            } catch(Exception e){}
        }
        return v;
    }

    public static String getCsv(Vino v){
        return v.getId() + ";" + v.getNombre() + ";" + v.getBodega() + ";" + v.getColor() + ";" + v.getOrigen() + ";" + v.getGraduacion() + ";" + v.getFecha() + "\n";
    }
}
