package Negocio.Entidades;

import java.util.Date;

public class Reserva {
    private String idReserva;
    private String matricula;
    private String idCliente;
    private String idPlaza;
    private String date;


    public Reserva(String idReserva, String matricula, String idCliente , String idPlaza, String date) {
        this.idReserva = idReserva;
        this.matricula = matricula;
        this.idCliente = idCliente;
        this.idPlaza = idPlaza;
        this.date=date;

    }
   public String getDate() {
        return date;
   }
    public String getIdReserva() {
        return idReserva;
    }

    public String getMatricula() {
        return matricula;
    }
    public String getIdCliente() {
        return idCliente;
    }

    public String getIdPlaza(){return idPlaza;}

}
