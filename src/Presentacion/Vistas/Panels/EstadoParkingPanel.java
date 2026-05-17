package Presentacion.Vistas.Panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;

/**
 * Vista pasiva del estado del parking. No conoce servicios ni el patrón Observer:
 * solo expone {@link #setContadores}, {@link #setFilas} y un {@link AccionFilaListener}.
 * El controlador (ControladorEstadoParking) se encarga de alimentarla.
 */
public class EstadoParkingPanel extends JPanel {

    public enum Modo {
        USUARIO,        // Vista usuario: Estado parking (sin acciones)
        ADMIN_ESTADO,   // Vista admin: Estado parking (sin acciones, igual que usuario)
        ADMIN_GESTION   // Vista admin: Gestión plazas (con columna Acciones Edit/Del)
    }

    /** DTO de fila para que el controlador la pase ya parseada. */
    public record FilaPlaza(String codigo, String planta, String estado, String reserva, String matricula, String tipoVehiculo) {}

    public interface AccionFilaListener {
        void onEditar(String codigoPlaza);
        void onEliminar(String codigoPlaza);
        void onClickFila(String codigoPlaza); // para abrir DetallePlazaDialog en admin
    }

    private final Modo modo;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblTituloCabecera;
    private JLabel lblSubtituloCabecera;

    private JLabel lblTotal;
    private JLabel lblLibres;
    private JLabel lblOcupadas;
    private JLabel lblReservadas;

    private AccionFilaListener accionFilaListener;

    public EstadoParkingPanel() {
        this(Modo.USUARIO);
    }

    public EstadoParkingPanel(Modo modo) {
        this.modo = modo;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearZonaCentral(), BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);
    }

    public void setAccionFilaListener(AccionFilaListener listener) {
        this.accionFilaListener = listener;
    }

    public Modo getModo() {
        return modo;
    }

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        String titulo;
        String subtitulo;
        switch (modo) {
            case ADMIN_GESTION:
                titulo = "Gestión de plazas";
                subtitulo = "Crea, edita y elimina plazas del sistema";
                break;
            case ADMIN_ESTADO:
                titulo = "Estado del Parking";
                subtitulo = "Vista administrador · Tiempo real";
                break;
            case USUARIO:
            default:
                titulo = "Estado del Parking";
                subtitulo = "Actualizado en tiempo real";
                break;
        }

        lblTituloCabecera = new JLabel(titulo);
        lblTituloCabecera.setFont(new Font("SpaceGrotesk", Font.BOLD, 20));

        lblSubtituloCabecera = new JLabel(subtitulo);
        lblSubtituloCabecera.setFont(new Font("Inter", Font.PLAIN, 14));
        lblSubtituloCabecera.setForeground(Color.GRAY);

        textos.add(lblTituloCabecera);
        textos.add(lblSubtituloCabecera);

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
        JPanel stats = new JPanel(new GridLayout(1, 4, 15, 15));
        stats.setOpaque(false);

        lblTotal = new JLabel("0");
        lblLibres = new JLabel("0");
        lblOcupadas = new JLabel("0");
        lblReservadas = new JLabel("0");

        stats.add(crearCard("Total plazas", lblTotal));
        stats.add(crearCard("Libres", lblLibres));
        stats.add(crearCard("Ocupadas", lblOcupadas));
        stats.add(crearCard("Reservadas", lblReservadas));

        return stats;
    }

    private JPanel crearCard(String titulo, JLabel lblValor) {
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

        lblValor.setFont(new Font("SpaceGrotesk", Font.BOLD, 28));

        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(10));
        card.add(lblValor);

        return card;
    }

    private JScrollPane crearTabla() {
        String[] columnas = obtenerColumnas();

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return modo == Modo.ADMIN_GESTION && column == columnas.length - 1;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(modo == Modo.ADMIN_GESTION ? 38 : 28);
        tabla.getTableHeader().setReorderingAllowed(false);

        if (modo == Modo.ADMIN_GESTION) {
            int colAcciones = columnas.length - 1;
            tabla.getColumnModel().getColumn(colAcciones).setCellRenderer(new AccionesRenderer());
            tabla.getColumnModel().getColumn(colAcciones).setCellEditor(new AccionesEditor());
            tabla.getColumnModel().getColumn(colAcciones).setPreferredWidth(160);
        }

        if (modo == Modo.ADMIN_ESTADO || modo == Modo.ADMIN_GESTION) {
            tabla.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = tabla.rowAtPoint(e.getPoint());
                    int col = tabla.columnAtPoint(e.getPoint());
                    if (modo == Modo.ADMIN_GESTION && col == columnas.length - 1) return;
                    if (fila >= 0 && accionFilaListener != null) {
                        String codigo = (String) modeloTabla.getValueAt(fila, 0);
                        accionFilaListener.onClickFila(codigo);
                    }
                }
            });
        }

        return new JScrollPane(tabla);
    }

    private String[] obtenerColumnas() {
        switch (modo) {
            case ADMIN_GESTION:
                return new String[]{"Código", "Piso", "Tipo vehículo", "Estado", "Reserva", "Acciones"};
            case ADMIN_ESTADO:
            case USUARIO:
            default:
                return new String[]{"Código", "Piso", "Tipo vehículo", "Estado", "Reserva", "Matrícula"};
        }
    }

    private JPanel crearPie() {
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pie.setOpaque(false);

        JLabel lblPie = new JLabel("Mostrando plazas del sistema");
        lblPie.setForeground(Color.GRAY);

        pie.add(lblPie);
        return pie;
    }

    public void setContadores(int total, int libres, int reservadas, int ocupadas) {
        lblTotal.setText(String.valueOf(total));
        lblLibres.setText(String.valueOf(libres));
        lblReservadas.setText(String.valueOf(reservadas));
        lblOcupadas.setText(String.valueOf(ocupadas));
    }

    public void setFilas(List<FilaPlaza> filas) {
        modeloTabla.setRowCount(0);
        if (filas == null) {
            modeloTabla.fireTableDataChanged();
            return;
        }
        for (FilaPlaza f : filas) {
            if (modo == Modo.ADMIN_GESTION) {
                modeloTabla.addRow(new Object[]{f.codigo(), f.planta(), f.tipoVehiculo(), f.estado(), f.reserva(), "ACCIONES"});
            } else {
                modeloTabla.addRow(new Object[]{f.codigo(), f.planta(), f.tipoVehiculo(), f.estado(), f.reserva(), f.matricula()});
            }
        }
        modeloTabla.fireTableDataChanged();
    }

    // ============================================================
    // Renderer y Editor para la columna Acciones (modo ADMIN_GESTION)
    // ============================================================

    private class AccionesRenderer extends JPanel implements TableCellRenderer {
        private final JButton btnEdit = new JButton("Edit");
        private final JButton btnDel = new JButton("Del");

        AccionesRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 6, 2));
            setOpaque(true);
            estilarBoton(btnEdit, new Color(52, 152, 219));
            estilarBoton(btnDel, new Color(198, 40, 40));
            add(btnEdit);
            add(btnDel);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return this;
        }
    }

    private class AccionesEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));
        private final JButton btnEdit = new JButton("Edit");
        private final JButton btnDel = new JButton("Del");
        private String codigoFila;

        AccionesEditor() {
            estilarBoton(btnEdit, new Color(52, 152, 219));
            estilarBoton(btnDel, new Color(198, 40, 40));
            panel.add(btnEdit);
            panel.add(btnDel);

            btnEdit.addActionListener(e -> {
                fireEditingStopped();
                if (accionFilaListener != null && codigoFila != null) {
                    accionFilaListener.onEditar(codigoFila);
                }
            });

            btnDel.addActionListener(e -> {
                fireEditingStopped();
                if (accionFilaListener != null && codigoFila != null) {
                    accionFilaListener.onEliminar(codigoFila);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.codigoFila = (String) table.getValueAt(row, 0);
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "ACCIONES";
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }
    }

    private static void estilarBoton(JButton b, Color fondo) {
        b.setBackground(fondo);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
    }
}
