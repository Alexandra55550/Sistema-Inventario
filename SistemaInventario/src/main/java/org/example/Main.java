package org.example;

import org.example.presentacion.LoginForm;
import org.example.presentacion.MainForm;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainForm mainForm  = new MainForm(); // Crear MainForm
            LoginForm loginForm = new LoginForm(mainForm); // Pasar MainForm al login

            loginForm.setVisible(true); // Mostrar login (bloquea aquí hasta cerrar)

            // Solo mostramos el MainForm si el usuario fue autenticado correctamente
            if (mainForm.getUserAutenticate() != null) {
                mainForm.setVisible(true); // Mostrar solo después del login
            } else {
                System.exit(0); // Si no hay usuario autenticado, salir
            }
        });
    }
}
