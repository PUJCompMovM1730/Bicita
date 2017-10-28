package com.pujhones.bicita.model;

import android.media.Image;

/**
 * Created by davl3232 on 10/26/17.
 */

public class Solicitud {
    protected String nombre;
    protected Image imagen;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }
}
