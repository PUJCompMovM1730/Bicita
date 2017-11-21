package com.pujhones.bicita.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaqui on 21/11/2017.
 */

public class Recorrido implements Serializable{
    private List <String> acompanantes;
    private List <Ubicacion> camino;
    private String creador;
    private Ubicacion fin;
    private Ubicacion inicio;

    public Recorrido ()
    {
        this.acompanantes = new ArrayList<String>();
        this.camino = new ArrayList <Ubicacion>();
        this.fin = new Ubicacion();
        this.inicio = new Ubicacion();
    }

    public Recorrido (List <String> acompanantes, List <Ubicacion> camino, String creador, Ubicacion fin, Ubicacion inicio)
    {
        this.acompanantes = acompanantes;
        this.camino = camino;
        this.creador = creador;
        this.fin = fin;
        this.inicio = inicio;
    }

    @Override
    public String toString() {
        String s = "{" +
                "\"acompanantes\" : [";
        boolean primero=true;
        for (String si : acompanantes)
        {
            if (primero)
                primero=false;
            else
                s+=",";
            s+="\"" + si + "\"";
        }
        s += "], \"camino\" : [";
        primero=true;
        for (Ubicacion u : camino)
        {
            if (primero)
                primero=false;
            else
                s+=",";
            s+= u.toString();
        }
        s +="], \"creador\" : " + creador +
                ", \"fin\" : " + fin.toString() +
                ", \"inicio\" : " + inicio.toString() +
                '}';
        return s;
    }
}
