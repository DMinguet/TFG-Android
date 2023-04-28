package com.daniminguet.fragments.temarios;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.R;
import com.daniminguet.adaptadores.AdaptadorTemarios;
import com.daniminguet.interfaces.ITemarioListener;
import com.daniminguet.models.Temario;
import com.daniminguet.rest.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTemarios extends Fragment {
    private ITemarioListener listener;

    public FragmentTemarios() {
        super(R.layout.lista);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("TEMARIOS DISPONIBLES");
        IAPIService apiService = RestClient.getInstance();
        RecyclerView rvLista = view.findViewById(R.id.rvLista);

        apiService.getTemarios().enqueue(new Callback<List<Temario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Temario>> call, @NonNull Response<List<Temario>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;

                    AdaptadorTemarios adaptadorTemarios = new AdaptadorTemarios(response.body(), listener);
                    rvLista.setHasFixedSize(true);
                    rvLista.setAdapter(adaptadorTemarios);
                    rvLista.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Temario>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "No se han podido obtener los temarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (ITemarioListener) context;
    }
}
