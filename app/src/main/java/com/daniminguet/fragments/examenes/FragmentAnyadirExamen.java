package com.daniminguet.fragments.examenes;

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
import com.daniminguet.models.Examen;
import com.daniminguet.rest.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAnyadirExamen extends Fragment {
    private IAPIService apiService;

    public FragmentAnyadirExamen() {
        super(R.layout.anyadir_examen);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();

        EditText etExamen = view.findViewById(R.id.etExamen);
        Button btnAnyadir = view.findViewById(R.id.btnAnyadirExamen);
        Button btnVolver = view.findViewById(R.id.btnVolverAnyadirExamen);

        btnAnyadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String examen = etExamen.getText().toString();

                if (examen.isEmpty()) {
                    etExamen.setError("Se requiere un título");
                    etExamen.requestFocus();
                    return;
                }

                Examen nuevoExamen = new Examen(examen);

                apiService.addExamen(nuevoExamen).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.isSuccessful()) {
                            if(Boolean.TRUE.equals(response.body())) {
                                Toast.makeText(getContext(), "Examen añadido correctamente", Toast.LENGTH_SHORT).show();
                                etExamen.setText("");
                            } else {
                                Toast.makeText(getContext(), "El examen ya existe", Toast.LENGTH_SHORT).show();
                                etExamen.setText("");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getContext(), "Error al añadir el examen", Toast.LENGTH_SHORT).show();
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
