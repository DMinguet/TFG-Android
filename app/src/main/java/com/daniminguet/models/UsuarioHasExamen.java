package com.daniminguet.models;

import java.io.Serializable;
import java.util.Date;

public class UsuarioHasExamen implements Serializable {
    private int id;
    private final int usuarioId;
    private final int examenId;
    private final double nota;
    private final String fecha;

    public UsuarioHasExamen(int usuarioId, int examenId, double nota, String fecha) {
        this.usuarioId = usuarioId;
        this.examenId = examenId;
        this.nota = nota;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public int getExamenId() {
        return examenId;
    }

    public double getNota() {
        return nota;
    }

    public String getFecha() {
        return fecha;
    }
}
