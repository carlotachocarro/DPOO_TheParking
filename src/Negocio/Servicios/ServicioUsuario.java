package Negocio.Servicios;

import Negocio.Entidades.Usuario;
import Persistencia.Daoimpl.Singleton;
import Persistencia.Daoimpl.UsuarioDAO;

import java.security.MessageDigest;
public class ServicioUsuario {
    private UsuarioDAO usuariDAO;

    public  ServicioUsuario() {

    }

    public int registrarUsua(String nombreUser, String correrElectro, String contra,String repContra) {
        String encryptedPassword ;
        if (validarCorreoElectro(correrElectro) == true){
            if (contra.equals(repContra)){
                if (comprbarMIT(contra)){
                    encryptedPassword  = encriptarContrasena(contra);// funcion para encriptar la contrasenya

                    if (usuariDAO.registrarUsuario(nombreUser,correrElectro,encryptedPassword)){
                        return 3;// es valido todo correo, longitud, caracteresm, reg base de datos
                    }
                    return 2;// es Valido correo, longitud , caracteres, no se ha registrado en la base de datps
                }
                return  1;// valdio el correo , la longitud, no los caracteres
            }
            else {
                return 0;// es valido el correo pero no la longitud de la contrasenya
            }
        }
        return -1;// no es valido el corre electronico
    }


    public boolean comprbarMIT(String contra){
        if ( contra.length() < 8 || contra.length() > 50){
            return false;
        }

        boolean mayuscula =false;
        boolean minuscula =false;
        boolean number = false;
        boolean caracter =false;

        for (int i = 0; i < contra.length(); i++){
            if (Character.isDigit(contra.charAt(i))){
                number = true;
            }else if(Character.isLowerCase(contra.charAt(i))){
                minuscula =  true;
            }
           else if (Character.isUpperCase(contra.charAt(i))){
                mayuscula = true;
            }
            else{
                caracter =  true;
            }

        }

        if (number && mayuscula && minuscula && caracter ){
            return true;
        }
        return  false;
    }


    public String encriptarContrasena(String contrasena) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");//función que transforma texto en algo irreconocible
            byte[] hash = md.digest(contrasena.getBytes());//→ convierte el texto a bytes (formato interno del ordenador)
            //→ aplica SHA-256
            //→ un array de bytes (byte[])

            StringBuilder hex = new StringBuilder();//Creamos un acumulador de texto
            for (byte b : hash) {//Recorremos cada byte del hash
                hex.append(String.format("%02x", b));//Convierte cada byte a hexadecimal
            }
            return hex.toString();// DEVOOLVEMOS LA STRING HASHEADA.

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public boolean validarCorreoElectro(String email) {
        if (email == null) {
            return false;
        }
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }

}
/**
 * Usuario se registra
 *         ↓
 * Validar formato email
 *         ↓
 * ¿Correo ya existe en BD?
 *         ↓
 * Guardar usuario como "NO VERIFICADO"
 *         ↓
 * Enviar email con enlace o código
 *         ↓
 * Usuario confirma
 *         ↓
 * Usuario pasa a "VERIFICADO"
 */
