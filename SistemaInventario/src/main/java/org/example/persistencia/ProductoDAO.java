package org.example.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.example.dominio.Producto;

public class ProductoDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public ProductoDAO() {
        conn = ConnectionManager.getInstance();
    }

    /**
     * Crea un nuevo producto en la base de datos.
     *
     * @param producto El objeto Product que contiene la información del nuevo producto.
     * @return El producto recién creado con el ID generado o null si hay error.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public Producto create(Producto producto) throws SQLException {
        Producto res = null;
        try {
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO productos (nombre, precio, cantidad) VALUES (?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, producto.getNombre());

            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getCantidad());

            int affectedRows = ps.executeUpdate();
            if (affectedRows != 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado);
                } else {
                    throw new SQLException("Creating product failed, no ID obtained.");
                }
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al crear el producto: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.connect();
        }
        return res;
    }

    /**
     * Actualiza un producto existente.
     *
     * @param producto El producto con datos actualizados.
     * @return true si la actualización fue exitosa, false si no.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public boolean update(Producto producto) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE productos SET nombre = ?, precio = ?, cantidad = ? WHERE id = ?"
            );
            ps.setString(1, producto.getNombre());

            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getCantidad());
            ps.setInt(4, producto.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el producto: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param producto El producto a eliminar.
     * @return true si se eliminó correctamente, false en caso contrario.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public boolean delete(Producto producto) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement("DELETE FROM productos WHERE id = ?");
            ps.setInt(1, producto.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el producto: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    /**
     * Busca productos cuyo nombre contenga cierta cadena.
     *
     * @param nombre El texto a buscar en los nombres de productos.
     * @return Lista de productos que coinciden con la búsqueda.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public ArrayList<Producto> search(String nombre) throws SQLException {
        ArrayList<Producto> records = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre,  precio, cantidad FROM productos WHERE nombre LIKE ?"
            );
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt(1));
                producto.setNombre(rs.getString(2));

                producto.setPrecio(rs.getDouble(3));
                producto.setCantidad(rs.getInt(4));
                records.add(producto);
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar productos: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }

    /**
     * Obtiene un producto por su ID.
     *
     * @param id El ID del producto.
     * @return El producto encontrado o null si no existe.
     * @throws SQLException si ocurre un error en la base de datos.
     */
    public Producto getById(int id) throws SQLException {
        Producto producto = new Producto();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre,  precio, cantidad FROM productos WHERE id = ?"
            );
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                producto.setId(rs.getInt(1));
                producto.setNombre(rs.getString(2));

                producto.setPrecio(rs.getDouble(3));
                producto.setCantidad(rs.getInt(4));
            } else {
                producto = null;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el producto por ID: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return producto;
    }
}