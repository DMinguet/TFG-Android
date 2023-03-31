package com.daniminguet.interfaces;

import com.daniminguet.models.Examen;
import com.daniminguet.models.ExamenesUser;
import com.daniminguet.models.Pregunta;
import com.daniminguet.models.Temario;
import com.daniminguet.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
    Call<Boolean> deleteExamen(@Path("id") Integer id);

    @GET("pregunta/all")
    Call<List<Pregunta>> getPreguntas();

    @GET("pregunta/{id}")
    Call<Pregunta> getPregunta(@Path("id") int id);

    @POST("pregunta/add")
    Call<Boolean> addPregunta(@Body Pregunta pregunta);

    @PUT("pregunta/update")
    Call<Boolean> updatePregunta(@Body Pregunta pregunta);

    @DELETE("pregunta/{id}")
    Call<Boolean> deletePregunta(@Path("id") Integer id);

    @GET("temario/all")
    Call<List<Temario>> getTemarios();

    @GET("temario/{id}")
    Call<Temario> getTemario(@Path("id") int id);

    @POST("temario/add")
    Call<Boolean> addTemario(@Body Temario temario);

    @PUT("temario/update")
    Call<Boolean> updateTemario(@Body Temario temario);

    @DELETE("temario/{id}")
    Call<Boolean> deleteTemario(@Path("id") Integer id);

    @GET("usuario/all")
    Call<List<Usuario>> getUsuarios();

    @GET("usuario/{id}")
    Call<Usuario> getUsuario(@Path("id") int id);

    @POST("usuario/add")
    Call<Boolean> addUsuario(@Body Usuario usuario);

    @PUT("usuario/update")
    Call<Boolean> updateUsuario(@Body Usuario usuario);

    @DELETE("usuario/{id}")
    Call<Boolean> deleteUsuario(@Path("id") Integer id);

    @POST("usuario/login")
    Call<Usuario> logUsuario (
            @Body Usuario usuario
    );

    @GET("examenUsuario/all")
    Call<List<ExamenesUser>> getExamenesUsuario();

    @GET("examenUsuario/{id}")
    Call<ExamenesUser> getExamenUsuario(@Path("id") int id);

    @POST("examenUsuario/add")
    Call<Boolean> addExamenUsuario(@Body ExamenesUser examenesUser);
}
