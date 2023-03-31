package com.daniminguet.models;

import java.io.Serializable;

public class PreguntasExam implements Serializable {
    private final int fkExamen;
    private final int fkPreguntas;

    public PreguntasExam(int fkExamen, int fkPreguntas) {
        this.fkExamen = fkExamen;
        this.fkPreguntas = fkPreguntas;
    }

    public int getFkExamen() {
        return fkExamen;
    }

    public int getFkPreguntas() {
        return fkPreguntas;
    }
}
