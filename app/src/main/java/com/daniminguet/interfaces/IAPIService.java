package com.daniminguet.interfaces;

import com.daniminguet.models.Examen;
import com.daniminguet.models.Preguntas;
import com.daniminguet.models.Temario;
import com.daniminguet.models.Usuario;

import java.io.File;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface IAPIService {

    @GET("examen/all")
    Call<List<Examen>> getExamenes();

    @GET("examen/{id}")
    Call<Examen> getExamen(@Path("id") int id);

    @POST("examen/add")
    Call<Boolean> addExamen(@Body Examen examen);

    @PUT("examen/update")
    Call<Boolean> updateExamen(@Body Examen examen);

    @DELETE("examen/{id}")
    Call<Boolean> deleteExamen(@Body Examen examen);

    @GET("preguntas/all")
    Call<List<Preguntas>> getPreguntas();

    @GET("preguntas/{id}")
    Call<Preguntas> getPregunta(@Path("id") int id);

    @POST("preguntas/add")
    Call<Boolean> addPregunta(@Body Preguntas preguntas);

    @PUT("preguntas/update")
    Call<Boolean> updatePregunta(@Body Preguntas preguntas);

    @DELETE("preguntas/{id}")
    Call<Boolean> deletePregunta(@Body Preguntas preguntas);

    @GET("temario/all")
    Call<List<Temario>> getTemarios();

    @GET("temario/{id}")
    Call<Temario> getTemario(@Path("id") int id);

    @POST("temario/add")
    Call<Boolean> addTemario(@Body Temario temario);

    @PUT("temario/update")
    Call<Boolean> updateTemario(@Body Temario temario);

    @DELETE("temario/{id}")
    Call<Boolean> deleteTemario(@Body Temario temario);

    @GET("usuario/all")
    Call<List<Usuario>> getUsuarios();

    @GET("usuario/{id}")
    Call<Usuario> getUsuario(@Path("id") int id);

    @POST("usuario/add")
    Call<Boolean> addUsuario(@Body Usuario usuario);

    @PUT("usuario/update")
    Call<Boolean> updateUsuario(@Body Usuario usuario);

    @DELETE("usuario/{id}")
    Call<Boolean> deleteUsuario(@Body Usuario usuario);

    @POST("usuario/login")
    Call<Usuario> logUsuario (
            @Body Usuario usuario
    );
}
