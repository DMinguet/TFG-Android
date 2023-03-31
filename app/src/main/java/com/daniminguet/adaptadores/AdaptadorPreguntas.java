package com.daniminguet.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.R;
import com.daniminguet.models.Preguntas;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class AdaptadorPreguntas extends RecyclerView.Adapter<AdaptadorPreguntas.ViewHolder> {
    private final List<Preguntas> preguntas;

    public AdaptadorPreguntas(List<Preguntas> preguntas) {
        this.preguntas = preguntas;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_preguntas, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Preguntas pregunta = preguntas.get(position);

        holder.bindPregunta(pregunta);
    }

    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPregunta;
        private final ChipGroup cgRespuestas;
        private final Chip cA, cB, cC, cD;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvPregunta = itemView.findViewById(R.id.tvPregunta);
            this.cgRespuestas = itemView.findViewById(R.id.cgRespuestas);
            this.cA = itemView.findViewById(R.id.cA);
            this.cB = itemView.findViewById(R.id.cB);
            this.cC = itemView.findViewById(R.id.cC);
            this.cD = itemView.findViewById(R.id.cD);
        }

        public void bindPregunta(Preguntas pregunta) {
            tvPregunta.setText(pregunta.getPregunta());
        }
    }
}
