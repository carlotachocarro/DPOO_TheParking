package Presentacion.Vistas.Dialogs;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDialog extends JDialog {

    protected BaseDialog(Window parent, String titulo) {
        super(parent, titulo, ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setResizable(false);
        getContentPane().setBackground(DialogStyles.BG_CONTENT);

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.add(crearCabecera(titulo), BorderLayout.NORTH);

        JSeparator sepHeaders = new JSeparator();
        sepHeaders.setForeground(DialogStyles.BORDER_SEPARATOR);
        sepHeaders.setBackground(DialogStyles.BORDER_SEPARATOR);
        encabezado.add(sepHeaders, BorderLayout.SOUTH);

        add(encabezado, BorderLayout.NORTH);
    }

    protected JPanel crearCabecera(String titulo) {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 18));
        cabecera.setBackground(DialogStyles.BG_CONTENT);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(DialogStyles.FONT_HEADER_DIALOG);
        lblTitulo.setForeground(DialogStyles.TEXT_PRIMARY);

        JButton btnCerrar = new JButton("✕");
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCerrar.setForeground(DialogStyles.TEXT_MUTED);
        btnCerrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCerrar.addActionListener(e -> dispose());

        cabecera.add(lblTitulo, BorderLayout.WEST);
        cabecera.add(btnCerrar, BorderLayout.EAST);

        return cabecera;
    }

    /**
     * Línea fina entre secciones dentro del contenido (mismo tono que el separador bajo la cabecera).
     */
    protected JPanel crearSeparador() {
        JPanel sep = new JPanel();
        sep.setPreferredSize(new Dimension(0, 1));
        sep.setBackground(DialogStyles.BORDER_SEPARATOR);
        return sep;
    }

    protected void centrarRespectoPadre() {
        pack();
        setLocationRelativeTo(getParent());
    }
}
