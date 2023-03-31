package com.daniminguet.models;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Preguntas implements Serializable {
    private int id;
    private String pregunta;
    private Respuesta respuesta;
    private int fkTemario;

    public Preguntas(String pregunta, Respuesta respuesta, int fkTemario) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.fkTemario = fkTemario;
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

    public Respuesta getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }

    public int getFkTemario() {
        return fkTemario;
    }

    public void setFkTemario(int fkTemario) {
        this.fkTemario = fkTemario;
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
                ", temario=" + fkTemario +
                '}';
    }
}
