package com.daniminguet.models;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

public class Preguntas implements Serializable {
    private int id;
    private Temario temario;
    private String pregunta;
    private Respuesta respuesta;
    private List<Examen> examenes;

    public Preguntas(Temario temario, String pregunta, Respuesta respuesta) {
        this.temario = temario;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public Temario getTemario() {
        return temario;
    }

    public void setTemario(Temario temario) {
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

    public List<Examen> getExamenes() {
        return examenes;
    }

    public void setExamenes(List<Examen> examenes) {
        this.examenes = examenes;
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
                ", examenes=" + examenes +
                '}';
    }
}
