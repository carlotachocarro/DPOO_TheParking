package Negocio.Servicios;

import Negocio.Entidades.Plaza;
import Negocio.Entidades.Reserva;
import Negocio.Excepciones.ExcepcionEntradaSalidaPlaza;
import Negocio.Excepciones.ExcepcionFicheroConfig;
import Persistencia.Daoimpl.PlazaDBDAO;
import Persistencia.Daoimpl.ReservaDBDAO;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.util.ArrayList;

/**
 * Operaciones reales sobre vehículos: entrada y salida iniciadas por el usuario o el admin.
 * La simulación automática de tráfico vive en {@link Simulador}.
 */
public class ServicioVehiculo {

    private PlazaDBDAO plazaDBDAO;
    private ReservaDBDAO reservaDBDAO;
    @SuppressWarnings("unused")
    private ServicioPlaza servicioPlaza;

    public ServicioVehiculo(ServicioPlaza servicioPlaza) throws ExcepcionFicheroConfig {
        try {
            this.plazaDBDAO = new PlazaDBDAO();
            this.reservaDBDAO = new ReservaDBDAO();
            this.servicioPlaza = servicioPlaza;
        } catch (ExcepcionFicheroNoEncontrado e) {
            throw new ExcepcionFicheroConfig(e);
        }
    }

    public String registroEntradaVehiculo(String matricula, String tipoVehiculo, String username) throws ExcepcionEntradaSalidaPlaza {
        try {
            ArrayList<Reserva> reservas = reservaDBDAO.getReservas();
            String IdplazaAsignada = null;
            for (Reserva r : reservas) {
                if (r.getMatricula().equals(matricula)) {
                    IdplazaAsignada = r.getIdPlaza();
                    break;
                }
            }
            if (IdplazaAsignada == null) {
                ArrayList<Plaza> plazasLibres = plazaDBDAO.getPlazasLibres(tipoVehiculo);
                if (!plazasLibres.isEmpty()) {
                    IdplazaAsignada = plazasLibres.getFirst().getCodigoPlaza();
                }
            }
            if (IdplazaAsignada != null) {
                if (plazaDBDAO.ocuparPlaza(IdplazaAsignada, true, matricula, username)) {
                    return IdplazaAsignada;
                }
            }
            return null;
        } catch (ExcepcionGeneralDB | ExcepcionFicheroNoEncontrado e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
    }

    public boolean registrarSalidaVehiculo(String matricula) throws ExcepcionEntradaSalidaPlaza {
        try {
            ArrayList<Plaza> plazasTotal = plazaDBDAO.getPlazas();
            for (Plaza p : plazasTotal) {
                if (p.getEstado_ocupado() && p.getMatricula().equals(matricula)) {
                    return plazaDBDAO.ocuparPlaza(p.getCodigoPlaza(), false, "none", null);
                }
            }
            return false;
        } catch (ExcepcionGeneralDB | ExcepcionFicheroNoEncontrado e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
    }

    public boolean vehiculoEnParking(String matricula) throws ExcepcionEntradaSalidaPlaza {
        try {
            ArrayList<Plaza> plazasTotal = plazaDBDAO.getPlazas();
            for (Plaza p : plazasTotal) {
                if (p.getEstado_ocupado() && p.getMatricula().equals(matricula)) {
                    return true;
                }
            }
            return false;
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
    }

    public int disponibilidadPorTipo(String tipo) throws ExcepcionEntradaSalidaPlaza {
        try {
            return plazaDBDAO.getPlazasLibres(tipo).size();
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionEntradaSalidaPlaza(e);
        }
    }
}
