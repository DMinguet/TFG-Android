package com.daniminguet.interfaces;

import com.daniminguet.models.Temario;

import java.io.Serializable;

public interface ITemarioListener extends Serializable {
    void onTemarioSeleccionado(Temario temario);
}
