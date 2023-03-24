package com.daniminguet.fragments.usuarios;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniminguet.HashGenerator;
import com.daniminguet.R;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Usuario;
import com.daniminguet.rest.RestClient;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAnyadirUsuario extends Fragment {
    private IAPIService apiService;

    public FragmentAnyadirUsuario() {
        super(R.layout.anyadir_usuario);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();

        EditText etNombre = view.findViewById(R.id.etNombre);
        EditText etApellidos = view.findViewById(R.id.etApellidos);
        EditText etNombreUsuario = view.findViewById(R.id.etNombreUsuario);
        EditText etContrasenya = view.findViewById(R.id.etContrasenyaAnyadir);
        EditText etEmail = view.findViewById(R.id.etEmail);
        Button btnAnyadir = view.findViewById(R.id.btnAnyadirUsuario);

        btnAnyadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombre.getText().toString();
                String apellidos = etApellidos.getText().toString();
                String nombreUsuario = etNombreUsuario.getText().toString();
                String contrasenya = etContrasenya.getText().toString();
                String email = etEmail.getText().toString();

                if (nombre.isEmpty()) {
                    etNombre.setError("Se requiere un nombre");
                    etNombre.requestFocus();
                    return;
                } else if (apellidos.isEmpty()) {
                    etApellidos.setError("Se requieren los apellidos");
                    etApellidos.requestFocus();
                    return;
                } else if (nombreUsuario.isEmpty()) {
                    etNombreUsuario.setError("Se requiere un nombre de usuario");
                    etNombreUsuario.requestFocus();
                    return;
                } else if (contrasenya.isEmpty()) {
                    etContrasenya.setError("Se requiere una contraseña");
                    etContrasenya.requestFocus();
                    return;
                } else if (email.isEmpty()) {
                    etEmail.setError("Se requiere un email");
                    etEmail.requestFocus();
                    return;
                } else if (!validarEmail(email)) {
                    etEmail.setError("Email no válido");
                    etEmail.requestFocus();
                    return;
                }

                Usuario nuevoUsuario = null;
                try {
                    nuevoUsuario = new Usuario(nombre, apellidos, nombreUsuario, HashGenerator.getSHAString(contrasenya), email);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                apiService.addUsuario(nuevoUsuario).enqueue(new Callback<Boolean>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.isSuccessful()) {
                            if(Boolean.TRUE.equals(response.body())) {
                                Toast.makeText(getContext(), "Usuario añadido correctamente", Toast.LENGTH_SHORT).show();
                                etNombre.setText("");
                                etApellidos.setText("");
                                etNombreUsuario.setText("");
                                etContrasenya.setText("");
                                etEmail.setText("");
                            } else {
                                Toast.makeText(getContext(), "El usuario ya existe", Toast.LENGTH_SHORT).show();
                                etNombre.setText("");
                                etApellidos.setText("");
                                etNombreUsuario.setText("");
                                etContrasenya.setText("");
                                etEmail.setText("");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getContext(), "Error al añadir el usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
