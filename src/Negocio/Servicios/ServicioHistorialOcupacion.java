package Negocio.Servicios;

import Persistencia.Daoimpl.HistorialDBDAO;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lecturas de ocupación registradas en {@code historial} (últimos 60 min),
 * ordenadas de más antiguas → más recientes para el gráfico.
 */
public class ServicioHistorialOcupacion {

    private final HistorialDBDAO historialDAO;

    public ServicioHistorialOcupacion() throws ExcepcionFicheroNoEncontrado {
        this.historialDAO = new HistorialDBDAO();
    }

    public List<Integer> serieUltimaHoraCronologica() throws ExcepcionGeneralDB {
        ArrayList<Integer> valores = historialDAO.sacaHistorial();
        if (valores == null || valores.isEmpty()) {
            return List.of();
        }
        Collections.reverse(valores);
        return new ArrayList<>(valores);
    }
}
