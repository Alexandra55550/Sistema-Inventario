package org.example.presentacion;

import org.example.dominio.Proveedores;
import org.example.persistencia.ProveedoresDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class ProveedoresForm extends JFrame {

    private JPanel mainPanel;
    private JTextField txtNombre, txtTelefono, txtEmail;
    private JButton btnCreate, btnUpdate, btnDelete, btnCancel;
    private JTable tblProveedores;
    private DefaultTableModel modeloTabla;
    private ProveedoresDAO proveedoresDAO;

    public ProveedoresForm() {
        super("Gestión de Proveedores");
        proveedoresDAO = new ProveedoresDAO();
        initComponents();
        initEvents();
        listarProveedores();

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

        // Etiquetas
        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblTelefono = new JLabel("Telefono:");
        JLabel lblEmail = new JLabel("Email:");

        // Campos de texto
        txtNombre = new JTextField(20);
        txtTelefono = new JTextField(20);
        txtEmail = new JTextField(15);

        // Posicionamiento
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(lblNombre, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(lblTelefono, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(lblEmail, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weighty = 0.1;
        panelFormulario.add(Box.createVerticalStrut(15), gbc);

        // Botones
        btnCreate = new JButton("Crear");
        btnUpdate = new JButton("Actualizar");
        btnDelete = new JButton("Eliminar");
        btnCancel = new JButton("Limpiar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.add(btnCreate);
        panelBotones.add(btnUpdate);
        panelBotones.add(btnDelete);
        panelBotones.add(btnCancel);

        // Tabla
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Telefono", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProveedores = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tblProveedores);

        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.add(panelFormulario, BorderLayout.NORTH);
        panelArriba.add(panelBotones, BorderLayout.SOUTH);

        mainPanel.add(panelArriba, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void initEvents() {
        btnCreate.addActionListener(e -> crearProveedores());
        btnUpdate.addActionListener(e -> actualizarProveedores());
        btnDelete.addActionListener(e -> eliminarProveedores());
        btnCancel.addActionListener(e -> limpiarCampos());

        tblProveedores.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tblProveedores.getSelectedRow();
                if (fila >= 0) {
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtTelefono.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtEmail.setText(modeloTabla.getValueAt(fila, 3).toString());
                }
            }
        });
    }

    private void crearProveedores() {
        try {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
                return;
            }

            Proveedores proveedores = new Proveedores(0, nombre, telefono, email);
            Proveedores creado = proveedoresDAO.create(proveedores);
            if (creado != null) {
                JOptionPane.showMessageDialog(this, "Proveedor creado con ID: " + creado.getId());
                limpiarCampos();
                listarProveedores();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al crear proveedor: " + ex.getMessage());
        }
    }

    private void actualizarProveedores() {
        try {
            int fila = tblProveedores.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un proveedor para actualizar.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);

            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
                return;
            }

            Proveedores proveedores = new Proveedores(id, nombre, telefono, email);
            boolean exito = proveedoresDAO.update(proveedores);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Proveedor actualizado.");
                limpiarCampos();
                listarProveedores();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el proveedor.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar proveedor: " + ex.getMessage());
        }
    }

    private void eliminarProveedores() {
        try {
            int fila = tblProveedores.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un proveedor para eliminar.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar el proveedor con ID " + id + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Proveedores proveedores = new Proveedores();
                proveedores.setId(id);
                boolean exito = proveedoresDAO.delete(proveedores);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Proveedor eliminado.");
                    limpiarCampos();
                    listarProveedores();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el proveedor.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar proveedor: " + ex.getMessage());
        }
    }

    private void listarProveedores() {
        try {
            List<Proveedores> proveedores = proveedoresDAO.search(""); // buscar todos
            cargarTabla(proveedores);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al listar proveedores: " + ex.getMessage());
        }
    }

    private void cargarTabla(List<Proveedores> proveedores) {
        modeloTabla.setRowCount(0);
        for (Proveedores p : proveedores) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getTelefono(),
                    p.getEmail()
            });
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        tblProveedores.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProveedoresForm form = new ProveedoresForm();
            form.setVisible(true);
        });
    }
}
