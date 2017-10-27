package com.pujhones.bicita.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Henry Salazar on 26/10/2017.
 */

public class BiciUsuario {
    private double altura;
    private double peso;
    private String nombre;
    private String correo;
    private String contrasena;
    private List<String> amigos;
    private double puntaje;
    private List<String> recorridosDondeAcompania;
    private boolean sexo;

    public BiciUsuario(double altura, double peso, String nombre, String correo, String contrasena, boolean sexo) {
        this.altura = altura;
        this.peso = peso;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.amigos = new ArrayList<String>();
        this.puntaje = 0.0;
        this.recorridosDondeAcompania = new ArrayList<String>();
        this.sexo = sexo;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List<String> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<String> amigos) {
        this.amigos = amigos;
    }

    public double getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(double puntaje) {
        this.puntaje = puntaje;
    }

    public List<String> getRecorridosDondeAcompania() {
        return recorridosDondeAcompania;
    }

    public void setRecorridosDondeAcompania(List<String> recorridosDondeAcompania) {
        this.recorridosDondeAcompania = recorridosDondeAcompania;
    }

    public boolean isSexo() {
        return sexo;
    }

    public void setSexo(boolean sexo) {
        this.sexo = sexo;
    }
}
