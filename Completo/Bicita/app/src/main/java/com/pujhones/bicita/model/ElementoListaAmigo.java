package com.pujhones.bicita.model;

import android.media.Image;

import org.w3c.dom.Text;

/**
 * Created by davl3232 on 8/29/17.
 */

public class ElementoListaAmigo {
    String nombre;
    String imagenPerfilR;

    public ElementoListaAmigo(String nombre, String imagenPerfilR) {
        this.nombre = nombre;
        this.imagenPerfilR = imagenPerfilR;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagenPerfilR() {
        return imagenPerfilR;
    }

    public void setImagenPerfilR(String imagenPerfilR) {
        this.imagenPerfilR = imagenPerfilR;
    }
}
