package com.daniminguet.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.R;
import com.daniminguet.interfaces.IAPIService;
import com.daniminguet.models.Examen;
import com.daniminguet.models.UsuarioHasExamen;
import com.daniminguet.rest.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdaptadorNotas extends RecyclerView.Adapter<AdaptadorNotas.ViewHolder> {
    private final List<UsuarioHasExamen> examenesUsuario;

    public AdaptadorNotas(List<UsuarioHasExamen> examenesUsuario) {
        this.examenesUsuario = examenesUsuario;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_notas, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsuarioHasExamen examenUsuario = examenesUsuario.get(position);

        holder.bindExamenUsuario(examenUsuario);
    }

    public int getItemCount() {
        return examenesUsuario.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTituloExamen;
        private final TextView tvFecha;
        private final TextView tvNota;
        private final IAPIService apiService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTituloExamen = itemView.findViewById(R.id.tvExamenNotas);
            this.tvFecha = itemView.findViewById(R.id.tvFecha);
            this.tvNota = itemView.findViewById(R.id.tvNota);
            this.apiService = RestClient.getInstance();
        }

        public void bindExamenUsuario(UsuarioHasExamen examenUsuario) {
            apiService.getExamen(examenUsuario.getExamen().getId()).enqueue(new Callback<Examen>() {
                @Override
                public void onResponse(Call<Examen> call, Response<Examen> response) {
                    assert response.body() != null;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date fecha = null;
                    try {
                        fecha = sdf.parse(examenUsuario.getFecha());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    tvTituloExamen.setText(response.body().getTitulo());
                    assert fecha != null;
                    tvFecha.setText(sdf.format(fecha));
                    tvNota.setText("NOTA: " + examenUsuario.getNota());
                }

                @Override
                public void onFailure(Call<Examen> call, Throwable t) {
                    Toast.makeText(itemView.getContext(), "Error al obtener el examen", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
