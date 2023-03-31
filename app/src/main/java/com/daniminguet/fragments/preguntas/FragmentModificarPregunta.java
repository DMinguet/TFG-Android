package com.daniminguet.fragments.preguntas;

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
import com.daniminguet.models.Preguntas;
import com.daniminguet.models.Respuesta;
import com.daniminguet.models.Temario;
import com.daniminguet.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentModificarPregunta extends Fragment implements SpinnerAdapter {
    private String[] preguntasString;
    private List<Preguntas> preguntas;
    private IAPIService apiService;
    private List<Temario> temarios;

    public FragmentModificarPregunta() {
        super(R.layout.modificar_pregunta);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = RestClient.getInstance();
        preguntas = new ArrayList<>();
        temarios = new ArrayList<>();

        Spinner sPreguntas = view.findViewById(R.id.sPreguntasModificar);
        Spinner sCampos = view.findViewById(R.id.sCamposPregunta);
        Spinner sNuevoValor = view.findViewById(R.id.sNuevoValor);
        EditText etNuevoValor = view.findViewById(R.id.etValorPregunta);
        Button btnModificar = view.findViewById(R.id.btnModificarPregunta);
        Button btnVolver = view.findViewById(R.id.btnVolverModificarPregunta);
        etNuevoValor.setVisibility(View.INVISIBLE);

        apiService.getPreguntas().enqueue(new Callback<List<Preguntas>>() {
            @Override
            public void onResponse(Call<List<Preguntas>> call, Response<List<Preguntas>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    preguntas.addAll(response.body());
                    preguntasString = new String[preguntas.size()];
                    String[] campos = {"Temario", "Pregunta", "Respuesta"};

                    for (int i = 0; i < preguntasString.length; i++) {
                        preguntasString[i] = preguntas.get(i).getPregunta();
                    }

                    sPreguntas.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, preguntasString));
                    sCampos.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, campos));

                    apiService.getTemarios().enqueue(new Callback<List<Temario>>() {
                        @Override
                        public void onResponse(Call<List<Temario>> call, Response<List<Temario>> response) {
                            assert response.body() != null;
                            temarios.addAll(response.body());
                            String[] titulosTemario = new String[temarios.size()];

                            for (int i = 0; i < titulosTemario.length; i++) {
                                titulosTemario[i] = temarios.get(i).getTitulo();
                            }

                            sNuevoValor.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosTemario));

                            sCampos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String campoSeleccionado = sCampos.getSelectedItem().toString();

                                    switch (campoSeleccionado) {
                                        case "Temario":
                                            sNuevoValor.setVisibility(View.VISIBLE);
                                            sNuevoValor.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosTemario));
                                            etNuevoValor.setVisibility(View.INVISIBLE);
                                            break;
                                        case "Pregunta":
                                            etNuevoValor.setVisibility(View.VISIBLE);
                                            sNuevoValor.setVisibility(View.INVISIBLE);
                                            break;
                                        case "Respuesta":
                                            sNuevoValor.setVisibility(View.VISIBLE);
                                            sNuevoValor.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Respuesta.values()));
                                            etNuevoValor.setVisibility(View.INVISIBLE);
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
                                    String preguntaSeleccionada = sPreguntas.getSelectedItem().toString();
                                    String campoSeleccionado = sCampos.getSelectedItem().toString();

                                    Preguntas preguntaCorrespondiente = obtenerPregunta(preguntaSeleccionada);

                                    switch (campoSeleccionado) {
                                        case "Temario":
                                            String temarioSeleccionado = sPreguntas.getSelectedItem().toString();
                                            Temario temarioCorrespondiente = obtenerTemario(temarioSeleccionado);

                                            preguntaCorrespondiente.setFkTemario(temarioCorrespondiente.getId());

                                            modificarPregunta(preguntaCorrespondiente);
                                            break;
                                        case "Pregunta":
                                            String nuevoValor = etNuevoValor.getText().toString();

                                            if (nuevoValor.isEmpty()) {
                                                etNuevoValor.setError("No has indicado el nuevo valor");
                                                etNuevoValor.requestFocus();
                                                return;
                                            }

                                            preguntaCorrespondiente.setPregunta(nuevoValor);

                                            modificarPregunta(preguntaCorrespondiente);
                                            etNuevoValor.setText("");

                                            break;
                                        case "Respuesta":
                                            Respuesta respuesta = (Respuesta) sNuevoValor.getSelectedItem();

                                            preguntaCorrespondiente.setRespuesta(respuesta);

                                            modificarPregunta(preguntaCorrespondiente);
                                            break;
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<List<Temario>> call, Throwable t) {
                            Toast.makeText(getContext(), "No se han podido obtener los temarios", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Preguntas>> call, Throwable t) {
                Toast.makeText(getContext(), "No se han podido obtener las preguntas", Toast.LENGTH_SHORT).show();
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

    private void modificarPregunta(Preguntas pregunta) {
        apiService.updatePregunta(pregunta).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()) {
                    Toast.makeText(getContext(), "Pregunta modificada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al modificar la pregunta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha podido modificar la pregunta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Preguntas obtenerPregunta(String pregunta) {
        for (Preguntas preguntaCorrespondiente : preguntas) {
            if (pregunta.equals(preguntaCorrespondiente.getPregunta())) {
                return preguntaCorrespondiente;
            }
        }

        return null;
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
