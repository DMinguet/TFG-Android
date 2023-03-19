package com.daniminguet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniminguet.R;
import com.daniminguet.models.Usuario;

public class FragmentPrincipal extends Fragment {
    public interface IOnAttachListener {
        Usuario getUsuario();
    }

    private Usuario usuario;

    public FragmentPrincipal() {
        super(R.layout.principal);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnTemarios = view.findViewById(R.id.btnTemarios);
        Button btnExamenes = view.findViewById(R.id.btnExamenes);
        Button btnNotas = view.findViewById(R.id.btnNotas);
        Button btnAdmin = view.findViewById(R.id.btnAdmin);

        if (usuario.getAdmin() == 0) {
            btnAdmin.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener attachListener = (IOnAttachListener) context;
        usuario = attachListener.getUsuario();
    }
}