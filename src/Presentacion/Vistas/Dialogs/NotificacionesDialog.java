package Presentacion.Vistas.Dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificacionesDialog extends BaseDialog {

    public record AvisoReservaCancelada(
            String codigoPlazaCancelada,
            String plantaCancelada,
            String matricula,
            String tipoVehiculo,
            String codigoPlazaNueva,    // null si no se pudo reasignar
            String plantaNueva           // null si no se pudo reasignar
    ) {}

    public NotificacionesDialog(Window parent, List<AvisoReservaCancelada> avisos) {
        super(parent, "Aviso importante");

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.add(crearSubcabecera(), BorderLayout.NORTH);
        contenido.add(crearListaAvisos(avisos), BorderLayout.CENTER);
        contenido.add(crearBotonEntendido(), BorderLayout.SOUTH);

        add(contenido, BorderLayout.CENTER);

        setMinimumSize(new Dimension(520, 0));
        centrarRespectoPadre();
    }

    private JPanel crearSubcabecera() {
        JPanel sub = new JPanel(new BorderLayout(10, 0));
        sub.setBackground(new Color(255, 248, 225));
        sub.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblIcono = new JLabel("🔔");
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 20));

        JLabel lblTexto = new JLabel("El administrador ha cancelado una reserva");
        lblTexto.setFont(new Font("Inter", Font.PLAIN, 13));
        lblTexto.setForeground(new Color(180, 140, 0));

        sub.add(lblIcono, BorderLayout.WEST);
        sub.add(lblTexto, BorderLayout.CENTER);
        return sub;
    }

    private JPanel crearListaAvisos(List<AvisoReservaCancelada> avisos) {
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setBackground(Color.WHITE);
        lista.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (AvisoReservaCancelada aviso : avisos) {
            lista.add(crearTarjetaCancelada(aviso));
            lista.add(Box.createVerticalStrut(10));

            if (aviso.codigoPlazaNueva() != null) {
                JLabel lblFlecha = new JLabel("⬇", SwingConstants.CENTER);
                lblFlecha.setAlignmentX(Component.CENTER_ALIGNMENT);
                lblFlecha.setFont(new Font("SansSerif", Font.PLAIN, 18));
                lista.add(lblFlecha);
                lista.add(Box.createVerticalStrut(10));
                lista.add(crearTarjetaReasignada(aviso));
                lista.add(Box.createVerticalStrut(15));
            }
        }

        // Nota inferior
        JLabel nota = new JLabel("Este aviso solo se muestra una vez al iniciar sesión.");
        nota.setForeground(Color.GRAY);
        nota.setFont(new Font("Inter", Font.ITALIC, 11));
        nota.setAlignmentX(Component.CENTER_ALIGNMENT);
        nota.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        lista.add(nota);

        return lista;
    }

    private JPanel crearTarjetaCancelada(AvisoReservaCancelada aviso) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(new Color(253, 240, 240));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(198, 40, 40)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel lblSeccion = new JLabel("RESERVA CANCELADA");
        lblSeccion.setFont(new Font("Inter", Font.BOLD, 11));
        lblSeccion.setForeground(Color.GRAY);

        JLabel lblPlaza = new JLabel("Plaza " + aviso.codigoPlazaCancelada()
                + "  ·  Planta " + aviso.plantaCancelada());
        lblPlaza.setFont(new Font("Inter", Font.BOLD, 14));

        JLabel lblDatos = new JLabel("Matrícula: " + aviso.matricula()
                + "  ·  Tipo: " + aviso.tipoVehiculo());
        lblDatos.setForeground(Color.GRAY);
        lblDatos.setFont(new Font("Inter", Font.PLAIN, 12));

        JLabel lblBadge = new JLabel("Cancelada por admin");
        lblBadge.setForeground(new Color(198, 40, 40));
        lblBadge.setFont(new Font("Inter", Font.BOLD, 12));

        tarjeta.add(lblSeccion);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblPlaza);
        tarjeta.add(lblDatos);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblBadge);

        return tarjeta;
    }

    private JPanel crearTarjetaReasignada(AvisoReservaCancelada aviso) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(new Color(232, 245, 233));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(46, 125, 50)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel lblSeccion = new JLabel("NUEVA PLAZA ASIGNADA");
        lblSeccion.setFont(new Font("Inter", Font.BOLD, 11));
        lblSeccion.setForeground(Color.GRAY);

        JLabel lblPlaza = new JLabel("Plaza " + aviso.codigoPlazaNueva()
                + "  ·  Planta " + aviso.plantaNueva());
        lblPlaza.setFont(new Font("Inter", Font.BOLD, 14));

        JLabel lblDatos = new JLabel("Matrícula: " + aviso.matricula()
                + "  ·  Tipo: " + aviso.tipoVehiculo());
        lblDatos.setForeground(Color.GRAY);
        lblDatos.setFont(new Font("Inter", Font.PLAIN, 12));

        JLabel lblBadge = new JLabel("Reasignada automáticamente");
        lblBadge.setForeground(new Color(46, 125, 50));
        lblBadge.setFont(new Font("Inter", Font.BOLD, 12));

        tarjeta.add(lblSeccion);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblPlaza);
        tarjeta.add(lblDatos);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblBadge);

        return tarjeta;
    }

    private JPanel crearBotonEntendido() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        JButton btn = new JButton("Entendido");
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        btn.setFont(new Font("Inter", Font.BOLD, 13));
        btn.addActionListener(e -> dispose());

        panel.add(btn);
        return panel;
    }
}