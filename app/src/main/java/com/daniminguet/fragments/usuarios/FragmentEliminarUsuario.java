package com.daniminguet.fragments.usuarios;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniminguet.R;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Usuario;
import com.daniminguet.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEliminarUsuario extends Fragment implements SpinnerAdapter {
    private String[] nombreUsuarios;
    private List<Usuario> usuarios;
    private Usuario usuarioSeleccionado;
    private IAPIService apiService;

    public FragmentEliminarUsuario() {
        super(R.layout.eliminar_usuario);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();
        usuarios = new ArrayList<>();

        Spinner sUsuarios = view.findViewById(R.id.sListaUsuariosEliminar);
        TextView tvNombreApellidos = view.findViewById(R.id.tvNombreApellidosUsuario);
        TextView tvEmail = view.findViewById(R.id.tvEmailUsuario);
        Button btnEliminar = view.findViewById(R.id.btnEliminarUsuario);

        apiService.getUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    usuarios.addAll(response.body());
                    nombreUsuarios = new String[usuarios.size()];

                    for (int i = 0; i < nombreUsuarios.length; i++) {
                        nombreUsuarios[i] = usuarios.get(i).getNombreUsuario();
                    }

                    sUsuarios.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nombreUsuarios));

                    usuarioSeleccionado = obtenerUsuario(sUsuarios.getSelectedItem().toString());

                    tvNombreApellidos.setText(usuarioSeleccionado.getNombre() + " " + usuarioSeleccionado.getApellidos());
                    tvEmail.setText(usuarioSeleccionado.getEmail());
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(getContext(), "No se han podido obtener los usuarios", Toast.LENGTH_SHORT).show();
            }
        });

        sUsuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                usuarioSeleccionado = obtenerUsuario(sUsuarios.getSelectedItem().toString());
                tvNombreApellidos.setText(usuarioSeleccionado.getNombre() + " " + usuarioSeleccionado.getApellidos());
                tvEmail.setText(usuarioSeleccionado.getEmail());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.deleteUsuario(usuarioSeleccionado.getId()).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(Boolean.TRUE.equals(response.body())) {
                            Toast.makeText(getContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show();
                            tvNombreApellidos.setText("");
                            tvEmail.setText("");
                            recargarUsuarios();
                            nombreUsuarios = new String[usuarios.size()];

                            for (int i = 0; i < nombreUsuarios.length; i++) {
                                nombreUsuarios[i] = usuarios.get(i).getNombreUsuario();
                            }

                            sUsuarios.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nombreUsuarios));
                            sUsuarios.setSelection(0);
                        } else {
                            Toast.makeText(getContext(), "Error al eliminar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(getContext(), "No se ha podido eliminar el usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void recargarUsuarios() {
        apiService.getUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                usuarios.clear();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    usuarios.addAll(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(getContext(), "No se han podido obtener los usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Usuario obtenerUsuario (String nombreUsuario) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().equals(nombreUsuario)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
