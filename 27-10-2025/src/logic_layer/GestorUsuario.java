package logic_layer;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class GestorUsuario {

    private static Map<String, Usuario> usuariosRegistrados = new HashMap<>();

    public static void inicializar() {
        Usuario admin = new Usuario("admin", "1234");
        usuariosRegistrados.put(admin.getUsername(), admin);
    }

    public static void registrarNuevoUsuario() {
        String username = JOptionPane.showInputDialog(null, "Ingrese un nuevo nombre de usuario:", "Registro", JOptionPane.QUESTION_MESSAGE);
        
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Registro cancelado.");
            return;
        }
        
        if (usuariosRegistrados.containsKey(username)) {
            JOptionPane.showMessageDialog(null, "Error: El nombre de usuario '" + username + "' ya existe.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String password = JOptionPane.showInputDialog(null, 
            "Ingrese una contraseña:\n(ADVERTENCIA: La contraseña será visible)", 
            "Registro", 
            JOptionPane.WARNING_MESSAGE);

        if (password == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Registro cancelado.");
            return;
        }

        String confirmPassword = JOptionPane.showInputDialog(null, 
            "Confirme la contraseña:", 
            "Registro", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmPassword == null) {
            JOptionPane.showMessageDialog(null, "Registro cancelado.");
            return;
        }

        if (password.equals(confirmPassword)) {
            Usuario nuevoUsuario = new Usuario(username, password);
            usuariosRegistrados.put(nuevoUsuario.getUsername(), nuevoUsuario);
            JOptionPane.showMessageDialog(null, "¡Usuario '" + username + "' registrado exitosamente!", "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Error: Las contraseñas no coinciden.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Usuario iniciarSesion() {
        for (int i = 0; i < 3; i++) {
            String username = JOptionPane.showInputDialog(null, "Ingrese su usuario:", "Iniciar Sesión", JOptionPane.QUESTION_MESSAGE);
            
            if (username == null) return null; 
            String password = JOptionPane.showInputDialog(null, 
                "Ingrese su contraseña:", 
                "Iniciar Sesión", 
                JOptionPane.WARNING_MESSAGE);

            if (password == null) return null; 
            Usuario usuario = usuariosRegistrados.get(username);
            
            if (usuario != null && usuario.checkPassword(password)) {
                JOptionPane.showMessageDialog(null, "¡Bienvenido, " + usuario.getUsername() + "!", "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);
                return usuario;
            } else {
                JOptionPane.showMessageDialog(null, "Error: Usuario o contraseña incorrectos. Intentos restantes: " + (2 - i), "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        JOptionPane.showMessageDialog(null, "Demasiados intentos fallidos. El programa se cerrará.", "Bloqueado", JOptionPane.ERROR_MESSAGE);
        return null;
    }
}
