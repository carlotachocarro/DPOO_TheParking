package Presentacion.Vistas.Dialogs;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.StrokeBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Confirmación de baja de una reserva propia (matrícula obligatoria).
 * Estética alineada con {@link DetallePlazaDialog} y mockups (franja aviso, tarjeta resumen, pie con acciones).
 */
public class CancelarReservaDialog extends BaseDialog {

    private boolean confirmado;
    private JTextField txtMatricula;
    private JButton btnConfirmar;
    private final String matriculaEsperada;

    public CancelarReservaDialog(Window parent,
                                 String codigoPlaza,
                                 String planta,
                                 String matricula,
                                 String fechaReserva,
                                 String tipoVehiculo) {
        super(parent, "Cancelar reserva");
        this.matriculaEsperada = matricula != null ? matricula.trim() : "";

        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setBackground(DialogStyles.BG_CONTENT);
        cuerpo.add(crearFranjaAviso(), BorderLayout.NORTH);
        cuerpo.add(crearCentral(codigoPlaza, planta, matricula, fechaReserva, tipoVehiculo), BorderLayout.CENTER);
        cuerpo.add(crearPie(), BorderLayout.SOUTH);

        add(cuerpo, BorderLayout.CENTER);
        setMinimumSize(new Dimension(460, 0));
        actualizarEstadoConfirmacion();
        centrarRespectoPadre();
    }

    public boolean fueConfirmado() {
        return confirmado;
    }

    private JPanel crearFranjaAviso() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        p.setBackground(DialogStyles.WARNING_SURFACE);
        p.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        JLabel icono = new JLabel("⚠");
        icono.setFont(new Font("SansSerif", Font.BOLD, 24));
        icono.setForeground(DialogStyles.WARNING_TEXT_DIM);

        JLabel texto = new JLabel("<html><div style='width:360px'>"
                + "<span style='font-family:Inter;font-size:14px;font-weight:bold;color:#3d3d3d'>Cancelación de reserva</span><br/>"
                + "<span style='font-family:Inter;font-size:13px;color:#946200'>Esta acción es irreversible.</span></div></html>");

        p.add(icono);
        p.add(texto);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(DialogStyles.BG_CONTENT);
        wrap.add(p, BorderLayout.CENTER);

        JSeparator s = new JSeparator();
        s.setForeground(DialogStyles.BORDER_SEPARATOR);
        wrap.add(s, BorderLayout.SOUTH);
        return wrap;
    }

    private JPanel crearCentral(String codigoPlaza, String planta, String matricula, String fechaReserva, String tipoVehiculo) {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        col.setBackground(DialogStyles.BG_CONTENT);

        col.add(crearTarjetaResumen(codigoPlaza, planta, matricula, fechaReserva));
        col.add(Box.createVerticalStrut(18));

        JSeparator sep = new JSeparator();
        sep.setForeground(DialogStyles.BORDER_SEPARATOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, sep.getPreferredSize().height));
        col.add(sep);
        col.add(Box.createVerticalStrut(16));

        JLabel lblConf = new JLabel("Confirma introduciendo la matrícula");
        lblConf.setFont(DialogStyles.FONT_BODY);
        lblConf.setForeground(DialogStyles.TEXT_PRIMARY);
        lblConf.setAlignmentX(Component.LEFT_ALIGNMENT);
        col.add(lblConf);

        col.add(Box.createVerticalStrut(6));

        txtMatricula = new JTextField();
        txtMatricula.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txtMatricula.setAlignmentX(Component.LEFT_ALIGNMENT);
        DialogStyles.aplicarCampo(txtMatricula);
        txtMatricula.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) {
                actualizarEstadoConfirmacion();
            }
            @Override public void removeUpdate(DocumentEvent e) {
                actualizarEstadoConfirmacion();
            }
            @Override public void changedUpdate(DocumentEvent e) {
                actualizarEstadoConfirmacion();
            }
        });
        txtMatricula.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "confirmar");
        txtMatricula.getActionMap().put("confirmar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnConfirmar != null && btnConfirmar.isEnabled()) {
                    btnConfirmar.doClick();
                }
            }
        });

        col.add(txtMatricula);

        String pieMat = matriculaEsperada.isEmpty() ? "—" : matriculaEsperada;
        JLabel lblPie = new JLabel("<html><span style='color:#909090;font-size:11px'>Debe coincidir con: <b>" + pieMat + "</b></span></html>");
        lblPie.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPie.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        col.add(lblPie);

        col.add(Box.createVerticalStrut(16));
        col.add(crearAdvertencia(tipoVehiculo));

        return col;
    }

    /** Bloque tipo “badge” del detalle de plaza: borde azul y panel gris claro. */
    private JPanel crearTarjetaResumen(String codigoPlaza, String planta, String matricula, String fechaReserva) {
        JPanel tarjeta = new JPanel(new BorderLayout(12, 0));
        tarjeta.setBackground(DialogStyles.BG_CARD);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(DialogStyles.ACCENT_BLUE, 2),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)
        ));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel pre = new JLabel("Vas a cancelar la reserva de");
        pre.setFont(DialogStyles.FONT_BADGE_TITLE);
        pre.setForeground(DialogStyles.TEXT_MUTED);

        String m = matricula != null && !matricula.isBlank() ? matricula : "—";
        String f = fechaReserva != null && !fechaReserva.isBlank() ? fechaReserva : "—";

        JLabel linea1 = new JLabel("Plaza " + codigoPlaza + "  ·  Planta " + planta);
        linea1.setFont(DialogStyles.FONT_EMPHASIS);
        linea1.setForeground(DialogStyles.ACCENT_BLUE);

        JLabel linea2 = new JLabel("Matrícula " + m + "  ·  Reservada el " + f);
        linea2.setFont(DialogStyles.FONT_BODY_SMALL);
        linea2.setForeground(DialogStyles.TEXT_PRIMARY);

        textos.add(pre);
        textos.add(Box.createVerticalStrut(6));
        textos.add(linea1);
        textos.add(Box.createVerticalStrut(4));
        textos.add(linea2);

        String icono = "🚗";
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 28));

        tarjeta.add(textos, BorderLayout.CENTER);
        tarjeta.add(lblIcono, BorderLayout.EAST);

        return tarjeta;
    }

    private JPanel crearAdvertencia(String tipoVehiculo) {
        BasicStroke dashStroke = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                10f, new float[]{4f, 4f}, 0f);

        JPanel advertencia = new JPanel();
        advertencia.setLayout(new BoxLayout(advertencia, BoxLayout.Y_AXIS));
        advertencia.setBackground(new Color(253, 246, 246));
        advertencia.setAlignmentX(Component.LEFT_ALIGNMENT);
        advertencia.setBorder(BorderFactory.createCompoundBorder(
                new StrokeBorder(dashStroke, DialogStyles.DANGER),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        JLabel av1 = new JLabel("La plaza quedará disponible para otros usuarios.");
        av1.setFont(DialogStyles.FONT_BODY);
        av1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel av2 = new JLabel("Tipo de plaza de la reserva");
        av2.setFont(DialogStyles.FONT_BODY_SMALL);
        av2.setForeground(DialogStyles.TEXT_MUTED);
        av2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> cmb = new JComboBox<>(new String[]{"Moto", "Coche", "Coche grande"});
        if (tipoVehiculo != null) {
            String t = tipoVehiculo.trim();
            for (int i = 0; i < cmb.getItemCount(); i++) {
                if (t.equalsIgnoreCase(cmb.getItemAt(i))) {
                    cmb.setSelectedIndex(i);
                    break;
                }
            }
        }
        cmb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cmb.setAlignmentX(Component.LEFT_ALIGNMENT);
        DialogStyles.aplicarCombo(cmb);
        cmb.setEnabled(false);
        cmb.setBackground(DialogStyles.BG_CARD);

        advertencia.add(av1);
        advertencia.add(Box.createVerticalStrut(10));
        advertencia.add(av2);
        advertencia.add(Box.createVerticalStrut(6));
        advertencia.add(cmb);

        return advertencia;
    }

    private JPanel crearPie() {
        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(DialogStyles.BG_CONTENT);
        JSeparator s = new JSeparator();
        s.setForeground(DialogStyles.BORDER_SEPARATOR);
        pie.add(s, BorderLayout.NORTH);

        JPanel inner = new JPanel(new BorderLayout());
        inner.setOpaque(false);
        inner.setBorder(BorderFactory.createEmptyBorder(12, 20, 18, 20));

        JButton volver = DialogStyles.botonSecundario("Volver");
        volver.addActionListener(e -> dispose());

        btnConfirmar = DialogStyles.botonPrimario("Sí, cancelar reserva", DialogStyles.DANGER, Color.WHITE);
        btnConfirmar.addActionListener(e -> {
            if (matriculaCoincide()) {
                confirmado = true;
                dispose();
            }
        });

        JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        izq.setOpaque(false);
        izq.add(volver);

        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        der.setOpaque(false);
        der.add(btnConfirmar);

        inner.add(izq, BorderLayout.WEST);
        inner.add(der, BorderLayout.EAST);
        pie.add(inner, BorderLayout.CENTER);
        return pie;
    }

    private void actualizarEstadoConfirmacion() {
        if (btnConfirmar != null) {
            btnConfirmar.setEnabled(matriculaCoincide());
        }
        if (txtMatricula != null) {
            Color borde = matriculaCoincide() ? DialogStyles.SUCCESS : DialogStyles.DANGER;
            txtMatricula.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(borde, 1),
                    BorderFactory.createEmptyBorder(9, 12, 9, 12)
            ));
        }
    }

    private boolean matriculaCoincide() {
        if (txtMatricula == null) {
            return false;
        }
        String t = txtMatricula.getText().trim();
        if (matriculaEsperada.isEmpty()) {
            return false;
        }
        return t.equalsIgnoreCase(matriculaEsperada);
    }
}
