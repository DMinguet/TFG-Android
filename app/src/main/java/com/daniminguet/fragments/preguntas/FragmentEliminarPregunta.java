package com.daniminguet.fragments.preguntas;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.daniminguet.fragments.FragmentAdmin;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Pregunta;
import com.daniminguet.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEliminarPregunta extends Fragment implements SpinnerAdapter {
    private String[] preguntasString;
    private List<Pregunta> preguntas;
    private Pregunta preguntaSeleccionada;
    private IAPIService apiService;

    public FragmentEliminarPregunta() {
        super(R.layout.eliminar_pregunta);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();
        preguntas = new ArrayList<>();

        Spinner sPreguntas = view.findViewById(R.id.sPreguntasEliminar);
        Button btnEliminar = view.findViewById(R.id.btnEliminarPregunta);
        Button btnVolver = view.findViewById(R.id.btnVolverEliminarPregunta);

        apiService.getPreguntas().enqueue(new Callback<List<Pregunta>>() {
            @Override
            public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    preguntas.addAll(response.body());
                    preguntasString = new String[preguntas.size()];

                    for (int i = 0; i < preguntasString.length; i++) {
                        preguntasString[i] = preguntas.get(i).getPregunta();
                    }

                    sPreguntas.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, preguntasString));

                    preguntaSeleccionada = obtenerPregunta(sPreguntas.getSelectedItem().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                Toast.makeText(getContext(), "No se han podido obtener las preguntas", Toast.LENGTH_SHORT).show();
            }
        });

        sPreguntas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preguntaSeleccionada = obtenerPregunta(sPreguntas.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.deletePregunta(preguntaSeleccionada.getId()).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(Boolean.TRUE.equals(response.body())) {
                            Toast.makeText(getContext(), "Pregunta y datos relacionados eliminados", Toast.LENGTH_SHORT).show();

                            apiService.getPreguntas().enqueue(new Callback<List<Pregunta>>() {
                                @Override
                                public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {
                                    preguntas.clear();
                                    if (response.isSuccessful()) {
                                        assert response.body() != null;
                                        preguntas.addAll(response.body());

                                        preguntasString = new String[preguntas.size()];

                                        for (int i = 0; i < preguntasString.length; i++) {
                                            preguntasString[i] = preguntas.get(i).getPregunta();
                                        }

                                        sPreguntas.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, preguntasString));
                                        sPreguntas.setSelection(0);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                                    Toast.makeText(getContext(), "No se han podido obtener las preguntas", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Error al eliminar la pregunta", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getContext(), "No se ha podido eliminar la pregunta", Toast.LENGTH_SHORT).show();
                    }
                });
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

    private Pregunta obtenerPregunta(String pregunta) {
        for (Pregunta preguntaCorrespondiente : preguntas) {
            if (preguntaCorrespondiente.getPregunta().equals(pregunta)) {
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
