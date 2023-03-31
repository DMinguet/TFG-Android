package com.daniminguet.models;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

public class Examen implements Serializable {
    private int id;
    private String titulo;
    private List<Pregunta> preguntas;
    private List<Usuario> usuarios;

    public Examen(int id) {
        this.id = id;
    }

    public Examen(String titulo) {
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Examen{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                '}';
    }
}
