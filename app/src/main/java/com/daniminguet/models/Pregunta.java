package com.daniminguet.models;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Pregunta implements Serializable {
    private int id;
    private String pregunta;
    private String respuesta;
    private Temario temario;
    private Examen examen;

    public Pregunta(String pregunta, String respuesta, Temario temario, Examen examen) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.temario = temario;
        this.examen = examen;
    }

    public int getId() {
        return id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Temario getTemario() {
        return temario;
    }

    public void setTemario(Temario temario) {
        this.temario = temario;
    }

    public Examen getExamen() {
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
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
        return "Preguntas{" +
                "id=" + id +
                ", pregunta='" + pregunta + '\'' +
                ", respuesta=" + respuesta +
                ", temario=" + temario +
                '}';
    }
}
