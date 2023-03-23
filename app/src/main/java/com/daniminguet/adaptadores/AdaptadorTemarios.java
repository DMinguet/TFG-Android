package com.daniminguet.adaptadores;

import android.content.Intent;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.R;
import com.daniminguet.models.Temario;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AdaptadorTemarios extends RecyclerView.Adapter<AdaptadorTemarios.ViewHolder> {
    private final List<Temario> temarios;

    public AdaptadorTemarios(List<Temario> temarios) {
        this.temarios = temarios;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_temarios, parent, false);

        return new ViewHolder(itemView);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvNumTema = itemView.findViewById(R.id.tvNumTema);
            this.tvTituloTema = itemView.findViewById(R.id.tvTituloTema);
            itemView.setOnClickListener(this);
        }

        public void bindTemario(Temario temario) {
            tvNumTema.setText(String.valueOf(temario.getTema()) + '.');
            tvTituloTema.setText(temario.getTitulo());
        }

        @Override
        public void onClick(View v) {
            try {
                URL url = new URL("http://localhost:8080/temario/" + temarios.get(getAdapterPosition()).getId());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();

                String Path = Environment.getExternalStorageDirectory() + "/download/";
                Log.v("PdfManager", "PATH: " + Path);
                File file = new File(Path);
                file.mkdirs();
                FileOutputStream fos = new FileOutputStream(temarios.get(getAdapterPosition()).getPdf());

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[702];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                Log.d("PdfManager", "Error: " + e);
            }
            Log.v("PdfManager", "Check: ");
        }
    }
}
