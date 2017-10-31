package com.pujhones.bicita.model;

/**
 * Created by davl3232 on 8/29/17.
 */

public class ElementoListaAmigo {
    String nombre;
    String photoURL;

    public ElementoListaAmigo(String nombre, String photoURL) {
        this.nombre = nombre;
        this.photoURL = photoURL;
    }

    @Override
    public String toString() {
        return "ElementoListaAmigo{" +
                "nombre='" + nombre + '\'' +
                ", photoURL='" + photoURL + '\'' +
                '}';
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
