package Persistencia.Daoimpl;

import java.util.ArrayList;

public interface HistorialDAO {

    public boolean nuevoRegistro();

    public ArrayList<Integer> sacaHistorial();
}
