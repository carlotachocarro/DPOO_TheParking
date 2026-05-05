package Presentacion.Vistas;

import Presentacion.Controladores.ControladorAplicacion;
import Presentacion.Controladores.ControladorEntradasSalidas;
import Presentacion.Controladores.ControladorMisReservas;
import Presentacion.Controladores.ControladorReservasPlaza;
import Presentacion.Controladores.ControllerMenuPrincipalAdmin;
import Presentacion.Vistas.Panels.EstadoParkingPanel;
import Presentacion.Vistas.Panels.EntradaSalidaPanel;
import Presentacion.Vistas.Panels.MisReservasPanel;
import Presentacion.Vistas.Panels.GraficoOcupacionPanel;
import Presentacion.Vistas.Panels.ReservasPlazaPanel;

import javax.swing.*;
import java.awt.*;

public class MainUsuarioFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private final String nombreUsuario;
    private ControllerMenuPrincipalAdmin controller;

    public MainUsuarioFrame(String nombreUsuario, ControllerMenuPrincipalAdmin controller) {
        this.nombreUsuario = nombreUsuario;
        this.controller = controller;
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("The Parking - Panel Usuario");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        add(crearSidebar(), BorderLayout.WEST);
        add(crearTopbar(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new EstadoParkingPanel(controller), "ESTADO");
        // Añadir ESA MISMA al CardLayout
        ReservasPlazaPanel reservasPanel = new ReservasPlazaPanel();

        new ControladorReservasPlaza(reservasPanel, nombreUsuario, controller.getServicioPlaza());

        EntradaSalidaPanel entradaSalida = new EntradaSalidaPanel();
        new ControladorEntradasSalidas(entradaSalida, nombreUsuario);
        contentPanel.add(entradaSalida, "ENTRADA_SALIDA");


        contentPanel.add(reservasPanel, "RESERVAR");
        MisReservasPanel reservas = new MisReservasPanel(this::mostrarVista);
        ControladorMisReservas misReservas = new ControladorMisReservas(reservas);

        contentPanel.add(reservas, "MIS_RESERVAS");
        contentPanel.add(new GraficoOcupacionPanel(), "GRAFICO");

        add(contentPanel, BorderLayout.CENTER);
        mostrarVista("ESTADO");

        misReservas.getReserva(nombreUsuario);

    }

//    private void mostrarAvisosPendientes() {
//        SwingUtilities.invokeLater(() -> {
//            List<NotificacionesDialog.AvisoReservaCancelada> avisos =
//                    controller.obtenerAvisosPendientes(nombreUsuario);
//            if (!avisos.isEmpty()) {
//                new NotificacionesDialog(this, avisos).setVisible(true);
//                controller.marcarAvisosComoLeidos(nombreUsuario);
//            }
//        });
//    }

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(34, 40, 49));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel lblLogo = new JLabel("P  The Parking");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SpaceGrotesk", Font.BOLD, 20));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBadgeUsuario = new JLabel("USUARIO");
        lblBadgeUsuario.setForeground(new Color(52, 152, 219));
        lblBadgeUsuario.setFont(new Font("Inter", Font.BOLD, 12));
        lblBadgeUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSeccionApp = crearEtiquetaSeccion("APLICACIÓN");
        JButton btnEstado = crearBotonMenu("Estado Parking");
        JButton btnEntradaSalida = crearBotonMenu("Entrada / Salida");
        JButton btnReservar = crearBotonMenu("Reservar Plaza");
        JButton btnMisReservas = crearBotonMenu("Mis Reservas");
        JButton btnGrafico = crearBotonMenu("Gráfico");

        JLabel lblSeccionCuenta = crearEtiquetaSeccion("CUENTA");
        JButton btnEliminarCuenta = crearBotonMenu("Eliminar cuenta");
        JButton btnCerrarSesion = crearBotonMenu("Cerrar Sesión");

        btnEstado.addActionListener(e -> mostrarVista("ESTADO"));
        btnEntradaSalida.addActionListener(e -> mostrarVista("ENTRADA_SALIDA"));
        btnReservar.addActionListener(e -> mostrarVista("RESERVAR"));
        btnMisReservas.addActionListener(e -> mostrarVista("MIS_RESERVAS"));
        btnGrafico.addActionListener(e -> mostrarVista("GRAFICO"));
        btnEliminarCuenta.addActionListener(e -> eliminarCuenta());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(lblBadgeUsuario);
        sidebar.add(Box.createVerticalStrut(25));
        sidebar.add(lblSeccionApp);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnEstado);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnEntradaSalida);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnReservar);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnMisReservas);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnGrafico);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(lblSeccionCuenta);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnEliminarCuenta);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnCerrarSesion);

        return sidebar;
    }

    private JLabel crearEtiquetaSeccion(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(140, 150, 160));
        lbl.setFont(new Font("Inter", Font.BOLD, 11));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(54, 62, 71));
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        boton.setHorizontalAlignment(SwingConstants.LEFT);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(70, 80, 90));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(54, 62, 71));
            }
        });
        return boton;
    }

    private JPanel crearTopbar() {
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        topbar.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Panel principal");
        lblTitulo.setFont(new Font("Inter", Font.BOLD, 22));

        JLabel lblUsuario = new JLabel("Usuario: " + nombreUsuario);
        lblUsuario.setFont(new Font("Inter", Font.PLAIN, 14));

        topbar.add(lblTitulo, BorderLayout.WEST);
        topbar.add(lblUsuario, BorderLayout.EAST);

        return topbar;
    }

    public void mostrarVista(String nombreVista) {
        cardLayout.show(contentPanel, nombreVista);
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            ControladorAplicacion.reiniciarFlujoAutenticacion();
        }
    }

    private void eliminarCuenta() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres eliminar tu cuenta?",
                "Eliminar cuenta",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(
                    this,
                    "La cuenta se eliminará próximamente de la base de datos.",
                    "Cuenta eliminada",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();
            ControladorAplicacion.reiniciarFlujoAutenticacion();
        }
    }
    public String  getNombreUsuario() {
        return nombreUsuario;
    }
}