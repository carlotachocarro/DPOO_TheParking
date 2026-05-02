package Presentacion.Vistas;

import Presentacion.Controladores.ControladorInicioSesion;
import Presentacion.Controladores.ControladorRegistroUsuario;
import Presentacion.Controladores.ControladorReservasPlaza;
import Presentacion.Controladores.ControllerMenuPrincipalAdmin;
import Presentacion.Vistas.Panels.EstadoParkingPanel;
import Presentacion.Vistas.Panels.ReservasPlazaPanel;

import javax.swing.*;
import java.awt.*;

public class AuthFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelCards;

    public static final String LOGIN = "LOGIN";
    public static final String REGISTRO = "REGISTRO";

    public AuthFrame() {
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("The Parking - Acceso");
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
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblLinea1 = new JLabel("Gestiona tu");
        JLabel lblLinea2 = new JLabel("aparcamiento");
        JLabel lblLinea3 = new JLabel("al instante.");

        for (JLabel lbl : new JLabel[]{lblLinea1, lblLinea2, lblLinea3}) {
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 52));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        JLabel lblSub = new JLabel("Plataforma de gestión inteligente para aparcamientos modernos.");
        lblSub.setForeground(new Color(180, 180, 180));
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 16));
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

        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);
        panelCards.setOpaque(false);

        LoginPanel loginPanel = new LoginPanel(this);
        RegistroPanel registroPanel = new RegistroPanel(this);

        new ControladorInicioSesion(loginPanel);
        new ControladorRegistroUsuario(registroPanel);



        panelCards.add(loginPanel, LOGIN);
        panelCards.add(registroPanel, REGISTRO);


        panelContenedor.add(panelCards);
        return panelContenedor;
    }

    public void mostrarLogin() {
        cardLayout.show(panelCards, LOGIN);
    }

    public void mostrarRegistro() {
        cardLayout.show(panelCards, REGISTRO);
    }
}