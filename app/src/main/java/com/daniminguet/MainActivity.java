package com.daniminguet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.daniminguet.fragments.FragmentPerfil;
import com.daniminguet.fragments.FragmentPrincipal;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.interfaces.ITemarioListener;
import com.daniminguet.models.Temario;
import com.daniminguet.models.Usuario;
import com.daniminguet.rest.RestClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FragmentPrincipal.IOnAttachListener, FragmentPerfil.IOnAttachListener, ITemarioListener {

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
                Toast.makeText(MainActivity.this, "No existe una aplicación para abrir el PDF", Toast.LENGTH_SHORT).show();
            }
        });
    }
}