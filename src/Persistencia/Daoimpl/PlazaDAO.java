package Persistencia.Daoimpl;

import Negocio.Entidades.*;

import java.util.ArrayList;

public interface PlazaDAO {
    public boolean crearPlaza(String tipoPlaza, int planta);

    public boolean eliminarPlaza(String id);

    public ArrayList<Plaza> getPlazas();

    public Usuario getPlazaUsuario(String id);


    public boolean ocuparPlaza(String id_plaza ,boolean ocupacion, String matricula, String nombreUsuario);

    public boolean ocuparPlazaSimul(String id_plaza ,boolean ocupacion, String matricula);

    public boolean reservarPlaza(String id_plaza ,boolean reserva);

    public boolean simularPlaza(String id_plaza ,boolean simul);
}
