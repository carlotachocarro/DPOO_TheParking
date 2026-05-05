package Presentacion.Vistas.Dialogs;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Paleta y tipografía alineadas con {@link DetallePlazaDialog} para que todos los JDialog se perciban como la misma familia UI.
 */
public final class DialogStyles {

    public static final Color ACCENT_BLUE = new Color(52, 152, 219);
    public static final Color BG_CONTENT = Color.WHITE;
    public static final Color BG_CARD = new Color(245, 247, 250);
    public static final Color TEXT_PRIMARY = new Color(38, 38, 38);
    public static final Color TEXT_MUTED = new Color(117, 117, 117);
    public static final Color BORDER_SEPARATOR = new Color(220, 220, 220);
    public static final Color BORDER_FIELD = new Color(200, 200, 200);

    public static final Color SUCCESS = new Color(46, 125, 50);
    public static final Color DANGER = new Color(198, 40, 40);

    public static final Color WARNING_SURFACE = new Color(255, 248, 225);
    public static final Color WARNING_TEXT_DIM = new Color(148, 124, 0);

    /* Tipografías (fallback sistema si falta Inter/SpaceGrotesk) */
    public static final Font FONT_HEADER_DIALOG = new Font("SpaceGrotesk", Font.BOLD, 18);
    public static final Font FONT_BADGE_TITLE = new Font("Inter", Font.PLAIN, 12);
    public static final Font FONT_SECTION = new Font("Inter", Font.BOLD, 11);
    public static final Font FONT_BODY = new Font("Inter", Font.PLAIN, 13);
    public static final Font FONT_BODY_SMALL = new Font("Inter", Font.PLAIN, 12);
    public static final Font FONT_EMPHASIS = new Font("SpaceGrotesk", Font.BOLD, 15);
    /** Código de plaza destacado en cabecera de detalle (tamaño grande). */
    public static final Font FONT_PLAZA_HERO = new Font("SpaceGrotesk", Font.BOLD, 28);
    public static final Font FONT_BUTTON = new Font("Inter", Font.BOLD, 13);

    private DialogStyles() {
    }

    public static JButton botonPrimario(String texto, Color fondo, Color colorTexto) {
        JButton b = new JButton(texto);
        b.setBackground(fondo);
        b.setForeground(colorTexto);
        b.setFont(FONT_BUTTON);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(10, 26, 10, 26));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JButton botonSecundario(String texto) {
        JButton b = new JButton(texto);
        b.setBackground(BG_CONTENT);
        b.setForeground(TEXT_PRIMARY);
        b.setFont(FONT_BUTTON);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_FIELD, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return b;
    }

    public static void aplicarCampo(JTextField field) {
        field.setCaretColor(TEXT_PRIMARY);
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_FIELD, 1),
                BorderFactory.createEmptyBorder(9, 12, 9, 12)
        ));
        field.setFont(FONT_BODY);
    }

    public static void aplicarCombo(JComboBox<?> combo) {
        combo.setForeground(TEXT_PRIMARY);
        combo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_FIELD, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        combo.setFont(FONT_BODY);
        combo.setOpaque(true);
    }
}
