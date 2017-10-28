package com.pujhones.bicita.model;

import android.media.Image;

/**
 * Created by davl3232 on 10/26/17.
 */

public class UsuarioEncontrado {
    protected String nombre;
    protected String correo;
    protected Image imagen;

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

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }
}
