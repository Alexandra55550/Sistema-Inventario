package org.example.presentacion;

import javax.swing.*;
import java.awt.*;
import org.example.dominio.User;

public class MainForm extends JFrame {

    private User userAutenticate;
    private JLabel lblWelcome;

    public User getUserAutenticate() {
        return userAutenticate;
    }

    public void setUserAutenticate(User userAutenticate) {
        this.userAutenticate = userAutenticate;
    }

    public MainForm() {
        setTitle("Sistema de Inventario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // pantalla completa
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE); // fondo blanco

        createMenu();  // menú de navegación
        createWelcomePanel(); // mensaje de bienvenida
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(0, 102, 204)); // azul fuerte
        menuBar.setForeground(Color.WHITE);
        setJMenuBar(menuBar);

        Font menuFont = new Font("Segoe UI", Font.BOLD, 14);

        JMenu menuPerfil = new JMenu("Perfil");
        menuPerfil.setForeground(Color.WHITE);
        menuPerfil.setFont(menuFont);
        menuBar.add(menuPerfil);

        JMenuItem itemChangePassword = new JMenuItem("Cambiar contraseña");
        menuPerfil.add(itemChangePassword);
        itemChangePassword.addActionListener(e -> {
            ChangePasswordForm changePassword = new ChangePasswordForm(this);
            changePassword.setVisible(true);
        });

        JMenuItem itemChangeUser = new JMenuItem("Cambiar de usuario");
        menuPerfil.add(itemChangeUser);
        itemChangeUser.addActionListener(e -> {
            LoginForm loginForm = new LoginForm(this);
            loginForm.setVisible(true);
        });

        JMenuItem itemSalir = new JMenuItem("Salir");
        menuPerfil.add(itemSalir);
        itemSalir.addActionListener(e -> System.exit(0));

        JMenu menuMantenimiento = new JMenu("Mantenimientos");
        menuMantenimiento.setForeground(Color.WHITE);
        menuMantenimiento.setFont(menuFont);
        menuBar.add(menuMantenimiento);

        JMenuItem itemUsers = new JMenuItem("Usuarios");
        menuMantenimiento.add(itemUsers);
        itemUsers.addActionListener(e -> {
            UserReadingForm userReadingForm = new UserReadingForm(this);
            userReadingForm.setVisible(true);
        });

        JMenuItem itemProducto = new JMenuItem("Producto");
        menuMantenimiento.add(itemProducto);
        itemProducto.addActionListener(e -> {
            ProductoForm productoForm = new ProductoForm();
            productoForm.setVisible(true);
        });

        JMenuItem itemProveedores = new JMenuItem("Proveedores");
        menuMantenimiento.add(itemProveedores);
        itemProveedores.addActionListener(e -> {
            ProveedoresForm proveedoresForm = new ProveedoresForm();
            proveedoresForm.setVisible(true);
        });

        JMenuItem itemEmpleados = new JMenuItem("Empleados");
        menuMantenimiento.add(itemEmpleados);
        itemEmpleados.addActionListener(e -> {
            EmpleadosForm empleadosForm = new EmpleadosForm();
            empleadosForm.setVisible(true);
        });
    }

    private void createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridBagLayout());

        lblWelcome = new JLabel("Bienvenidos al sistema de inventario");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblWelcome.setForeground(new Color(0, 102, 204)); // azul bonito

        panel.add(lblWelcome);
        getContentPane().add(panel, BorderLayout.CENTER);
    }
}
