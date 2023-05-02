package com.daniminguet.fragments.preguntas;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniminguet.R;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Examen;
import com.daniminguet.models.Pregunta;
import com.daniminguet.models.PreguntaHasExamen;
import com.daniminguet.models.Temario;
import com.daniminguet.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentRelacionPreguntaExamen extends Fragment implements SpinnerAdapter {
    private Pregunta pregunta;
    private List<String> titulosExamenCorresp, titulosExamenNoCorresp;
    private IAPIService apiService;
    private List<PreguntaHasExamen> preguntasExamenes;
    private List<Examen> examenesCorresp, examenesNoCorresp;
    private PreguntaHasExamen preguntaExamenSeleccionadoContiene, preguntaExamenSeleccionadoNoContiene;

    public FragmentRelacionPreguntaExamen() {
        super(R.layout.relacion_pregunta_examen);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pregunta = (Pregunta) getArguments().getSerializable("pregunta");

        apiService = RestClient.getInstance();
        titulosExamenCorresp = new ArrayList<>();
        titulosExamenNoCorresp = new ArrayList<>();
        preguntasExamenes = new ArrayList<>();
        examenesCorresp = new ArrayList<>();
        examenesNoCorresp = new ArrayList<>();

        TextView tvPregunta = view.findViewById(R.id.tvPreguntaRelacion);
        Spinner sExamenesContiene = view.findViewById(R.id.sExamenesContiene);
        Spinner sExamenesNoContiene = view.findViewById(R.id.sExamenesNoContiene);
        Button btnDesvincular = view.findViewById(R.id.btnDesvincularExamen);
        Button btnVincular = view.findViewById(R.id.btnVincularExamenes);
        Button btnVolver = view.findViewById(R.id.btnVolverRelaciones);

        tvPregunta.setText(pregunta.getPregunta());

        apiService.getPreguntasExamenes().enqueue(new Callback<List<PreguntaHasExamen>>() {
            @Override
            public void onResponse(Call<List<PreguntaHasExamen>> call, Response<List<PreguntaHasExamen>> response) {
                assert response.body() != null;
                preguntasExamenes.addAll(response.body());

                for (PreguntaHasExamen preguntaExamen : preguntasExamenes) {
                    if (preguntaExamen.getPregunta().getId() == pregunta.getId()) {
                        examenesCorresp.add(preguntaExamen.getExamen());
                        titulosExamenCorresp.add(preguntaExamen.getExamen().getTitulo());
                    }
                }

                sExamenesContiene.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosExamenCorresp));

                apiService.getExamenes().enqueue(new Callback<List<Examen>>() {
                    @Override
                    public void onResponse(Call<List<Examen>> call, Response<List<Examen>> response) {
                        assert response.body() != null;
                        examenesNoCorresp.addAll(response.body());

                        for (Examen examen : examenesCorresp) {
                            examenesNoCorresp.removeIf(examen1 -> examen.getId() == examen1.getId());
                        }

                        for (Examen examen : examenesNoCorresp) {
                            titulosExamenNoCorresp.add(examen.getTitulo());
                        }

                        sExamenesNoContiene.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosExamenNoCorresp));
                    }

                    @Override
                    public void onFailure(Call<List<Examen>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<PreguntaHasExamen>> call, Throwable t) {

            }
        });

        sExamenesContiene.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preguntaExamenSeleccionadoContiene = obtenerPreguntaExamen(sExamenesContiene.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sExamenesNoContiene.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preguntaExamenSeleccionadoNoContiene = obtenerPreguntaExamen(sExamenesNoContiene.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnDesvincular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.deletePreguntaExamen(preguntaExamenSeleccionadoContiene.getId()).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.body()) {
                            Toast.makeText(getContext(), "Se ha eliminado la pregunta del examen", Toast.LENGTH_SHORT).show();

                            titulosExamenCorresp.clear();
                            titulosExamenNoCorresp.clear();
                            preguntasExamenes.clear();
                            examenesCorresp.clear();
                            examenesNoCorresp.clear();

                            apiService.getPreguntasExamenes().enqueue(new Callback<List<PreguntaHasExamen>>() {
                                @Override
                                public void onResponse(Call<List<PreguntaHasExamen>> call, Response<List<PreguntaHasExamen>> response) {
                                    assert response.body() != null;
                                    preguntasExamenes.addAll(response.body());

                                    for (PreguntaHasExamen preguntaExamen : preguntasExamenes) {
                                        if (preguntaExamen.getPregunta().getId() == pregunta.getId()) {
                                            examenesCorresp.add(preguntaExamen.getExamen());
                                            titulosExamenCorresp.add(preguntaExamen.getExamen().getTitulo());
                                        }
                                    }

                                    sExamenesContiene.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosExamenCorresp));

                                    apiService.getExamenes().enqueue(new Callback<List<Examen>>() {
                                        @Override
                                        public void onResponse(Call<List<Examen>> call, Response<List<Examen>> response) {
                                            assert response.body() != null;
                                            examenesNoCorresp.addAll(response.body());

                                            for (Examen examen : examenesCorresp) {
                                                examenesNoCorresp.removeIf(examen1 -> examen.getId() == examen1.getId());
                                            }

                                            for (Examen examen : examenesNoCorresp) {
                                                titulosExamenNoCorresp.add(examen.getTitulo());
                                            }

                                            sExamenesNoContiene.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosExamenNoCorresp));
                                        }

                                        @Override
                                        public void onFailure(Call<List<Examen>> call, Throwable t) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<List<PreguntaHasExamen>> call, Throwable t) {

                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "No se ha podido eliminar la pregunta del examen", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getContext(), "Error al eliminar la pregunta del examen", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnVincular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.addPreguntaExamen(preguntaExamenSeleccionadoNoContiene).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.body()) {
                            Toast.makeText(getContext(), "Se ha añadido la pregunta al examen", Toast.LENGTH_SHORT).show();

                            titulosExamenCorresp.clear();
                            titulosExamenNoCorresp.clear();
                            preguntasExamenes.clear();
                            examenesCorresp.clear();
                            examenesNoCorresp.clear();

                            apiService.getPreguntasExamenes().enqueue(new Callback<List<PreguntaHasExamen>>() {
                                @Override
                                public void onResponse(Call<List<PreguntaHasExamen>> call, Response<List<PreguntaHasExamen>> response) {
                                    assert response.body() != null;
                                    preguntasExamenes.addAll(response.body());

                                    for (PreguntaHasExamen preguntaExamen : preguntasExamenes) {
                                        if (preguntaExamen.getPregunta().getId() == pregunta.getId()) {
                                            examenesCorresp.add(preguntaExamen.getExamen());
                                            titulosExamenCorresp.add(preguntaExamen.getExamen().getTitulo());
                                        }
                                    }

                                    sExamenesContiene.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosExamenCorresp));

                                    apiService.getExamenes().enqueue(new Callback<List<Examen>>() {
                                        @Override
                                        public void onResponse(Call<List<Examen>> call, Response<List<Examen>> response) {
                                            assert response.body() != null;
                                            examenesNoCorresp.addAll(response.body());

                                            for (Examen examen : examenesCorresp) {
                                                examenesNoCorresp.removeIf(examen1 -> examen.getId() == examen1.getId());
                                            }

                                            for (Examen examen : examenesNoCorresp) {
                                                titulosExamenNoCorresp.add(examen.getTitulo());
                                            }

                                            sExamenesNoContiene.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titulosExamenNoCorresp));
                                        }

                                        @Override
                                        public void onFailure(Call<List<Examen>> call, Throwable t) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<List<PreguntaHasExamen>> call, Throwable t) {

                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "No se ha podido añadir la pregunta al examen", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getContext(), "Error al añadir la pregunta al examen", Toast.LENGTH_SHORT).show();
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
                        .replace(R.id.frgPrincipal, FragmentModificarPregunta.class, null)
                        .commit();
            }
        });
    }

    private PreguntaHasExamen obtenerPreguntaExamen(String tituloExamen) {
        for (PreguntaHasExamen preguntaHasExamen : preguntasExamenes) {
            if (preguntaHasExamen.getExamen().getTitulo().equals(tituloExamen) && preguntaHasExamen.getPregunta().getId() == pregunta.getId()) {
                return preguntaHasExamen;
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
