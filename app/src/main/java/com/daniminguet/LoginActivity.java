package com.daniminguet;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daniminguet.rest.RestClient;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private IAPIService apiService;
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        apiService = RestClient.getInstance();

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
        } else if (password.isEmpty()) {
            etPassword.setError("Se requiere una contraseña");
            etPassword.requestFocus();
            return;
        }


    }
}
