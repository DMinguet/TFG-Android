package com.daniminguet.interfaces;

import java.io.Serializable;

public interface IExamenListener extends Serializable {
    void onExamenSeleccionado(int id);
}
