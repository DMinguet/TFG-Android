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
import com.daniminguet.fragments.examenes.FragmentAnyadirExamen;
import com.daniminguet.fragments.examenes.FragmentEliminarExamen;
import com.daniminguet.fragments.examenes.FragmentModificarExamen;
import com.daniminguet.fragments.preguntas.FragmentAnyadirPregunta;
import com.daniminguet.fragments.preguntas.FragmentEliminarPregunta;
import com.daniminguet.fragments.preguntas.FragmentModificarPregunta;
import com.daniminguet.fragments.temarios.FragmentAnyadirTemario;
import com.daniminguet.fragments.temarios.FragmentEliminarTemario;
import com.daniminguet.fragments.temarios.FragmentModificarTemario;
import com.daniminguet.fragments.usuarios.FragmentAnyadirUsuario;
import com.daniminguet.fragments.usuarios.FragmentEliminarUsuario;
import com.daniminguet.fragments.usuarios.FragmentModificarUsuario;

public class FragmentAdmin extends Fragment implements View.OnClickListener{
    public FragmentAdmin() {
        super(R.layout.admin);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("ADMINISTRACIÓN");

        Button btnAnyadirUsuario = view.findViewById(R.id.btnAnyadirUsuarioAdmin);
        Button btnModificarUsuario = view.findViewById(R.id.btnModificarUsuarioAdmin);
        Button btnEliminarUsuario = view.findViewById(R.id.btnEliminarUsuarioAdmin);
        Button btnAnyadirTemario = view.findViewById(R.id.btnAnyadirTemarioAdmin);
        Button btnModificarTemario = view.findViewById(R.id.btnModificarTemarioAdmin);
        Button btnEliminarTemario = view.findViewById(R.id.btnEliminarTemarioAdmin);
        Button btnAnyadirExamen = view.findViewById(R.id.btnAnyadirExamenAdmin);
        Button btnModificarExamen = view.findViewById(R.id.btnModificarExamenAdmin);
        Button btnEliminarExamen = view.findViewById(R.id.btnEliminarExamenAdmin);
        Button btnAnyadirPreguntas = view.findViewById(R.id.btnAnyadirPreguntaAdmin);
        Button btnModificarPreguntas = view.findViewById(R.id.btnModificarPreguntaAdmin);
        Button btnEliminarPreguntas = view.findViewById(R.id.btnEliminarPreguntaAdmin);
        Button btnVolver = view.findViewById(R.id.btnVolverAdmin);

        btnAnyadirUsuario.setOnClickListener(this);
        btnModificarUsuario.setOnClickListener(this);
        btnEliminarUsuario.setOnClickListener(this);
        btnAnyadirTemario.setOnClickListener(this);
        btnModificarTemario.setOnClickListener(this);
        btnEliminarTemario.setOnClickListener(this);
        btnAnyadirExamen.setOnClickListener(this);
        btnModificarExamen.setOnClickListener(this);
        btnEliminarExamen.setOnClickListener(this);
        btnAnyadirPreguntas.setOnClickListener(this);
        btnModificarPreguntas.setOnClickListener(this);
        btnEliminarPreguntas.setOnClickListener(this);
        btnVolver.setOnClickListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getParentFragmentManager();
        switch (v.getId()) {
            case R.id.btnAnyadirUsuarioAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentAnyadirUsuario.class, null)
                        .commit();
                break;
            case R.id.btnModificarUsuarioAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentModificarUsuario.class, null)
                        .commit();
                break;
            case R.id.btnEliminarUsuarioAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentEliminarUsuario.class, null)
                        .commit();
                break;
            case R.id.btnAnyadirTemarioAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentAnyadirTemario.class, null)
                        .commit();
                break;
            case R.id.btnModificarTemarioAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentModificarTemario.class, null)
                        .commit();
                break;
            case R.id.btnEliminarTemarioAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentEliminarTemario.class, null)
                        .commit();
                break;
            case R.id.btnAnyadirExamenAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentAnyadirExamen.class, null)
                        .commit();
                break;
            case R.id.btnModificarExamenAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentModificarExamen.class, null)
                        .commit();
                break;
            case R.id.btnEliminarExamenAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentEliminarExamen.class, null)
                        .commit();
                break;
            case R.id.btnAnyadirPreguntaAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentAnyadirPregunta.class, null)
                        .commit();
                break;
            case R.id.btnModificarPreguntaAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentModificarPregunta.class, null)
                        .commit();
                break;
            case R.id.btnEliminarPreguntaAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentEliminarPregunta.class, null)
                        .commit();
                break;
            case R.id.btnVolverAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentPrincipal.class, null)
                        .commit();
                break;
            default:
                break;
        }
    }
}
