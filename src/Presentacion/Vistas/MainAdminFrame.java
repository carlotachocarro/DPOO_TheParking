package Presentacion.Vistas;

import Negocio.Excepciones.ExcepcionNegocio;
import Presentacion.Controladores.*;
import Presentacion.Vistas.Panels.*;

import javax.swing.*;
import java.awt.*;

public class MainAdminFrame extends MainBaseFrame {

    private EstadoParkingPanel panelGestionPlazas;
    private EstadoParkingPanel panelEstadoAdmin;

    private ControladorEstadoParking ctrlGestion;
    private ControladorEstadoParking ctrlEstado;
    private ControladorPOPAP_DetallePlaza ctrlDetalle;

    private static final String GESTION_PLAZAS   = "GESTION_PLAZAS";
    private static final String GESTION_RESERVAS = "GESTION_RESERVAS";
    private static final String ESTADO           = "ESTADO";
    private static final String GRAFICO          = "GRAFICO";

    public MainAdminFrame(ControllerMenuPrincipalAdmin controller) {
        super(controller);
        inicializarComponentes();
    }

    @Override protected String getTitulo()       { return "The Parking - Panel Administrador"; }
    @Override protected String getTituloTopbar() { return "Panel Administrador"; }

    @Override
    protected JComponent crearComponenteDerechoTopbar() {
        JButton btnNuevaPlaza = new JButton("+ Nueva Plaza");
        btnNuevaPlaza.setBackground(new Color(52, 152, 219));
        btnNuevaPlaza.setForeground(Color.WHITE);
        btnNuevaPlaza.setFocusPainted(false);
        btnNuevaPlaza.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btnNuevaPlaza.addActionListener(e -> abrirNuevaPlaza());
        return btnNuevaPlaza;
    }

    @Override
    protected void crearPaneles() throws ExcepcionNegocio {
        ctrlDetalle = new ControladorPOPAP_DetallePlaza(
                this,
                controller.getServicioPlaza(),
                controller.getServicioReserva(),
                this::recargarPanelesEstado);

        panelGestionPlazas = new EstadoParkingPanel(EstadoParkingPanel.Modo.ADMIN_GESTION);
        panelGestionPlazas.setAccionFilaListener(new EstadoParkingPanel.AccionFilaListener() {
            @Override public void onEditar(String codigo)   { abrirEditarPlaza(codigo); }
            @Override public void onEliminar(String codigo) { confirmarEliminarPlaza(codigo); }
            @Override public void onClickFila(String codigo) { ctrlDetalle.abrirDialogo(codigo); }
        });
        ctrlGestion = new ControladorEstadoParking(panelGestionPlazas, controller.getServicioPlaza());
        ctrlGestion.cargarInicial();

        panelEstadoAdmin = new EstadoParkingPanel(EstadoParkingPanel.Modo.ADMIN_ESTADO);
        panelEstadoAdmin.setAccionFilaListener(new EstadoParkingPanel.AccionFilaListener() {
            @Override public void onEditar(String codigo)   {}
            @Override public void onEliminar(String codigo) {}
            @Override public void onClickFila(String codigo) { ctrlDetalle.abrirDialogo(codigo); }
        });
        ctrlEstado = new ControladorEstadoParking(panelEstadoAdmin, controller.getServicioPlaza());
        ctrlEstado.cargarInicial();

        contentPanel.add(panelGestionPlazas, GESTION_PLAZAS);
        contentPanel.add(crearPanelPlaceholder("Gestión de reservas"), GESTION_RESERVAS);
        contentPanel.add(panelEstadoAdmin, ESTADO);
        contentPanel.add(new GraficoOcupacionPanel(controller), GRAFICO);

        mostrarVista(GESTION_PLAZAS);
    }

    @Override
    protected JPanel crearSidebar() {
        JPanel sidebar = crearSidebarBase("ADMINISTRADOR");

        JLabel lblSeccionGestion       = crearEtiquetaSeccion("GESTIÓN");
        JButton btnGestionPlazas       = crearBotonMenu("Gestión Plazas");
        JButton btnGestionReservas     = crearBotonMenu("Gestión Reservas");

        JLabel lblSeccionVisualizacion = crearEtiquetaSeccion("VISUALIZACIÓN");
        JButton btnEstado              = crearBotonMenu("Estado Parking");
        JButton btnGrafico             = crearBotonMenu("Gráfico");

        JLabel lblSeccionSimulacion    = crearEtiquetaSeccion("SIMULACIÓN");
        JButton btnSimulacion          = crearBotonMenu(textoBotonSimulacion());

        JButton btnCerrarSesion        = crearBotonMenu("Cerrar Sesión");

        btnGestionPlazas.addActionListener(e   -> mostrarVista(GESTION_PLAZAS));
        btnGestionReservas.addActionListener(e -> mostrarVista(GESTION_RESERVAS));
        btnEstado.addActionListener(e          -> mostrarVista(ESTADO));
        btnGrafico.addActionListener(e         -> mostrarVista(GRAFICO));
        btnSimulacion.addActionListener(e      -> alternarSimulacion(btnSimulacion));
        btnCerrarSesion.addActionListener(e    -> cerrarSesion());

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
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(lblSeccionSimulacion);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnSimulacion);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnCerrarSesion);

        return sidebar;
    }

    private String textoBotonSimulacion() {
        return controller.simuladorActivo() ? "Detener simulación" : "Iniciar simulación";
    }

    private void alternarSimulacion(JButton boton) {
        if (controller.simuladorActivo()) {
            controller.detenerSimulador();
        } else {
            controller.iniciarSimulador();
        }
        boton.setText(textoBotonSimulacion());
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

    private void abrirNuevaPlaza() {
        new ControladorPOPAP_NuevaPlaza(
                this,
                controller.getServicioPlaza(),
                this::recargarPanelesEstado
        ).abrirDialogo();
    }

    private void abrirEditarPlaza(String codigoPlaza) {
        new ControladorPOPAP_EditarPlaza(
                this,
                controller.getServicioPlaza(),
                this::recargarPanelesEstado
        ).abrirDialogo(codigoPlaza);
    }

    private void confirmarEliminarPlaza(String codigoPlaza) {
        new ControladorPOPAP_EliminarPlaza(
                this,
                controller.getServicioPlaza(),
                this::recargarPanelesEstado
        ).confirmarYEliminar(codigoPlaza);
    }

    /**
     * Tras una operación de admin sobre plazas (crear/editar/eliminar/cancelar reserva) refrescamos
     * los paneles. Aunque el servicio también notifica observers en muchos flujos, ejecutar la
     * carga inicial garantiza coherencia inmediata.
     */
    private void recargarPanelesEstado() {
        try {
            ctrlGestion.cargarInicial();
            ctrlEstado.cargarInicial();
        } catch (ExcepcionNegocio e) {
            JOptionPane.showMessageDialog(this,
                    e.getMensajeExcepcion(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
