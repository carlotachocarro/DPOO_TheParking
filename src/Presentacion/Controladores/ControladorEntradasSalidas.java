package Presentacion.Controladores;

import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioVehiculo;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;
import Presentacion.Vistas.Panels.EntradaSalidaPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorEntradasSalidas implements ActionListener {

    private EntradaSalidaPanel entradaSalidaPanel;
    private  ControladorReservasPlaza ControladorReservasPlaza;
    private ServicioVehiculo servicioVehiculo;
    private String nombreUsuario;
    private ServicioPlaza servicioPlaza;

    public ControladorEntradasSalidas(EntradaSalidaPanel entradaSalidaPanel, String nombreUsuario, ServicioPlaza servicioPlaza) throws ExcepcionFicheroNoEncontrado {
        this.entradaSalidaPanel = entradaSalidaPanel;
        entradaSalidaPanel.addEntradaSalidaListener(this);
        this.servicioVehiculo = new ServicioVehiculo(servicioPlaza);
        this.nombreUsuario = nombreUsuario;
        this.servicioPlaza = servicioPlaza;

    }



    @Override
    public void actionPerformed(ActionEvent e){

        String matricula= entradaSalidaPanel.getMatriculaEntrada();
        String matriculaSalida = entradaSalidaPanel.getMatriculaSalida();
        String tipoVhiculo = entradaSalidaPanel.getTipoVehiculoEntrada();




        switch (e.getActionCommand()) {
            case "ENTRADA":
                if (matricula == null || matricula == ""){
                    JOptionPane.showMessageDialog(entradaSalidaPanel, "Porfavor introduzca la matricula");

                    break;
                }

                try {
                    if (servicioVehiculo.registroEntradaVehiculo(matricula,tipoVhiculo,nombreUsuario) == null){
                        JOptionPane.showMessageDialog(entradaSalidaPanel, "La plaza no existe");
                    }else {
                        JOptionPane.showMessageDialog(entradaSalidaPanel, "Se ha registrado una entrada correctamente!!!!");

                    }
                } catch (ExcepcionGeneralDB ex) {
                    throw new RuntimeException(ex);
                } catch (ExcepcionFicheroNoEncontrado ex) {
                    throw new RuntimeException(ex);
                }
                entradaSalidaPanel.limpiarCampos();
                try {
                    servicioPlaza.notifyObservers();
                } catch (ExcepcionGeneralDB ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "SALIDA":

                if (matriculaSalida == null || matriculaSalida == ""){
                    JOptionPane.showMessageDialog(entradaSalidaPanel, "Porfavor introduzca la matricula");

                    break;
                }

                try {
                    if (!servicioVehiculo.registrarSalidaVehiculo(matriculaSalida)){
                        JOptionPane.showMessageDialog(entradaSalidaPanel, "La plaza no existe");

                    }else {
                        JOptionPane.showMessageDialog(entradaSalidaPanel, "Se ha registrado una salida correctamente!!!!");
                    }
                } catch (ExcepcionGeneralDB ex) {
                    throw new RuntimeException(ex);
                } catch (ExcepcionFicheroNoEncontrado ex) {
                    throw new RuntimeException(ex);
                }
                entradaSalidaPanel.limpiarCampos();
                try {
                    servicioPlaza.notifyObservers();
                } catch (ExcepcionGeneralDB ex) {
                    throw new RuntimeException(ex);
                }
                break;

        }


    }

}
