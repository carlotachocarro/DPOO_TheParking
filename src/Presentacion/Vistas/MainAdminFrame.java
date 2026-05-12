package Presentacion.Vistas;

import Negocio.Servicios.ServicioPlaza;
import Presentacion.Controladores.*;
import Presentacion.Vistas.Dialogs.*;
import Presentacion.Vistas.Panels.*;
import Negocio.Entidades.Plaza;
import Negocio.Entidades.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainAdminFrame extends MainBaseFrame {

    private EstadoParkingPanel panelGestionPlazas;
    private EstadoParkingPanel panelEstadoAdmin;

    private static final String GESTION_PLAZAS   = "GESTION_PLAZAS";
    private static final String GESTION_RESERVAS  = "GESTION_RESERVAS";
    private static final String ESTADO            = "ESTADO";
    private static final String GRAFICO           = "GRAFICO";

    public MainAdminFrame(ControllerMenuPrincipalAdmin controller) {
        super(controller);
    }

    @Override protected String getTitulo()       { return "The Parking - Panel Administrador"; }
    @Override protected String getTituloTopbar() { return "Panel Administrador"; }

    // Admin SÍ tiene un botón a la derecha de la topbar
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
    protected void crearPaneles() {
        panelGestionPlazas = new EstadoParkingPanel(controller, EstadoParkingPanel.Modo.ADMIN_GESTION);
        panelGestionPlazas.setAccionFilaListener(new EstadoParkingPanel.AccionFilaListener() {
            @Override public void onEditar(String codigo)   { abrirEditarPlaza(codigo); }
            @Override public void onEliminar(String codigo) { confirmarEliminarPlaza(codigo); }
            @Override public void onClickFila(String codigo){ abrirDetallePlaza(codigo); }
        });

        panelEstadoAdmin = new EstadoParkingPanel(controller, EstadoParkingPanel.Modo.ADMIN_ESTADO);
        panelEstadoAdmin.setAccionFilaListener(new EstadoParkingPanel.AccionFilaListener() {
            @Override public void onEditar(String codigo)   {}
            @Override public void onEliminar(String codigo) {}
            @Override public void onClickFila(String codigo){ abrirDetallePlaza(codigo); }
        });

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
        JButton btnCerrarSesion        = crearBotonMenu("Cerrar Sesión");

        btnGestionPlazas.addActionListener(e   -> mostrarVista(GESTION_PLAZAS));
        btnGestionReservas.addActionListener(e -> mostrarVista(GESTION_RESERVAS));
        btnEstado.addActionListener(e -> {
            panelEstadoAdmin.mostrarPlazas();
            mostrarVista(ESTADO);
        });
        btnGrafico.addActionListener(e      -> mostrarVista(GRAFICO));
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

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

    // ── Lógica específica del admin ───────────────────────────────────────────

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
        ControladorPOPAP_NuevaPlaza controlador = new ControladorPOPAP_NuevaPlaza(
                this,
                controller.getServicioPlaza(),
                () -> panelGestionPlazas.mostrarPlazas()  // callback de refresco
        );
        controlador.abrirDialogo();
    }

    private void abrirEditarPlaza(String codigoPlaza) {
        new ControladorPOPAP_EditarPlaza(
                this,
                controller.getServicioPlaza(),
                () -> panelGestionPlazas.mostrarPlazas()
        ).abrirDialogo(codigoPlaza);
    }

    private void confirmarEliminarPlaza(String codigoPlaza) {
        new ControladorPOPAP_EliminarPlaza(
                this,
                controller.getServicioPlaza(),
                () -> {
                    panelGestionPlazas.mostrarPlazas();
                    panelEstadoAdmin.mostrarPlazas();
                }
        ).confirmarYEliminar(codigoPlaza);
    }

    private void abrirDetallePlaza(String codigoPlaza) {
        Plaza p = controller.getServicioPlaza().obtenerPlazaPorCodigo(codigoPlaza);
        if (p == null) {
            JOptionPane.showMessageDialog(this,
                    "No se encontró la plaza.",
                    "Detalle de plaza",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String estado = p.getEstado_ocupado() ? "Ocupada" : "Libre";
        String reservaTxt = p.getEstado_reserva() ? "Reservada" : "Disponible";
        String mat = p.getMatricula();
        if (mat == null || mat.isBlank() || "none".equalsIgnoreCase(mat)) {
            mat = null;
        }
        Usuario u = p.getUser();
        String nombre = u != null ? u.getNombre() : null;
        String correo = u != null ? u.getCorreoElectronico() : null;

        DetallePlazaDialog dlg = new DetallePlazaDialog(this,
                p.getCodigoPlaza(),
                String.valueOf(p.getPlanta()),
                ServicioPlaza.tipoVehiculoParaCombo(p.getTipoVehiculo()),
                estado,
                reservaTxt,
                mat,
                nombre,
                correo);
        if ("Reservada".equalsIgnoreCase(reservaTxt)) {
            dlg.setCancelarReservaListener(plazaId -> {
                if (controller.getServicioReserva().adminCancelarReservaEnPlaza(plazaId)) {
                    JOptionPane.showMessageDialog(this, "Reserva cancelada.");
                    panelGestionPlazas.mostrarPlazas();
                    panelEstadoAdmin.mostrarPlazas();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo cancelar la reserva.",
                            "Detalle de plaza",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        dlg.setVisible(true);
    }
}