package com.pujhones.bicita.model;

/**
 * Created by Tefa on 20/11/2017.
 */

public class Lugar {
    private String uid;
    private String nombre;
    private String descripción;
    private String photoURL;
    private double longitud;
    private double latitud;
    private double altitud;
    private boolean prom;

    public Lugar(String nombre, String descripción, String photoURL, double longitud, double latitud, double altitud, boolean prom) {
        this.nombre = nombre;
        this.descripción = descripción;
        this.photoURL = photoURL;
        this.longitud = longitud;
        this.latitud = latitud;
        this.altitud = altitud;
        this.prom = prom;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripción() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getAltitud() {
        return altitud;
    }

    public void setAltitud(double altitud) {
        this.altitud = altitud;
    }

    public boolean isProm() {
        return prom;
    }

    public void setProm(boolean prom) {
        this.prom = prom;
    }
}

