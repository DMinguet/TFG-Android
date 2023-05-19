package com.daniminguet.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.R;
import com.daniminguet.interfaces.ITemarioListener;
import com.daniminguet.models.Temario;

import java.util.List;

public class AdaptadorTemarios extends RecyclerView.Adapter<AdaptadorTemarios.ViewHolder> {
    private final List<Temario> temarios;
    private final ITemarioListener temarioListener;

    public AdaptadorTemarios(List<Temario> temarios, ITemarioListener temarioListener) {
        this.temarios = temarios;
        this.temarioListener = temarioListener;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_temarios, parent, false);

        return new ViewHolder(itemView, temarioListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Temario temario = temarios.get(position);

        holder.bindTemario(temario);
    }

    public int getItemCount() {
        return temarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvNumTema;
        private final TextView tvTituloTema;
        private final ITemarioListener temarioListener;

        public ViewHolder(@NonNull View itemView, ITemarioListener temarioListener) {
            super(itemView);
            this.tvNumTema = itemView.findViewById(R.id.tvNumTema);
            this.tvTituloTema = itemView.findViewById(R.id.tvTituloTema);
            this.temarioListener = temarioListener;
            itemView.setOnClickListener(this);
        }

        public void bindTemario(Temario temario) {
            tvNumTema.setText(String.valueOf(temario.getTema()) + '.');
            tvTituloTema.setText(temario.getTitulo());
        }

        @Override
        public void onClick(View v) {
            if (temarioListener != null) {
                temarioListener.onTemarioSeleccionado(temarios.get(getAdapterPosition()));
            }
        }
    }
}
