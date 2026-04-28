package Presentacion.Vistas;

import Presentacion.Controladores.ControllerMenuPrincipalAdmin;
import Presentacion.Vistas.Dialogs.DetallePlazaDialog;
import Presentacion.Vistas.Dialogs.PlazaFormDialog;
import Presentacion.Vistas.Panels.EstadoParkingPanel;
import Presentacion.Vistas.Panels.GraficoOcupacionPanel;

import javax.swing.*;
import java.awt.*;

public class MainAdminFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private final ControllerMenuPrincipalAdmin controller;

    private EstadoParkingPanel panelGestionPlazas;
    private EstadoParkingPanel panelEstadoAdmin;

    private static final String GESTION_PLAZAS = "GESTION_PLAZAS";
    private static final String GESTION_RESERVAS = "GESTION_RESERVAS";
    private static final String ESTADO = "ESTADO";
    private static final String GRAFICO = "GRAFICO";

    public MainAdminFrame(ControllerMenuPrincipalAdmin controller) {
        this.controller = controller;
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("The Parking - Panel Administrador");
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

        // Panel Gestión Plazas (modo ADMIN_GESTION → columna Acciones)
        panelGestionPlazas = new EstadoParkingPanel(controller, EstadoParkingPanel.Modo.ADMIN_GESTION);
        panelGestionPlazas.setAccionFilaListener(new EstadoParkingPanel.AccionFilaListener() {
            @Override
            public void onEditar(String codigoPlaza) {
                abrirEditarPlaza(codigoPlaza);
            }

            @Override
            public void onEliminar(String codigoPlaza) {
                confirmarEliminarPlaza(codigoPlaza);
            }

            @Override
            public void onClickFila(String codigoPlaza) {
                abrirDetallePlaza(codigoPlaza);
            }
        });

        // Panel Estado Parking (modo ADMIN_ESTADO → columnas iguales que usuario, click abre detalle)
        panelEstadoAdmin = new EstadoParkingPanel(controller, EstadoParkingPanel.Modo.ADMIN_ESTADO);
        panelEstadoAdmin.setAccionFilaListener(new EstadoParkingPanel.AccionFilaListener() {
            @Override public void onEditar(String codigoPlaza) {}
            @Override public void onEliminar(String codigoPlaza) {}
            @Override public void onClickFila(String codigoPlaza) {
                abrirDetallePlaza(codigoPlaza);
            }
        });

        contentPanel.add(panelGestionPlazas, GESTION_PLAZAS);
        contentPanel.add(crearPanelPlaceholder("Gestión de reservas"), GESTION_RESERVAS);
        contentPanel.add(panelEstadoAdmin, ESTADO);
        contentPanel.add(new GraficoOcupacionPanel(), GRAFICO);

        add(contentPanel, BorderLayout.CENTER);
        mostrarVista(GESTION_PLAZAS);
    }

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

        JLabel lblBadgeAdmin = new JLabel("ADMINISTRADOR");
        lblBadgeAdmin.setForeground(new Color(52, 152, 219));
        lblBadgeAdmin.setFont(new Font("Inter", Font.BOLD, 12));
        lblBadgeAdmin.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSeccionGestion = crearEtiquetaSeccion("GESTIÓN");
        JButton btnGestionPlazas = crearBotonMenu("Gestión Plazas");
        JButton btnGestionReservas = crearBotonMenu("Gestión Reservas");

        JLabel lblSeccionVisualizacion = crearEtiquetaSeccion("VISUALIZACIÓN");
        JButton btnEstado = crearBotonMenu("Estado Parking");
        JButton btnGrafico = crearBotonMenu("Gráfico");

        JButton btnCerrarSesion = crearBotonMenu("Cerrar Sesión");

        btnGestionPlazas.addActionListener(e -> mostrarVista(GESTION_PLAZAS));
        btnGestionReservas.addActionListener(e -> mostrarVista(GESTION_RESERVAS));
        btnEstado.addActionListener(e -> {
            panelEstadoAdmin.mostrarPlazas();
            mostrarVista(ESTADO);
        });
        btnGrafico.addActionListener(e -> mostrarVista(GRAFICO));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(lblBadgeAdmin);
        sidebar.add(Box.createVerticalStrut(25));
        sidebar.add(lblSeccionGestion);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnGestionPlazas);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnGestionReservas);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(lblSeccionVisualizacion);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnEstado);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnGrafico);
        sidebar.add(Box.createVerticalGlue());
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

        JLabel lblTitulo = new JLabel("Panel Administrador");
        lblTitulo.setFont(new Font("Inter", Font.BOLD, 22));

        JButton btnNuevaPlaza = new JButton("+ Nueva Plaza");
        btnNuevaPlaza.setBackground(new Color(52, 152, 219));
        btnNuevaPlaza.setForeground(Color.WHITE);
        btnNuevaPlaza.setFocusPainted(false);
        btnNuevaPlaza.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnNuevaPlaza.addActionListener(e -> abrirNuevaPlaza());

        topbar.add(lblTitulo, BorderLayout.WEST);
        topbar.add(btnNuevaPlaza, BorderLayout.EAST);

        return topbar;
    }

    private JPanel crearPanelPlaceholder(String texto) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(245, 247, 250));
        JLabel lbl = new JLabel(texto + " (pendiente)");
        lbl.setFont(new Font("Inter", Font.PLAIN, 18));
        lbl.setForeground(Color.GRAY);
        p.add(lbl);
        return p;
    }

    public void mostrarVista(String nombreVista) {
        cardLayout.show(contentPanel, nombreVista);
    }

    private void abrirNuevaPlaza() {
        PlazaFormDialog dialog = PlazaFormDialog.paraNueva(this);
        dialog.setVisible(true);
        if (dialog.fueConfirmado()) {
            // controller.crearPlaza(dialog.getCodigo(), dialog.getPiso(), dialog.getTipoVehiculo());
            panelGestionPlazas.mostrarPlazas();
        }
    }

    private void abrirEditarPlaza(String codigoPlaza) {
        // Pides al controller los datos actuales:
        // String[] datos = controller.obtenerDatosPlaza(codigoPlaza);
        // PlazaFormDialog dialog = PlazaFormDialog.paraEditar(this, datos[0], datos[1], datos[2]);
        PlazaFormDialog dialog = PlazaFormDialog.paraEditar(this, codigoPlaza, "1", "Coche");
        dialog.setVisible(true);
        if (dialog.fueConfirmado()) {
            // controller.editarPlaza(...);
            panelGestionPlazas.mostrarPlazas();
        }
    }

    private void confirmarEliminarPlaza(String codigoPlaza) {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la plaza " + codigoPlaza + "?\n" +
                        "Si tiene reserva, se intentará reasignar al usuario.",
                "Eliminar plaza",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            // TODO: controller.eliminarPlaza(codigoPlaza);
            panelGestionPlazas.mostrarPlazas();
        }
    }

    private void abrirDetallePlaza(String codigoPlaza) {
        // Datos reales del controller:
        // DetallePlazaInfo info = controller.obtenerDetallePlaza(codigoPlaza);
        DetallePlazaDialog dialog = new DetallePlazaDialog(
                this, codigoPlaza, "1", "Coche", "Libre", "Reservada",
                "1234 XYZ", "joan.doe", "joan.doe@email.com"
        );
        dialog.setCancelarReservaListener(codigo -> {
            // controller.cancelarReservaPorAdmin(codigo);
            panelGestionPlazas.mostrarPlazas();
            panelEstadoAdmin.mostrarPlazas();
        });
        dialog.setVisible(true);
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
            new AuthFrame().setVisible(true);
        }
    }
}