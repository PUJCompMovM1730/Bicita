package com.pujhones.bicita.model;

/**
 * Created by Henry Salazar on 26/10/2017.
 */

public class BiciUsuario {
    private String uid;
    private double altura;
    private double peso;
    private String nombre;
    private String correo;
    private double puntaje;
    private String sexo;
    private String photoURL;

    public BiciUsuario() {
    }

    public BiciUsuario(String uid, double altura, double peso, String nombre, String correo,
                       double puntaje, String sexo, String photoURL) {
        this.uid = uid;
        this.altura = altura;
        this.peso = peso;
        this.nombre = nombre;
        this.correo = correo;
        this.puntaje = puntaje;
        this.sexo = sexo;
        this.photoURL = photoURL;
    }

    @Override
    public String toString() {
        return "BiciUsuario{" +
                "uid='" + uid + '\'' +
                ", altura=" + altura +
                ", peso=" + peso +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", puntaje=" + puntaje +
                ", sexo='" + sexo + '\'' +
                ", photoURL='" + photoURL + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public double getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(double puntaje) {
        this.puntaje = puntaje;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
