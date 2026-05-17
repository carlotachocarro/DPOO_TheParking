package Presentacion.Controladores;


import Negocio.Excepciones.ExcepcionDatosIncorrectos;
import Negocio.Excepciones.ExcepcionEntradaSalidaPlaza;
import Negocio.Excepciones.ExcepcionFicheroConfig;
import Negocio.Excepciones.ExcepcionHistorial;
import Negocio.Servicios.ServicioHistorialOcupacion;
import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioReserva;
import Negocio.Servicios.ServicioUsuario;
import Negocio.Servicios.ServicioVehiculo;
import Negocio.Servicios.Simulador;
import Persistencia.Config.ConfigJSONDAO;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Orquesta servicios de la sesión principal (usuario y administrador).
 * El nombre histórico sugiere solo admin, pero también se usa en {@code MainUsuarioFrame}.
 */
public class ControllerMenuPrincipalAdmin {
    private final ServicioPlaza servicioPlaza;
    private final ServicioReserva servicioReserva;
    private final ServicioUsuario servicioUsuario;
    private final ServicioVehiculo servicioVehiculo;
    private final ServicioHistorialOcupacion servicioHistorialOcupacion;
    private final Simulador simulador;
    private Timer timerRenovarGraficos;

    public ControllerMenuPrincipalAdmin() throws ExcepcionFicheroConfig {
        this.servicioPlaza = new ServicioPlaza();
        this.servicioReserva = new ServicioReserva(servicioPlaza);
        this.servicioUsuario = new ServicioUsuario();
        this.servicioVehiculo = new ServicioVehiculo(servicioPlaza);
        this.servicioHistorialOcupacion = new ServicioHistorialOcupacion();

        int maxSegundos = leerMaxSegundosConfig();
        this.simulador = new Simulador(servicioPlaza, maxSegundos);

        if (leerSimuladorActivoConfig()) {
            simulador.iniciar();
        }
    }

    private static int leerMaxSegundosConfig() {
        try {
            return new ConfigJSONDAO().getMaxSegundosEntreEventosSimulacion();
        } catch (ExcepcionFicheroNoEncontrado e) {
            return 0;
        }
    }

    private static boolean leerSimuladorActivoConfig() {
        try {
            return new ConfigJSONDAO().isSimuladorActivoPorDefecto();
        } catch (ExcepcionFicheroNoEncontrado e) {
            return false;
        }
    }

    /** Arranca la simulación de tráfico. Disponible para conectar a un botón del admin. */
    public void iniciarSimulador() {
        simulador.iniciar();
    }

    /** Detiene la simulación de tráfico. Disponible para conectar a un botón del admin. */
    public void detenerSimulador() {
        simulador.detener();
    }

    public boolean simuladorActivo() {
        return simulador.estaActivo();
    }

    public boolean eliminarCuenta(String name) throws ExcepcionDatosIncorrectos {
        return servicioUsuario.eliminarCuenta(name);
    }

    public ServicioPlaza getServicioPlaza() {
        return servicioPlaza;
    }

    public ServicioReserva getServicioReserva() {
        return servicioReserva;
    }

    public ServicioVehiculo getServicioVehiculo() {
        return servicioVehiculo;
    }

    public List<Integer> getSerieHistorialOcupacionUltimaHora() throws ExcepcionHistorial {
        return servicioHistorialOcupacion.serieUltimaHoraCronologica();
    }

    public int getCapacidadTotalPlazas() throws ExcepcionEntradaSalidaPlaza {
        return servicioPlaza.getTotalPlazasEnSistema();
    }

    /** Refresco periódico cada minuto coordinado desde la ventana (listener que repinta los gráficos). */
    public void registrarRefrescoGraficos(ActionListener cadaMinutoGraficos) {
        if (timerRenovarGraficos != null) {
            timerRenovarGraficos.stop();
            timerRenovarGraficos = null;
        }
        timerRenovarGraficos = new Timer(60_000, cadaMinutoGraficos);
        timerRenovarGraficos.setRepeats(true);
        timerRenovarGraficos.start();
    }

    /** Antes de dispose del frame principal: para el simulador y el refresco del gráfico. */
    public void detenerTimersSecundarios() {
        simulador.detener();
        if (timerRenovarGraficos != null) {
            timerRenovarGraficos.stop();
            timerRenovarGraficos = null;
        }
    }

    public String actualizarPlazasLibres() throws ExcepcionEntradaSalidaPlaza {
        return servicioPlaza.Actualizar_PlazasMenu();
    }


    public String actualizarEstadoParking() throws ExcepcionEntradaSalidaPlaza {
        return servicioPlaza.saberTodaslasPlazas();
    }


}
