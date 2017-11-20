package com.pujhones.bicita.model;

import java.util.Date;

/**
 * Created by Juan on 1/11/2017.
 */

public class Mensaje {

    protected String autor;
    protected String contenido;
    protected Date fecha;

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) { this.autor = autor; }

    public String getContenido() { return contenido; }

    public void setContenido(String contenido) { this.contenido = contenido; }

    public Date getFecha() { return fecha; }

    public void setFecha(Date fecha) { this.fecha = fecha; }
}
