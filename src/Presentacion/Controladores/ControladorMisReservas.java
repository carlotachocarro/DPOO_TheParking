package Presentacion.Controladores;

import Negocio.Entidades.Reserva;
import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioReserva;
import Negocio.Servicios.ServicioUsuario;
import Persistencia.Daoimpl.ReservaDBDAO;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;
import Presentacion.Vistas.Panels.MisReservasPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ControladorMisReservas implements ActionListener  {

    private ReservaDBDAO reservaDBDAO;
    private ServicioReserva sesReserva;
    private ServicioPlaza servicioPlaza;
    private MisReservasPanel misReservasPanel;


    public ControladorMisReservas(MisReservasPanel misReservasPanel) throws ExcepcionFicheroNoEncontrado {
        this.misReservasPanel = misReservasPanel;
        reservaDBDAO = new ReservaDBDAO();
        servicioPlaza = new ServicioPlaza();
        sesReserva = new ServicioReserva(servicioPlaza);
    }

    public void getReserva(String nombre) throws ExcepcionGeneralDB {
        /*
        List<MisReservasPanel.ReservaVista> reserva = List.of(
                new MisReservasPanel.ReservaVista("A1", "Coche", "1234ABC", "01/05/2026", "Planta 1", true),
                new MisReservasPanel.ReservaVista("B2", "Moto", "5678DEF", "02/05/2026", "Planta 2", false)
        );*/

        List<String> reserv = sesReserva.ObtenerReservas(nombre);

        List<MisReservasPanel.ReservaVista> reservas = new ArrayList<>();

        for (String r : reserv) {

            String[] partes = r.split("-");

            MisReservasPanel.ReservaVista obj =
                    new MisReservasPanel.ReservaVista(
                            partes[0], // idPlaza
                            partes[1], // tipoVehiculo
                            partes[2], // matricula
                            partes[3], // fecha
                            partes[4], // planta
                            Boolean.parseBoolean(partes[5]) // estado
                    );

            reservas.add(obj);
        }
        misReservasPanel.setReservas(reservas);

    }
    @Override
    public void actionPerformed(ActionEvent e){



    }





}
