package com.daniminguet.fragments.examenes;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniminguet.R;
import com.daniminguet.models.Pregunta;
import com.daniminguet.models.Respuesta;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Arrays;
import java.util.List;

public class FragmentHacerExamen extends Fragment {
    public interface IOnAttachListener {
        int getCuenta();
        Pregunta getPregunta();
        Respuesta[] getRespuestas();
        boolean examenEmpezado();
    }

    public interface OnRespuestaListener {
        void onRespuesta(Respuesta[] respuestas);
    }

    private boolean examenEmpezado;
    private int cuenta;
    private Pregunta pregunta;
    private Respuesta[] respuestas;
    private OnRespuestaListener listener;

    public FragmentHacerExamen() {
        super(R.layout.listitem_preguntas);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvNumPregunta = view.findViewById(R.id.tvNumPregunta);
        TextView tvPregunta = view.findViewById(R.id.tvPregunta);
        ChipGroup cgRespuesta = view.findViewById(R.id.cgRespuestas);

        if (examenEmpezado) {
            tvNumPregunta.setText(cuenta + 1 + ".");
            tvPregunta.setText(pregunta.getPregunta());

            cgRespuesta.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
                @Override
                public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                    Chip chip = group.findViewById(group.getCheckedChipId());

                    if (group.getCheckedChipId() == -1) {
                        respuestas[cuenta] = null;
                        updateRespuesta();
                    }

                    if (view.findViewById(R.id.cA).equals(chip)) {
                        respuestas[cuenta] = Respuesta.A;
                        updateRespuesta();
                    } else if (view.findViewById(R.id.cB).equals(chip)) {
                        respuestas[cuenta] = Respuesta.B;
                        updateRespuesta();
                    } else if (view.findViewById(R.id.cC).equals(chip)) {
                        respuestas[cuenta] = Respuesta.C;
                        updateRespuesta();
                    } else if (view.findViewById(R.id.cD).equals(chip)) {
                        respuestas[cuenta] = Respuesta.D;
                        updateRespuesta();
                    }
                }
            });
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener attachListener = (IOnAttachListener) context;
        examenEmpezado = attachListener.examenEmpezado();

        if (examenEmpezado) {
            cuenta = attachListener.getCuenta();
            pregunta = attachListener.getPregunta();
            respuestas = attachListener.getRespuestas();
            listener = (OnRespuestaListener) context;
        }
    }

    public void updateRespuesta() {
        System.out.println(cuenta);
        System.out.println(Arrays.toString(respuestas));
        listener.onRespuesta(respuestas);
    }
}
