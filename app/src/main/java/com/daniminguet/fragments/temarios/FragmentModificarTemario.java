package com.daniminguet.fragments.temarios;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.daniminguet.models.Temario;
import com.daniminguet.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentModificarTemario extends Fragment implements SpinnerAdapter {
    private String[] tituloTemarios;
    private List<Temario> temarios;
    private IAPIService apiService;

    public FragmentModificarTemario() {
        super(R.layout.modificar_temario);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = RestClient.getInstance();
        temarios = new ArrayList<>();

        Spinner sTemarios = view.findViewById(R.id.sTemariosModificar);
        Spinner sCampos = view.findViewById(R.id.sCamposTemario);
        EditText etNuevoValor = view.findViewById(R.id.etValorTemario);
        Button btnModificar = view.findViewById(R.id.btnModificarTemario);
        Button btnVolver = view.findViewById(R.id.btnVolverModificarTemario);

        apiService.getTemarios().enqueue(new Callback<List<Temario>>() {
            @Override
            public void onResponse(Call<List<Temario>> call, Response<List<Temario>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    temarios.addAll(response.body());
                    tituloTemarios = new String[temarios.size()];
                    String[] campos = {"Tema", "Titulo", "URL PDF"};

                    for (int i = 0; i < tituloTemarios.length; i++) {
                        tituloTemarios[i] = temarios.get(i).getTitulo();
                    }

                    sTemarios.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tituloTemarios));
                    sCampos.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, campos));

                    sCampos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String campoSeleccionado = sCampos.getSelectedItem().toString();

                            switch (campoSeleccionado) {
                                case "Titulo":
                                case "URL PDF":
                                    etNuevoValor.setInputType(View.AUTOFILL_TYPE_TEXT);
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    btnModificar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String temarioSeleccionado = sTemarios.getSelectedItem().toString();
                            String campoSeleccionado = sCampos.getSelectedItem().toString();

                            Temario temarioCorrespondiente = obtenerTemario(temarioSeleccionado);

                            String nuevoValor;

                            switch (campoSeleccionado) {
                                case "Tema":
                                    nuevoValor = etNuevoValor.getText().toString();

                                    if (nuevoValor.isEmpty()) {
                                        etNuevoValor.setError("No has indicado el nuevo valor");
                                        etNuevoValor.requestFocus();
                                        return;
                                    }

                                    temarioCorrespondiente.setTema(Integer.valueOf(nuevoValor));

                                    modificarTemario(temarioCorrespondiente);
                                    etNuevoValor.setText("");

                                    break;
                                case "Titulo":
                                    nuevoValor = etNuevoValor.getText().toString();

                                    if (nuevoValor.isEmpty()) {
                                        etNuevoValor.setError("No has indicado el nuevo valor");
                                        etNuevoValor.requestFocus();
                                        return;
                                    }

                                    temarioCorrespondiente.setTitulo(nuevoValor);

                                    modificarTemario(temarioCorrespondiente);
                                    etNuevoValor.setText("");

                                    break;
                                case "URL PDF":
                                    nuevoValor = etNuevoValor.getText().toString();

                                    if (nuevoValor.isEmpty()) {
                                        etNuevoValor.setError("No has indicado el nuevo valor");
                                        etNuevoValor.requestFocus();
                                        return;
                                    }

                                    temarioCorrespondiente.setPdf(nuevoValor);

                                    modificarTemario(temarioCorrespondiente);
                                    etNuevoValor.setText("");

                                    break;
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Temario>> call, Throwable t) {
                Toast.makeText(getContext(), "No se han podido obtener los temarios", Toast.LENGTH_SHORT).show();
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

    private void modificarTemario(Temario temario) {
        apiService.updateTemario(temario).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()) {
                    Toast.makeText(getContext(), "Temario modificado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al modificar el temario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha podido modificar el temario", Toast.LENGTH_SHORT).show();
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
