package com.daniminguet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniminguet.HashGenerator;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.R;
import com.daniminguet.models.Usuario;
import com.daniminguet.rest.RestClient;

import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPerfil extends Fragment {
    public interface IOnAttachListener {
        Usuario getUsuarioFrgPerfl();
    }

    private IAPIService apiService;

    private Usuario usuario;

    public FragmentPerfil() {
        super(R.layout.perfil);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("PERFIL DE USUARIO");
        apiService = RestClient.getInstance();

        TextView tvNombre = view.findViewById(R.id.tvNombrePerfil);
        TextView tvApellidos = view.findViewById(R.id.tvApellidosPerfil);
        TextView tvEmail = view.findViewById(R.id.tvEmailPerfil);
        EditText etNombreUsuario = view.findViewById(R.id.etNombreUsuarioPerfil);
        EditText etContrasenya = view.findViewById(R.id.etContrasenyaPerfil);
        Button btnCambiarNombre = view.findViewById(R.id.btnModificarUsername);
        Button btnCambiarContrasenya = view.findViewById(R.id.btnModificarContrasenya);
        Button btnVolver = view.findViewById(R.id.btnVolverPerfil);

        tvNombre.setText(usuario.getNombre());
        tvApellidos.setText(usuario.getApellidos());
        tvEmail.setText(usuario.getEmail());
        etNombreUsuario.setHint(usuario.getNombreUsuario());
        etContrasenya.setHint("Contraseña");

        btnCambiarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoNombreUsuario = etNombreUsuario.getText().toString();

                if (nuevoNombreUsuario.isEmpty()) {
                    etNombreUsuario.setError("Debes introducir un nuevo nombre de usuario");
                    etNombreUsuario.requestFocus();
                    return;
                }

                usuario.setNombreUsuario(nuevoNombreUsuario);

                apiService.updateUsuario(usuario).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (Boolean.TRUE.equals(response.body())) {
                            Toast.makeText(getActivity(), "Nombre de usuario modificado", Toast.LENGTH_SHORT).show();
                            etNombreUsuario.setText("");
                            etNombreUsuario.setHint(usuario.getNombreUsuario());
                        } else {
                            Toast.makeText(getActivity(), "Nombre de usuario existente", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error al modificar el nombre de usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnCambiarContrasenya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevaContrasenya = etContrasenya.getText().toString();

                if (nuevaContrasenya.isEmpty()) {
                    etContrasenya.setError("Debes introducir una contraseña");
                    etContrasenya.requestFocus();
                    return;
                }

                try {
                    usuario.setContrasenya(HashGenerator.getSHAString(nuevaContrasenya));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                apiService.updateUsuario(usuario).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (Boolean.TRUE.equals(response.body())) {
                            Toast.makeText(getActivity(), "Contraseña modificada", Toast.LENGTH_SHORT).show();
                            etContrasenya.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                            etContrasenya.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getActivity(), "Error al modificar la contraseña", Toast.LENGTH_SHORT).show();
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
                        .replace(R.id.frgPrincipal, FragmentPrincipal.class, null)
                        .commit();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener attachListener = (IOnAttachListener) context;
        usuario = attachListener.getUsuarioFrgPerfl();
    }
}
