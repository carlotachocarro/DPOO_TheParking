package Persistencia.Daoimpl;

import java.util.ArrayList;

/**
 * Interfaz que implementará una forma de interactuar con el historial de la BBDD
 */
public interface HistorialDAO {
    /**
     * Esta función genera un nuevo registro del historial.
     * @return Devuelve un boolean en función de si la operación ha tenido éxito o no.
     */
    public boolean nuevoRegistro();

    /**
     * Esta función devuelve los registros de historial de los últimos 60 minutos.
     * @return Devuelve un ArrayList de integer con la cantidad de plazas ocupadas.
     */
    public ArrayList<Integer> sacaHistorial();
}
