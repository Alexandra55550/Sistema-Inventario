package org.example.persistencia;

import org.example.dominio.Empleados;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmpleadosDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public EmpleadosDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Empleados create(Empleados empleados) throws SQLException {
        Empleados res = null;
        String sql = "INSERT INTO empleados (nombre, puesto, fechaingreso) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.connect().prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, empleados.getNombre());
            ps.setString(2, empleados.getPuesto());
            ps.setDate(3, java.sql.Date.valueOf(empleados.getFechaIngreso()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating employee failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating employee failed, no ID obtained.");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el empleado: " + ex.getMessage(), ex);
        }

        return res;
    }


    public boolean update(Empleados empleados) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE empleados SET nombre = ?, puesto = ?, fechaingreso = ? WHERE id = ?"
            );
            ps.setString(1, empleados.getNombre());
            ps.setString(2, empleados.getPuesto());
            ps.setDate(3, Date.valueOf(empleados.getFechaIngreso()));
            ps.setInt(4, empleados.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el empleado: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean delete(Empleados empleados) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement("DELETE FROM empleados WHERE id = ?");
            ps.setInt(1, empleados.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el empleado: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public ArrayList<Empleados> search(String nombre) throws SQLException {
        ArrayList<Empleados> records = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre, puesto, fechaingreso FROM empleados WHERE nombre LIKE ?"
            );
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Empleados empleados = new Empleados();
                empleados.setId(rs.getInt(1));
                empleados.setNombre(rs.getString(2));
                empleados.setPuesto(rs.getString(3));
                empleados.setFechaIngreso(rs.getDate(4).toLocalDate());
                records.add(empleados);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar empleados: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }

    public Empleados getById(int id) throws SQLException {
        Empleados empleados = new Empleados();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre, puesto, fechaingreso FROM empleados WHERE id = ?"
            );
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                empleados.setId(rs.getInt(1));
                empleados.setNombre(rs.getString(2));
                empleados.setPuesto(rs.getString(3));
                empleados.setFechaIngreso(rs.getDate(4).toLocalDate());
            } else {
                empleados = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el empleado por ID: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return empleados;
    }
}

