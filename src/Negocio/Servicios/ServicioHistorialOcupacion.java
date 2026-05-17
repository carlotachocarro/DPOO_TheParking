package Negocio.Servicios;

import Negocio.Excepciones.ExcepcionFicheroConfig;
import Negocio.Excepciones.ExcepcionHistorial;
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

    public ServicioHistorialOcupacion() throws ExcepcionFicheroConfig {
        try {
            this.historialDAO = new HistorialDBDAO();
        } catch (ExcepcionFicheroNoEncontrado e) {
            throw new ExcepcionFicheroConfig(e);
        }
    }

    public List<Integer> serieUltimaHoraCronologica() throws ExcepcionHistorial {
        try {
            ArrayList<Integer> valores = historialDAO.sacaHistorial();
            if (valores == null || valores.isEmpty()) {
                return List.of();
            }
            Collections.reverse(valores);
            return new ArrayList<>(valores);
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionHistorial(e);
        }
    }
}
