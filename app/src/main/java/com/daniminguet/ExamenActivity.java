package com.daniminguet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.daniminguet.fragments.examenes.FragmentHacerExamen;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Examen;
import com.daniminguet.models.Pregunta;
import com.daniminguet.models.Respuesta;
import com.daniminguet.models.Usuario;
import com.daniminguet.models.UsuarioHasExamen;
import com.daniminguet.rest.RestClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamenActivity extends AppCompatActivity implements FragmentHacerExamen.IOnAttachListener, FragmentHacerExamen.OnRespuestaListener {

    private final int NUM_PREGUNTAS = 10;
    private IAPIService apiService;
    private Usuario usuarioActivo;
    private Examen examenSeleccionado;
    private List<Pregunta> preguntas;
    private boolean examenAcabado, examenEmpezado;
    private int cuenta;
    private double nota;
    private Respuesta[] respuestasUsuario;

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.examen);
        apiService = RestClient.getInstance();

        cargarDatos();

        TextView tvInfo1 = findViewById(R.id.tvInfo1);
        TextView tvInfo2 = findViewById(R.id.tvInfo2);
        Button btnSiguiente = findViewById(R.id.btnSiguiente);
        Button btnEmpezar = findViewById(R.id.btnEmpezar);

        btnSiguiente.setVisibility(View.INVISIBLE);
        setTitle(examenSeleccionado.getTitulo());

        btnEmpezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                examenEmpezado = true;
                tvInfo1.setVisibility(View.GONE);
                tvInfo2.setVisibility(View.GONE);
                btnEmpezar.setVisibility(View.GONE);
                btnSiguiente.setVisibility(View.VISIBLE);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPreguntas, FragmentHacerExamen.class, null)
                        .commit();
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cuenta++;

                if (cuenta == NUM_PREGUNTAS-1) {
                    btnSiguiente.setText("ACABAR EXAMEN");
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .replace(R.id.frgPreguntas, FragmentHacerExamen.class, null)
                            .commit();
                } else if (cuenta == NUM_PREGUNTAS) {
                    examenAcabado = true;

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String fechaExamen = sdf.format(new Date());

                    Respuesta[] respuestasCorrectas = new Respuesta[NUM_PREGUNTAS];
                    for (int i = 0; i < preguntas.size(); i++) {
                        respuestasCorrectas[i] = Respuesta.valueOf(preguntas.get(i).getRespuesta());
                    }

                    for (int i = 0; i < respuestasCorrectas.length; i++) {
                        if (respuestasCorrectas[i] == respuestasUsuario[i]) {
                            nota++;
                        } else if (respuestasUsuario[i] == null) {
                            //Al no haber seleccionado ninguna opcion, no suma ni resta
                        } else {
                            nota -= 0.25;
                        }
                    }

                    UsuarioHasExamen resultadoExamen = new UsuarioHasExamen(usuarioActivo, examenSeleccionado, nota, fechaExamen);
                    apiService.addExamenUsuario(resultadoExamen).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (Boolean.TRUE.equals(response.body())) {
                                if (nota >= 5) {
                                    Toast.makeText(ExamenActivity.this, "Enhorabuena, has sacado un " + nota, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ExamenActivity.this, "Debes esforzarte, has sacado un " + nota, Toast.LENGTH_LONG).show();
                                }
                                Toast.makeText(ExamenActivity.this, "Podrás ver el resultado también en el apartado de las notas", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ExamenActivity.this, "Solo puedes hacer el examen una vez, nota obtenida: " + nota, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(ExamenActivity.this, "No se ha podido guardar el examen", Toast.LENGTH_SHORT).show();
                        }
                    });
                    startActivity(new Intent(ExamenActivity.this, MainActivity.class).putExtra("user", usuarioActivo));
                } else {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .replace(R.id.frgPreguntas, FragmentHacerExamen.class, null)
                            .commit();
                }
            }
        });
    }

    private void cargarDatos() {
        examenAcabado = false;
        examenEmpezado = false;
        obtenerPreguntas();
        cargarUsuarioActivo();
        cargarExamenActivo();
        preguntas = new ArrayList<>();
        respuestasUsuario = new Respuesta[NUM_PREGUNTAS];
        cuenta = 0;
        nota = 0;
    }

    private void obtenerPreguntas() {
        if (preguntas == null) {
            preguntas = (List<Pregunta>) getIntent().getSerializableExtra("preguntas");
        }
    }

    private void cargarUsuarioActivo() {
        if (usuarioActivo == null) {
            usuarioActivo = (Usuario) getIntent().getSerializableExtra("usuario");
        }
    }

    private void cargarExamenActivo() {
        if (examenSeleccionado == null) {
            examenSeleccionado = (Examen) getIntent().getSerializableExtra("examen");
        }
    }

    @Override
    public void onBackPressed() {
        if (!examenAcabado) {
            Toast.makeText(ExamenActivity.this, "Debes acabar el examen", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onUserLeaveHint() {
        if (!examenAcabado) {
            Toast.makeText(ExamenActivity.this, "Debes acabar el examen", Toast.LENGTH_SHORT).show();
        } else {
            super.onUserLeaveHint();
        }
    }

    @Override
    public int getCuenta() {
        return cuenta;
    }

    @Override
    public Pregunta getPregunta() {
        if (preguntas.isEmpty()) {
            preguntas = (List<Pregunta>) getIntent().getSerializableExtra("preguntas");
        }
        return preguntas.get(cuenta);
    }

    @Override
    public Respuesta[] getRespuestas() {
        return respuestasUsuario;
    }

    @Override
    public boolean examenEmpezado() {
        return examenEmpezado;
    }

    @Override
    public void onRespuesta(Respuesta[] respuestas) {
        this.respuestasUsuario = respuestas;
    }
}
