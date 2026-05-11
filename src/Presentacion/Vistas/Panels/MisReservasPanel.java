package Presentacion.Vistas.Panels;

import Negocio.Servicios.ParkingObserver;
import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioReserva;
import Presentacion.Controladores.ControladorPOPAP_CancelarReserva;
import Presentacion.Vistas.Dialogs.CancelarReservaDialog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MisReservasPanel extends JPanel
        implements ParkingObserver {

    // =========================
    // ATRIBUTOS
    // =========================

    private JPanel panelLista;
    private JLabel lblResumen;

    private final Consumer<String> navigator;

    private final String nombreUsuario;

    private final ServicioReserva servicioReserva;
    private final ServicioPlaza servicioPlaza;

    // =========================
    // CONSTRUCTOR
    // =========================

    public MisReservasPanel(Consumer<String> navigator, String nombreUsuario, ServicioPlaza servicioPlaza) {

        this.navigator = navigator;
        this.nombreUsuario = nombreUsuario;

        this.servicioPlaza = servicioPlaza;
        this.servicioReserva = new ServicioReserva(servicioPlaza);

        configurarPanel();

        servicioPlaza.addObserver(this);

        actualizarReservas();
    }

    // =========================
    // CONFIGURACIÓN
    // =========================

    private void configurarPanel() {

        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(crearCabecera(), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
        add(crearResumen(), BorderLayout.SOUTH);
    }

    // =========================
    // CABECERA
    // =========================

    private JPanel crearCabecera() {

        JPanel cabecera = new JPanel(new BorderLayout());

        JPanel textos = new JPanel();

        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Mis reservas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Consulta y cancela tus reservas activas");

        subtitulo.setForeground(Color.GRAY);

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(subtitulo);

        JButton btnNuevaReserva = new JButton("Nueva reserva");

        btnNuevaReserva.addActionListener(e -> navigator.accept("RESERVAR"));

        cabecera.add(textos, BorderLayout.WEST);
        cabecera.add(btnNuevaReserva, BorderLayout.EAST);

        return cabecera;
    }

    // =========================
    // CENTRO
    // =========================

    private JScrollPane crearCentro() {

        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));

        return new JScrollPane(panelLista);
    }

    // =========================
    // RESUMEN
    // =========================

    private JPanel crearResumen() {

        JPanel resumen = new JPanel(new FlowLayout(FlowLayout.LEFT));

        lblResumen = new JLabel("0 reservas");

        resumen.add(lblResumen);

        return resumen;
    }

    // =========================
    // ACTUALIZAR RESERVAS
    // =========================

    public void actualizarReservas() {

        List<String> reserv = servicioReserva.ObtenerReservas(nombreUsuario);

        List<ReservaVista> reservas = new ArrayList<>();
        for (String r : reserv) {

            String[] partes = r.split("-");

            if (partes.length < 6) {
                continue;
            }
            reservas.add(new ReservaVista(partes[0], partes[1], partes[2], partes[3], partes[4], Boolean.parseBoolean(partes[5])));
        }

        setReservas(reservas);
    }

    // =========================
    // PINTAR RESERVAS
    // =========================

    public void setReservas(List<ReservaVista> reservas) {

        panelLista.removeAll();

        int activas = 0;
        int canceladas = 0;

        for (ReservaVista reserva : reservas) {

            if (reserva.activa()) {
                activas++;
            } else {
                canceladas++;
            }
            panelLista.add(crearTarjetaReserva(reserva));

            panelLista.add(Box.createVerticalStrut(12));
        }

        lblResumen.setText(activas + " reservas activas, " + canceladas + " canceladas");

        panelLista.revalidate();
        panelLista.repaint();
    }

    // =========================
    // TARJETA RESERVA
    // =========================

    private JPanel crearTarjetaReserva(ReservaVista reserva) {

        JPanel card = new JPanel(new BorderLayout());

        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)), BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        JPanel info = new JPanel();

        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel lblPlaza = new JLabel("Plaza " + reserva.codigoPlaza() + " - " + reserva.tipoVehiculo());

        lblPlaza.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel lblMatricula = new JLabel("Matrícula: " + reserva.matricula());

        JLabel lblFecha = new JLabel("Reservada el: " + reserva.fechaReserva());

        JLabel lblPiso = new JLabel("Planta " + reserva.planta());

        info.add(lblPlaza);info.add(Box.createVerticalStrut(6));info.add(lblMatricula);info.add(lblFecha);info.add(lblPiso);

        JLabel estado = new JLabel(reserva.activa() ? "Activa" : "Cancelada");
        estado.setForeground(reserva.activa() ? new Color(46, 125, 50) : new Color(198, 40, 40));

        JPanel derecha = new JPanel();
        derecha.setLayout(new BoxLayout(derecha, BoxLayout.Y_AXIS));
        derecha.add(estado);
        derecha.add(Box.createVerticalStrut(8));
        if (reserva.activa()) {

            JButton btnCancelar = new JButton("Cancelar");

            btnCancelar.addActionListener(
                    e -> abrirDialogoCancelar(reserva)
            );

            derecha.add(btnCancelar);
        }

        card.add(info, BorderLayout.CENTER);
        card.add(derecha, BorderLayout.EAST);

        return card;
    }

    // =========================
    // DIALOG CANCELAR
    // =========================

    private void abrirDialogoCancelar(ReservaVista reserva) {
        Window padre = SwingUtilities.getWindowAncestor(this);

        CancelarReservaDialog dlg = new CancelarReservaDialog(padre, reserva.codigoPlaza(), reserva.planta(), reserva.matricula(), reserva.fechaReserva(), reserva.tipoVehiculo());

        new ControladorPOPAP_CancelarReserva(dlg, nombreUsuario,servicioPlaza);

        dlg.setVisible(true);
    }

    // =========================
    // OBSERVER
    // =========================

    @Override
    public void onParkingChange(String estado, String resumen) {

        SwingUtilities.invokeLater(this::actualizarReservas);
    }

    // =========================
    // RECORD
    // =========================

    public record ReservaVista(String codigoPlaza, String tipoVehiculo, String matricula, String fechaReserva, String planta, boolean activa) {
    }
}