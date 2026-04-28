package Presentacion.Vistas.Dialogs;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDialog extends JDialog {

    protected BaseDialog(Window parent, String titulo) {
        super(parent, titulo, ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setResizable(false);
        add(crearCabecera(titulo), BorderLayout.NORTH);
    }

    private JPanel crearCabecera(String titulo) {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 15));
        cabecera.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SpaceGrotesk", Font.BOLD, 18));

        JButton btnCerrar = new JButton("✕");
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorderPainted(false);
        btnCerrar.setContentAreaFilled(false);
        btnCerrar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnCerrar.setForeground(Color.GRAY);
        btnCerrar.addActionListener(e -> dispose());

        cabecera.add(lblTitulo, BorderLayout.WEST);
        cabecera.add(btnCerrar, BorderLayout.EAST);

        return cabecera;
    }

    protected JPanel crearSeparador() {
        JPanel sep = new JPanel();
        sep.setPreferredSize(new Dimension(0, 1));
        sep.setBackground(new Color(220, 220, 220));
        return sep;
    }

    /**
     * Centra el diálogo respecto a la ventana padre tras hacer pack().
     */
    protected void centrarRespectoPadre() {
        pack();
        setLocationRelativeTo(getParent());
    }
}