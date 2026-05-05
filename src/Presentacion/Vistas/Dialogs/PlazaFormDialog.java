package Presentacion.Vistas.Dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class PlazaFormDialog extends BaseDialog {

    public enum Modo { NUEVA, EDITAR }

    private final Modo modo;
    private JTextField txtCodigo;
    private JTextField txtPiso;
    private JComboBox<String> cmbTipoVehiculo;

    private boolean confirmado = false;

    public static PlazaFormDialog paraNueva(Window parent) {
        return new PlazaFormDialog(parent, Modo.NUEVA, null, null, null);
    }

    public static PlazaFormDialog paraEditar(Window parent,
                                             String codigoActual,
                                             String pisoActual,
                                             String tipoActual) {
        return new PlazaFormDialog(parent, Modo.EDITAR, codigoActual, pisoActual, tipoActual);
    }

    private PlazaFormDialog(Window parent, Modo modo,
                            String codigoActual, String pisoActual, String tipoActual) {

        super(parent, modo == Modo.NUEVA ? "Nueva plaza" : "Editar plaza");
        this.modo = modo;

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.setBackground(DialogStyles.BG_CONTENT);
        contenido.add(crearFormulario(codigoActual, pisoActual, tipoActual), BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(DialogStyles.BG_CONTENT);
        JSeparator sep = new JSeparator();
        sep.setForeground(DialogStyles.BORDER_SEPARATOR);
        pie.add(sep, BorderLayout.NORTH);
        pie.add(crearBotonera(), BorderLayout.CENTER);

        contenido.add(pie, BorderLayout.SOUTH);
        add(contenido, BorderLayout.CENTER);

        setMinimumSize(new Dimension(440, 0));
        centrarRespectoPadre();
    }

    private JPanel crearFormulario(String codigoActual, String pisoActual, String tipoActual) {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(22, 24, 18, 24));
        form.setBackground(DialogStyles.BG_CONTENT);

        // Código
        form.add(agruparCampo(
                "Código de plaza",
                modo == Modo.NUEVA ? "Ej: A-01" : null,
                () -> {
                    txtCodigo = new JTextField();
                    txtCodigo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                    if (codigoActual != null) {
                        txtCodigo.setText(codigoActual);
                    }
                    if (modo == Modo.EDITAR) {
                        txtCodigo.setEditable(false);
                        txtCodigo.setBackground(DialogStyles.BG_CARD);
                    }
                    DialogStyles.aplicarCampo(txtCodigo);
                    return txtCodigo;
                },
                modo == Modo.NUEVA ? crearHint("Identificador único en todo el sistema.") : null
        ));

        form.add(Box.createVerticalStrut(18));

        // Piso
        form.add(agruparCampo(
                "Piso",
                modo == Modo.NUEVA ? "Ej: 1" : null,
                () -> {
                    txtPiso = new JTextField();
                    txtPiso.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                    if (pisoActual != null) {
                        txtPiso.setText(pisoActual);
                    }
                    DialogStyles.aplicarCampo(txtPiso);
                    return txtPiso;
                },
                crearHint("Debe ser un valor numérico.")
        ));

        form.add(Box.createVerticalStrut(18));

        // Tipo
        form.add(agruparCampo(
                "Tipo de vehículo",
                null,
                () -> {
                    cmbTipoVehiculo = new JComboBox<>(new String[]{"Moto", "Coche", "Coche grande"});
                    cmbTipoVehiculo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
                    if (tipoActual != null) {
                        cmbTipoVehiculo.setSelectedItem(tipoActual);
                    }
                    DialogStyles.aplicarCombo(cmbTipoVehiculo);
                    return cmbTipoVehiculo;
                },
                null
        ));

        return form;
    }

    /**
     * Tarjeta suave igual que las celdas del detalle de plaza (panel gris muy claro).
     */
    private JPanel agruparCampo(String etiquetaTitulo, String ejemplo, Supplier<JComponent> crearControl, JLabel hintAdicional) {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setBackground(DialogStyles.BG_CARD);
        grupo.setAlignmentX(Component.LEFT_ALIGNMENT);
        grupo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 232, 236), 1),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        JLabel lblTit = new JLabel(etiquetaTitulo);
        lblTit.setFont(DialogStyles.FONT_BADGE_TITLE);
        lblTit.setForeground(DialogStyles.TEXT_MUTED);
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);

        grupo.add(lblTit);
        if (ejemplo != null) {
            JLabel ej = crearMicroEjemplo(ejemplo);
            grupo.add(ej);
            grupo.add(Box.createVerticalStrut(6));
        } else {
            grupo.add(Box.createVerticalStrut(6));
        }

        JComponent c = crearControl.get();
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        grupo.add(c);

        if (hintAdicional != null) {
            grupo.add(Box.createVerticalStrut(6));
            hintAdicional.setAlignmentX(Component.LEFT_ALIGNMENT);
            grupo.add(hintAdicional);
        }

        return grupo;
    }

    private JLabel crearMicroEjemplo(String texto) {
        JLabel lbl = new JLabel("<html><span style='color:#909090;font-size:11px'>" + texto + "</span></html>");
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JLabel crearHint(String texto) {
        JLabel lbl = new JLabel("<html><span style='color:#757575;font-size:11px'>" + texto + "</span></html>");
        return lbl;
    }

    private JPanel crearBotonera() {
        JPanel botonera = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 14));
        botonera.setBackground(DialogStyles.BG_CONTENT);

        JButton btnCancelar = DialogStyles.botonSecundario("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        String textoOk = modo == Modo.NUEVA ? "Crear plaza" : "Guardar cambios";
        JButton btnGuardar = DialogStyles.botonPrimario(textoOk, DialogStyles.ACCENT_BLUE, Color.WHITE);
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
