package Presentacion.Vistas.Panels;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Vista pasiva de "Mis reservas". No conoce servicios ni el patrón Observer.
 * El controlador la alimenta vía {@link #setReservas} y se suscribe con
 * {@link #setCancelarReservaListener} para abrir el diálogo de cancelación.
 */
public class MisReservasPanel extends JPanel {

    private JPanel panelLista;
    private JLabel lblResumen;

    private final Consumer<String> navigator;
    private Consumer<ReservaVista> cancelarReservaListener;

    public MisReservasPanel(Consumer<String> navigator) {
        this.navigator = navigator;
        configurarPanel();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(crearCabecera(), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
        add(crearResumen(), BorderLayout.SOUTH);
    }

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

    private JScrollPane crearCentro() {
        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        return new JScrollPane(panelLista);
    }

    private JPanel crearResumen() {
        JPanel resumen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblResumen = new JLabel("0 reservas");
        resumen.add(lblResumen);
        return resumen;
    }

    /** Listener invocado al pulsar "Cancelar" en una reserva activa. */
    public void setCancelarReservaListener(Consumer<ReservaVista> listener) {
        this.cancelarReservaListener = listener;
    }

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

    private JPanel crearTarjetaReserva(ReservaVista reserva) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel lblPlaza = new JLabel("Plaza " + reserva.codigoPlaza() + " - " + reserva.tipoVehiculo());
        lblPlaza.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel lblMatricula = new JLabel("Matrícula: " + reserva.matricula());
        JLabel lblFecha = new JLabel("Reservada el: " + reserva.fechaReserva());
        JLabel lblPiso = new JLabel("Planta " + reserva.planta());

        info.add(lblPlaza);
        info.add(Box.createVerticalStrut(6));
        info.add(lblMatricula);
        info.add(lblFecha);
        info.add(lblPiso);

        JLabel estado = new JLabel(reserva.activa() ? "Activa" : "Cancelada");
        estado.setForeground(reserva.activa() ? new Color(46, 125, 50) : new Color(198, 40, 40));

        JPanel derecha = new JPanel();
        derecha.setLayout(new BoxLayout(derecha, BoxLayout.Y_AXIS));
        derecha.add(estado);
        derecha.add(Box.createVerticalStrut(8));
        if (reserva.activa()) {
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.addActionListener(e -> {
                if (cancelarReservaListener != null) {
                    cancelarReservaListener.accept(reserva);
                }
            });
            derecha.add(btnCancelar);
        }

        card.add(info, BorderLayout.CENTER);
        card.add(derecha, BorderLayout.EAST);

        return card;
    }

    public record ReservaVista(String codigoPlaza, String tipoVehiculo, String matricula, String fechaReserva, String planta, boolean activa) {}
}
