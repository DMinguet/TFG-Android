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
    public void onTemarioSeleccionado(int id) {
        apiService.getTemario(id).enqueue(new Callback<Temario>() {
            @Override
            public void onResponse(Call<Temario> call, Response<Temario> response) {
                assert response.body() != null;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.body().getPdf()));
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Temario> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error al abrir el enlace del temario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onExamenSeleccionado(int id) {
        apiService.getExamen(id).enqueue(new Callback<Examen>() {
            @Override
            public void onResponse(Call<Examen> call, Response<Examen> response) {
                Toast.makeText(MainActivity.this, "Solo tendrás 1 oportunidad. ¡Aprovéchala!", Toast.LENGTH_SHORT).show();
                Examen examen = response.body();

                apiService.getPreguntas().enqueue(new Callback<List<Pregunta>>() {
                    @Override
                    public void onResponse(Call<List<Pregunta>> call, Response<List<Pregunta>> response) {
                        List<Pregunta> preguntasExamen = new ArrayList<>();

                        assert response.body() != null;
                        for (Pregunta pregunta : response.body()) {
                            if (pregunta.getExamen().getId() == examen.getId()) {
                                preguntasExamen.add(pregunta);
                            }
                        }

                        startActivity(new Intent(MainActivity.this, ExamenActivity.class).putExtra("usuario", usuarioActivo).putExtra("examen", examen).putExtra("preguntas", (Serializable) preguntasExamen));
                    }

                    @Override
                    public void onFailure(Call<List<Pregunta>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "No se han podido obtener las preguntas", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<Examen> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error al iniciar el examen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Usuario getUsuarioActivoNotas() {
        return usuarioActivo;
    }
}