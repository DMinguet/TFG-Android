package com.daniminguet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniminguet.R;
import com.daniminguet.fragments.examenes.FragmentExamenes;
import com.daniminguet.fragments.temarios.FragmentTemarios;
import com.daniminguet.models.Usuario;

public class FragmentPrincipal extends Fragment {
    public interface IOnAttachListener {
        Usuario getUsuarioFrgPrinc();
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

        btnTemarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentTemarios.class, null)
                        .commit();
            }
        });

        btnExamenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentExamenes.class, null)
                        .commit();
            }
        });

        btnNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentExamenes.class, null)
                        .commit();
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener attachListener = (IOnAttachListener) context;
        usuario = attachListener.getUsuarioFrgPrinc();
    }
}