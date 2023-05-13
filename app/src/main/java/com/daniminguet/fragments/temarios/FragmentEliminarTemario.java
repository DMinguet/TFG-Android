package com.daniminguet.fragments.temarios;

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
import com.daniminguet.models.Temario;
import com.daniminguet.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEliminarTemario extends Fragment implements SpinnerAdapter {
    private String[] titulosTemarios;
    private List<Temario> temarios;
    private Temario temarioSeleccionado;
    private IAPIService apiService;

    public FragmentEliminarTemario() {
        super(R.layout.eliminar_temario);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();
        temarios = new ArrayList<>();

        Spinner sTemarios = view.findViewById(R.id.sTemariosEliminar);
        Button btnEliminar = view.findViewById(R.id.btnEliminarTemario);
        Button btnVolver = view.findViewById(R.id.btnVolverEliminarTemario);

        apiService.getTemarios().enqueue(new Callback<List<Temario>>() {
            @Override
            public void onResponse(Call<List<Temario>> call, Response<List<Temario>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    temarios.addAll(response.body());
                    titulosTemarios = new String[temarios.size()];

                    for (int i = 0; i < titulosTemarios.length; i++) {
                        titulosTemarios[i] = temarios.get(i).getTitulo();
                    }

                    sTemarios.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosTemarios));

                    temarioSeleccionado = obtenerTemario(sTemarios.getSelectedItem().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Temario>> call, Throwable t) {
                Toast.makeText(getContext(), "No se han podido obtener los temarios", Toast.LENGTH_SHORT).show();
            }
        });

        sTemarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                temarioSeleccionado = obtenerTemario(sTemarios.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.deleteTemario(temarioSeleccionado.getId()).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(Boolean.TRUE.equals(response.body())) {
                            Toast.makeText(getContext(), "Temario y datos relacionados eliminados", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(), "Las preguntas relacionadas con el temario eliminado deberás asignarles un nuevo temario", Toast.LENGTH_LONG).show();

                            apiService.getTemarios().enqueue(new Callback<List<Temario>>() {
                                @Override
                                public void onResponse(Call<List<Temario>> call, Response<List<Temario>> response) {
                                    temarios.clear();
                                    if (response.isSuccessful()) {
                                        assert response.body() != null;
                                        temarios.addAll(response.body());

                                        titulosTemarios = new String[temarios.size()];

                                        for (int i = 0; i < titulosTemarios.length; i++) {
                                            titulosTemarios[i] = temarios.get(i).getTitulo();
                                        }

                                        sTemarios.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosTemarios));
                                        sTemarios.setSelection(0);
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Temario>> call, Throwable t) {
                                    Toast.makeText(getContext(), "No se han podido obtener los temarios", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Error al eliminar el temario", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getContext(), "No se ha podido eliminar el temario", Toast.LENGTH_SHORT).show();
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
