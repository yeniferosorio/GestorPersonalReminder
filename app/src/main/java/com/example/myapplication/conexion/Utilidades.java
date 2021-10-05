package com.example.myapplication.conexion;

public class Utilidades {
    //tabla Usuario

    public static final String TABLE_USUARIO="usuario";
    public static final String CAMPO_ID="id";
    public static final String CAMPO_NOMBRE="nombre";
    public static final String CAMPO_APELLIDO="apellido";
    public static final String CAMPO_CORREO="correo";
    public static final String CAMPO_TELEFONO="telefono";

    //---------------------------------------------------------

    //tabla Tarea

    public static final String TABLE_TAREA="tarea";
    public static final String CAMPO_ID_TAREA="id";
    public static final String CAMPO_NOMBRE_TAREA="nombre";
    public static final String CAMPO_DESCRIPCION="descripcion";
    public static final String CAMPO_HORA="hora";
    public static final String CAMPO_FECHA="fecha";




    public static final String CREATE_TABLE_USUARIO="CREATE TABLE "+TABLE_USUARIO+" ("+CAMPO_ID+" INTEGER, "+CAMPO_NOMBRE+" TEXT,"+CAMPO_APELLIDO+" TEXT,"+CAMPO_CORREO+" TEXT, "+CAMPO_TELEFONO+" TEXT)";
    public static final String CREATE_TABLE_TAREA="CREATE TABLE "+TABLE_TAREA+" ("+CAMPO_ID_TAREA+" INTEGER, "+CAMPO_NOMBRE_TAREA+" TEXT,"+CAMPO_DESCRIPCION+" TEXT, "+CAMPO_HORA+" TIME,"+CAMPO_FECHA+" DATE )";

}
