package com.daniminguet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Usuario;
import com.daniminguet.rest.RestClient;
import com.google.android.material.textfield.TextInputEditText;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private IAPIService apiService;
    private TextInputEditText etUsername, etPassword;
    private Usuario usuarioActivo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usuarioActivo = null;

        apiService = RestClient.getInstance();

        Call<List<Usuario>> usuariosCall = apiService.getUsuarios();
        usuariosCall.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                System.out.println("No se han podido obtener los usuarios");
            }
        });

        etUsername = findViewById(R.id.etUsernameLogin);
        etPassword = findViewById(R.id.etContrasenyaLogin);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loginUser();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void loginUser() throws NoSuchAlgorithmException {
        String username = etUsername.getText().toString();
        String password = HashGenerator.getSHAString(etPassword.getText().toString());

        if (username.isEmpty()) {
            etUsername.setError("Se requiere un nombre de usuario");
            etUsername.requestFocus();
            return;
        } else if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError("Se requiere una contraseña");
            etPassword.requestFocus();
            return;
        }

        usuarioActivo = new Usuario(username, password);

        System.out.println(usuarioActivo);

        Call<Usuario> usuarioCall = apiService.logUsuario(usuarioActivo);

        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.body() != null) {
                    Toast.makeText(LoginActivity.this, "Has iniciado sesión!", Toast.LENGTH_SHORT).show();
                    usuarioActivo = response.body();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("user", usuarioActivo));
                } else {
                    Toast.makeText(LoginActivity.this, "Usuario no válido", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_LONG).show();
            }
        });
    }
}
