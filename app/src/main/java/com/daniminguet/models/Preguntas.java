package com.daniminguet.models;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

public class Preguntas implements Serializable {
    private int id;
    private int temario;
    private String pregunta;
    private Respuesta respuesta;

    public Preguntas(int temario, String pregunta, Respuesta respuesta) {
        this.temario = temario;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public int getId() {
        return id;
    }

    public int getTemario() {
        return temario;
    }

    public void setTemario(int temario) {
        this.temario = temario;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public Respuesta getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
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
                ", temario=" + temario +
                ", pregunta='" + pregunta + '\'' +
                ", respuesta=" + respuesta +
                '}';
    }
}
