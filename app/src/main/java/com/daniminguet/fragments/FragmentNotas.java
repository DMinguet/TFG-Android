package com.daniminguet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.R;
import com.daniminguet.adaptadores.AdaptadorNotas;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Usuario;
import com.daniminguet.models.UsuarioHasExamen;
import com.daniminguet.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentNotas extends Fragment {
    public interface IOnAttachListener {
        Usuario getUsuarioActivoNotas();
    }

    private IAPIService apiService;
    private Usuario usuarioActivo;

    public FragmentNotas() {
        super(R.layout.lista);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("NOTAS DISPONIBLES");
        apiService = RestClient.getInstance();

        RecyclerView rvLista = view.findViewById(R.id.rvLista);

        apiService.getExamenesUsuario().enqueue(new Callback<List<UsuarioHasExamen>>() {
            @Override
            public void onResponse(@NonNull Call<List<UsuarioHasExamen>> call, @NonNull Response<List<UsuarioHasExamen>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;

                    List<UsuarioHasExamen> examenesUsuario = new ArrayList<>();

                    for (UsuarioHasExamen usuarioHasExamen : response.body()) {
                        if (usuarioActivo.getId() == usuarioHasExamen.getUsuario().getId()) {
                            examenesUsuario.add(usuarioHasExamen);
                        }
                    }

                    AdaptadorNotas adaptadorNotas = new AdaptadorNotas(examenesUsuario);
                    rvLista.setHasFixedSize(true);
                    rvLista.setAdapter(adaptadorNotas);
                    rvLista.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UsuarioHasExamen>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "No se han podido obtener los exámenes del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener attachListener = (IOnAttachListener) context;
        usuarioActivo = attachListener.getUsuarioActivoNotas();
    }
}
