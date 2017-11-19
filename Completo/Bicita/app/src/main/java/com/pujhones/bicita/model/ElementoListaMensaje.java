package com.pujhones.bicita.model;


public class ElementoListaMensaje {
    String contenido;
    String photoURL;

    public ElementoListaMensaje(String nombre, String photoURL) {
        this.contenido = nombre;
        this.photoURL = photoURL;
    }

    @Override
    public String toString() {
        return "ElementoListaAmigo{" +
                "nombre='" + contenido + '\'' +
                ", photoURL='" + photoURL + '\'' +
                '}';
    }

    public String getNombre() {
        return contenido;
    }

    public void setNombre(String contenido) {
        this.contenido = contenido;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
