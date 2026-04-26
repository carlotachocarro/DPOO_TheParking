package Negocio.Servicios;

import Negocio.Entidades.Plaza;
import Negocio.Entidades.Reserva;
import Persistencia.Daoimpl.PlazaDBDAO;
import Persistencia.Daoimpl.ReservaDBDAO;
import Persistencia.Daoimpl.HistorialDBDAO;
import Persistencia.Config.ConfigJSONDAO;

import java.util.ArrayList;
import java.util.Random;

public class ServicioVehiculo {

    private PlazaDBDAO plazaDBDAO;
    private ReservaDBDAO reservaDBDAO;
    private HistorialDBDAO historialDAO;
    private Random random;

    public ServicioVehiculo() {
        this.plazaDBDAO = new PlazaDBDAO();
        this.reservaDBDAO = new ReservaDBDAO();
        this.historialDAO = new HistorialDBDAO();
        this.random = new Random();

    }

    //Funciones entrada y salida de vehiculos
    public String registroEntradaVehiculo(String matricula, String tipoVehiculo, String username) {
        ArrayList<Reserva> reservas = reservaDBDAO.getReservas();
        String IdplazaAsignada = null;
        //Bucle para ver si esta matricula tiene alguna reserva ya hecha
        for (Reserva r : reservas) {
            if (r.getMatricula().equals(matricula)) {
                IdplazaAsignada = r.getIdPlaza();
                break;
            }
        }
        //En caso de que no tenga reserva:
        if (IdplazaAsignada == null) {
            ArrayList<Plaza> plazasLibres = plazaDBDAO.getPlazasLibres(tipoVehiculo);
            if (!plazasLibres.isEmpty()) {
                IdplazaAsignada = plazasLibres.getFirst().getCodigoPlaza(); //Cogemos la primera disponible
            }
        }
        //Si se ha conseguido una plaza, se ocupa
        if (IdplazaAsignada != null) {
            if (plazaDBDAO.ocuparPlaza(IdplazaAsignada, true, matricula, username)) {
                return IdplazaAsignada;
            }
        }

        //Si no se ha conseguido una plaza, se retorna null
        return null;
    }

    public boolean registrarSalidaVehiculo(String matricula){
        ArrayList<Plaza> plazasTotal = plazaDBDAO.getPlazas();
        for(Plaza p : plazasTotal){
            if(p.getEstado_ocupado() && p.getMatricula().equals(matricula)){ //Buscamos la plaza que tenia reservada
                //Vaciamos plaza
                return plazaDBDAO.ocuparPlaza(p.getCodigoPlaza(), false, "none", null);
            }
        }
        //No se ha podido encontrar la matricula
        return false;
    }

    //Funciones para la simulacion del transito de vehiculos


    public double probabilidadEntrada(){
        ArrayList<Plaza> plazasTotal = plazaDBDAO.getPlazas();
        int availableNoReservadas = 0;
        int totalNoReservadas = 0;

        for(Plaza p : plazasTotal){
            if(!p.getEstado_reserva()){
                totalNoReservadas++;
                if(!p.getEstado_ocupado()){
                    availableNoReservadas++;
                }
            }
        }
        if(totalNoReservadas == 0){ //Si no hay plazas libres de reserva
            return 0;
        }
        else{
            return (double)availableNoReservadas/totalNoReservadas;
        }
    }

    private String generarMatricula(){
        String matriculaAleatoria = "";
        //4 numeros
        for(int i = 0; i<4; i++){
            matriculaAleatoria += random.nextInt(10);
        }

        //Creamos array con las letras que pueden contener las matriculas (todas las consonantes)
        String letras = "BCDFGHJKLMNPQRSTVWXYZ";
        for(int i = 0; i<3; i++){
            matriculaAleatoria += letras.charAt(random.nextInt(letras.length())); //Asignamos 3 letras
        }
        return matriculaAleatoria;
    }

    public boolean simulaEntrada(){
        ArrayList<Plaza> plazasTotal = plazaDBDAO.getPlazas();
        ArrayList<Plaza> plazasDisponibles = new ArrayList<>();

        for (Plaza p : plazasTotal) {
            if (!p.getEstado_reserva() && !p.getEstado_ocupado()) {
                plazasDisponibles.add(p);
            }
        }
        if(!plazasDisponibles.isEmpty()){
            Plaza plazaSeleccionada = plazasDisponibles.get(random.nextInt(plazasDisponibles.size()));
            String matriculaSimulada = generarMatricula();
            if(plazaDBDAO.ocuparPlazaSimul(plazaSeleccionada.getCodigoPlaza(), true, matriculaSimulada)){
                historialDAO.nuevoRegistro();
                return true;
            }
        }
        return false;

    }

    public boolean simulaSalida(){
        ArrayList<Plaza> plazasTotal = plazaDBDAO.getPlazas();
        ArrayList<Plaza> ocupadasSimuladas = new ArrayList<>();

        for(Plaza p : plazasTotal){
            //Si esta ocupado pero sin usuario asignado es simulada
            if(p.getEstado_ocupado() && p.getUser() == null){
                ocupadasSimuladas.add(p);
            }
        }
        if(!ocupadasSimuladas.isEmpty()){
            Plaza paraLiberar = ocupadasSimuladas.get(random.nextInt(ocupadasSimuladas.size()));
            if(plazaDBDAO.ocuparPlazaSimul(paraLiberar.getCodigoPlaza(), false, "none")){
                historialDAO.nuevoRegistro();
                return true;
            }
        }
        return false;
    }

    public boolean vehiculoEnParking(String matricula){
        ArrayList<Plaza> plazasTotal = plazaDBDAO.getPlazas();
        for(Plaza p: plazasTotal){
            if(p.getEstado_ocupado() && p.getMatricula().equals(matricula)){
                return true;
            }
        }
        return false;
    }

    public int disponibilidadPorTipo(String tipo){
        return plazaDBDAO.getPlazasLibres(tipo).size();
    }
}
