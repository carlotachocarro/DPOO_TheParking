package Persistencia.Daoimpl;

import Negocio.Entidades.*;

import java.util.ArrayList;

public interface PlazaDAO {
    public boolean crearPlaza(String tipoPlaza, int planta);

    public boolean eliminarPlaza(String id);

    public ArrayList<Plaza> getPlazas();

    public Usuario getPlazaUsuario(String id);


    /** Si ocupacion es true, matricula e idUsuario (clave numérica en tabla usuario) son obligatorios. Si es false, idUsuario se ignora. */
    public boolean ocuparPlaza(String id_plaza ,boolean ocupacion, String matricula, String idUsuario);

    public boolean ocuparPlazaSimul(String id_plaza ,boolean ocupacion, String matricula);

    public boolean reservarPlaza(String id_plaza ,boolean reserva);

    public boolean simularPlaza(String id_plaza ,boolean simul);
}
