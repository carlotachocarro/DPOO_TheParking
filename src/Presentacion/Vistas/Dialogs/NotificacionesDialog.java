package Presentacion.Vistas.Dialogs;

import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Notificación post-login (cancelación admin + opcional reasignación).
 * Misma línea visual que {@link DetallePlazaDialog}: tarjetas con lateral de color y tipografía Inter / SpaceGrotesk.
 */
public class NotificacionesDialog extends BaseDialog {

    public record AvisoReservaCancelada(
            String codigoPlazaCancelada,
            String plantaCancelada,
            String matricula,
            String tipoVehiculo,
            String codigoPlazaNueva,
            String plantaNueva
    ) {}

    public NotificacionesDialog(Window parent, List<AvisoReservaCancelada> avisos) {
        super(parent, "Aviso importante");

        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(DialogStyles.BG_CONTENT);
        JSeparator s1 = new JSeparator();
        s1.setForeground(DialogStyles.BORDER_SEPARATOR);
        pie.add(s1, BorderLayout.NORTH);
        pie.add(crearBotonEntendido(), BorderLayout.CENTER);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(DialogStyles.BG_CONTENT);
        contenido.add(crearSubcabecera(), BorderLayout.NORTH);
        contenido.add(crearListaAvisos(avisos), BorderLayout.CENTER);
        contenido.add(pie, BorderLayout.SOUTH);

        add(contenido, BorderLayout.CENTER);

        setMinimumSize(new Dimension(540, 0));
        centrarRespectoPadre();
    }

    private JPanel crearSubcabecera() {
        JPanel sub = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        sub.setBackground(DialogStyles.WARNING_SURFACE);
        sub.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));

        JLabel lblIcono = new JLabel("🔔");
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 22));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        JLabel lblTituloMini = new JLabel("Aviso importante");
        lblTituloMini.setFont(DialogStyles.FONT_SECTION);
        lblTituloMini.setForeground(DialogStyles.WARNING_TEXT_DIM);

        JLabel lblTexto = new JLabel("El administrador ha cancelado una reserva");
        lblTexto.setFont(DialogStyles.FONT_BODY);
        lblTexto.setForeground(new Color(180, 140, 0));

        textos.add(lblTituloMini);
        textos.add(Box.createVerticalStrut(2));
        textos.add(lblTexto);

        sub.add(lblIcono);
        sub.add(textos);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(DialogStyles.BG_CONTENT);
        wrap.add(sub, BorderLayout.CENTER);
        JSeparator sep = new JSeparator();
        sep.setForeground(DialogStyles.BORDER_SEPARATOR);
        wrap.add(sep, BorderLayout.SOUTH);
        return wrap;
    }

    private JPanel crearListaAvisos(List<AvisoReservaCancelada> avisos) {
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(DialogStyles.BG_CONTENT);
        lista.setBorder(BorderFactory.createEmptyBorder(18, 22, 14, 22));

        for (AvisoReservaCancelada aviso : avisos) {
            lista.add(crearTarjetaCancelada(aviso));
            lista.add(Box.createVerticalStrut(14));

            if (aviso.codigoPlazaNueva() != null) {
                lista.add(crearFlechaTransicion());
                lista.add(Box.createVerticalStrut(14));
                lista.add(crearTarjetaReasignada(aviso));
                lista.add(Box.createVerticalStrut(14));
            }
        }

        lista.add(Box.createVerticalStrut(4));
        lista.add(crearPanelNotaPie());

        return lista;
    }

    /** Cuadrado azul compacto como en los mockups (transición cancelado → nueva plaza). */
    private JPanel crearFlechaTransicion() {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        fila.setOpaque(false);

        JPanel cuadro = new JPanel(new GridBagLayout());
        cuadro.setOpaque(true);
        cuadro.setBackground(DialogStyles.ACCENT_BLUE);
        cuadro.setPreferredSize(new Dimension(36, 36));
        cuadro.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JLabel lbl = new JLabel("▼", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        cuadro.add(lbl);

        fila.add(cuadro);
        return fila;
    }

    private JPanel crearTarjetaCancelada(AvisoReservaCancelada aviso) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(new Color(253, 240, 240));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, DialogStyles.DANGER),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSeccion = new JLabel("RESERVA CANCELADA");
        lblSeccion.setFont(DialogStyles.FONT_SECTION);
        lblSeccion.setForeground(DialogStyles.TEXT_MUTED);

        JLabel lblPlaza = new JLabel("Plaza " + aviso.codigoPlazaCancelada()
                + "  ·  Planta " + aviso.plantaCancelada());
        lblPlaza.setFont(DialogStyles.FONT_EMPHASIS);
        lblPlaza.setForeground(DialogStyles.TEXT_PRIMARY);

        JLabel lblDatos = new JLabel("Matrícula: " + aviso.matricula()
                + "  ·  Tipo: " + aviso.tipoVehiculo());
        lblDatos.setForeground(DialogStyles.TEXT_MUTED);
        lblDatos.setFont(DialogStyles.FONT_BODY_SMALL);

        JLabel lblBadge = new JLabel("Cancelada por admin");
        lblBadge.setForeground(DialogStyles.DANGER);
        lblBadge.setFont(DialogStyles.FONT_BODY_SMALL.deriveFont(Font.BOLD));

        tarjeta.add(lblSeccion);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblPlaza);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblDatos);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblBadge);

        return tarjeta;
    }

    private JPanel crearTarjetaReasignada(AvisoReservaCancelada aviso) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(new Color(232, 245, 233));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, DialogStyles.SUCCESS),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSeccion = new JLabel("NUEVA PLAZA ASIGNADA");
        lblSeccion.setFont(DialogStyles.FONT_SECTION);
        lblSeccion.setForeground(DialogStyles.TEXT_MUTED);

        JLabel lblPlaza = new JLabel("Plaza " + aviso.codigoPlazaNueva()
                + "  ·  Planta " + aviso.plantaNueva());
        lblPlaza.setFont(DialogStyles.FONT_EMPHASIS);
        lblPlaza.setForeground(DialogStyles.TEXT_PRIMARY);

        JLabel lblDatos = new JLabel("Matrícula: " + aviso.matricula()
                + "  ·  Tipo: " + aviso.tipoVehiculo());
        lblDatos.setForeground(DialogStyles.TEXT_MUTED);
        lblDatos.setFont(DialogStyles.FONT_BODY_SMALL);

        JLabel lblBadge = new JLabel("Reasignada automáticamente");
        lblBadge.setForeground(DialogStyles.SUCCESS);
        lblBadge.setFont(DialogStyles.FONT_BODY_SMALL.deriveFont(Font.BOLD));

        tarjeta.add(lblSeccion);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblPlaza);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblDatos);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblBadge);

        return tarjeta;
    }

    private JPanel crearPanelNotaPie() {
        BasicStroke dashStroke = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                10f, new float[]{4f, 4f}, 0f);
        JPanel env = new JPanel(new BorderLayout());
        env.setOpaque(true);
        env.setBackground(new Color(248, 252, 255));
        env.setAlignmentX(Component.LEFT_ALIGNMENT);
        env.setBorder(BorderFactory.createCompoundBorder(
                new StrokeBorder(dashStroke, DialogStyles.ACCENT_BLUE),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        JLabel nota = new JLabel("<html>Este aviso solo se muestra una vez al iniciar sesión.</html>");
        nota.setForeground(DialogStyles.TEXT_MUTED);
        nota.setFont(DialogStyles.FONT_BODY_SMALL.deriveFont(Font.ITALIC));
        env.add(nota, BorderLayout.CENTER);
        return env;
    }

    private JPanel crearBotonEntendido() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DialogStyles.BG_CONTENT);
        panel.setBorder(BorderFactory.createEmptyBorder(4, 20, 14, 20));

        JButton btn = DialogStyles.botonPrimario("Entendido", DialogStyles.ACCENT_BLUE, Color.WHITE);
        btn.addActionListener((ActionEvent e) -> dispose());
        panel.add(btn, BorderLayout.CENTER);
        return panel;
    }
}
