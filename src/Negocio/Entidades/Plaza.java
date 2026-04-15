package Negocio.Entidades;

public class Plaza {
    private String tipoVheiculo;
    private int planta;
    private String codigoPlaza;

    public Plaza(String tipoVheiculo, int planta, String codigoPlaza) {
        this.tipoVheiculo = tipoVheiculo;
        this.planta = planta;
        this.codigoPlaza = codigoPlaza;
    }


    public String getTipoVheiculo() {
        return tipoVheiculo;
    }

    public int getPlanta() {
        return planta;
    }

    public String getCodigoPlaza() {
        return codigoPlaza;
    }
}


