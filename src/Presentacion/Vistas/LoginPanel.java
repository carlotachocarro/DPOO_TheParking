package Presentacion.Vistas;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private JTextField txtUsuarioCorreo;
    private JPasswordField txtPassword;
    private JButton btnEntrar;
    private JButton btnRegistro;
    private AuthFrame frame;

    public LoginPanel(AuthFrame frame) {
        this.frame = frame;
        inicializar();
    }

    private void inicializar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        setPreferredSize(new Dimension(350, 400));

        JLabel lblTitulo = new JLabel("Iniciar sesión");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBienvenida = new JLabel("Bienvenido de nuevo");
        lblBienvenida.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblBienvenida.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUsuario = new JLabel("Usuario o correo");
        txtUsuarioCorreo = new JTextField();
        txtUsuarioCorreo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblPassword = new JLabel("Contraseña");
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        btnEntrar = new JButton("Entrar");
        btnEntrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnEntrar.setBackground(new Color(52, 152, 219));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);

        btnRegistro = new JButton("Regístrate aquí");
        btnRegistro.setBorderPainted(false);
        btnRegistro.setContentAreaFilled(false);
        btnRegistro.setForeground(new Color(52, 152, 219));
        btnRegistro.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnEntrar.addActionListener(e -> iniciarSesion());
        btnRegistro.addActionListener(e -> frame.mostrarRegistro());

        add(lblTitulo);
        add(Box.createVerticalStrut(40));
        add(lblBienvenida);
        add(Box.createVerticalStrut(20));
        add(lblUsuario);
        add(Box.createVerticalStrut(5));
        add(txtUsuarioCorreo);
        add(Box.createVerticalStrut(15));
        add(lblPassword);
        add(Box.createVerticalStrut(5));
        add(txtPassword);
        add(Box.createVerticalStrut(20));
        add(btnEntrar);
        add(Box.createVerticalStrut(15));
        add(new JLabel("¿No tienes cuenta?"));
        add(btnRegistro);
    }

    private void iniciarSesion() {
        String usuarioCorreo = txtUsuarioCorreo.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (usuarioCorreo.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes rellenar usuario/correo y contraseña.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Window ventana = SwingUtilities.getWindowAncestor(this);
        ventana.dispose();
        new MainUsuarioFrame("usuario1").setVisible(true);
    }
}