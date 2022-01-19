package com.example.myapplication.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class Recordatorio_registrado implements Serializable {
    private String titulo;
    private String descripcion;
    private Time hora;
    private Date fecha;
    private int icono;


    public Recordatorio_registrado(String titulo, String descripcion, Time hora, Date fecha, int icono) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.hora = hora;
        this.fecha = fecha;
        this.icono = icono;

    }

    public Recordatorio_registrado() {
    }

    public Recordatorio_registrado(String titulo, String descripcion, Time hora, Date fecha) {
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
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

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("\nTitulo");
        stringBuilder.append(titulo);

        stringBuilder.append("\nDescripcion");
        stringBuilder.append(titulo);

        stringBuilder.append("\nHora");
        stringBuilder.append(hora);

        stringBuilder.append("\nFecha");
        stringBuilder.append(fecha);

        return stringBuilder.toString();

    }
    public Map<String,Object>toMap(){
        HashMap<String,Object>result=new HashMap<>();
        result.put("titulo",titulo);
        result.put("descripcion",descripcion);
        result.put("hora",hora);
        result.put("fecha",fecha);

        return result;
    }


}

