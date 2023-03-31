package com.daniminguet.fragments.examenes;

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
import com.daniminguet.adaptadores.AdaptadorExamenes;
import com.daniminguet.adaptadores.AdaptadorTemarios;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.interfaces.IExamenListener;
import com.daniminguet.models.Examen;
import com.daniminguet.models.Temario;
import com.daniminguet.rest.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentExamenes extends Fragment {
    private IExamenListener listener;

    public FragmentExamenes() {
        super(R.layout.lista);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IAPIService apiService = RestClient.getInstance();
        RecyclerView rvLista = view.findViewById(R.id.rvLista);

        apiService.getExamenes().enqueue(new Callback<List<Examen>>() {
            @Override
            public void onResponse(@NonNull Call<List<Examen>> call, @NonNull Response<List<Examen>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;

                    AdaptadorExamenes adaptadorExamenes = new AdaptadorExamenes(response.body(), listener);
                    rvLista.setHasFixedSize(true);
                    rvLista.setAdapter(adaptadorExamenes);
                    rvLista.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Examen>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "No se han podido obtener los exámenes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (IExamenListener) context;
    }
}
