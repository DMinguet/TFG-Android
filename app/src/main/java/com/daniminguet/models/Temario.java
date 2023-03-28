package com.daniminguet.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

public class Temario implements Serializable {
    private int id;
    private int tema;
    private String titulo;
    private String pdf;

    public Temario(int tema, String titulo, String pdf) {
        this.tema = tema;
        this.titulo = titulo;
        this.pdf = pdf;
    }

    public int getId() {
        return id;
    }

    public int getTema() {
        return tema;
    }

    public void setTema(Integer tema) {
        this.tema = tema;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
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
        return "Temario{" +
                "id=" + id +
                ", tema=" + tema +
                ", titulo='" + titulo + '\'' +
                ", pdf='" + pdf + '\'' +
                '}';
    }
}
