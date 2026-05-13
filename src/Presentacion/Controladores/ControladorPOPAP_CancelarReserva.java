package Presentacion.Controladores;

import Negocio.Servicios.ServicioPlaza;
import Negocio.Servicios.ServicioReserva;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;
import Presentacion.Vistas.Dialogs.CancelarReservaDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ControladorPOPAP_CancelarReserva extends JDialog implements ActionListener {

    private CancelarReservaDialog cancelarReservaDialog;
    private ServicioReserva servicioReserva;
    private String nombreUsuario;
    private ServicioPlaza servicioPlaza;

    public  ControladorPOPAP_CancelarReserva(CancelarReservaDialog cancelarReservaDialog ,String nombreUsuario,ServicioPlaza servicioPlaza) throws ExcepcionFicheroNoEncontrado {

        this.cancelarReservaDialog = cancelarReservaDialog;
        this.nombreUsuario = nombreUsuario;
        this.servicioPlaza = servicioPlaza;
        this.servicioReserva = new ServicioReserva(servicioPlaza);
        this.cancelarReservaDialog.addCancelarReservaListener(this);
    }



    @Override
    public void actionPerformed(ActionEvent e){

        switch (e.getActionCommand()){
            case "volver":
                cancelarReservaDialog.dispose();
                break;
            case "cancelar":
                if (cancelarReservaDialog.getMatricula().equals(cancelarReservaDialog.getMatricula())){
                    try {
                        if(servicioReserva.cancelarReserva(cancelarReservaDialog.getIdPlaza(),nombreUsuario)){
                            JOptionPane.showMessageDialog(null, "Reserva Cancelada Correctamente");
                            cancelarReservaDialog.dispose();
                            break;
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "La reserva no se puede cancelar no se ha encontrado");
                            cancelarReservaDialog.dispose();
                            break;
                        }
                    } catch (ExcepcionGeneralDB ex) {
                        throw new RuntimeException(ex);
                    }

                }
                JOptionPane.showMessageDialog(cancelarReservaDialog, "Matricula introducida incorreta !!");
                cancelarReservaDialog.limiarMatricula("");

                break;
        }

    }


}
