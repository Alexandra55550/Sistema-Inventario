package org.example.persistencia;

import org.example.dominio.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ProductoDAOTest {
    private ProductoDAO productoDAO;

    @BeforeEach
    void setUp() {
        productoDAO = new ProductoDAO();
    }

    private Producto create(Producto producto) throws SQLException {
        Producto res = productoDAO.create(producto);

        assertNotNull(res, "El producto creado no debería ser nulo.");
        assertEquals(producto.getNombre(), res.getNombre(), "El nombre del producto creado debe ser igual al original.");
        assertEquals(producto.getPrecio(), res.getPrecio(), "El precio del producto creado debe ser igual al original.");
        assertEquals(producto.getCantidad(), res.getCantidad(), "El stock del producto creado debe ser igual al original.");

        return res;
    }

    private void update(Producto producto) throws SQLException {
        producto.setNombre(producto.getNombre() + "_u");
        producto.setPrecio(producto.getPrecio() + 5.0);
        producto.setCantidad(producto.getCantidad() + 10);

        boolean res = productoDAO.update(producto);
        assertTrue(res, "La actualización del producto debería ser exitosa.");

        getById(producto);
    }

    private void getById(Producto producto) throws SQLException {
        Producto res = productoDAO.getById(producto.getId());

        assertNotNull(res, "El producto obtenido por ID no debería ser nulo.");
        assertEquals(producto.getId(), res.getId(), "El ID del producto debe coincidir.");
        assertEquals(producto.getNombre(), res.getNombre(), "El nombre del producto debe coincidir.");
        assertEquals(producto.getPrecio(), res.getPrecio(), "El precio del producto debe coincidir.");
        assertEquals(producto.getCantidad(), res.getCantidad(), "El stock del producto debe coincidir.");
    }

    private void search(Producto producto) throws SQLException {
        ArrayList<Producto> productos = productoDAO.search(producto.getNombre());
        boolean encontrado = false;

        for (Producto p : productos) {
            if (p.getNombre().contains(producto.getNombre())) {
                encontrado = true;
            } else {
                encontrado = false;
                break;
            }
        }

        assertTrue(encontrado, "El nombre buscado no fue encontrado: " + producto.getNombre());
    }

    private void delete(Producto producto) throws SQLException {
        boolean res = productoDAO.delete(producto);
        assertTrue(res, "La eliminación del producto debería ser exitosa.");

        Producto res2 = productoDAO.getById(producto.getId());
        assertNull(res2, "El producto debería haber sido eliminado y no encontrado por ID.");
    }

    @Test
    void testProductoDAO() throws SQLException {
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        String nombre = "Producto" + num;

        Producto producto = new Producto(0, nombre, 10.5, 100);

        Producto productoDePrueba = create(producto);
        update(productoDePrueba);
        search(productoDePrueba);

    }

    @Test
    void createProducto() throws SQLException {
        Producto producto = new Producto(0, "Producto Test", 9.99, 50);
        Producto res = productoDAO.create(producto);
        assertNotNull(res, "El producto creado no debe ser nulo.");
    }
}