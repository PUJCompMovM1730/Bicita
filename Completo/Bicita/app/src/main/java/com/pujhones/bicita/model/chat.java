package com.pujhones.bicita.model;

import java.util.List;

/**
 * Created by Juan on 21/11/2017.
 */

public class chat {

    private String origen;
    private String destino;
    private List<Mensaje> mensajes;

    public chat(String origen, String destino, List<Mensaje> mensajes) {
        this.origen = origen;
        this.destino = destino;
        this.mensajes = mensajes;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
}
