package com.example.myapplication.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Recordatorio implements Serializable {

    private String id;
    private String titulo;
    private String descripcion;
    private String hora;
    private String fecha;

    public Recordatorio(String id, String titulo, String descripcion) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Recordatorio() {
    }

    public Recordatorio(String id, String titulo, String descripcion, String hora, String fecha) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.hora = hora;
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nTitulo");
        stringBuilder.append(titulo);
        stringBuilder.append("\nDescripción");
        stringBuilder.append(titulo);
        stringBuilder.append("\nHora");
        stringBuilder.append(hora);
        stringBuilder.append("\nFecha");
        stringBuilder.append(fecha);

        return stringBuilder.toString();

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("titulo", titulo);
        result.put("descripcion", descripcion);
        result.put("hora", hora);
        result.put("fecha", fecha);

        return result;
    }


}

