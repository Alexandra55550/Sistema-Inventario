package org.example.presentacion;

import org.example.dominio.Producto;
import org.example.persistencia.ProductoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class ProductoForm extends JFrame {

    private JPanel mainPanel;
    private JTextField txtNombre, txtPrecio, txtCantidad;
    private JButton btnCreate, btnUpdate, btnDelete, btnCancel;
    private JTable tblProductos;
    private DefaultTableModel modeloTabla;
    private ProductoDAO productoDAO;

    public ProductoForm() {
        super("Gestión de Productos");
        productoDAO = new ProductoDAO();
        initComponents();
        initEvents();
        listarProductos();

        setContentPane(mainPanel); // importante
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10,10));

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        // Etiquetas
        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblPrecio = new JLabel("Precio:");
        JLabel lblCantidad = new JLabel("Cantidad:");

        // Campos de texto
        txtNombre = new JTextField(20);
        txtPrecio = new JTextField(10);
        txtCantidad = new JTextField(10);

        // Posicionando etiquetas y campos
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(lblNombre, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(lblPrecio, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtPrecio, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(lblCantidad, gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtCantidad, gbc);

        // Espaciador vertical
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

        // Tabla productos
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Cantidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProductos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tblProductos);

        // --- Aquí el cambio para arreglar layout ---
        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.add(panelFormulario, BorderLayout.NORTH);
        panelArriba.add(panelBotones, BorderLayout.SOUTH);

        mainPanel.add(panelArriba, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        // --------------------------------------------
    }

    private void initEvents() {
        btnCreate.addActionListener(e -> crearProducto());
        btnUpdate.addActionListener(e -> actualizarProducto());
        btnDelete.addActionListener(e -> eliminarProducto());
        btnCancel.addActionListener(e -> limpiarCampos());

        tblProductos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tblProductos.getSelectedRow();
                if (fila >= 0) {
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtPrecio.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtCantidad.setText(modeloTabla.getValueAt(fila, 3).toString());
                }
            }
        });
    }

    private void crearProducto() {
        try {
            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
                return;
            }

            Producto producto = new Producto(0, nombre, precio, cantidad);
            Producto creado = productoDAO.create(producto);
            if (creado != null) {
                JOptionPane.showMessageDialog(this, "Producto creado con ID: " + creado.getId());
                limpiarCampos();
                listarProductos();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y cantidad deben ser números válidos.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al crear producto: " + ex.getMessage());
        }
    }

    private void actualizarProducto() {
        try {
            int fila = tblProductos.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para actualizar.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);

            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
                return;
            }

            Producto producto = new Producto(id, nombre, precio, cantidad);
            boolean exito = productoDAO.update(producto);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto actualizado.");
                limpiarCampos();
                listarProductos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el producto.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y cantidad deben ser números válidos.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar producto: " + ex.getMessage());
        }
    }

    private void eliminarProducto() {
        try {
            int fila = tblProductos.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.");
                return;
            }
            int id = (int) modeloTabla.getValueAt(fila, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar el producto con ID " + id + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Producto producto = new Producto();
                producto.setId(id);
                boolean exito = productoDAO.delete(producto);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado.");
                    limpiarCampos();
                    listarProductos();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el producto.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar producto: " + ex.getMessage());
        }
    }

    private void listarProductos() {
        try {
            List<Producto> productos = productoDAO.search(""); // buscar todos
            cargarTabla(productos);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al listar productos: " + ex.getMessage());
        }
    }

    private void cargarTabla(List<Producto> productos) {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getPrecio(),
                    p.getCantidad()
            });
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        tblProductos.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductoForm form = new ProductoForm();
            form.setVisible(true);
        });
    }
}