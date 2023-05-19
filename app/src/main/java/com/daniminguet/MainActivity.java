package com.daniminguet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.daniminguet.fragments.FragmentNotas;
import com.daniminguet.fragments.FragmentPerfil;
import com.daniminguet.fragments.FragmentPrincipal;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.interfaces.IExamenListener;
import com.daniminguet.interfaces.ITemarioListener;
import com.daniminguet.models.Examen;
import com.daniminguet.models.Pregunta;
import com.daniminguet.models.PreguntaHasExamen;
import com.daniminguet.models.Temario;
import com.daniminguet.models.Usuario;
import com.daniminguet.rest.RestClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FragmentPrincipal.IOnAttachListener, FragmentPerfil.IOnAttachListener, ITemarioListener, IExamenListener,
        FragmentNotas.IOnAttachListener {

    private IAPIService apiService;
    private Usuario usuarioActivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cargarDatos();
    }

    private void cargarDatos() {
        apiService = RestClient.getInstance();
        cargarUsuarioActivo();
    }

    private void cargarUsuarioActivo() {
        if (usuarioActivo == null) {
            usuarioActivo = (Usuario) getIntent().getSerializableExtra("user");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.iInicio) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.frgPrincipal, FragmentPrincipal.class, null)
                    .commit();
            return true;
        } else if (id == R.id.iPerfil) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .replace(R.id.frgPrincipal, FragmentPerfil.class, null)
                    .commit();
            return true;
        }else if (id == R.id.iCerrarSesion) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Usuario getUsuarioFrgPrinc() {
        if (usuarioActivo == null) {
            cargarUsuarioActivo();
        }
        return usuarioActivo;
    }

    @Override
    public Usuario getUsuarioFrgPerfl() {
        return usuarioActivo;
    }

    @Override
    public void onTemarioSeleccionado(Temario temario) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(temario.getPdf()));
        startActivity(intent);
    }

    @Override
    public void onExamenSeleccionado(Examen examenSeleccionado) {
        Toast.makeText(MainActivity.this, "Solo tendrás 1 oportunidad. ¡Aprovéchala!", Toast.LENGTH_SHORT).show();

        apiService.getPreguntasExamenes().enqueue(new Callback<List<PreguntaHasExamen>>() {
            @Override
            public void onResponse(Call<List<PreguntaHasExamen>> call, Response<List<PreguntaHasExamen>> response) {
                List<Pregunta> preguntasCorrespondientes = new ArrayList<>();

                assert response.body() != null;
                List<PreguntaHasExamen> preguntasExamen = new ArrayList<>(response.body());

                assert examenSeleccionado != null;
                for (PreguntaHasExamen pregunta : preguntasExamen) {
                    if (examenSeleccionado.getId() == pregunta.getExamen().getId()) {
                        preguntasCorrespondientes.add(pregunta.getPregunta());
                    }
                }

                startActivity(new Intent(MainActivity.this, ExamenActivity.class).putExtra("usuario", usuarioActivo).putExtra("examen", examenSeleccionado).putExtra("preguntas", (Serializable) preguntasCorrespondientes));
            }

            @Override
            public void onFailure(Call<List<PreguntaHasExamen>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No se han podido obtener las preguntas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Usuario getUsuarioActivoNotas() {
        return usuarioActivo;
    }
}