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

        setContentPane(mainPanel);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initComponents() {
        // Colores y fuente según estilo de proveedores
        Color fondo = new Color(245, 245, 245);
        Color azul = new Color(70, 130, 180);
        Color verde = new Color(70, 130, 180);
        Color rojo = new Color(70, 130, 180);
        Color gris = new Color(70, 130, 180);
        Font fontLabel = new Font("Segoe UI", Font.PLAIN, 14);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(fondo);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(fondo);
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Formulario Productos"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblPrecio = new JLabel("Precio:");
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblNombre.setFont(fontLabel);
        lblPrecio.setFont(fontLabel);
        lblCantidad.setFont(fontLabel);

        txtNombre = new JTextField(20);
        txtPrecio = new JTextField(10);
        txtCantidad = new JTextField(10);
        txtNombre.setFont(fontLabel);
        txtPrecio.setFont(fontLabel);
        txtCantidad.setFont(fontLabel);

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

        btnCreate = makeStyledButton("Crear");
        btnUpdate = makeStyledButton("Actualizar");
        btnDelete = makeStyledButton("Eliminar");
        btnCancel = makeStyledButton("Limpiar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(fondo);
        panelBotones.add(btnCreate);
        panelBotones.add(btnUpdate);
        panelBotones.add(btnDelete);
        panelBotones.add(btnCancel);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Cantidad"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProductos = new JTable(modeloTabla);
        tblProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblProductos.setRowHeight(25);
        tblProductos.getTableHeader().setBackground(new Color(100, 149, 237));
        tblProductos.getTableHeader().setForeground(Color.WHITE);
        tblProductos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblProductos.setSelectionBackground(new Color(100, 149, 237));

        JScrollPane scrollPane = new JScrollPane(tblProductos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Productos"));

        JPanel panelArriba = new JPanel(new BorderLayout());
        panelArriba.setBackground(fondo);
        panelArriba.add(panelFormulario, BorderLayout.CENTER);
        panelArriba.add(panelBotones, BorderLayout.SOUTH);

        mainPanel.add(panelArriba, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private JButton makeStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(110, 30));
        btn.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 40)));
        return btn;
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
            if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "El nombre es obligatorio."); return; }
            Producto producto = new Producto(0, nombre, precio, cantidad);
            Producto creado = productoDAO.create(producto);
            if (creado != null) {
                JOptionPane.showMessageDialog(this, "Producto creado con ID: " + creado.getId());
                limpiarCampos(); listarProductos();
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
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un producto para actualizar."); return; }
            int id = (int) modeloTabla.getValueAt(fila, 0);
            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (nombre.isEmpty()) { JOptionPane.showMessageDialog(this, "El nombre es obligatorio."); return; }
            Producto producto = new Producto(id, nombre, precio, cantidad);
            boolean exito = productoDAO.update(producto);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Producto actualizado.");
                limpiarCampos(); listarProductos();
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
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar."); return; }
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
                    limpiarCampos(); listarProductos();
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
            List<Producto> productos = productoDAO.search("");
            cargarTabla(productos);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al listar productos: " + ex.getMessage());
        }
    }

    private void cargarTabla(List<Producto> productos) {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{ p.getId(), p.getNombre(), p.getPrecio(), p.getCantidad() });
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
