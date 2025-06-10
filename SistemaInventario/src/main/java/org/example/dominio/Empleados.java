package org.example.dominio;

import java.time.LocalDate;

public class Empleados {
    private int id;
    private String nombre;
    private String puesto;
    private LocalDate fechaingreso;

    public Empleados() {
    }

    public Empleados(int id, String nombre, String puesto, LocalDate fechaingreso) {
        this.id = id;
        this.nombre = nombre;
        this.puesto = puesto;
        this.fechaingreso = fechaingreso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public LocalDate getFechaIngreso() {
        return fechaingreso;
    }

    public void setFechaIngreso(LocalDate fechaingreso) {
        this.fechaingreso = fechaingreso;
    }
}

