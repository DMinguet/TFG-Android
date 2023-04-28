package com.daniminguet.fragments.preguntas;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniminguet.R;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Examen;
import com.daniminguet.models.Pregunta;
import com.daniminguet.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

public class FragmentRelacionPreguntaExamen extends Fragment implements SpinnerAdapter {
    private Pregunta pregunta;
    private String[] titulosExamen;
    private IAPIService apiService;
    private List<Examen> examenes;

    public FragmentRelacionPreguntaExamen() {
        super(R.layout.relacion_pregunta_examen);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pregunta = (Pregunta) getArguments().getSerializable("pregunta");

        apiService = RestClient.getInstance();
        examenes = new ArrayList<>();

        TextView tvPregunta = view.findViewById(R.id.tvPreguntaRelacion);
        RadioGroup rgExamenesContiene = view.findViewById(R.id.rgExamenesContiene);
        RadioGroup rgExamenesNoContiene = view.findViewById(R.id.rgExamenesNoContiene);
        Button btnDesvincular = view.findViewById(R.id.btnDesvincularExamen);
        Button btnVincular = view.findViewById(R.id.btnVincularExamenes);
        Button btnVolver = view.findViewById(R.id.btnVolverRelaciones);

        tvPregunta.setText(pregunta.getPregunta());

        //apiService.getPreguntasExamenes().enqueue();

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentModificarPregunta.class, null)
                        .commit();
            }
        });
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
