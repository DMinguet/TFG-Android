package com.daniminguet.interfaces;

import java.io.Serializable;

public interface ITemarioListener extends Serializable {
    void onTemarioSeleccionado(int id);
}
