package com.daniminguet.fragments.preguntas;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.daniminguet.models.Preguntas;
import com.daniminguet.models.Respuesta;
import com.daniminguet.models.Temario;
import com.daniminguet.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAnyadirPregunta extends Fragment implements SpinnerAdapter {
    private IAPIService apiService;
    private String[] titulosTemarios;
    private List<Temario> temarios;
    private Temario temarioSeleccionado;

    public FragmentAnyadirPregunta() {
        super(R.layout.anyadir_pregunta);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();
        temarios = new ArrayList<>();

        Spinner sTemarios = view.findViewById(R.id.sTemariosAnyadirPregunta);
        EditText etPregunta = view.findViewById(R.id.etPregunta);
        Spinner sRespuestas = view.findViewById(R.id.sPosiblesRespuestas);
        Button btnAnyadir = view.findViewById(R.id.btnAnyadirPregunta);
        Button btnVolver = view.findViewById(R.id.btnVolverAnyadirPregunta);

        apiService.getTemarios().enqueue(new Callback<List<Temario>>() {
            @Override
            public void onResponse(Call<List<Temario>> call, Response<List<Temario>> response) {
                assert response.body() != null;
                temarios.addAll(response.body());

                titulosTemarios = new String[temarios.size()];

                for (int i = 0; i < titulosTemarios.length; i++) {
                    titulosTemarios[i] = temarios.get(i).getTitulo();
                }

                sTemarios.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosTemarios));

                temarioSeleccionado = obtenerTemario(sTemarios.getSelectedItem().toString());
            }

            @Override
            public void onFailure(Call<List<Temario>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al obtener los temarios", Toast.LENGTH_SHORT).show();
            }
        });

        sRespuestas.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Respuesta.values()));

        btnAnyadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temarioSeleccionado = obtenerTemario(sTemarios.getSelectedItem().toString());
                String titulo = etPregunta.getText().toString();
                Respuesta respuesta = (Respuesta) sRespuestas.getSelectedItem();

                if (titulo.isEmpty()) {
                    etPregunta.setError("Se requiere una pregunta");
                    etPregunta.requestFocus();
                    return;
                }

                Preguntas nuevaPregunta = new Preguntas(temarioSeleccionado.getId(), titulo, respuesta);

                apiService.addPregunta(nuevaPregunta).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.isSuccessful()) {
                            if(Boolean.TRUE.equals(response.body())) {
                                Toast.makeText(getContext(), "Pregunta añadida correctamente", Toast.LENGTH_SHORT).show();
                                etPregunta.setText("");
                            } else {
                                Toast.makeText(getContext(), "La pregunta ya existe", Toast.LENGTH_SHORT).show();
                                etPregunta.setText("");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getContext(), "Error al añadir la pregunta", Toast.LENGTH_SHORT).show();
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

    private Temario obtenerTemario(String tituloTemario) {
        for (Temario temario : temarios) {
            if (temario.getTitulo().equals(tituloTemario)) {
                return temario;
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
