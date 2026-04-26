package Negocio.Entidades;

public class Plaza {

    private String tipoVheiculo;
    private int planta;
    private String codigoPlaza;
    private String matricula;
    private boolean estado_ocupado;
    private boolean estado_reserva;
    private boolean simulado;

    private Usuario user;


    public Plaza(String tipoVheiculo, int planta, String codigoPlaza, boolean ocu, boolean res, boolean sim, Usuario u) {
        this.tipoVheiculo = tipoVheiculo;
        this.planta = planta;
        this.codigoPlaza = codigoPlaza;
        this.estado_ocupado = ocu;
        this.estado_reserva = res;
        this.simulado = sim;
        this.user = u;
    }



    public String getTipoVehiculo() {
        return tipoVheiculo;
    }

    public int getPlanta() {
        return planta;
    }

    public String getCodigoPlaza() {
        return codigoPlaza;
    }

    public boolean getEstado_ocupado() {
        return estado_ocupado;
    }

    public void setEstado_ocupado(boolean estado_ocupado) {
        this.estado_ocupado = estado_ocupado;
    }

    public boolean getEstado_reserva() {
        return estado_reserva;
    }

    public void setEstado_reserva(boolean estado_reserva) {
        this.estado_reserva = estado_reserva;
    }

    public boolean getSimulado() {
        return simulado;
    }

    public void setSimulado(boolean simulado) {
        this.simulado = simulado;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getMatricula(){return this.matricula;}
}


