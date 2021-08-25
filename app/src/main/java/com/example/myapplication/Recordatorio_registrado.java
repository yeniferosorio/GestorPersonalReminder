package com.example.myapplication;

import java.io.Serializable;

public class Recordatorio_registrado implements Serializable {
    private String titulo;
    private String descripcion;
    private int icono;

    public Recordatorio_registrado(String titulo, String descripcion, int icono) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.icono = icono;
    }

    public Recordatorio_registrado() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}
