package com.daniminguet.models;

import java.io.Serializable;
import java.util.List;

public class Usuario implements Serializable {
    private int id;
    private String nombre;
    private String apellidos;
    private String nombreUsuario;
    private String contrasenya;
    private String email;
    private Byte admin;
    private List<Examen> examenes;

    public Usuario(String nombreUsuario, String contrasenya) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenya = contrasenya;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public String getEmail() {
        return email;
    }

    public Byte getAdmin() {
        return admin;
    }

    public List<Examen> getExamenes() {
        return examenes;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public void setAdmin(Byte admin) {
        this.admin = admin;
    }

    public void setExamenes(List<Examen> examenes) {
        this.examenes = examenes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", contrasenya='" + contrasenya + '\'' +
                ", email='" + email + '\'' +
                ", admin=" + admin +
                ", examenes=" + examenes +
                '}';
    }
}
