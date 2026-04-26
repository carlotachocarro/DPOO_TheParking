package Presentacion.Vistas.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ReservasPlazaPanel extends JPanel{
    private JTextField txtMatricula;
    private JComboBox<String> comboTipoVehiculo;
    private DefaultListModel<String> modeloLista;
    private JList<String> listaPlazas;
    private JButton btnConfirmarReserva;
    private JLabel lblResultado;
    private JButton btnBuscar;

    public ReservasPlazaPanel() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
    }

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Reservar plaza");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Busca y reserva una plaza disponible");
        subtitulo.setForeground(Color.GRAY);

        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(4));
        cabecera.add(subtitulo);

        return cabecera;
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new GridLayout(1, 2, 16, 16));
        contenido.add(crearPanelBusqueda());
        contenido.add(crearPanelResultados());
        return contenido;
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        txtMatricula = new JTextField();
        comboTipoVehiculo = new JComboBox<>(new String[]{"Coche", "Moto", "Coche grande"});

        JButton btnBuscar = new JButton("Buscar plazas");
        btnBuscar.addActionListener(e -> cargarPlazasDisponiblesMock());

        panel.add(new JLabel("Matrícula del vehículo"));
        panel.add(txtMatricula);
        panel.add(Box.createVerticalStrut(12));

        panel.add(new JLabel("Tipo de vehículo"));
        panel.add(comboTipoVehiculo);
        panel.add(Box.createVerticalStrut(12));

        panel.add(crearCajaInfo("Solo se muestran plazas compatibles con tu vehículo y no reservadas."));
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnBuscar);

        return panel;
    }

    private JPanel crearPanelResultados() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titulo = new JLabel("Plazas disponibles");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));

        modeloLista = new DefaultListModel<>();
        listaPlazas = new JList<>(modeloLista);
        listaPlazas.addListSelectionListener(e ->
                btnConfirmarReserva.setEnabled(listaPlazas.getSelectedIndex() != -1));

        btnConfirmarReserva = new JButton("Confirmar reserva");
        btnConfirmarReserva.setEnabled(false);
        btnConfirmarReserva.addActionListener(e -> {
            String seleccion = listaPlazas.getSelectedValue();
            if (seleccion != null) {
                lblResultado.setText("Reserva confirmada para " + seleccion);
            }
        });

        lblResultado = new JLabel(" ");
        lblResultado.setForeground(new Color(46, 125, 50));

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaPlazas), BorderLayout.CENTER);

        JPanel sur = new JPanel();
        sur.setLayout(new BoxLayout(sur, BoxLayout.Y_AXIS));
        sur.add(btnConfirmarReserva);
        sur.add(Box.createVerticalStrut(8));
        sur.add(lblResultado);

        panel.add(sur, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearCajaInfo(String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        JLabel label = new JLabel("<html>" + texto + "</html>");
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    public void setPlazasDisponibles(List<String> plazas) {
        modeloLista.clear();
        for (String plaza : plazas) {
            modeloLista.addElement(plaza);
        }
    }

    private void cargarPlazasDisponiblesMock() {
        setPlazasDisponibles(List.of(
                "A-01 - Planta 1 - Coche - Libre",
                "B-02 - Planta 2 - Coche - Libre",
                "B-04 - Planta 2 - Coche - Libre",
                "C-01 - Planta 3 - Coche - Libre"
        ));
    }



    public void setConfirmarListener(ActionListener listener) {
        btnConfirmarReserva.addActionListener(listener);
    }

    public String getPlazaSeleccionada() {
        return listaPlazas.getSelectedValue();
    }


    public String getMatricula() {
        return txtMatricula.getText().trim();
    }

    public String getTipoVehiculo() {
        return (String) comboTipoVehiculo.getSelectedItem();
    }

    public void mostrarResultado(String texto) {
        lblResultado.setText(texto);
    }

    public void setBuscarListener(ActionListener listener) {
        for (ActionListener al : ((JButton)((JPanel)crearPanelBusqueda()).getComponent(6)).getActionListeners()) {
            ((JButton)((JPanel)crearPanelBusqueda()).getComponent(6)).removeActionListener(al);
        }
    }


}
