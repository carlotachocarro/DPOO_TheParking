package Negocio.Servicios;

import Negocio.Entidades.Plaza;
import Negocio.Excepciones.ExcepcionEntradaSalidaPlaza;
import Negocio.Excepciones.ExcepcionFicheroConfig;
import Negocio.Excepciones.ExcepcionHistorial;
import Persistencia.Daoimpl.HistorialDBDAO;
import Persistencia.Daoimpl.PlazaDBDAO;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Random;

/**
 * Simulador de tránsito de vehículos del parking (enunciado).
 *
 * Cada {@code 1..maxSegundos} segundos se programa un tick que:
 *   1. Calcula la probabilidad de entrada como #plazas libres no reservadas / #plazas no reservadas.
 *   2. Saca un número aleatorio y decide entrada (si {@code r < pEntrada}) o salida.
 *   3. Aplica la operación (ocupa/libera una plaza simulada).
 *   4. Notifica a los observers del parking para que la UI se refresque.
 *
 * Es un componente de negocio puro: solo se le encarga {@link #iniciar()} / {@link #detener()}
 * desde el controlador. La presentación nunca toca su lógica interna.
 */
public class Simulador {

    private final PlazaDBDAO plazaDBDAO;
    private final HistorialDBDAO historialDAO;
    private final ServicioPlaza servicioPlaza;
    private final Random random = new Random();

    private Timer timer;
    private volatile boolean activo;
    /** Máximo N para la espera aleatoria entre ticks (1..N) en segundos. */
    private int maxSegundos;

    public Simulador(ServicioPlaza servicioPlaza, int maxSegundos) throws ExcepcionFicheroConfig {
        try {
            this.plazaDBDAO = new PlazaDBDAO();
            this.historialDAO = new HistorialDBDAO();
        } catch (ExcepcionFicheroNoEncontrado e) {
            throw new ExcepcionFicheroConfig(e);
        }
        this.servicioPlaza = servicioPlaza;
        this.maxSegundos = maxSegundos;
    }

    /** Arranca la simulación (no hace nada si ya estaba activa o si {@code maxSegundos <= 0}). */
    public synchronized void iniciar() {
        if (activo || maxSegundos <= 0) {
            return;
        }
        activo = true;
        programarSiguienteEvento();
    }

    /** Detiene la simulación. Llamar siempre al cerrar sesión. */
    public synchronized void detener() {
        activo = false;
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    public boolean estaActivo() {
        return activo;
    }

    /** Permite cambiar la ventana de tiempo entre ticks (útil si se ajusta desde la UI o config). */
    public synchronized void setMaxSegundos(int maxSegundos) {
        this.maxSegundos = maxSegundos;
    }

    private void programarSiguienteEvento() {
        if (!activo || maxSegundos <= 0) {
            return;
        }
        int esperaSeg = 1 + random.nextInt(maxSegundos);
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(esperaSeg * 1000, e -> {
            if (!activo) {
                return;
            }
            try {
                double pEntrada = probabilidadEntrada();
                if (random.nextDouble() < pEntrada) {
                    simulaEntrada();
                } else {
                    simulaSalida();
                }
                servicioPlaza.notifyObservers();
            } catch (ExcepcionEntradaSalidaPlaza | ExcepcionHistorial ignore) {
                // Tick fallido aislado: no detenemos la simulación.
            }
            programarSiguienteEvento();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private double probabilidadEntrada() throws ExcepcionEntradaSalidaPlaza {
        try {
            ArrayList<Plaza> plazas = plazaDBDAO.getPlazas();
            int libres = 0;
            int totalNoReservadas = 0;
            for (Plaza p : plazas) {
                if (!p.getEstado_reserva()) {
                    totalNoReservadas++;
                    if (!p.getEstado_ocupado()) {
                        libres++;
                    }
                }
            }
            return totalNoReservadas == 0 ? 0 : (double) libres / totalNoReservadas;
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
    }

    private void simulaEntrada() throws ExcepcionEntradaSalidaPlaza, ExcepcionHistorial {
        try {
            ArrayList<Plaza> plazas = plazaDBDAO.getPlazas();
            ArrayList<Plaza> disponibles = new ArrayList<>();
            for (Plaza p : plazas) {
                if (!p.getEstado_reserva() && !p.getEstado_ocupado()) {
                    disponibles.add(p);
                }
            }
            if (disponibles.isEmpty()) {
                return;
            }
            Plaza sel = disponibles.get(random.nextInt(disponibles.size()));
            String matricula = generarMatricula();
            if (plazaDBDAO.ocuparPlazaSimul(sel.getCodigoPlaza(), true, matricula)) {
                try {
                    historialDAO.nuevoRegistro();
                } catch (ExcepcionGeneralDB e) {
                    throw new ExcepcionHistorial(e);
                }
            }
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
    }

    private void simulaSalida() throws ExcepcionEntradaSalidaPlaza, ExcepcionHistorial {
        try {
            ArrayList<Plaza> plazas = plazaDBDAO.getPlazas();
            ArrayList<Plaza> ocupadasSimuladas = new ArrayList<>();
            for (Plaza p : plazas) {
                if (p.getEstado_ocupado() && p.getUser() == null) {
                    ocupadasSimuladas.add(p);
                }
            }
            if (ocupadasSimuladas.isEmpty()) {
                return;
            }
            Plaza sel = ocupadasSimuladas.get(random.nextInt(ocupadasSimuladas.size()));
            if (plazaDBDAO.ocuparPlazaSimul(sel.getCodigoPlaza(), false, "none")) {
                try {
                    historialDAO.nuevoRegistro();
                } catch (ExcepcionGeneralDB e) {
                    throw new ExcepcionHistorial(e);
                }
            }
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
    }

    private String generarMatricula() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        String letras = "BCDFGHJKLMNPQRSTVWXYZ";
        for (int i = 0; i < 3; i++) {
            sb.append(letras.charAt(random.nextInt(letras.length())));
        }
        return sb.toString();
    }
}
