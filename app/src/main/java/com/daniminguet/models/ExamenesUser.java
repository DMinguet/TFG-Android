package com.daniminguet.models;

import java.io.Serializable;
import java.util.Date;

public class ExamenesUser implements Serializable {
    private final int fkUsuario;
    private final int fkExamen;
    private final double nota;
    private final Date fecha;

    public ExamenesUser(int fkUsuario, int fkExamen, double nota, Date fecha) {
        this.fkUsuario = fkUsuario;
        this.fkExamen = fkExamen;
        this.nota = nota;
        this.fecha = fecha;
    }

    public int getFkUsuario() {
        return fkUsuario;
    }

    public int getFkExamen() {
        return fkExamen;
    }

    public double getNota() {
        return nota;
    }

    public Date getFecha() {
        return fecha;
    }
}
