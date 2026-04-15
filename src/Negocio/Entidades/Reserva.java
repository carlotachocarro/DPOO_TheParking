package Negocio.Entidades;

import java.util.Date;

public class Reserva {
    private int idReserva;
    private int idVehiculo;
    private String idCliente;
    private Date date;

    public Reserva(int idReserva, int idVehiculo, String idCliente , Date date) {
        this.idReserva = idReserva;
        this.idVehiculo = idVehiculo;
        this.idCliente = idCliente;
        this.date=date;

    }
   public Date getDate() {
        return date;
   }
    public int getIdReserva() {
        return idReserva;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }
    public String getIdCliente() {
        return idCliente;
    }

}
