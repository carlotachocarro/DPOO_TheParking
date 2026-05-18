package Persistencia.Daoimpl;

import Negocio.Entidades.*;

import java.util.ArrayList;

/**
 * Interfaz que implementará una forma de interactuar con las Plazas de la BBDD
 */
public interface PlazaDAO {
    /**
     * Esta función crea una nueva plaza de parking en la BBDD.
     * @param tipoPlaza Tipo de plaza de parking.
     * @param planta Planta en la que se situa la plaza.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @return
     */
    public boolean crearPlaza(String tipoPlaza, int planta);

    /**
     * Esta función elimina una plaza de parking y todos sus registros asociados.
     * @param idPlaza Identificador de la plaza a eliminar.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     */
    public boolean eliminarPlaza(String idPlaza);

    /**
     * Esta función devuelve todas las plazas del parking.
     * @return Devuelve un ArrayList con todas las plazas.
     */
    public ArrayList<Plaza> getPlazas();

    /**
     * Devuelve el usuario que ocupa una plaza de parking.
     * @param id Identificador de la plaza de parking.
     * @return Instancia del usuario que ocupa la plaza.
     */
    public Usuario getPlazaUsuario(String id);

    /**
     * Esta función ocupa una plaza de parking.
     * @param id_plaza Identificador de la plaza.
     * @param ocupacion Booleano que nos dice si se ocupa o desocupa.
     * @param matricula Matricula a poner en caso de ocupar.
     * @param nombreUsuario Nombre del usuario que ocupa.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @return
     */
    public boolean ocuparPlaza(String id_plaza ,boolean ocupacion, String matricula, String nombreUsuario);

    /**
     * Esta función ocupa una plaza de parking para la simulación.
     * @param id_plaza Identificador de la plaza.
     * @param ocupacion Estado de la ocupación.
     * @param matricula Matricula que ocupará la plaza.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @return
     */
    public boolean ocuparPlazaSimul(String id_plaza ,boolean ocupacion, String matricula);

    /**
     * Esta función reserva una plaza.
     * @param id_plaza Identificador de la plaza.
     * @param reserva Boolean que define si reservamos o no.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @return
     */
    public boolean reservarPlaza(String id_plaza ,boolean reserva);

    /**
     * Esta función define que una plaza se usará para la simulación.
     * @param id_plaza Identificador de la plaza.
     * @param simul Boolean que define si se simula o deja de simular.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     */
    public boolean simularPlaza(String id_plaza ,boolean simul);
}
