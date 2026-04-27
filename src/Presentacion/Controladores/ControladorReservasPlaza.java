package Presentacion.Controladores;

import Negocio.Servicios.ServicioReserva;
import Presentacion.Vistas.Panels.ReservasPlazaPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ControladorReservasPlaza implements ActionListener {

    private ReservasPlazaPanel vista;
    private ServicioReserva servicioReserva;

    private String nombreUsuario;

    public ControladorReservasPlaza(ReservasPlazaPanel vista,String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.vista = vista;
        this.servicioReserva = new ServicioReserva();
        this.vista.addRegistroListener(this);

    }



    public boolean esMatriculaValida(String matricula) {
        return matricula.matches("^[0-9]{4}\\s?[A-Z]{3}$");
    }


    @Override
    public void actionPerformed(ActionEvent e) {


        switch (e.getActionCommand()) {

            case "CONFIRM_RESERVA":
                String matricula = vista.getTxtMatricula().getText();
                if (matricula.equals("") || !esMatriculaValida(matricula)) {
                    JOptionPane.showMessageDialog(vista, "Datos incorrectos");
                    vista.limpiarCampos();
                    return;
                }

                String seleccion = vista.getListaPlazas().getSelectedValue();
                String idPlaza = seleccion.split(" - ")[0];
                if (!servicioReserva.realizarReservaVeiculo(idPlaza, matricula,nombreUsuario)) {
                    JOptionPane.showMessageDialog(vista, "No Se ha podido realizar la reserva del plaza");
                    vista.limpiarCampos();
                    return;
                }
                JOptionPane.showMessageDialog(vista, "Reserva realizada con éxito");
                vista.limpiarCampos();
                break;

            case "BUSCAR":


                vista.cargarPlazasDisponibles(servicioReserva.buscarPlazasDeParking(vista.getComboTipoVehiculo()));

                break;
        }


    }
}
