package com.daniminguet.interfaces;

import com.daniminguet.models.Examen;

import java.io.Serializable;

public interface IExamenListener extends Serializable {
    void onExamenSeleccionado(Examen examen);
}
