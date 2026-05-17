package Presentacion.Controladores;

import Negocio.Excepciones.ExcepcionEntradaSalidaPlaza;
import Negocio.Servicios.ParkingObserver;
import Negocio.Servicios.ServicioPlaza;
import Presentacion.Vistas.Panels.EstadoParkingPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador que media entre el servicio de plazas y {@link EstadoParkingPanel}.
 * Implementa {@link ParkingObserver}: la vista deja de observar el servicio.
 */
public class ControladorEstadoParking implements ParkingObserver {

    private final EstadoParkingPanel panel;
    private final ServicioPlaza servicioPlaza;

    public ControladorEstadoParking(EstadoParkingPanel panel, ServicioPlaza servicioPlaza) {
        this.panel = panel;
        this.servicioPlaza = servicioPlaza;
        servicioPlaza.addObserver(this);
    }

    /** Carga inicial al montar el panel. */
    public void cargarInicial() throws ExcepcionEntradaSalidaPlaza {
        actualizarContadores(servicioPlaza.Actualizar_PlazasMenu());
        actualizarFilas(servicioPlaza.saberTodaslasPlazas());
    }

    @Override
    public void onParkingChange(String estado, String resumen) {
        SwingUtilities.invokeLater(() -> {
            actualizarContadores(resumen);
            actualizarFilas(estado);
        });
    }

    private void actualizarContadores(String resumen) {
        if (resumen == null || resumen.isEmpty()) {
            return;
        }
        try {
            String[] partes = resumen.split(",");
            int libres = Integer.parseInt(partes[0]);
            int reservadas = Integer.parseInt(partes[1]);
            int ocupadas = Integer.parseInt(partes[2]);
            int total = Integer.parseInt(partes[3]);
            panel.setContadores(total, libres, reservadas, ocupadas);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
            // Si el formato del resumen no es válido no actualizamos contadores.
        }
    }

    private void actualizarFilas(String estado) {
        List<EstadoParkingPanel.FilaPlaza> filas = new ArrayList<>();
        if (estado != null && !estado.isEmpty()) {
            for (String linea : estado.split("\n")) {
                if (linea.trim().isEmpty()) continue;
                // Formato: "-codigo-planta-estado-reserva-matricula-fechaReserva-tipoVehiculo"
                String[] datos = linea.split("-");
                String codigo = datos.length > 1 ? datos[1] : "";
                String planta = datos.length > 2 ? datos[2] : "";
                String est = datos.length > 3 ? datos[3] : "";
                String reserva = datos.length > 4 ? datos[4] : "";
                String matricula = datos.length > 5 ? datos[5] : "";
                String tipoVehiculo = datos.length > 7 ? datos[7] : "";
                filas.add(new EstadoParkingPanel.FilaPlaza(codigo, planta, est, reserva, matricula, tipoVehiculo));
            }
        }
        panel.setFilas(filas);
    }
}
