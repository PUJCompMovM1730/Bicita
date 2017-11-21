package com.pujhones.bicita.model;

/**
 * Created by davl3232 on 11/20/17.
 */

public class Amistad {
    protected String amigo1;
    protected String amigo2;
    protected String estado;

    public Amistad() {
    }

    public Amistad(String amigo1, String amigo2, String estado) {
        this.amigo1 = amigo1;
        this.amigo2 = amigo2;
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Amistad{" +
                "amigo1='" + amigo1 + '\'' +
                ", amigo2='" + amigo2 + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }

    public String getAmigo1() {
        return amigo1;
    }

    public void setAmigo1(String amigo1) {
        this.amigo1 = amigo1;
    }

    public String getAmigo2() {
        return amigo2;
    }

    public void setAmigo2(String amigo2) {
        this.amigo2 = amigo2;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
