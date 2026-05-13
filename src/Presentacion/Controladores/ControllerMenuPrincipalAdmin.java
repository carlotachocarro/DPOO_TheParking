package Presentacion.Controladores;


import Negocio.Servicios.ServicioHistorialOcupacion;
import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioReserva;
import Negocio.Servicios.ServicioUsuario;
import Negocio.Servicios.ServicioVehiculo;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

/**
 * Orquesta servicios de la sesión principal (usuario y administrador).
 * El nombre histórico sugiere solo admin, pero también se usa en {@code MainUsuarioFrame}.
 */
public class ControllerMenuPrincipalAdmin {
    private ServicioPlaza servicioPlaza;
    private ServicioReserva servicioReserva;
    private ServicioUsuario servicioUsuario;
    private ServicioVehiculo servicioVehiculo;
    private ServicioHistorialOcupacion servicioHistorialOcupacion;
    private Timer timerSimulacionTrafico;
    private Timer timerRenovarGraficos;

    private final Random randomSimulacion = new Random();
    /** Evita encadenar nuevos ticks tras {@link #detenerTimersSecundarios()}. */
    private volatile boolean simulacionTraficoEncendida;

    public ControllerMenuPrincipalAdmin() throws ExcepcionFicheroNoEncontrado {
        this.servicioPlaza = new ServicioPlaza();
        this.servicioReserva = new ServicioReserva(servicioPlaza);
        this.servicioUsuario = new ServicioUsuario();
        this.servicioVehiculo = new ServicioVehiculo(servicioPlaza);
        this.servicioHistorialOcupacion = new ServicioHistorialOcupacion();
        iniciarSimulacionDesdeConfig();
    }

    /**
     * Simulación de tránsito según enunciado: espera aleatoria entre 1 y N segundos (N desde config),
     * luego entrada con probabilidad (#plazas libres no reservadas / #plazas no reservadas) o sortida.
     */
    private void iniciarSimulacionDesdeConfig() {
        int maxSegundos;
        try {
            Persistencia.Config.ConfigJSONDAO cfg = new Persistencia.Config.ConfigJSONDAO();
            maxSegundos = cfg.getMaxSegundosEntreEventosSimulacion();
        } catch (ExcepcionFicheroNoEncontrado e) {
            return;
        }
        if (maxSegundos <= 0) {
            return;
        }
        simulacionTraficoEncendida = true;
        programarSiguienteEventoSimulacion(maxSegundos);
    }

    private void programarSiguienteEventoSimulacion(int maxSegundos) {
        if (!simulacionTraficoEncendida) {
            return;
        }
        int esperaSeg = 1 + randomSimulacion.nextInt(maxSegundos);
        if (timerSimulacionTrafico != null) {
            timerSimulacionTrafico.stop();
        }
        timerSimulacionTrafico = new Timer(esperaSeg * 1000, e -> {
            if (!simulacionTraficoEncendida) {
                return;
            }
            double pEntrada = 0;
            try {
                pEntrada = servicioVehiculo.probabilidadEntrada();
            } catch (ExcepcionGeneralDB ex) {
                throw new RuntimeException(ex);
            }
            double r = randomSimulacion.nextDouble();
            if (r < pEntrada) {
                try {
                    servicioVehiculo.simulaEntrada();
                } catch (ExcepcionGeneralDB ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                try {
                    servicioVehiculo.simulaSalida();
                } catch (ExcepcionGeneralDB ex) {
                    throw new RuntimeException(ex);
                }
            }
            programarSiguienteEventoSimulacion(maxSegundos);
        });
        timerSimulacionTrafico.setRepeats(false);
        timerSimulacionTrafico.start();
    }

    public boolean eliminarCuenta(String name) throws ExcepcionGeneralDB {
        if (servicioUsuario.eliminarCuenta(name)){
            return true;
        }
        return false;

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

    public List<Integer> getSerieHistorialOcupacionUltimaHora() throws ExcepcionGeneralDB {
        return servicioHistorialOcupacion.serieUltimaHoraCronologica();
    }

    public int getCapacidadTotalPlazas() throws ExcepcionGeneralDB {
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

    /** Antes de dispose del frame principal (opcional): evitar timers repetidos si se cambia de sesión dentro de las mismas clases cargadas. */
    public void detenerTimersSecundarios() {
        simulacionTraficoEncendida = false;
        if (timerSimulacionTrafico != null) {
            timerSimulacionTrafico.stop();
            timerSimulacionTrafico = null;
        }
        if (timerRenovarGraficos != null) {
            timerRenovarGraficos.stop();
            timerRenovarGraficos = null;
        }
    }

    public String actualizarPlazasLibres() throws ExcepcionGeneralDB {
       String Value =servicioPlaza.Actualizar_PlazasMenu();

        return Value;
    }


    public String actualizarEstadoParking() throws ExcepcionGeneralDB {
         return servicioPlaza.saberTodaslasPlazas();
    }


}
