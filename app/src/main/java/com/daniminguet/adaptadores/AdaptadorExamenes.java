package com.daniminguet.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.R;
import com.daniminguet.interfaces.IExamenListener;
import com.daniminguet.models.Examen;

import java.util.List;

public class AdaptadorExamenes extends RecyclerView.Adapter<AdaptadorExamenes.ViewHolder> {
    private final List<Examen> examenes;
    private final IExamenListener examenListener;

    public AdaptadorExamenes(List<Examen> examenes, IExamenListener examenListener) {
        this.examenes = examenes;
        this.examenListener = examenListener;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_examenes, parent, false);

        return new ViewHolder(itemView, examenListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Examen examen = examenes.get(position);

        holder.bindExamen(examen);
    }

    public int getItemCount() {
        return examenes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTituloExamen;
        private final IExamenListener examenListener;

        public ViewHolder(@NonNull View itemView, IExamenListener examenListener) {
            super(itemView);
            this.tvTituloExamen = itemView.findViewById(R.id.tvTituloExamen);
            this.examenListener = examenListener;
            itemView.setOnClickListener(this);
        }

        public void bindExamen(Examen examen) {
            tvTituloExamen.setText(examen.getTitulo());
        }

        @Override
        public void onClick(View v) {
            if (examenListener != null) {
                examenListener.onExamenSeleccionado(examenes.get(getAdapterPosition()));
            }
        }
    }
}
