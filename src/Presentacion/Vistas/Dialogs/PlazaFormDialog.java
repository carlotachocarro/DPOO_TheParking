package Presentacion.Vistas.Dialogs;

import javax.swing.*;
import java.awt.*;

public class PlazaFormDialog extends BaseDialog {

    public enum Modo { NUEVA, EDITAR }

    private final Modo modo;
    private JTextField txtCodigo;
    private JTextField txtPiso;
    private JComboBox<String> cmbTipoVehiculo;

    private boolean confirmado = false;

    // ---- Factory methods ----

    public static PlazaFormDialog paraNueva(Window parent) {
        return new PlazaFormDialog(parent, Modo.NUEVA, null, null, null);
    }

    public static PlazaFormDialog paraEditar(Window parent,
                                             String codigoActual,
                                             String pisoActual,
                                             String tipoActual) {
        return new PlazaFormDialog(parent, Modo.EDITAR, codigoActual, pisoActual, tipoActual);
    }

    // ---- Constructor privado ----

    private PlazaFormDialog(Window parent, Modo modo,
                            String codigoActual, String pisoActual, String tipoActual) {

        super(parent, modo == Modo.NUEVA ? "Nueva plaza" : "Editar plaza");
        this.modo = modo;

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.add(crearSeparador(), BorderLayout.NORTH);
        contenido.add(crearFormulario(codigoActual, pisoActual, tipoActual), BorderLayout.CENTER);
        contenido.add(crearBotonera(), BorderLayout.SOUTH);
        add(contenido, BorderLayout.CENTER);

        setMinimumSize(new Dimension(420, 0));
        centrarRespectoPadre();
    }

    private JPanel crearFormulario(String codigoActual, String pisoActual, String tipoActual) {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        form.setBackground(Color.WHITE);

        // Código
        form.add(crearLabel("Código de plaza"));
        form.add(Box.createVerticalStrut(5));
        txtCodigo = new JTextField();
        txtCodigo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        if (codigoActual != null) txtCodigo.setText(codigoActual);
        form.add(txtCodigo);

        form.add(Box.createVerticalStrut(15));

        // Piso
        form.add(crearLabel("Piso"));
        form.add(Box.createVerticalStrut(5));
        txtPiso = new JTextField();
        txtPiso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        if (pisoActual != null) txtPiso.setText(pisoActual);
        form.add(txtPiso);

        form.add(Box.createVerticalStrut(15));

        // Tipo de vehículo
        form.add(crearLabel("Tipo de vehículo"));
        form.add(Box.createVerticalStrut(5));
        cmbTipoVehiculo = new JComboBox<>(new String[]{"Coche", "Moto", "Coche grande"});
        cmbTipoVehiculo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        if (tipoActual != null) cmbTipoVehiculo.setSelectedItem(tipoActual);
        form.add(cmbTipoVehiculo);

        return form;
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Inter", Font.PLAIN, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel crearBotonera() {
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botonera.setBackground(Color.WHITE);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGuardar = new JButton(modo == Modo.NUEVA ? "Crear plaza" : "Guardar cambios");
        btnGuardar.setBackground(new Color(52, 152, 219));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btnGuardar.addActionListener(e -> {
            if (validarFormulario()) {
                confirmado = true;
                dispose();
            }
        });

        botonera.add(btnCancelar);
        botonera.add(btnGuardar);
        return botonera;
    }

    private boolean validarFormulario() {
        String codigo = txtCodigo.getText().trim();
        String piso = txtPiso.getText().trim();

        if (codigo.isEmpty()) {
            mostrarError("El código de plaza no puede estar vacío.");
            return false;
        }
        if (piso.isEmpty()) {
            mostrarError("El piso no puede estar vacío.");
            return false;
        }
        try {
            Integer.parseInt(piso);
        } catch (NumberFormatException ex) {
            mostrarError("El piso debe ser un valor numérico.");
            return false;
        }
        return true;
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error de validación", JOptionPane.ERROR_MESSAGE);
    }

    // ---- Getters ----

    public boolean fueConfirmado() {
        return confirmado;
    }

    public String getCodigo() {
        return txtCodigo.getText().trim();
    }

    public String getPiso() {
        return txtPiso.getText().trim();
    }

    public String getTipoVehiculo() {
        return (String) cmbTipoVehiculo.getSelectedItem();
    }
}