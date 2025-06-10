package org.example.dominio;

import java.time.LocalDate;

public class Empleados {
    private int id;
    private String nombre;
    private String puesto;
    private LocalDate fechaIngreso;

    public Empleados() {
    }

    public Empleados(int id, String nombre, String puesto, LocalDate fechaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.puesto = puesto;
        this.fechaIngreso = fechaIngreso;
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
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}

