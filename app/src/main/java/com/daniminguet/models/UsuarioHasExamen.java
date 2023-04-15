package com.daniminguet.models;

import java.io.Serializable;

public class UsuarioHasExamen implements Serializable {
    private int id;
    private final Usuario usuario;
    private final Examen examen;
    private final double nota;
    private final String fecha;

    public UsuarioHasExamen(Usuario usuario, Examen examen, double nota, String fecha) {
        this.usuario = usuario;
        this.examen = examen;
        this.nota = nota;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Examen getExamen() {
        return examen;
    }

    public double getNota() {
        return nota;
    }

    public String getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return "UsuarioHasExamen{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", examen=" + examen +
                ", nota=" + nota +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
