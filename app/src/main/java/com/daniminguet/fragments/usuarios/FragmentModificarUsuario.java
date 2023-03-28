package com.daniminguet.fragments.usuarios;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniminguet.HashGenerator;
import com.daniminguet.R;
import com.daniminguet.fragments.FragmentAdmin;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Usuario;
import com.daniminguet.rest.RestClient;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentModificarUsuario extends Fragment implements SpinnerAdapter {
    private String[] nombreUsuarios;
    private List<Usuario> usuarios;
    private IAPIService apiService;

    public FragmentModificarUsuario() {
        super(R.layout.modificar_usuario);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = RestClient.getInstance();
        usuarios = new ArrayList<>();

        Spinner sUsuarios = view.findViewById(R.id.sListaUsuariosModificar);
        Spinner sCampos = view.findViewById(R.id.sListaCamposUsuario);
        CheckBox admin = view.findViewById(R.id.checkBoxAdmin);
        EditText etNuevoValor = view.findViewById(R.id.etValorUsuario);
        Button btnModificar = view.findViewById(R.id.btnModificarUsuario);
        Button btnVolver = view.findViewById(R.id.btnVolverModificarUsuario);

        apiService.getUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    usuarios.addAll(response.body());
                    nombreUsuarios = new String[usuarios.size()];
                    String[] campos = {"Nombre", "Apellidos", "Email", "Contraseña", "Admin"};

                    for (int i = 0; i < nombreUsuarios.length; i++) {
                        nombreUsuarios[i] = usuarios.get(i).getNombreUsuario();
                    }

                    sUsuarios.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nombreUsuarios));
                    sCampos.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, campos));
                    etNuevoValor.setVisibility(View.VISIBLE);
                    admin.setVisibility(View.INVISIBLE);

                    sCampos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String campoSeleccionado = sCampos.getSelectedItem().toString();

                            switch (campoSeleccionado) {
                                case "Nombre":
                                case "Email":
                                case "Apellidos":
                                case "Contraseña":
                                    etNuevoValor.setInputType(View.AUTOFILL_TYPE_TEXT);
                                    admin.setVisibility(View.INVISIBLE);
                                    etNuevoValor.setVisibility(View.VISIBLE);
                                    break;
                                case "Admin":
                                    admin.setVisibility(View.VISIBLE);
                                    etNuevoValor.setVisibility(View.INVISIBLE);
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    btnModificar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String usuarioSeleccionado = sUsuarios.getSelectedItem().toString();
                            String campoSeleccionado = sCampos.getSelectedItem().toString();

                            Usuario usuarioCorrespondiente = obtenerUsuario(usuarioSeleccionado);

                            if (campoSeleccionado.equals("Admin")) {
                                if (admin.isChecked()) {
                                    usuarioCorrespondiente.setAdmin((byte) 1);
                                } else {
                                    usuarioCorrespondiente.setAdmin((byte) 0);
                                }

                                modificarUsuario(usuarioCorrespondiente);
                            } else {
                                String nuevoValor;

                                switch (campoSeleccionado) {
                                    case "Nombre":
                                        nuevoValor = etNuevoValor.getText().toString();

                                        if (nuevoValor.isEmpty()) {
                                            etNuevoValor.setError("No has indicado el nuevo valor");
                                            etNuevoValor.requestFocus();
                                            return;
                                        }

                                        usuarioCorrespondiente.setNombre(nuevoValor);

                                        modificarUsuario(usuarioCorrespondiente);
                                        etNuevoValor.setText("");

                                        break;
                                    case "Email":
                                        nuevoValor = etNuevoValor.getText().toString();

                                        if (nuevoValor.isEmpty()) {
                                            etNuevoValor.setError("No has indicado el nuevo valor");
                                            etNuevoValor.requestFocus();
                                            return;
                                        } else if (!validarEmail(nuevoValor)) {
                                            etNuevoValor.setError("Email no válido");
                                            etNuevoValor.requestFocus();
                                            return;
                                        }

                                        usuarioCorrespondiente.setEmail(nuevoValor);

                                        modificarUsuario(usuarioCorrespondiente);
                                        etNuevoValor.setText("");

                                        break;
                                    case "Apellidos":
                                        nuevoValor = etNuevoValor.getText().toString();

                                        if (nuevoValor.isEmpty()) {
                                            etNuevoValor.setError("No has indicado el nuevo valor");
                                            etNuevoValor.requestFocus();
                                            return;
                                        }

                                        usuarioCorrespondiente.setApellidos(nuevoValor);

                                        modificarUsuario(usuarioCorrespondiente);
                                        etNuevoValor.setText("");

                                        break;
                                    case "Contraseña":
                                        nuevoValor = etNuevoValor.getText().toString();

                                        if (nuevoValor.isEmpty()) {
                                            etNuevoValor.setError("No has indicado el nuevo valor");
                                            etNuevoValor.requestFocus();
                                            return;
                                        }

                                        try {
                                            usuarioCorrespondiente.setContrasenya(HashGenerator.getSHAString(nuevoValor));
                                        } catch (NoSuchAlgorithmException e) {
                                            e.printStackTrace();
                                        }

                                        modificarUsuario(usuarioCorrespondiente);
                                        etNuevoValor.setText("");

                                        break;
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(getContext(), "No se han podido obtener los usuarios", Toast.LENGTH_SHORT).show();
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

    private void modificarUsuario(Usuario usuario) {
        apiService.updateUsuario(usuario).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()) {
                    Toast.makeText(getContext(), "Usuario modificado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al modificar el usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getContext(), "No se ha podido modificar el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
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
