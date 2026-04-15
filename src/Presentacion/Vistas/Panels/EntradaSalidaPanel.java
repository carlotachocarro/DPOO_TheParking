package Presentacion.Vistas.Panels;

import javax.swing.*;
import java.awt.*;

public class EntradaSalidaPanel extends JPanel {

    private JTextField txtMatriculaEntrada;
    private JComboBox<String> comboTipoVehiculo;
    private JLabel lblResultadoEntrada;

    private JTextField txtMatriculaSalida;
    private JLabel lblResultadoSalida;

    public EntradaSalidaPanel() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
    }

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Entrada / Salida");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Gestiona la entrada y salida de tu vehículo");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(Color.GRAY);

        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(4));
        cabecera.add(subtitulo);

        return cabecera;
    }

    private JPanel crearContenido() {
        JPanel contenido = new JPanel(new GridLayout(1, 2, 16, 16));
        contenido.add(crearPanelEntrada());
        contenido.add(crearPanelSalida());
        return contenido;
    }

    private JPanel crearPanelEntrada() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(46, 125, 50), 2),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titulo = new JLabel("Entrada");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(new Color(46, 125, 50));

        JLabel subtitulo = new JLabel("Registra entrada de vehículo");

        txtMatriculaEntrada = new JTextField();
        comboTipoVehiculo = new JComboBox<>(new String[]{"Coche", "Moto", "Coche grande"});

        JButton btnRegistrarEntrada = new JButton("Registrar entrada");
        btnRegistrarEntrada.addActionListener(e ->
                mostrarResultadoEntrada("Acceso concedido. Plaza asignada: A-03"));

        lblResultadoEntrada = new JLabel(" ");
        lblResultadoEntrada.setForeground(new Color(46, 125, 50));

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitulo);
        panel.add(Box.createVerticalStrut(20));

        panel.add(new JLabel("Matrícula del vehículo"));
        panel.add(txtMatriculaEntrada);
        panel.add(Box.createVerticalStrut(12));

        panel.add(new JLabel("Tipo de vehículo"));
        panel.add(comboTipoVehiculo);
        panel.add(Box.createVerticalStrut(12));

        panel.add(crearCajaInfo(
                "Si tienes una reserva, se asignará automáticamente tu plaza.\n" +
                        "Si no, se buscará plaza libre."
        ));
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnRegistrarEntrada);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblResultadoEntrada);

        return panel;
    }

    private JPanel crearPanelSalida() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(198, 40, 40), 2),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titulo = new JLabel("Salida");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(new Color(198, 40, 40));

        JLabel subtitulo = new JLabel("Registra salida de vehículo");

        txtMatriculaSalida = new JTextField();

        JButton btnRegistrarSalida = new JButton("Registrar salida");
        btnRegistrarSalida.addActionListener(e ->
                mostrarResultadoSalida("Salida registrada. Plaza A-03 liberada."));

        lblResultadoSalida = new JLabel(" ");
        lblResultadoSalida.setForeground(new Color(198, 40, 40));

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitulo);
        panel.add(Box.createVerticalStrut(20));

        panel.add(new JLabel("Matrícula del vehículo"));
        panel.add(txtMatriculaSalida);
        panel.add(Box.createVerticalStrut(12));

        panel.add(crearCajaInfo("Introduce la matrícula del vehículo que quiere salir.\nLa plaza quedará libre."));
        panel.add(Box.createVerticalStrut(12));
        panel.add(btnRegistrarSalida);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblResultadoSalida);

        return panel;
    }

    private JPanel crearCajaInfo(String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        area.setOpaque(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        panel.add(area, BorderLayout.CENTER);
        return panel;
    }

    public String getMatriculaEntrada() {
        return txtMatriculaEntrada.getText();
    }

    public String getTipoVehiculoEntrada() {
        return (String) comboTipoVehiculo.getSelectedItem();
    }

    public String getMatriculaSalida() {
        return txtMatriculaSalida.getText();
    }

    public void mostrarResultadoEntrada(String mensaje) {
        lblResultadoEntrada.setText(mensaje);
    }

    public void mostrarResultadoSalida(String mensaje) {
        lblResultadoSalida.setText(mensaje);
    }
}
