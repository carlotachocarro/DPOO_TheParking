package Presentacion.Vistas;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsuarioCorreo;
    private JPasswordField txtPassword;
    private JButton btnEntrar;
    private JButton btnRegistro;

    public LoginFrame() {
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("The Parking - Login");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void inicializarComponentes() {
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2));

        JPanel panelIzquierdo = crearPanelIzquierdo();
        JPanel panelDerecho = crearPanelDerecho();

        panelPrincipal.add(panelIzquierdo);
        panelPrincipal.add(panelDerecho);

        setContentPane(panelPrincipal);
    }

    private JPanel crearPanelIzquierdo() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(34, 40, 49));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        JLabel lblLogo = new JLabel("P  The Parking");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SpaceGrotesk", Font.BOLD, 24));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblLinea1 = new JLabel("Gestiona tu");
        JLabel lblLinea2 = new JLabel("aparcamiento");
        JLabel lblLinea3 = new JLabel("al instante.");

        for (JLabel lbl : new JLabel[]{lblLinea1, lblLinea2, lblLinea3}) {
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("SpaceGrotesk", Font.BOLD, 52));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        JLabel lblSub = new JLabel("Plataforma de gestión inteligente para aparcamientos modernos.");
        lblSub.setForeground(new Color(180, 180, 180));
        lblSub.setFont(new Font("Inter", Font.PLAIN, 16));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblLogo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblLinea1);
        panel.add(lblLinea2);
        panel.add(lblLinea3);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblSub);

        return panel;
    }

    private JPanel crearPanelDerecho() {
        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setBackground(new Color(245, 247, 250));

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        formulario.setPreferredSize(new Dimension(350, 400));

        JLabel lblTitulo = new JLabel("Iniciar sesión");
        lblTitulo.setFont(new Font("SpaceGrotesk", Font.BOLD, 24));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBienvenida = new JLabel("Bienvenido de nuevo");
        lblBienvenida.setFont(new Font("Inter", Font.PLAIN, 14));
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
        btnRegistro.addActionListener(e -> abrirRegistro());

        formulario.add(lblTitulo);
        formulario.add(Box.createVerticalStrut(40));
        formulario.add(lblBienvenida);
        formulario.add(Box.createVerticalStrut(20));
        formulario.add(lblUsuario);
        formulario.add(Box.createVerticalStrut(5));
        formulario.add(txtUsuarioCorreo);
        formulario.add(Box.createVerticalStrut(15));
        formulario.add(lblPassword);
        formulario.add(Box.createVerticalStrut(5));
        formulario.add(txtPassword);
        formulario.add(Box.createVerticalStrut(20));
        formulario.add(btnEntrar);
        formulario.add(Box.createVerticalStrut(15));
        formulario.add(new JLabel("¿No tienes cuenta?"));
        formulario.add(btnRegistro);

        panelContenedor.add(formulario);
        return panelContenedor;
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

        dispose();
        new MainUsuarioFrame("usuario1").setVisible(true);

    }

    private void abrirRegistro() {
        dispose();
        new RegistroFrame().setVisible(true);
    }

}
