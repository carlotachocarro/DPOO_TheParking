package Presentacion.Vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegistroPanel extends JPanel {

    private JTextField txtNombreUsuario;
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmarPassword;
    private JButton btnRegistrarse;
    private JButton btnVolverLogin;

    public RegistroPanel() {
        inicializar();
    }

    private void inicializar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        setPreferredSize(new Dimension(350, 460));

        JLabel lblTitulo = new JLabel("Crear cuenta");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Regístrate para continuar");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblNombreUsuario = new JLabel("Nombre usuario");
        txtNombreUsuario = new JTextField();
        txtNombreUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblCorreo = new JLabel("Correo");
        txtCorreo = new JTextField();
        txtCorreo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblPassword = new JLabel("Contraseña");
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JLabel lblConfirmarPassword = new JLabel("Confirmar contraseña");
        txtConfirmarPassword = new JPasswordField();
        txtConfirmarPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRegistrarse.setBackground(new Color(52, 152, 219));
        btnRegistrarse.setForeground(Color.WHITE);
        btnRegistrarse.setFocusPainted(false);

        btnVolverLogin = new JButton("Inicia sesión aquí");
        btnVolverLogin.setBorderPainted(false);
        btnVolverLogin.setContentAreaFilled(false);
        btnVolverLogin.setForeground(new Color(52, 152, 219));
        btnVolverLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(lblTitulo);
        add(Box.createVerticalStrut(30));
        add(lblSubtitulo);
        add(Box.createVerticalStrut(20));
        add(lblNombreUsuario);
        add(Box.createVerticalStrut(5));
        add(txtNombreUsuario);
        add(Box.createVerticalStrut(15));
        add(lblCorreo);
        add(Box.createVerticalStrut(5));
        add(txtCorreo);
        add(Box.createVerticalStrut(15));
        add(lblPassword);
        add(Box.createVerticalStrut(5));
        add(txtPassword);
        add(Box.createVerticalStrut(15));
        add(lblConfirmarPassword);
        add(Box.createVerticalStrut(5));
        add(txtConfirmarPassword);
        add(Box.createVerticalStrut(20));
        add(btnRegistrarse);
        add(Box.createVerticalStrut(15));
        add(new JLabel("¿Ya tienes cuenta?"));
        add(btnVolverLogin);
    }

    public void limpiarCampos() {
        txtNombreUsuario.setText("");
        txtCorreo.setText("");
        txtPassword.setText("");
        txtConfirmarPassword.setText("");
    }

    public void limpiarEmail() {
        txtCorreo.setText("");
    }

    public void focoEnNombreUsuario() {
        txtNombreUsuario.requestFocusInWindow();
    }

    public void focoEnCorreo() {
        txtCorreo.requestFocusInWindow();
    }

    public void focoEnPassword() {
        txtPassword.requestFocusInWindow();
    }

    public void addRegistroListener(ActionListener listener) {
        btnRegistrarse.addActionListener(listener);
    }

    public void addIrALoginListener(ActionListener listener) {
        btnVolverLogin.addActionListener(listener);
    }

    public String getNombreUsuario() {
        return txtNombreUsuario.getText().trim();
    }

    public String getCorreo() {
        return txtCorreo.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword()).trim();
    }

    public String getConfirmarPassword() {
        return new String(txtConfirmarPassword.getPassword()).trim();
    }
}