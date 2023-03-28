package com.daniminguet.fragments.temarios;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAnyadirTemario extends Fragment {
    private IAPIService apiService;

    public FragmentAnyadirTemario() {
        super(R.layout.anyadir_temario);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();

        EditText etTema = view.findViewById(R.id.etTema);
        EditText etTitulo = view.findViewById(R.id.etTitulo);
        EditText etURL = view.findViewById(R.id.etURL);
        Button btnAnyadir = view.findViewById(R.id.btnAnyadirTemario);
        Button btnVolver = view.findViewById(R.id.btnVolverAnyadirTemario);

        btnAnyadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tema = etTema.getText().toString();
                String titulo = etTitulo.getText().toString();
                String url = etURL.getText().toString();

                if (tema.isEmpty()) {
                    etTema.setError("Se requiere un número de tema");
                    etTema.requestFocus();
                    return;
                } else if (titulo.isEmpty()) {
                    etTitulo.setError("Se requiere un título");
                    etTitulo.requestFocus();
                    return;
                } else if (url.isEmpty()) {
                    etURL.setError("Se requiere una URL del PDF");
                    etURL.requestFocus();
                    return;
                }

                int numTema = 0;
                try {
                    numTema = Integer.parseInt(tema);
                } catch (NumberFormatException nfe) {
                    etTema.setError("Número de temario no válido");
                    etTema.requestFocus();
                    return;
                }

                Temario nuevoTemario = new Temario(numTema, titulo, url);

                apiService.addTemario(nuevoTemario).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.isSuccessful()) {
                            if(Boolean.TRUE.equals(response.body())) {
                                Toast.makeText(getContext(), "Temario añadido correctamente", Toast.LENGTH_SHORT).show();
                                etTema.setText("");
                                etTitulo.setText("");
                                etURL.setText("");
                            } else {
                                Toast.makeText(getContext(), "El temario ya existe", Toast.LENGTH_SHORT).show();
                                etTema.setText("");
                                etTitulo.setText("");
                                etURL.setText("");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getContext(), "Error al añadir el temario", Toast.LENGTH_SHORT).show();
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
}
