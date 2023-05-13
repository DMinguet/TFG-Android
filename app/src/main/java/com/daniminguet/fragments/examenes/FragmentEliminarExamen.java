package com.daniminguet.fragments.examenes;

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
import com.daniminguet.fragments.FragmentAdmin;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Examen;
import com.daniminguet.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEliminarExamen extends Fragment implements SpinnerAdapter {
    private String[] titulosExamen;
    private List<Examen> examenes;
    private IAPIService apiService;

    public FragmentEliminarExamen() {
        super(R.layout.eliminar_examen);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = RestClient.getInstance();
        examenes = new ArrayList<>();

        Spinner sExamenes = view.findViewById(R.id.sExamenesEliminar);
        Button btnEliminar = view.findViewById(R.id.btnEliminarExamen);
        Button btnVolver = view.findViewById(R.id.btnVolverEliminarExamen);

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

                    btnEliminar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String examenSeleccionado = sExamenes.getSelectedItem().toString();

                            Examen examenCorrespondiente = obtenerExamen(examenSeleccionado);

                            apiService.deleteExamen(examenCorrespondiente.getId()).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if(Boolean.TRUE.equals(response.body())) {
                                        Toast.makeText(getContext(), "Examen y datos relacionados eliminados", Toast.LENGTH_SHORT).show();

                                        apiService.getExamenes().enqueue(new Callback<List<Examen>>() {
                                            @Override
                                            public void onResponse(Call<List<Examen>> call, Response<List<Examen>> response) {
                                                examenes.clear();
                                                if (response.isSuccessful()) {
                                                    assert response.body() != null;
                                                    examenes.addAll(response.body());

                                                    titulosExamen = new String[examenes.size()];

                                                    for (int i = 0; i < titulosExamen.length; i++) {
                                                        titulosExamen[i] = examenes.get(i).getTitulo();
                                                    }

                                                    sExamenes.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosExamen));
                                                    sExamenes.setSelection(0);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<Examen>> call, Throwable t) {
                                                Toast.makeText(getContext(), "No se han podido obtener los exámenes", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getContext(), "Error al eliminar el examen", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    Toast.makeText(getContext(), "No se ha podido eliminar el examen", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Examen>> call, Throwable t) {
                Toast.makeText(getContext(), "No se han podido obtener los exámenes", Toast.LENGTH_SHORT).show();
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

    private Examen obtenerExamen (String titulo) {
        for (Examen examen : examenes) {
            if (examen.getTitulo().equals(titulo)) {
                return examen;
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
