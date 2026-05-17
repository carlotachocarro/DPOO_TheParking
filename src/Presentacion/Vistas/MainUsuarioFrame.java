package Presentacion.Vistas;

import Negocio.Excepciones.ExcepcionDatosIncorrectos;
import Negocio.Excepciones.ExcepcionFicheroConfig;
import Negocio.Excepciones.ExcepcionNegocio;
import Presentacion.Controladores.*;
import Presentacion.Vistas.Panels.*;

import javax.swing.*;
import java.awt.*;

public class MainUsuarioFrame extends MainBaseFrame {

    private final String nombreUsuario;
    /** id_usuario en base de datos (FK plaza_parking); resuelto en login, no se recalcula por discrepancias de texto */
    private final String idUsuarioSesion;

    public MainUsuarioFrame(String nombreUsuario, String idUsuarioSesion, ControllerMenuPrincipalAdmin controller) {
        super(controller);
        this.nombreUsuario = nombreUsuario;
        this.idUsuarioSesion = idUsuarioSesion;
        inicializarComponentes();
    }

    @Override protected String getTitulo()       { return "The Parking - Panel Usuario"; }
    @Override protected String getTituloTopbar() { return "Panel principal"; }

    @Override
    protected void crearPaneles() throws ExcepcionNegocio {
        EstadoParkingPanel estadoPanel = new EstadoParkingPanel(EstadoParkingPanel.Modo.USUARIO);
        ControladorEstadoParking ctrlEstado = new ControladorEstadoParking(estadoPanel, controller.getServicioPlaza());
        ctrlEstado.cargarInicial();
        contentPanel.add(estadoPanel, "ESTADO");

        ReservasPlazaPanel reservasPanel = new ReservasPlazaPanel();
        new ControladorReservasPlaza(reservasPanel, nombreUsuario, controller.getServicioReserva());
        contentPanel.add(reservasPanel, "RESERVAR");

        EntradaSalidaPanel entradaSalida = new EntradaSalidaPanel();
        new ControladorEntradasSalidas(entradaSalida, idUsuarioSesion,
                controller.getServicioPlaza(), controller.getServicioVehiculo());
        contentPanel.add(entradaSalida, "ENTRADA_SALIDA");

        MisReservasPanel misReservasPanel = new MisReservasPanel(this::mostrarVista);
        ControladorMisReservas ctrlMis = new ControladorMisReservas(
                misReservasPanel, nombreUsuario,
                controller.getServicioPlaza(), controller.getServicioReserva());
        ctrlMis.cargarInicial();
        contentPanel.add(misReservasPanel, "MIS_RESERVAS");

        contentPanel.add(new GraficoOcupacionPanel(controller), "GRAFICO");

        mostrarVista("ESTADO");
    }

    @Override
    protected JPanel crearSidebar() {
        JPanel sidebar = crearSidebarBase("USUARIO");

        JLabel lblSeccionApp = crearEtiquetaSeccion("APLICACIÓN");
        JButton btnEstado        = crearBotonMenu("Estado Parking");
        JButton btnEntradaSalida = crearBotonMenu("Entrada / Salida");
        JButton btnReservar      = crearBotonMenu("Reservar Plaza");
        JButton btnMisReservas   = crearBotonMenu("Mis Reservas");
        JButton btnGrafico       = crearBotonMenu("Gráfico");

        JLabel lblSeccionCuenta  = crearEtiquetaSeccion("CUENTA");
        JButton btnEliminarCuenta = crearBotonMenu("Eliminar cuenta");
        JButton btnCerrarSesion   = crearBotonMenu("Cerrar Sesión");

        btnEstado.addActionListener(e        -> mostrarVista("ESTADO"));
        btnEntradaSalida.addActionListener(e -> mostrarVista("ENTRADA_SALIDA"));
        btnReservar.addActionListener(e      -> mostrarVista("RESERVAR"));
        btnMisReservas.addActionListener(e   -> mostrarVista("MIS_RESERVAS"));
        btnGrafico.addActionListener(e       -> mostrarVista("GRAFICO"));
        btnEliminarCuenta.addActionListener(e -> eliminarCuenta());
        btnCerrarSesion.addActionListener(e   -> cerrarSesion());

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

    private void eliminarCuenta() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Seguro que quieres eliminar tu cuenta?",
                "Eliminar cuenta",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (controller.eliminarCuenta(nombreUsuario)) {
                JOptionPane.showMessageDialog(this, "Cuenta eliminada correctamente.");
                controller.detenerTimersSecundarios();
                dispose();
                ControladorAplicacion.reiniciarFlujoAutenticacion();
            } else {
                JOptionPane.showMessageDialog(this, "La cuenta no se ha podido eliminar.");
            }
        } catch (ExcepcionDatosIncorrectos ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMensajeExcepcion(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (ExcepcionFicheroConfig ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMensajeExcepcion(),
                    "Error de configuración",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}