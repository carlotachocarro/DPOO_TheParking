package Persistencia.Daoimpl;

import Negocio.Entidades.Reserva;

import java.util.ArrayList;

public interface ReservaDAO {

    public int nuevaReserva(String id_plaza, String id_usuario, String matricula);

    public ArrayList<Reserva> getReservas();

    public ArrayList<Reserva> getReservaByUser(String id_usuario);

    public boolean borrarReservasUser(String id_usuario);

    public boolean borrarReserva(String id_plaza, String id_usuario);
}
