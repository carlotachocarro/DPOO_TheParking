package Presentacion.Vistas.Panels;

import Negocio.Servicios.ParkingObserver;
import Presentacion.Controladores.ControllerMenuPrincipalAdmin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

public class EstadoParkingPanel extends JPanel implements ParkingObserver {

    public enum Modo {
        USUARIO,        // Vista usuario: Estado parking (sin acciones)
        ADMIN_ESTADO,   // Vista admin: Estado parking (sin acciones, igual que usuario)
        ADMIN_GESTION   // Vista admin: Gestión plazas (con columna Acciones Edit/Del)
    }

    private final Modo modo;
    private final ControllerMenuPrincipalAdmin controller;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JLabel lblTituloCabecera;
    private JLabel lblSubtituloCabecera;

    // Stat cards
    private JLabel lblTotal;
    private JLabel lblLibres;
    private JLabel lblOcupadas;
    private JLabel lblReservadas;

    // Listener externo para acciones admin (Edit/Del de una fila)
    private AccionFilaListener accionFilaListener;

    public interface AccionFilaListener {
        void onEditar(String codigoPlaza);
        void onEliminar(String codigoPlaza);
        void onClickFila(String codigoPlaza); // para abrir DetallePlazaDialog en admin
    }



    public EstadoParkingPanel(ControllerMenuPrincipalAdmin controller) {

        this(controller, Modo.USUARIO);
       // controller.getServicioPlaza().addObserver(this);
    }

    public EstadoParkingPanel(ControllerMenuPrincipalAdmin controller, Modo modo) {

        this.controller = controller;
        this.modo = modo;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearZonaCentral(), BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);
        controller.getServicioPlaza().addObserver(this);
        mostrarPlazas();

        System.out.println("Panel servicio: " + controller.getServicioPlaza());
    }


    public void setAccionFilaListener(AccionFilaListener listener) {
        this.accionFilaListener = listener;
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

        // Las cards se crean vacías; los valores se rellenan en mostrarPlazas()
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
                // Solo editable la columna Acciones en modo gestión (para que reciba clicks)
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

        // Click en fila → abrir detalle (solo admin)
        if (modo == Modo.ADMIN_ESTADO || modo == Modo.ADMIN_GESTION) {
            tabla.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = tabla.rowAtPoint(e.getPoint());
                    int col = tabla.columnAtPoint(e.getPoint());
                    // No disparar si han clicado en la columna Acciones
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
                return new String[]{"Código", "Piso", "Estado", "Reserva", "Acciones"};
            case ADMIN_ESTADO:
            case USUARIO:
            default:
                return new String[]{"Código", "Piso", "Estado", "Reserva", "Matrícula"};
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

    public void mostrarPlazas() {
        // Stats
        String infCantidadDePlazas = controller.actualizarPlazasLibres();
        if (infCantidadDePlazas != null && !infCantidadDePlazas.isEmpty()) {
            String[] partes = infCantidadDePlazas.split(",");
            int libres = Integer.parseInt(partes[0]);
            int reservadas = Integer.parseInt(partes[1]);
            int ocupadas = Integer.parseInt(partes[2]);
            int totalPlazas = Integer.parseInt(partes[3]);

            lblTotal.setText(String.valueOf(totalPlazas));
            lblLibres.setText(String.valueOf(libres));
            lblOcupadas.setText(String.valueOf(ocupadas));
            lblReservadas.setText(String.valueOf(reservadas));
        }

        // Tabla
        String info = controller.actualizarEstadoParking();
        modeloTabla.setRowCount(0);
        if (info == null || info.isEmpty()) return;

        String[] lineas = info.split("\n");
        for (String linea : lineas) {
            if (linea.trim().isEmpty()) continue;
            String[] datos = linea.split("-");
            // datos[0] = "" (por el "-" inicial)
            // datos[1] = código, datos[2] = planta, datos[3] = estado ocupado,
            // datos[4] = reserva, datos[5] = matrícula

            String codigo = datos[1];
            String planta = datos[2];
            String estado = datos[3];
            String reserva = datos[4];
            String matricula = datos.length > 5 ? datos[5] : "";

            if (modo == Modo.ADMIN_GESTION) {
                modeloTabla.addRow(new Object[]{codigo, planta, estado, reserva, "ACCIONES"});
            } else {
                modeloTabla.addRow(new Object[]{codigo, planta, estado, reserva, matricula});
            }
        }
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

    @Override
    public void onParkingChange(String estado, String resumen) {

        SwingUtilities.invokeLater(() -> {

            // actualizar contadores
            String[] datos = resumen.split(",");
            lblLibres.setText(datos[0]);
            lblReservadas.setText(datos[1]);
            lblOcupadas.setText(datos[2]);
            lblTotal.setText(datos[3]);

            modeloTabla.setRowCount(0);
            if (estado == null || estado.isEmpty()) return;

            String[] lineas = estado.split("\n");
            for (String linea : lineas) {
                if (linea.trim().isEmpty()) continue;
                String[] dato = linea.split("-");

                String codigo = dato[1];
                String planta = dato[2];
                String esta = dato[3];
                String reserva = dato[4];
                String matricula = dato.length > 5 ? dato[5] : "";

                if (modo == Modo.ADMIN_GESTION) {
                    modeloTabla.addRow(new Object[]{codigo, planta, esta, reserva, "ACCIONES"});
                } else {
                    modeloTabla.addRow(new Object[]{codigo, planta, esta, reserva, matricula});
                }
            }
            modeloTabla.fireTableDataChanged(); //  importante
        });
    }
}