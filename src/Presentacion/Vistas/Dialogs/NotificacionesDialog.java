package Presentacion.Vistas.Dialogs;

import Negocio.Entidades.AvisoCancelacionUsuario;

import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Aviso al iniciar sesión: cancelación por administrador (+ reasignación opcional).
 * Tema oscuro alineado con la referencia visual; tipografía y botón primario = {@link DialogStyles}.
 */
public class NotificacionesDialog extends JDialog {

    private static final Color DARK_MAIN = new Color(26, 29, 35);
    private static final Color DARK_HEADER = new Color(40, 34, 26);
    private static final Color DARK_SUBHEADER = new Color(55, 42, 28);
    private static final Color DARK_CARD_CANCEL = new Color(42, 30, 30);
    private static final Color DARK_CARD_OK = new Color(28, 42, 32);
    private static final Color DARK_FOOTER_NOTE = new Color(24, 36, 52);
    private static final Color TEXT_ON_DARK = new Color(235, 238, 242);
    private static final Color GOLD_SUBTITLE = new Color(212, 175, 55);
    private static final Color ARROW_BOX = new Color(52, 120, 186);

    public NotificacionesDialog(Window parent, List<AvisoCancelacionUsuario> avisos) {
        super(parent, "Aviso importante", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(DARK_MAIN);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(crearCabeceraOscura(), BorderLayout.NORTH);
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(55, 58, 65));
        sep.setBackground(new Color(55, 58, 65));
        top.add(sep, BorderLayout.SOUTH);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.setBackground(DARK_MAIN);
        centro.add(crearSubcabeceraOscura(), BorderLayout.NORTH);
        centro.add(crearListaAvisos(avisos), BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(crearPieBoton(), BorderLayout.SOUTH);

        setMinimumSize(new Dimension(520, 0));
        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel crearCabeceraOscura() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 16));
        cabecera.setBackground(DARK_HEADER);

        JLabel lblTitulo = new JLabel("Aviso importante");
        lblTitulo.setFont(DialogStyles.FONT_HEADER_DIALOG);
        lblTitulo.setForeground(TEXT_ON_DARK);

        JButton btnCerrar = new JButton("✕");
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCerrar.setForeground(new Color(160, 165, 172));
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());

        cabecera.add(lblTitulo, BorderLayout.WEST);
        cabecera.add(btnCerrar, BorderLayout.EAST);
        return cabecera;
    }

    private JPanel crearSubcabeceraOscura() {
        JPanel sub = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        sub.setBackground(DARK_SUBHEADER);
        sub.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblIcono = new JLabel("🔔");
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 22));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        JLabel lblTituloMini = new JLabel("Aviso importante");
        lblTituloMini.setFont(DialogStyles.FONT_SECTION);
        lblTituloMini.setForeground(new Color(180, 160, 120));

        JLabel lblTexto = new JLabel("El administrador ha cancelado una reserva");
        lblTexto.setFont(DialogStyles.FONT_BODY);
        lblTexto.setForeground(GOLD_SUBTITLE);

        textos.add(lblTituloMini);
        textos.add(Box.createVerticalStrut(2));
        textos.add(lblTexto);

        sub.add(lblIcono);
        sub.add(textos);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBackground(DARK_MAIN);
        wrap.add(sub, BorderLayout.CENTER);
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(55, 58, 65));
        wrap.add(sep, BorderLayout.SOUTH);
        return wrap;
    }

    private JPanel crearListaAvisos(List<AvisoCancelacionUsuario> avisos) {
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(DARK_MAIN);
        lista.setBorder(BorderFactory.createEmptyBorder(14, 20, 10, 20));

        for (AvisoCancelacionUsuario aviso : avisos) {
            lista.add(crearTarjetaCancelada(aviso));
            lista.add(Box.createVerticalStrut(12));

            if (aviso.tieneReasignacion()) {
                lista.add(crearFlechaTransicion());
                lista.add(Box.createVerticalStrut(12));
                lista.add(crearTarjetaReasignada(aviso));
                lista.add(Box.createVerticalStrut(12));
            }
        }

        lista.add(Box.createVerticalStrut(4));
        lista.add(crearPanelNotaPie());

        return lista;
    }

    private JPanel crearFlechaTransicion() {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        fila.setOpaque(false);

        JPanel cuadro = new JPanel(new GridBagLayout());
        cuadro.setOpaque(true);
        cuadro.setBackground(ARROW_BOX);
        cuadro.setPreferredSize(new Dimension(36, 36));
        cuadro.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JLabel lbl = new JLabel("▼", SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        cuadro.add(lbl);

        fila.add(cuadro);
        return fila;
    }

    private JPanel crearTarjetaCancelada(AvisoCancelacionUsuario aviso) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(DARK_CARD_CANCEL);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(229, 60, 60)),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSeccion = new JLabel("RESERVA CANCELADA");
        lblSeccion.setFont(DialogStyles.FONT_SECTION);
        lblSeccion.setForeground(new Color(150, 155, 165));

        JLabel lblPlaza = new JLabel("Plaza " + aviso.codigoPlazaCancelada()
                + "  ·  Planta " + aviso.plantaCancelada());
        lblPlaza.setFont(DialogStyles.FONT_EMPHASIS);
        lblPlaza.setForeground(TEXT_ON_DARK);

        JLabel lblDatos = new JLabel("Matrícula: " + aviso.matricula()
                + "  ·  Tipo: " + aviso.tipoVehiculo());
        lblDatos.setForeground(new Color(170, 175, 185));
        lblDatos.setFont(DialogStyles.FONT_BODY_SMALL);

        JLabel lblBadge = new JLabel("Cancelada por admin");
        lblBadge.setForeground(new Color(255, 112, 112));
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

    private JPanel crearTarjetaReasignada(AvisoCancelacionUsuario aviso) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(DARK_CARD_OK);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(76, 175, 80)),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSeccion = new JLabel("NUEVA PLAZA ASIGNADA");
        lblSeccion.setFont(DialogStyles.FONT_SECTION);
        lblSeccion.setForeground(new Color(150, 155, 165));

        JLabel lblPlaza = new JLabel("Plaza " + aviso.codigoPlazaNueva()
                + "  ·  Planta " + aviso.plantaNueva());
        lblPlaza.setFont(DialogStyles.FONT_EMPHASIS);
        lblPlaza.setForeground(TEXT_ON_DARK);

        JLabel lblDatos = new JLabel("Matrícula: " + aviso.matricula()
                + "  ·  Tipo: " + aviso.tipoVehiculo());
        lblDatos.setForeground(new Color(170, 175, 185));
        lblDatos.setFont(DialogStyles.FONT_BODY_SMALL);

        JLabel lblBadge = new JLabel("Reasignada automáticamente");
        lblBadge.setForeground(new Color(129, 214, 132));
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
        JPanel env = new JPanel(new BorderLayout(8, 0));
        env.setOpaque(true);
        env.setBackground(DARK_FOOTER_NOTE);
        env.setAlignmentX(Component.LEFT_ALIGNMENT);
        env.setBorder(BorderFactory.createCompoundBorder(
                new StrokeBorder(dashStroke, DialogStyles.ACCENT_BLUE),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        JLabel icono = new JLabel("ℹ");
        icono.setForeground(DialogStyles.ACCENT_BLUE);
        icono.setFont(DialogStyles.FONT_BODY_SMALL.deriveFont(Font.BOLD));
        JLabel nota = new JLabel("<html><span style='color:#aeb4bf'>Este aviso solo se muestra una vez al iniciar sesión.</span></html>");
        nota.setFont(DialogStyles.FONT_BODY_SMALL.deriveFont(Font.ITALIC));
        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        izq.setOpaque(false);
        izq.add(icono);
        izq.add(Box.createHorizontalStrut(8));
        izq.add(nota);
        env.add(izq, BorderLayout.CENTER);
        return env;
    }

    private JPanel crearPieBoton() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARK_MAIN);
        panel.setBorder(BorderFactory.createEmptyBorder(4, 20, 16, 20));

        JButton btn = DialogStyles.botonPrimario("Entendido", DialogStyles.ACCENT_BLUE, Color.WHITE);
        btn.addActionListener((ActionEvent e) -> dispose());
        panel.add(btn, BorderLayout.CENTER);
        return panel;
    }
}
