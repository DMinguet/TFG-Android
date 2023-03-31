package com.daniminguet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.adaptadores.AdaptadorPreguntas;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Examen;
import com.daniminguet.models.Pregunta;
import com.daniminguet.models.Usuario;
import com.daniminguet.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamenActivity extends AppCompatActivity {
    private IAPIService apiService;
    private Usuario usuarioActivo;
    private Examen examenSeleccionado;
    private List<Pregunta> preguntas;
    private boolean examenAcabado;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examen);

        apiService = RestClient.getInstance();
        examenAcabado = false;
        usuarioActivo = (Usuario) getIntent().getSerializableExtra("usuario");
        examenSeleccionado = (Examen) getIntent().getSerializableExtra("examen");
        preguntas = new ArrayList<>();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView rvLista = findViewById(R.id.rvPreguntas);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnAcabarExamen = findViewById(R.id.btnAcabarExamen);

        apiService.getPreguntas().enqueue(new Callback<List<Pregunta>>() {
            @Override
            public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    for (Pregunta pregunta : response.body()) {
                        if (pregunta.getTemario().getId() == 1) {
                            System.out.println("HOLA");
                        }
                    }

                    AdaptadorPreguntas adaptadorPreguntas = new AdaptadorPreguntas(response.body());
                    rvLista.setHasFixedSize(true);
                    rvLista.setAdapter(adaptadorPreguntas);
                    rvLista.setLayoutManager(new LinearLayoutManager(ExamenActivity.this, LinearLayoutManager.VERTICAL, false));
                }
            }

            @Override
            public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                Toast.makeText(ExamenActivity.this, "No se han podido obtener las preguntas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!examenAcabado) {
            Toast.makeText(ExamenActivity.this, "Debes de acabar el examen", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onUserLeaveHint() {
        if (!examenAcabado) {
            Toast.makeText(ExamenActivity.this, "Debes de acabar el examen", Toast.LENGTH_SHORT).show();
        } else {
            super.onUserLeaveHint();
        }
    }
}
