package Persistencia.Daoimpl;

import Negocio.Entidades.Reserva;

import java.util.ArrayList;

/**
 * Interfaz que implementará una forma de interactuar con las Plazas de la BBDD
 */
public interface ReservaDAO {

    /**
     * Este metodo genera una nueva reserva en la BBDD.
     * @param id_plaza Identificador de la plaza a reservar.
     * @param id_usuario Identificador del usuario que ha hecho la reserva.
     * @param matricula Matrícula del vehículo registrado en la reserva.
     * @return Devuelve un boolean en funcion de si se ha creado el registro con exito o no.
     */
    public int nuevaReserva(String id_plaza, String id_usuario, String matricula);

    /**
     * Este metodo devuelve una lista con todas las reservas que hay en el sistema registrados.
     * @return Devuelve un ArrayList de reservas, con todas las reservas del sistema.
     */
    public ArrayList<Reserva> getReservas();

    /**
     * Este metodo devuelve las reservas hechas por un usuario en específico.
     * @param id_usuario Identificador del usuario del cual estamos buscando sus reservas.
     * @return Devuelve un ArrayList de reservas con las reservas del usuario (solo las activas, el resto se eliminan).
     */
    public ArrayList<Reserva> getReservaByUser(String id_usuario);

    /**
     * Borra todas las reservas de un usuario.
     * @param id_usuario Id del usuario
     * @return Devuelve un boolean en función de si se ha conseguido borrar los registros.
     */
    public boolean borrarReservasUser(String id_usuario);

    /**
     * Borra una reserva en específico de un usuario.
     * @param id_plaza Identificador de la plaza de la reserva.
     * @param id_usuario Identificador del usuario de la reserva.
     * @return Devuelve un booleano en función de si la operación se ha completado con exito o no.
     */
    public boolean borrarReserva(String id_plaza, String id_usuario);
}
