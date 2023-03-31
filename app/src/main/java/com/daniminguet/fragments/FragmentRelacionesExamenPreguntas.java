package com.daniminguet.fragments;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniminguet.R;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Examen;
import com.daniminguet.models.Preguntas;
import com.daniminguet.models.PreguntasExam;
import com.daniminguet.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRelacionesExamenPreguntas extends Fragment implements SpinnerAdapter {

    private String[] titulosExamen;
    private List<Examen> examenes;
    private String[] preguntasString;
    private List<Preguntas> preguntas;
    private IAPIService apiService;

    public FragmentRelacionesExamenPreguntas() {
        super(R.layout.relacion_exampreg);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = RestClient.getInstance();
        preguntas = new ArrayList<>();
        examenes = new ArrayList<>();

        Spinner sExamenes = view.findViewById(R.id.sExamenesRelaciones);
        Spinner sPreguntas = view.findViewById(R.id.sPreguntasRelaciones);
        Button btnRelacionar = view.findViewById(R.id.btnRelacionar);
        Button btnVolver = view.findViewById(R.id.btnVolverRelaciones);

        apiService.getExamenes().enqueue(new Callback<List<Examen>>() {
            @Override
            public void onResponse(Call<List<Examen>> call, Response<List<Examen>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    examenes.addAll(response.body());
                    titulosExamen = new String[examenes.size()];

                    for (int i = 0; i < titulosExamen.length; i++) {
                        titulosExamen[i] = examenes.get(i).getTitulo();
                    }

                    sExamenes.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosExamen));

                    apiService.getPreguntas().enqueue(new Callback<List<Preguntas>>() {
                        @Override
                        public void onResponse(Call<List<Preguntas>> call, Response<List<Preguntas>> response) {
                            assert response.body() != null;
                            preguntas.addAll(response.body());
                            String[] preguntasString = new String[preguntas.size()];

                            for (int i = 0; i < preguntasString.length; i++) {
                                preguntasString[i] = preguntas.get(i).getPregunta();
                            }

                            sPreguntas.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, preguntasString));

                            btnRelacionar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String examenSeleccionado = sExamenes.getSelectedItem().toString();
                                    String preguntaSeleccionada = sPreguntas.getSelectedItem().toString();

                                    Examen examenCorrespondiente = obtenerExamen(examenSeleccionado);
                                    Preguntas preguntaCorrespondiente = obtenerPregunta(preguntaSeleccionada);

                                    apiService.addPreguntasExamen(new PreguntasExam(examenCorrespondiente.getId(), preguntaCorrespondiente.getId())).enqueue(new Callback<Boolean>() {
                                        @Override
                                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                            if(response.isSuccessful()) {
                                                if(Boolean.TRUE.equals(response.body())) {
                                                    Toast.makeText(getContext(), "Relación añadida correctamente", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getContext(), "La relación ya existe", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Boolean> call, Throwable t) {
                                            Toast.makeText(getContext(), "Error al añadir la nueva relación", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<List<Preguntas>> call, Throwable t) {
                            Toast.makeText(getContext(), "No se han podido obtener las preguntas", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Examen>> call, Throwable t) {
                Toast.makeText(getContext(), "No se han podido obtener los examenes", Toast.LENGTH_SHORT).show();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentAdmin.class, null)
                        .commit();
            }
        });
    }

    private Examen obtenerExamen(String tituloExamen) {
        for (Examen examenCorrespondiente : examenes) {
            if (tituloExamen.equals(examenCorrespondiente.getTitulo())) {
                return examenCorrespondiente;
            }
        }

        return null;
    }

    private Preguntas obtenerPregunta(String pregunta) {
        for (Preguntas preguntaCorrespondiente : preguntas) {
            if (pregunta.equals(preguntaCorrespondiente.getPregunta())) {
                return preguntaCorrespondiente;
            }
        }

        return null;
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
