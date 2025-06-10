package org.example.persistencia;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.example.dominio.Proveedores;

public class ProveedoresDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public ProveedoresDAO() {
        conn = ConnectionManager.getInstance();
    }

    /**
     * Crea un nuevo proveedor en la base de datos.
     *
     * @param proveedores El objeto Proveedor que contiene la información del nuevo proveedor.
     * @return El proveedor recién creado con el ID generado o null si hay error.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public Proveedores create(Proveedores proveedores) throws SQLException {
        Proveedores res = null;
        try {
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO proveedores (nombre, telefono, email) VALUES (?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, proveedores.getNombre());
            ps.setString(2, proveedores.getTelefono());
            ps.setString(3, proveedores.getEmail());

            int affectedRows = ps.executeUpdate();
            if (affectedRows != 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating provider failed, no ID obtained.");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el proveedor: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.connect();
        }
        return res;
    }

    /**
     * Actualiza un proveedor existente.
     *
     * @param proveedores El proveedor con datos actualizados.
     * @return true si la actualización fue exitosa, false si no.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public boolean update(Proveedores proveedores) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE proveedores SET nombre = ?, telefono = ?, email = ? WHERE id = ?"
            );
            ps.setString(1, proveedores.getNombre());
            ps.setString(2, proveedores.getTelefono());
            ps.setString(3, proveedores.getEmail());
            ps.setInt(4, proveedores.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el proveedor: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    /**
     * Elimina un proveedor por su ID.
     *
     * @param proveedores El proveedor a eliminar.
     * @return true si se eliminó correctamente, false en caso contrario.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public boolean delete(Proveedores proveedores) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement("DELETE FROM proveedores WHERE id = ?");
            ps.setInt(1, proveedores.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el proveedor: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    /**
     * Busca proveedores cuyo nombre contenga cierta cadena.
     *
     * @param nombre El texto a buscar en los nombres de proveedores.
     * @return Lista de proveedores que coinciden con la búsqueda.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public ArrayList<Proveedores> search(String nombre) throws SQLException {
        ArrayList<Proveedores> records = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre, telefono, email FROM proveedores WHERE nombre LIKE ?"
            );
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Proveedores proveedores = new Proveedores();
                proveedores.setId(rs.getInt(1));
                proveedores.setNombre(rs.getString(2));
                proveedores.setTelefono(rs.getString(3));
                proveedores.setEmail(rs.getString(4));
                records.add(proveedores);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar proveedores: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }

    /**
     * Obtiene un proveedor por su ID.
     *
     * @param id El ID del proveedor.
     * @return El proveedor encontrado o null si no existe.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public Proveedores getById(int id) throws SQLException {
        Proveedores proveedores = new Proveedores();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre, telefono, email FROM proveedores WHERE id = ?"
            );
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                proveedores.setId(rs.getInt(1));
                proveedores.setNombre(rs.getString(2));
                proveedores.setTelefono(rs.getString(3));
                proveedores.setEmail(rs.getString(4));
            } else {
                proveedores = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el proveedor por ID: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return proveedores;
    }
}

