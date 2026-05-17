package Presentacion.Vistas.Dialogs;

import javax.swing.*;
import java.awt.*;

public class DetallePlazaDialog extends BaseDialog {

    public interface CancelarReservaListener {
        void onCancelarReserva(String codigoPlaza);
    }

    private final String codigoPlaza;
    private final String piso;
    private final String tipoVehiculo;
    private final String estado;       // "Libre" | "Ocupada"
    private final String reserva;      // "Disponible" | "Reservada"
    private final String matriculaReserva;  // null si no hay reserva
    private final String nombreUsuarioReserva; // null si no hay reserva
    private final String correoUsuarioReserva; // null si no hay reserva

    private CancelarReservaListener listener;

    public DetallePlazaDialog(Window parent,
                              String codigoPlaza,
                              String piso,
                              String tipoVehiculo,
                              String estado,
                              String reserva,
                              String matriculaReserva,
                              String nombreUsuarioReserva,
                              String correoUsuarioReserva) {
        super(parent, "Detalle de plaza");
        this.codigoPlaza = codigoPlaza;
        this.piso = piso;
        this.tipoVehiculo = tipoVehiculo;
        this.estado = estado;
        this.reserva = reserva;
        this.matriculaReserva = matriculaReserva;
        this.nombreUsuarioReserva = nombreUsuarioReserva;
        this.correoUsuarioReserva = correoUsuarioReserva;

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.add(crearCentro(), BorderLayout.CENTER);
        if (esReservada()) {
            contenido.add(crearBotonCancelar(), BorderLayout.SOUTH);
        }
        add(contenido, BorderLayout.CENTER);

        setMinimumSize(new Dimension(480, 0));
        centrarRespectoPadre();
    }

    public void setCancelarReservaListener(CancelarReservaListener listener) {
        this.listener = listener;
    }

    private boolean esReservada() {
        return "Reservada".equalsIgnoreCase(reserva);
    }

    private JPanel crearCentro() {
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(DialogStyles.BG_CONTENT);
        centro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Badge código
        centro.add(crearBadgeCodigo());
        centro.add(Box.createVerticalStrut(15));

        // Grid 2x2 con Piso / Tipo / Estado / Reserva
        centro.add(crearGridInfo());

        // Si está reservada, mostrar usuario
        if (esReservada()) {
            centro.add(Box.createVerticalStrut(15));
            centro.add(crearSeparador());
            centro.add(Box.createVerticalStrut(15));
            centro.add(crearPanelUsuarioReserva());
        }

        return centro;
    }

    private JPanel crearBadgeCodigo() {
        JPanel badge = new JPanel(new BorderLayout());
        badge.setBackground(DialogStyles.BG_CARD);
        badge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DialogStyles.ACCENT_BLUE, 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel lblTitulo = new JLabel("Código de plaza");
        lblTitulo.setForeground(DialogStyles.TEXT_MUTED);
        lblTitulo.setFont(DialogStyles.FONT_BADGE_TITLE);

        JLabel lblCodigo = new JLabel(codigoPlaza);
        lblCodigo.setFont(DialogStyles.FONT_PLAZA_HERO);
        lblCodigo.setForeground(DialogStyles.ACCENT_BLUE);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(lblTitulo);
        textos.add(lblCodigo);

        // Icono según tipo (emoji simple)
        String icono;
        switch (tipoVehiculo == null ? "" : tipoVehiculo.toLowerCase()) {
            case "moto": icono = "🏍"; break;
            case "coche grande": icono = "🚙"; break;
            default: icono = "🚗"; break;
        }
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 32));

        badge.add(textos, BorderLayout.WEST);
        badge.add(lblIcono, BorderLayout.EAST);

        return badge;
    }

    private JPanel crearGridInfo() {
        JPanel grid = new JPanel(new GridLayout(2, 2, 10, 10));
        grid.setOpaque(false);

        grid.add(crearCelda("Piso", "Planta " + piso, null, null));
        grid.add(crearCelda("Tipo de vehículo", tipoVehiculo, null, null));

        Color fondoEstado = "Libre".equalsIgnoreCase(estado)
                ? new Color(232, 245, 233) : new Color(253, 232, 232);
        Color textoEstado = "Libre".equalsIgnoreCase(estado)
                ? DialogStyles.SUCCESS : DialogStyles.DANGER;
        grid.add(crearCelda("Estado", estado, fondoEstado, textoEstado));

        Color fondoReserva = esReservada()
                ? DialogStyles.WARNING_SURFACE : new Color(232, 245, 253);
        Color textoReserva = esReservada()
                ? DialogStyles.WARNING_TEXT_DIM : DialogStyles.ACCENT_BLUE;
        grid.add(crearCelda("Reserva", reserva, fondoReserva, textoReserva));

        return grid;
    }

    private JPanel crearCelda(String titulo, String valor, Color fondo, Color colorValor) {
        JPanel celda = new JPanel();
        celda.setLayout(new BoxLayout(celda, BoxLayout.Y_AXIS));
        celda.setBackground(fondo != null ? fondo : DialogStyles.BG_CARD);
        celda.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setForeground(DialogStyles.TEXT_MUTED);
        lblTit.setFont(DialogStyles.FONT_BADGE_TITLE);
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblVal = new JLabel(valor != null ? valor : "-");
        lblVal.setFont(DialogStyles.FONT_EMPHASIS);
        if (colorValor != null) lblVal.setForeground(colorValor);
        lblVal.setAlignmentX(Component.LEFT_ALIGNMENT);

        celda.add(lblTit);
        celda.add(Box.createVerticalStrut(4));
        celda.add(lblVal);

        return celda;
    }

    private JPanel crearPanelUsuarioReserva() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setOpaque(false);

        JLabel lblSeccion = new JLabel("Reservada por:");
        lblSeccion.setFont(DialogStyles.FONT_SECTION);
        lblSeccion.setForeground(DialogStyles.TEXT_MUTED);

        // Avatar circular simple con iniciales
        String iniciales = obtenerIniciales(nombreUsuarioReserva);
        JLabel lblAvatar = new JLabel(iniciales, SwingConstants.CENTER);
        lblAvatar.setOpaque(true);
        lblAvatar.setBackground(new Color(220, 230, 240));
        lblAvatar.setForeground(DialogStyles.ACCENT_BLUE);
        lblAvatar.setFont(DialogStyles.FONT_BODY_SMALL.deriveFont(Font.BOLD));
        lblAvatar.setPreferredSize(new Dimension(36, 36));
        lblAvatar.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 220)));

        JPanel datosUsuario = new JPanel();
        datosUsuario.setLayout(new BoxLayout(datosUsuario, BoxLayout.Y_AXIS));
        datosUsuario.setOpaque(false);
        JLabel lblNombre = new JLabel(nombreUsuarioReserva != null ? nombreUsuarioReserva : "-");
        lblNombre.setFont(new Font("Inter", Font.BOLD, 13));
        JLabel lblCorreo = new JLabel(correoUsuarioReserva != null ? correoUsuarioReserva : "");
        lblCorreo.setFont(new Font("Inter", Font.PLAIN, 12));
        lblCorreo.setForeground(DialogStyles.TEXT_MUTED);
        datosUsuario.add(lblNombre);
        datosUsuario.add(lblCorreo);

        JLabel lblMatricula = new JLabel(matriculaReserva != null ? matriculaReserva : "");
        lblMatricula.setFont(new Font("Inter", Font.BOLD, 13));
        lblMatricula.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DialogStyles.BORDER_FIELD),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        JPanel filaUsuario = new JPanel(new BorderLayout(10, 0));
        filaUsuario.setOpaque(false);
        filaUsuario.add(lblAvatar, BorderLayout.WEST);
        filaUsuario.add(datosUsuario, BorderLayout.CENTER);
        filaUsuario.add(lblMatricula, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout(0, 8));
        wrapper.setOpaque(false);
        wrapper.add(lblSeccion, BorderLayout.NORTH);
        wrapper.add(filaUsuario, BorderLayout.CENTER);

        return wrapper;
    }

    private String obtenerIniciales(String nombre) {
        if (nombre == null || nombre.isEmpty()) return "?";
        String[] partes = nombre.split("[.\\s]+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(2, partes.length); i++) {
            if (!partes[i].isEmpty()) sb.append(Character.toUpperCase(partes[i].charAt(0)));
        }
        return sb.toString();
    }

    private JPanel crearBotonCancelar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        panel.setBackground(DialogStyles.BG_CONTENT);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        JButton btn = DialogStyles.botonPrimario("Cancelar Reserva", DialogStyles.DANGER, Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(11, 32, 11, 32));
        btn.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Cancelar la reserva de la plaza " + codigoPlaza + "?\n" +
                            "El usuario será notificado la próxima vez que inicie sesión.",
                    "Confirmar cancelación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (opcion == JOptionPane.YES_OPTION) {
                if (listener != null) {
                    listener.onCancelarReserva(codigoPlaza);
                }
                dispose();
            }
        });

        panel.add(btn);
        return panel;
    }
}