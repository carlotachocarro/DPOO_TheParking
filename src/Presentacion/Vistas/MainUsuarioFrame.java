package Presentacion.Vistas;

import Presentacion.Controladores.*;
import Presentacion.Vistas.Panels.*;

import javax.swing.*;
import java.awt.*;

public class MainUsuarioFrame extends MainBaseFrame {

    private final String nombreUsuario;

    public MainUsuarioFrame(String nombreUsuario, ControllerMenuPrincipalAdmin controller) {
        super(controller);  // llama a configurarVentana + inicializarComponentes
        this.nombreUsuario = nombreUsuario;
    }

    @Override protected String getTitulo()       { return "The Parking - Panel Usuario"; }
    @Override protected String getTituloTopbar() { return "Panel principal"; }

    // El usuario NO tiene componente derecho en la topbar → hereda null por defecto ✓

    @Override
    protected void crearPaneles() {
        contentPanel.add(new EstadoParkingPanel(controller), "ESTADO");

        ReservasPlazaPanel reservasPanel = new ReservasPlazaPanel();
        new ControladorReservasPlaza(reservasPanel, nombreUsuario, controller.getServicioPlaza());
        contentPanel.add(reservasPanel, "RESERVAR");

        EntradaSalidaPanel entradaSalida = new EntradaSalidaPanel();
        new ControladorEntradasSalidas(entradaSalida, nombreUsuario, controller.getServicioPlaza());
        contentPanel.add(entradaSalida, "ENTRADA_SALIDA");

        MisReservasPanel misReservasPanel = new MisReservasPanel(this::mostrarVista, nombreUsuario, controller.getServicioPlaza());
        new ControladorMisReservas(misReservasPanel).getReserva(nombreUsuario);
        contentPanel.add(misReservasPanel, "MIS_RESERVAS");

        contentPanel.add(new GraficoOcupacionPanel(), "GRAFICO");

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
        btnCerrarSesion.addActionListener(e  -> cerrarSesion());

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

        if (opcion == JOptionPane.YES_OPTION && controller.eliminarCuenta(nombreUsuario)) {
            JOptionPane.showMessageDialog(this, "Cuenta eliminada correctamente.");
            dispose();
            ControladorAplicacion.reiniciarFlujoAutenticacion();
        } else {
            JOptionPane.showMessageDialog(this, "La cuenta no se ha podido eliminar.");
        }
    }
}