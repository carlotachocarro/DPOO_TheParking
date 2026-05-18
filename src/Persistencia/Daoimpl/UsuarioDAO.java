package Persistencia.Daoimpl;

import Negocio.Entidades.Usuario;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

/**
 * Interfaz que implementará una forma de interactuar con los Usuarios de la BBDD
 */
public interface UsuarioDAO {

    /**
     *
     * Esta función permite crear un nuevo usuario en la BBDD.
     * @param nombre Nombre del usuario.
     * @param eMail Email del usuario.
     * @param contraseña Contraseña del usuario. (Encriptada)
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
    public boolean registrarUsuario(String nombre, String eMail, String contraseña) throws ExcepcionGeneralDB;

    /**
     * Esta función elimina un usuario de la BBDD.
     * @param nombre Nombre del usuario a eliminar.
     * @param mail Mail del usuario a eliminar.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
    public boolean eliminarUsuario(String nombre, String mail) throws ExcepcionGeneralDB;

    /**
     * Comprueba si un usuario existe.
     * @param nombre Nombre del usuario.
     * @param mail Mail del usuario.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     * @throws ExcepcionGeneralDB
     */
    public boolean checkUsuario(String nombre, String mail) throws ExcepcionGeneralDB;

    /**
     * Este metodo devuelve los datos de un usuario en específico.
     * @param nombre Nombre del usuario.
     * @param mail Mail del usuario.
     * @return Devuelve una instancia de Usuario con los datos del usuario.
     * @throws ExcepcionGeneralDB
     */
    public Usuario getUsuario(String nombre, String mail) throws ExcepcionGeneralDB;

    /**
     * Este metodo devuelve los datos de un usuario en específico.
     * @param id Identificador del usuario.
     * @return Devuelve una instancia de Usuario con los datos del usuario.
     * @throws ExcepcionGeneralDB
     */
    public Usuario getUsuarioById(String id) throws ExcepcionGeneralDB;

    /**
     * Devuelve la contraseña (encriptada) de un usuario en específico.
     * @param nombre Nombre del usuario.
     * @param mail Mail del usuario.
     * @return Devuelve un String con la contraseña.
     * @throws ExcepcionGeneralDB
     */
    public String getUsuarioContraseña(String nombre, String mail) throws ExcepcionGeneralDB;

    /**
     * Esta función devuelve el identificador de un usuario de la BBDD.
     * @param nombre Nombre del usuario.
     * @param mail Mail del usuario.
     * @return String con el identificador del usuario.
     * @throws ExcepcionGeneralDB
     */
    public String getUsuarioId(String nombre, String mail) throws ExcepcionGeneralDB;



}
