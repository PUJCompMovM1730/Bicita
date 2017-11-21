package com.pujhones.bicita.model;

import java.io.Serializable;

/**
 * Created by jaqui on 21/11/2017.
 */

public class Ubicacion implements Serializable {
    private double latitud;
    private double longitud;

    public Ubicacion ()
    {

    }

    public Ubicacion(double lat,double lon)
    {
        this.latitud=lat;
        this.longitud=lon;
    }

    @Override
    public String toString() {
        return "{" +
                "\"latitud\" : " + latitud + "," +
                "\"longitud\" : " + longitud +
                "}";
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}