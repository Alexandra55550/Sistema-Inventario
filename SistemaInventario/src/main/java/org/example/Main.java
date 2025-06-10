package org.example;

import org.example.presentacion.LoginForm;
import org.example.presentacion.MainForm;
import org.example.presentacion.ProductoForm; // Importa ProductoForm
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            // Instancia y muestra MainForm
            MainForm mainForm  = new MainForm();
            mainForm.setVisible(true);

            // Instancia y muestra LoginForm
            LoginForm loginForm = new LoginForm(mainForm);
            loginForm.setVisible(true);

            // Instancia y muestra ProductoForm
            ProductoForm productoForm = new ProductoForm();
            productoForm.setVisible(true);
        });
    }
}