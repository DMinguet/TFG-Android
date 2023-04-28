package com.daniminguet.models;

import java.io.Serializable;

public class PreguntaHasExamen implements Serializable {
    private int id;
    private final Pregunta pregunta;
    private final Examen examen;

    public PreguntaHasExamen(Pregunta pregunta, Examen examen) {
        this.pregunta = pregunta;
        this.examen = examen;
    }

    public int getId() {
        return id;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public Examen getExamen() {
        return examen;
    }

    @Override
    public String toString() {
        return "PreguntaHasExamen{" +
                "id=" + id +
                ", pregunta=" + pregunta +
                ", examen=" + examen +
                '}';
    }
}
