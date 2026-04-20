package Persistencia.Daoimpl;

import Negocio.Entidades.*;

import java.util.ArrayList;

public interface PlazaDAO {
    public boolean crearPlaza(String tipoPlaza, int planta);

    public boolean eliminarPlaza(String id);

    public ArrayList<Plaza> getPlazas();

    public Usuario getPlazaUsuario(String id);

    public ArrayList<Plaza> getPlazaLibre(String tipo);

    public boolean setOcupadoLibre(boolean estado);

    public boolean setReserva(boolean estado);

    public boolean setSimul(boolean estado);
}
