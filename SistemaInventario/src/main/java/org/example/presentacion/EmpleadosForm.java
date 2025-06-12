package org.example.presentacion;

import org.example.dominio.Empleados;
import org.example.persistencia.EmpleadosDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmpleadosForm extends JFrame {

    private JPanel mainPanel;
    private JTextField txtNombre, txtPuesto, txtFechaingreso;
    private JButton btnCreate, btnUpdate, btnDelete, btnCancel;
    private JTable tblEmpleados;
    private DefaultTableModel modeloTabla;
    private EmpleadosDAO empleadosDAO;

    public EmpleadosForm() {
        super("Gestión de Empleados");
        empleadosDAO = new EmpleadosDAO();
        initComponents();
        initEvents();
        listarEmpleados();

        setContentPane(mainPanel);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblPuesto = new JLabel("Puesto:");
        JLabel lblFechaingreso = new JLabel("Fecha de Ingreso:");

        txtNombre = new JTextField(20);
        txtPuesto = new JTextField(20);
        txtFechaingreso = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(lblNombre, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(lblPuesto, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtPuesto, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(lblFechaingreso, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtFechaingreso, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weighty = 0.1;
        panelFormulario.add(Box.createVerticalStrut(15), gbc);

        btnCreate = new JButton("Crear");
        btnUpdate = new JButton("Actualizar");
        btnDelete = new JButton("Eliminar");
        btnCancel = new JButton("Limpiar");

        // Estilo azul como en el formulario de productos
        Color azulBoton = new Color(0, 123, 255);
        Color blanco = Color.WHITE;
        JButton[] botones = {btnCreate, btnUpdate, btnDelete, btnCancel};
        for (JButton boton : botones) {
            boton.setBackground(azulBoton);
            boton.setForeground(blanco);
            boton.setFocusPainted(false);
        }

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.add(btnCreate);
        panelBotones.add(btnUpdate);
        panelBotones.add(btnDelete);
        panelBotones.add(btnCancel);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Puesto", "Fechaingreso"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblEmpleados = new JTable(modeloTabla);

// 🎨 Estilo de encabezado de tabla (azul con letras blancas)
        tblEmpleados.getTableHeader().setBackground(new Color(0, 123, 255));
        tblEmpleados.getTableHeader().setForeground(Color.WHITE);
        tblEmpleados.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tblEmpleados);


        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.add(panelFormulario, BorderLayout.NORTH);
        panelArriba.add(panelBotones, BorderLayout.SOUTH);

        mainPanel.add(panelArriba, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void initEvents() {
        btnCreate.addActionListener(e -> crearEmpleados());
        btnUpdate.addActionListener(e -> actualizarEmpleado());
        btnDelete.addActionListener(e -> eliminarEmpleado());
        btnCancel.addActionListener(e -> limpiarCampos());

        tblEmpleados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tblEmpleados.getSelectedRow();
                if (fila >= 0) {
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtPuesto.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtFechaingreso.setText(modeloTabla.getValueAt(fila, 3).toString());
                }
            }
        });
    }

    private void crearEmpleados() {
        try {
            String nombre = txtNombre.getText().trim();
            String puesto = txtPuesto.getText().trim();
            LocalDate fechaingreso = LocalDate.parse(txtFechaingreso.getText().trim());

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
                return;
            }

            Empleados empleados = new Empleados(0, nombre, puesto, fechaingreso);
            Empleados creado = empleadosDAO.create(empleados);
            if (creado != null) {
                JOptionPane.showMessageDialog(this, "Empleado creado con ID: " + creado.getId());
                limpiarCampos();
                listarEmpleados();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al crear empleado: " + ex.getMessage());
        }
    }

    private void actualizarEmpleado() {
        try {
            int fila = tblEmpleados.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un empleado para actualizar.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);

            String nombre = txtNombre.getText().trim();
            String puesto = txtPuesto.getText().trim();
            LocalDate fechaingreso = LocalDate.parse(txtFechaingreso.getText().trim());

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
                return;
            }

            Empleados empleados = new Empleados(id, nombre, puesto, fechaingreso);
            boolean exito = empleadosDAO.update(empleados);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Empleado actualizado.");
                limpiarCampos();
                listarEmpleados();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el empleado.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar empleado: " + ex.getMessage());
        }
    }

    private void eliminarEmpleado() {
        try {
            int fila = tblEmpleados.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un empleado para eliminar.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar el empleado con ID " + id + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Empleados empleados = new Empleados();
                empleados.setId(id);
                boolean exito = empleadosDAO.delete(empleados);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Empleado eliminado.");
                    limpiarCampos();
                    listarEmpleados();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el empleado.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar empleado: " + ex.getMessage());
        }
    }

    private void listarEmpleados() {
        try {
            List<Empleados> empleados = empleadosDAO.search("");
            cargarTabla(empleados);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al listar empleados: " + ex.getMessage());
        }
    }

    private void cargarTabla(List<Empleados> empleados) {
        modeloTabla.setRowCount(0);
        for (Empleados e : empleados) {
            modeloTabla.addRow(new Object[]{
                    e.getId(),
                    e.getNombre(),
                    e.getPuesto(),
                    e.getFechaIngreso()
            });
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtPuesto.setText("");
        txtFechaingreso.setText("");
        tblEmpleados.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmpleadosForm form = new EmpleadosForm();
            form.setVisible(true);
        });
    }
}