package com.pujhones.bicita.model;

import java.util.List;

/**
 * Created by Tefa on 20/11/2017.
 */

public class BiciEmpresa {
    private String uid;
    private String nombre;
    private String nit;
    private String correo;
    private String photoURL;
    private List<Lugar> lugares;

    public BiciEmpresa(String uid, String nombre, String nit, String correo, String photoURL) {
        this.uid = uid;
        this.nombre = nombre;
        this.nit = nit;
        this.correo = correo;
        this.photoURL = photoURL;


    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public List<Lugar> getLugares() {
        return lugares;
    }

    public void setLugares(List<Lugar> lugares) {
        this.lugares = lugares;
    }
}
