package com.example.myapplication.model;

public class Usuarios {
   private Integer id;
   private String nombre;
   private String correoElectronico;
   private String telefono;
   private String contrasenia;

   public Usuarios(Integer id,String nombre, String correoElectronico, String telefono, String contrasenia) {
      this.id=id;
      this.nombre = nombre;
      this.correoElectronico = correoElectronico;
      this.telefono = telefono;
      this.contrasenia = contrasenia;
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public String getCorreoElectronico() {
      return correoElectronico;
   }

   public void setCorreoElectronico(String correoElectronico) {
      this.correoElectronico = correoElectronico;
   }

   public String getTelefono() {
      return telefono;
   }

   public void setTelefono(String telefono) {
      this.telefono = telefono;
   }

   public String getContrasenia() {
      return contrasenia;
   }

   public void setContrasenia(String contrasenia) {
      this.contrasenia = contrasenia;
   }
}
