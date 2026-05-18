package Presentacion.Vistas;

import Negocio.Excepciones.ExcepcionFicheroConfig;
import Negocio.Excepciones.ExcepcionNegocio;
import Presentacion.Controladores.ControladorAplicacion;
import Presentacion.Controladores.ControllerMenuPrincipalAdmin;

import javax.swing.*;
import java.awt.*;

/**
 * Plantilla base de los frames principales (usuario / admin).
 * Las subclases solo implementan los métodos abstractos; este frame no conoce servicios.
 */

public abstract class MainBaseFrame extends JFrame {

    protected CardLayout cardLayout;
    protected JPanel contentPanel;
    protected final ControllerMenuPrincipalAdmin controller;

    public MainBaseFrame(ControllerMenuPrincipalAdmin controller) {
        this.controller = controller;
        configurarVentana();
    }

    private void configurarVentana() {
        setTitle(getTitulo());
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    protected JPanel crearSidebarBase(String badge) {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(34, 40, 49));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel lblLogo = new JLabel("P  The Parking");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("SpaceGrotesk", Font.BOLD, 20));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBadge = new JLabel(badge);
        lblBadge.setForeground(new Color(52, 152, 219));
        lblBadge.setFont(new Font("Inter", Font.BOLD, 12));
        lblBadge.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(lblBadge);
        sidebar.add(Box.createVerticalStrut(25));

        return sidebar;
    }

    public void inicializarComponentes() {
        setLayout(new BorderLayout());

        add(crearSidebar(), BorderLayout.WEST);
        add(crearTopbar(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        try {
            crearPaneles();
        } catch (ExcepcionNegocio e) {
            JOptionPane.showMessageDialog(this,
                    e.getMensajeExcepcion(),
                    "Error al cargar la aplicación",
                    JOptionPane.ERROR_MESSAGE);
        }

        add(contentPanel, BorderLayout.CENTER);
    }

    protected JPanel crearTopbar() {
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        topbar.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(getTituloTopbar());
        lblTitulo.setFont(new Font("Inter", Font.BOLD, 22));
        topbar.add(lblTitulo, BorderLayout.WEST);

        JComponent componenteDerecho = crearComponenteDerechoTopbar();
        if (componenteDerecho != null) {
            topbar.add(componenteDerecho, BorderLayout.EAST);
        }

        return topbar;
    }

    public void mostrarVista(String nombreVista) {
        cardLayout.show(contentPanel, nombreVista);
    }

    protected void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que quieres cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION
        );
        if (opcion == JOptionPane.YES_OPTION) {
            controller.detenerTimersSecundarios();
            dispose();
            try {
                ControladorAplicacion.reiniciarFlujoAutenticacion();
            } catch (ExcepcionFicheroConfig e) {
                JOptionPane.showMessageDialog(null,
                        e.getMensajeExcepcion(),
                        "Error de configuración",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected JLabel crearEtiquetaSeccion(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(140, 150, 160));
        lbl.setFont(new Font("Inter", Font.BOLD, 11));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    protected JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(54, 62, 71));
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        boton.setHorizontalAlignment(SwingConstants.LEFT);

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(70, 80, 90));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(54, 62, 71));
            }
        });
        return boton;
    }

    /** Título de la ventana (JFrame title bar) */
    protected abstract String getTitulo();

    /** Título visible en la topbar */
    protected abstract String getTituloTopbar();

    /** Sidebar izquierdo con sus botones específicos */
    protected abstract JPanel crearSidebar();

    /** Añade los JPanels al contentPanel con cardLayout */
    protected abstract void crearPaneles() throws ExcepcionNegocio;

    /**
     * Componente opcional a la derecha de la topbar.
     * Por defecto null (no se muestra nada). Admin lo sobreescribe con el botón "+ Nueva Plaza".
     */
    protected JComponent crearComponenteDerechoTopbar() {
        return null;
    }
}