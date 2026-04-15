package Presentacion.Vistas.Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EstadoParkingPanel extends JPanel{
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public EstadoParkingPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearZonaCentral(), BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);

        cargarDatosPrueba();
    }

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        JLabel lblTitulo = new JLabel("Estado del Parking");
        lblTitulo.setFont(new Font("SpaceGrotesk", Font.BOLD, 20));

        JLabel lblSubtitulo = new JLabel("Actualizado en tiempo real");
        lblSubtitulo.setFont(new Font("Inter", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);

        textos.add(lblTitulo);
        textos.add(lblSubtitulo);

        cabecera.add(textos, BorderLayout.WEST);
        return cabecera;
    }

    private JPanel crearZonaCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setOpaque(false);

        panelCentral.add(crearPanelStats(), BorderLayout.NORTH);
        panelCentral.add(crearTabla(), BorderLayout.CENTER);

        return panelCentral;
    }

    private JPanel crearPanelStats() {
        //TEMPORAL: Modificar cuando se conecte con negocio/controlador
        //Pasaríamos a: actualizarStats(int total, int libres, int ocupadas, int reservadas)
        JPanel stats = new JPanel(new GridLayout(1, 4, 15, 15));
        stats.setOpaque(false);

        stats.add(crearCard("Total plazas", "48"));
        stats.add(crearCard("Libres", "21"));
        stats.add(crearCard("Ocupadas", "19"));
        stats.add(crearCard("Reservadas", "8"));

        return stats;
    }

    private JPanel crearCard(String titulo, String valor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Inter", Font.PLAIN, 14));
        lblTitulo.setForeground(Color.GRAY);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("SpaceGrotesk", Font.BOLD, 28));

        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(10));
        card.add(lblValor);

        return card;
    }

    private JScrollPane crearTabla() {
        String[] columnas = {"Código", "Piso", "Estado", "Reserva", "Matrícula"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(28);
        tabla.getTableHeader().setReorderingAllowed(false);

        return new JScrollPane(tabla);
    }

    private JPanel crearPie() {
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pie.setOpaque(false);

        JLabel lblPie = new JLabel("Mostrando plazas del sistema");
        lblPie.setForeground(Color.GRAY);

        pie.add(lblPie);
        return pie;
    }

    private void cargarDatosPrueba() {
        //TEMPORAL: Modificar cuando se conecte con negocio/controlador
        //Pasaríamos a: actualizarTabla(List<Plaza> plazas)
        modeloTabla.addRow(new Object[]{"A-01", "P1", "Libre", "Disponible", "4268 BBD"});
        modeloTabla.addRow(new Object[]{"A-02", "P1", "Ocupada", "Disponible", "4832 BCN"});
        modeloTabla.addRow(new Object[]{"A-03", "P1", "Libre", "Reservada", "1234 XYZ"});
        modeloTabla.addRow(new Object[]{"B-01", "P2", "Libre", "Disponible", "0719 LXK"});
        modeloTabla.addRow(new Object[]{"B-02", "P2", "Ocupada", "Disponible", "9921 GHI"});
    }
}
