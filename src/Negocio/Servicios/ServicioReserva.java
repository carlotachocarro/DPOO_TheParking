package Negocio.Servicios;

import Negocio.Entidades.AvisoCancelacionUsuario;
import Negocio.Entidades.Plaza;
import Negocio.Entidades.Reserva;
import Negocio.Excepciones.ExcepcionEntradaSalidaPlaza;
import Negocio.Excepciones.ExcepcionFicheroConfig;
import Negocio.Excepciones.ExcepcionReservaPlaza;
import Persistencia.Daoimpl.PlazaDBDAO;
import Persistencia.Daoimpl.ReservaDBDAO;
import Persistencia.Daoimpl.UsuarioDBDAO;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.util.ArrayList;
import java.util.List;

public class ServicioReserva {
    private ReservaDBDAO reservaDBDAO;
    private UsuarioDBDAO usuarioDBDAO;
    private PlazaDBDAO plazaDBDAO;
    private ServicioPlaza servicioPlaza;
    private ServicioUsuario servicioUsuario;



    public ServicioReserva(ServicioPlaza servicioPlaza) throws ExcepcionFicheroConfig {
        try {
            this.servicioUsuario = new ServicioUsuario();
            this.reservaDBDAO = new ReservaDBDAO();
            this.usuarioDBDAO = new UsuarioDBDAO();
            this.plazaDBDAO = new PlazaDBDAO();
            this.servicioPlaza = servicioPlaza;
        } catch (ExcepcionFicheroNoEncontrado e) {
            throw new ExcepcionFicheroConfig(e);
        }
    }


    public boolean realizarReservaVeiculo(String id_plaza, String matricula, String usuario) throws ExcepcionReservaPlaza {
        try {
            String idUsuario;

            if (servicioUsuario.validarCorreoElectro(usuario)) {
                idUsuario = usuarioDBDAO.getUsuarioId(null, usuario);
            } else {
                idUsuario = usuarioDBDAO.getUsuarioId(usuario, null);
            }

            if (plazaDBDAO.ocuparPlaza(id_plaza, true, matricula, usuario)) {
                if (reservaDBDAO.nuevaReserva(id_plaza, idUsuario, matricula)) {
                    servicioPlaza.notifyObservers();
                    return true;
                }
            }
            return false;
        } catch (ExcepcionGeneralDB | ExcepcionFicheroNoEncontrado e) {
            throw new ExcepcionReservaPlaza(e);
        } catch (ExcepcionEntradaSalidaPlaza e) {
            throw new ExcepcionReservaPlaza(e);
        }
    }

    public List<String> buscarPlazasDeParking(String tipoVehiculo) throws ExcepcionReservaPlaza {
        try {
            ArrayList<Plaza> plazas = plazaDBDAO.getPlazasLibres(tipoVehiculo);
            List<String> resultado = new ArrayList<>();
            for (Plaza plaza : plazas) {
                resultado.add(formatearPlazaParaVista(plaza));
            }
            return resultado;
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionReservaPlaza(e);
        }
    }


    private String formatearPlazaParaVista(Plaza plaza) {

        String estado;

        if (plaza.getEstado_reserva()) {
            estado = "Reservada";
        } else if (plaza.getEstado_ocupado()) {
            estado = "Ocupada";
        } else {
            estado = "Libre";
        }

        return plaza.getCodigoPlaza()
                + " - Planta " + plaza.getPlanta()
                + " - " + plaza.getTipoVehiculo()
                + " - " + estado;
    }



    public List<String> ObtenerReservas(String Nombre) throws ExcepcionReservaPlaza {
        try {
            ArrayList<Reserva> reservas;
            ArrayList<Plaza> plazas;
            String idUsuario;
            if (servicioUsuario.validarCorreoElectro(Nombre)) {
                idUsuario = usuarioDBDAO.getUsuarioId(null, Nombre);
            } else {
                idUsuario = usuarioDBDAO.getUsuarioId(Nombre, null);
            }

            reservas = reservaDBDAO.getReservaByUser(idUsuario);
            plazas = plazaDBDAO.getPlazas();
            List<String> resultado = new ArrayList<>();

            for (Reserva reserva : reservas) {
                for (Plaza plaza : plazas) {
                    String m = plaza.getCodigoPlaza();
                    String rm = reserva.getIdPlaza();

                    if (m.equals(rm)) {
                        resultado.add(reserva.getIdPlaza() + "-" + plaza.getTipoVehiculo() + "-" + reserva.getMatricula() + "-" + reserva.getDate() + "-" + plaza.getPlanta() + "-" + "True");
                    }
                }
            }
            return resultado;
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionReservaPlaza(e);
        }
    }

    public boolean cancelarReserva(String idPlaza, String nombre) throws ExcepcionReservaPlaza {
        try {
            String idUsuario;
            if (servicioUsuario.validarCorreoElectro(nombre)) {
                idUsuario = usuarioDBDAO.getUsuarioId(null, nombre);
            } else {
                idUsuario = usuarioDBDAO.getUsuarioId(nombre, null);
            }

            if (reservaDBDAO.borrarReserva(idPlaza, idUsuario)) {
                plazaDBDAO.limpiarPlaza(idPlaza);
                servicioPlaza.notifyObservers();
                return true;
            }

            return false;
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionReservaPlaza(e);
        } catch (ExcepcionEntradaSalidaPlaza e) {
            throw new ExcepcionReservaPlaza(e);
        }
    }

    /**
     * Cancelación desde el panel admin (detalle de plaza). Registra aviso para el próximo login del usuario.
     */
    public boolean adminCancelarReservaEnPlaza(String idPlaza) throws ExcepcionReservaPlaza {
        try {
            Reserva reserva = null;
            for (Reserva r : reservaDBDAO.getReservas()) {
                if (idPlaza.equals(r.getIdPlaza())) {
                    reserva = r;
                    break;
                }
            }
            if (reserva == null) {
                return false;
            }
            Plaza plaza = null;
            for (Plaza pl : plazaDBDAO.getPlazas()) {
                if (idPlaza.equals(pl.getCodigoPlaza())) {
                    plaza = pl;
                    break;
                }
            }
            if (plaza == null) {
                return false;
            }
            AvisoCancelacionUsuario aviso = new AvisoCancelacionUsuario(
                    plaza.getCodigoPlaza(),
                    String.valueOf(plaza.getPlanta()),
                    reserva.getMatricula(),
                    plaza.getTipoVehiculo(),
                    null,
                    null
            );
            // TODO: persistir el aviso en BD cuando exista AvisoDBDAO.

            if (reservaDBDAO.borrarReserva(idPlaza, reserva.getIdCliente())) {
                plazaDBDAO.limpiarPlaza(idPlaza);
                servicioPlaza.notifyObservers();
                return true;
            }
            return false;
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionReservaPlaza(e);
        } catch (ExcepcionEntradaSalidaPlaza e) {
            throw new ExcepcionReservaPlaza(e);
        }
    }
}
