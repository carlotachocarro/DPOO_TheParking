package PRESENTACION.VISTAS;

import javax.swing.*;
import java.awt.*;

public class RegistroFrame extends AuthBaseFrame {

    private JTextField txtUsuarioCorreo;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmarPassword;
    private JButton btnRegistrarse;
    private JButton btnVolverLogin;

    public RegistroFrame() {
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("The Parking - Registro");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2));

        JPanel panelIzquierdo = crearPanelIzquierdoAuth(
                "P  The Parking",
                "Crea tu",
                "cuenta",
                "ahora.",
                "Empieza a gestionar tu aparcamiento de forma inteligente."
        );

        JPanel panelDerecho = crearPanelDerecho();

        panelPrincipal.add(panelIzquierdo);
        panelPrincipal.add(panelDerecho);

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelDerecho() {
        JPanel panelContenedor = crearContenedorFormulario();

        JPanel formulario = crearFormularioBase();

        JLabel lblTitulo = new JLabel("Registrarse");
        lblTitulo.setFont(new Font("SpaceGrotesk", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBienvenida = new JLabel("Bienvenido");
        lblBienvenida.setFont(new Font("Inter", Font.PLAIN, 14));
        lblBienvenida.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblUsuario = new JLabel("Usuario o correo");
        txtUsuarioCorreo = crearCampoTexto();

        JLabel lblPassword = new JLabel("Contraseña");
        txtPassword = crearCampoPassword();

        JLabel lblConfirmar = new JLabel("Confirmar contraseña");
        txtConfirmarPassword = crearCampoPassword();

        btnRegistrarse = crearBotonPrincipal("Registrarse");
        btnVolverLogin = crearBotonLink("Volver al login");

        btnRegistrarse.addActionListener(e -> registrarUsuario());
        btnVolverLogin.addActionListener(e -> volverLogin());

        formulario.add(lblTitulo);
        formulario.add(Box.createVerticalStrut(30));
        formulario.add(lblBienvenida);
        formulario.add(Box.createVerticalStrut(20));
        formulario.add(lblUsuario);
        formulario.add(Box.createVerticalStrut(5));
        formulario.add(txtUsuarioCorreo);
        formulario.add(Box.createVerticalStrut(15));
        formulario.add(lblPassword);
        formulario.add(Box.createVerticalStrut(5));
        formulario.add(txtPassword);
        formulario.add(Box.createVerticalStrut(15));
        formulario.add(lblConfirmar);
        formulario.add(Box.createVerticalStrut(5));
        formulario.add(txtConfirmarPassword);
        formulario.add(Box.createVerticalStrut(20));
        formulario.add(btnRegistrarse);
        formulario.add(Box.createVerticalStrut(15));
        formulario.add(btnVolverLogin);

        panelContenedor.add(formulario);
        return panelContenedor;
    }

    private void registrarUsuario() {
        String usuarioCorreo = txtUsuarioCorreo.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmar = new String(txtConfirmarPassword.getPassword()).trim();

        if (usuarioCorreo.isEmpty() || password.isEmpty() || confirmar.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes rellenar todos los campos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmar)) {
            JOptionPane.showMessageDialog(this,
                    "Las contraseñas no coinciden.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Usuario registrado correctamente.",
                "Registro correcto",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
        new LoginFrame().setVisible(true);
    }

    private void volverLogin() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}