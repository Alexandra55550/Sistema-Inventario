package org.example.persistencia;

import org.example.dominio.Proveedores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ProveedoresDAOTest {
    private ProveedoresDAO proveedoresDAO;

    @BeforeEach
    void setUp() {
        proveedoresDAO = new ProveedoresDAO();
    }

    private Proveedores create(Proveedores proveedores) throws SQLException {
        Proveedores res = proveedoresDAO.create(proveedores);

        assertNotNull(res, "El proveedor creado no debería ser nulo.");
        assertEquals(proveedores.getNombre(), res.getNombre(), "El nombre del proveedor creado debe ser igual al original.");
        assertEquals(proveedores.getTelefono(), res.getTelefono(), "El teléfono del proveedor creado debe ser igual al original.");
        assertEquals(proveedores.getEmail(), res.getEmail(), "El email del proveedor creado debe ser igual al original.");

        return res;
    }

    private void update(Proveedores proveedores) throws SQLException {
        proveedores.setNombre(proveedores.getNombre() + "_u");
        proveedores.setTelefono("9999-9999");
        proveedores.setEmail("nuevo_" + proveedores.getEmail());

        boolean res = proveedoresDAO.update(proveedores);
        assertTrue(res, "La actualización del proveedor debería ser exitosa.");

        getById(proveedores);
    }

    private void getById(Proveedores proveedores) throws SQLException {
        Proveedores res = proveedoresDAO.getById(proveedores.getId());

        assertNotNull(res, "El proveedor obtenido por ID no debería ser nulo.");
        assertEquals(proveedores.getId(), res.getId(), "El ID del proveedor debe coincidir.");
        assertEquals(proveedores.getNombre(), res.getNombre(), "El nombre del proveedor debe coincidir.");
        assertEquals(proveedores.getTelefono(), res.getTelefono(), "El teléfono del proveedor debe coincidir.");
        assertEquals(proveedores.getEmail(), res.getEmail(), "El email del proveedor debe coincidir.");
    }

    private void search(Proveedores proveedores) throws SQLException {
        ArrayList<Proveedores> proveedor = proveedoresDAO.search(proveedores.getNombre());
        boolean encontrado = false;

        for (Proveedores p : proveedor) {
            if (p.getNombre().contains(proveedores.getNombre())) {
                encontrado = true;
            } else {
                encontrado = false;
                break;
            }
        }

        assertTrue(encontrado, "El nombre buscado no fue encontrado: " + proveedores.getNombre());
    }

    private void delete(Proveedores proveedores) throws SQLException {
        boolean res = proveedoresDAO.delete(proveedores);
        assertTrue(res, "La eliminación del proveedor debería ser exitosa.");

        Proveedores res2 = proveedoresDAO.getById(proveedores.getId());
        assertNull(res2, "El proveedor debería haber sido eliminado y no encontrado por ID.");
    }

    @Test
    void testProveedoresDAO() throws SQLException {
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        String nombre = "Proveedores" + num;

        Proveedores proveedores = new Proveedores(0, nombre, "2222-3333", "correo" + num + "@test.com");

        Proveedores proveedorDePrueba = create(proveedores);
        update(proveedorDePrueba);
        search(proveedorDePrueba);
    }

    @Test
    void createProveedores() throws SQLException {
        Proveedores proveedores = new Proveedores(0, "Proveedor Test", "1234-5678", "proveedor@test.com");
        Proveedores res = proveedoresDAO.create(proveedores);
        assertNotNull(res, "El proveedor creado no debe ser nulo.");
    }
}

