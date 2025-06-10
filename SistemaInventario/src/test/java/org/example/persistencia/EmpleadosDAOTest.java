package org.example.persistencia;

import org.example.dominio.Empleados;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EmpleadosDAOTest {
    private EmpleadosDAO empleadosDAO;

    @BeforeEach
    void setUp() {
        empleadosDAO = new EmpleadosDAO();
    }

    private Empleados create(Empleados empleados) throws SQLException {
        Empleados res = empleadosDAO.create(empleados);

        assertNotNull(res, "El empleado creado no debería ser nulo.");
        assertEquals(empleados.getNombre(), res.getNombre(), "El nombre del empleado creado debe ser igual al original.");
        assertEquals(empleados.getPuesto(), res.getPuesto(), "El puesto del empleado creado debe ser igual al original.");
        assertEquals(empleados.getFechaIngreso(), res.getFechaIngreso(), "La fecha de ingreso debe ser igual.");

        return res;
    }

    private void update(Empleados empleados) throws SQLException {
        empleados.setNombre(empleados.getNombre() + "_u");
        empleados.setPuesto("Nuevo Puesto");
        empleados.setFechaIngreso(LocalDate.now());

        boolean res = empleadosDAO.update(empleados);
        assertTrue(res, "La actualización del empleado debería ser exitosa.");

        getById(empleados);
    }

    private void getById(Empleados empleados) throws SQLException {
        Empleados res = empleadosDAO.getById(empleados.getId());

        assertNotNull(res, "El empleado obtenido por ID no debería ser nulo.");
        assertEquals(empleados.getId(), res.getId(), "El ID del empleado debe coincidir.");
        assertEquals(empleados.getNombre(), res.getNombre(), "El nombre del empleado debe coincidir.");
        assertEquals(empleados.getPuesto(), res.getPuesto(), "El puesto del empleado debe coincidir.");
        assertEquals(empleados.getFechaIngreso(), res.getFechaIngreso(), "La fecha de ingreso debe coincidir.");
    }

    private void search(Empleados empleados) throws SQLException {
        ArrayList<Empleados> empleado = empleadosDAO.search(empleados.getNombre());
        boolean encontrado = false;

        for (Empleados e : empleado) {
            if (e.getNombre().contains(empleados.getNombre())) {
                encontrado = true;
            } else {
                encontrado = false;
                break;
            }
        }

        assertTrue(encontrado, "El nombre buscado no fue encontrado: " + empleados.getNombre());
    }

    private void delete(Empleados empleados) throws SQLException {
        boolean res = empleadosDAO.delete(empleados);
        assertTrue(res, "La eliminación del empleado debería ser exitosa.");

        Empleados res2 = empleadosDAO.getById(empleados.getId());
        assertNull(res2, "El empleado debería haber sido eliminado y no encontrado por ID.");
    }

    @Test
    void testEmpleadosDAO() throws SQLException {
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        String nombre = "Empleado" + num;

        Empleados empleados = new Empleados(0, nombre, "Puesto Inicial", LocalDate.now().minusYears(1));

        Empleados empleadosDePrueba = create(empleados);
        update(empleadosDePrueba);
        search(empleadosDePrueba);
        delete(empleadosDePrueba);
    }

    @Test
    void createEmpleados() throws SQLException {
        Empleados empleados = new Empleados(0, "Empleado Test", "Analista", LocalDate.now());
        Empleados res = empleadosDAO.create(empleados);
        assertNotNull(res, "El empleado creado no debe ser nulo.");
    }
}

