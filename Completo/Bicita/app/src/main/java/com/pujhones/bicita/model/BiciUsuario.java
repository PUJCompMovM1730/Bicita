package com.pujhones.bicita.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Henry Salazar on 26/10/2017.
 */

public class BiciUsuario {
    private double altura;
    private double peso;
    private String nombre;
    private String correo;
    private Map<String, String> amigos;
    private double puntaje;
    private Map<String, String> recorridosDondeAcompania;
    private boolean sexo;
    private String photoURL;

    public BiciUsuario(double altura, double peso, String nombre, String correo, boolean sexo, String url) {
        this.altura = altura;
        this.peso = peso;
        this.nombre = nombre;
        this.correo = correo;
        this.amigos = new HashMap<String, String>();
        this.puntaje = 0.0;
        this.recorridosDondeAcompania = new HashMap<String, String>();
        this.sexo = sexo;
        this.photoURL = url;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Map<String, String> getAmigos() {
        return amigos;
    }

    public void setAmigos(Map<String, String> amigos) {
        this.amigos = amigos;
    }

    public double getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(double puntaje) {
        this.puntaje = puntaje;
    }

    public Map<String, String> getRecorridosDondeAcompania() {
        return recorridosDondeAcompania;
    }

    public void setRecorridosDondeAcompania(Map<String, String> recorridosDondeAcompania) {
        this.recorridosDondeAcompania = recorridosDondeAcompania;
    }

    public boolean isSexo() {
        return sexo;
    }

    public void setSexo(boolean sexo) {
        this.sexo = sexo;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
