package Presentacion.Controladores;

import Negocio.Servicios.ServicioVehiculo;
import Presentacion.Vistas.Panels.EntradaSalidaPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorEntradasSalidas implements ActionListener {

    private EntradaSalidaPanel entradaSalidaPanel;
    private  ControladorReservasPlaza ControladorReservasPlaza;
    private ServicioVehiculo servicioVehiculo;
    private String nombreUsuario;

    public ControladorEntradasSalidas(EntradaSalidaPanel entradaSalidaPanel,String nombreUsuario) {
        this.entradaSalidaPanel = entradaSalidaPanel;
        entradaSalidaPanel.addEntradaSalidaListener(this);
        this.servicioVehiculo = new ServicioVehiculo();
        this.nombreUsuario = nombreUsuario;
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

               if (servicioVehiculo.registroEntradaVehiculo(matricula,tipoVhiculo,nombreUsuario) == null){
                   JOptionPane.showMessageDialog(entradaSalidaPanel, "La plaza no existe");
               }else {
                   JOptionPane.showMessageDialog(entradaSalidaPanel, "Se ha registrado una entrada correctamente!!!!");

               }
                entradaSalidaPanel.limpiarCampos();

                break;
            case "SALIDA":

                if (matriculaSalida == null || matriculaSalida == ""){
                    JOptionPane.showMessageDialog(entradaSalidaPanel, "Porfavor introduzca la matricula");

                    break;
                }

                if (!servicioVehiculo.registrarSalidaVehiculo(matriculaSalida)){
                    JOptionPane.showMessageDialog(entradaSalidaPanel, "La plaza no existe");

                }else {
                    JOptionPane.showMessageDialog(entradaSalidaPanel, "Se ha registrado una salida correctamente!!!!");
                }
                entradaSalidaPanel.limpiarCampos();
                break;

        }


    }

}
