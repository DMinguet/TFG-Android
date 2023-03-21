package com.daniminguet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.daniminguet.fragments.FragmentPerfil;
import com.daniminguet.fragments.FragmentPrincipal;
import com.daniminguet.models.Usuario;
import com.daniminguet.rest.RestClient;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements FragmentPrincipal.IOnAttachListener, FragmentPerfil.IOnAttachListener{

    private Usuario usuarioActivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cargarDatos();
    }

    private void cargarDatos() {
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
}