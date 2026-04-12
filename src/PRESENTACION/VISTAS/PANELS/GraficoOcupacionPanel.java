package PRESENTACION.VISTAS.PANELS;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraficoOcupacionPanel extends JPanel {

    private JLabel lblAhoraMismo;
    private JLabel lblMaximo;
    private JLabel lblMinimo;
    private List<Integer> datos = new ArrayList<>();

    public GraficoOcupacionPanel() {
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);

        cargarDatosMock();
    }

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel();
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Gráfico de ocupación");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Última hora · Actualización cada minuto");
        subtitulo.setForeground(Color.GRAY);

        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(4));
        cabecera.add(subtitulo);

        return cabecera;
    }

    private JPanel crearCentro() {
        JPanel centro = new JPanel(new BorderLayout(0, 16));
        centro.add(crearPanelResumen(), BorderLayout.NORTH);
        centro.add(new PanelGrafica(), BorderLayout.CENTER);
        return centro;
    }

    private JPanel crearPanelResumen() {
        JPanel resumen = new JPanel(new GridLayout(1, 3, 12, 12));

        lblAhoraMismo = crearCardResumen("Ahora mismo", "0/0");
        lblMaximo = crearCardResumen("Máximo última hora", "0");
        lblMinimo = crearCardResumen("Mínimo última hora", "0");

        resumen.add(wrapCard(lblAhoraMismo));
        resumen.add(wrapCard(lblMaximo));
        resumen.add(wrapCard(lblMinimo));

        return resumen;
    }

    private JLabel crearCardResumen(String titulo, String valor) {
        JLabel label = new JLabel("<html><b>" + titulo + "</b><br><span style='font-size:18px'>" + valor + "</span></html>");
        return label;
    }

    private JPanel wrapCard(JLabel contenido) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    public void setDatosOcupacion(List<Integer> ocupaciones, int capacidadTotal) {
        this.datos = new ArrayList<>(ocupaciones);

        if (datos.isEmpty()) {
            lblAhoraMismo.setText("<html><b>Ahora mismo</b><br><span style='font-size:18px'>0/" + capacidadTotal + "</span></html>");
            lblMaximo.setText("<html><b>Máximo última hora</b><br><span style='font-size:18px'>0</span></html>");
            lblMinimo.setText("<html><b>Mínimo última hora</b><br><span style='font-size:18px'>0</span></html>");
        } else {
            int actual = datos.get(datos.size() - 1);
            int max = datos.stream().mapToInt(Integer::intValue).max().orElse(0);
            int min = datos.stream().mapToInt(Integer::intValue).min().orElse(0);

            lblAhoraMismo.setText("<html><b>Ahora mismo</b><br><span style='font-size:18px'>" + actual + "/" + capacidadTotal + "</span></html>");
            lblMaximo.setText("<html><b>Máximo última hora</b><br><span style='font-size:18px'>" + max + "</span></html>");
            lblMinimo.setText("<html><b>Mínimo última hora</b><br><span style='font-size:18px'>" + min + "</span></html>");
        }

        repaint();
    }

    private void cargarDatosMock() {
        setDatosOcupacion(List.of(
                10, 12, 15, 18, 20, 19, 21, 23, 25, 24,
                26, 28, 27, 29, 30, 31, 33, 32, 35, 36,
                34, 37, 39, 38, 40, 41, 42, 40, 39, 38
        ), 48);
    }

    private class PanelGrafica extends JPanel {
        public PanelGrafica() {
            setPreferredSize(new Dimension(800, 400));
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (datos == null || datos.isEmpty()) {
                g.drawString("Sin datos de ocupación", 20, 20);
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            int margenIzq = 50;
            int margenDer = 20;
            int margenSup = 20;
            int margenInf = 50;

            int areaAncho = ancho - margenIzq - margenDer;
            int areaAlto = alto - margenSup - margenInf;

            int max = datos.stream().mapToInt(Integer::intValue).max().orElse(1);
            int numBarras = datos.size();
            int anchoBarra = Math.max(8, areaAncho / Math.max(numBarras, 1));

            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(margenIzq, margenSup, margenIzq, margenSup + areaAlto);
            g2.drawLine(margenIzq, margenSup + areaAlto, margenIzq + areaAncho, margenSup + areaAlto);

            for (int i = 0; i < numBarras; i++) {
                int valor = datos.get(i);
                int alturaBarra = (int) ((valor / (double) max) * (areaAlto - 10));
                int x = margenIzq + i * anchoBarra;
                int y = margenSup + areaAlto - alturaBarra;

                g2.setColor(new Color(33, 150, 243));
                g2.fillRect(x, y, anchoBarra - 2, alturaBarra);
            }

            g2.setColor(Color.DARK_GRAY);
            g2.drawString("0", 30, margenSup + areaAlto);
            g2.drawString(String.valueOf(max), 20, margenSup + 10);

            g2.drawString("-60m", margenIzq, alto - 20);
            g2.drawString("-45m", margenIzq + areaAncho / 4, alto - 20);
            g2.drawString("-30m", margenIzq + areaAncho / 2, alto - 20);
            g2.drawString("-15m", margenIzq + (areaAncho * 3) / 4, alto - 20);
            g2.drawString("Ahora", margenIzq + areaAncho - 30, alto - 20);
        }
    }
}
