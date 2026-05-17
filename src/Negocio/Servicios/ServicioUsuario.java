package Negocio.Servicios;

import Negocio.Entidades.Usuario;
import Negocio.Excepciones.ExcepcionDatosIncorrectos;
import Negocio.Excepciones.ExcepcionFicheroConfig;
import Persistencia.Config.ConfigJSONDAO;
import Persistencia.Daoimpl.UsuarioDBDAO;
import Persistencia.persistenciaExcepciones.ExcepcionFicheroNoEncontrado;
import Persistencia.persistenciaExcepciones.ExcepcionGeneralDB;

import java.security.MessageDigest;

public class ServicioUsuario {

    private UsuarioDBDAO usuariDAO;
    private ConfigJSONDAO configJSONDAO;
    private Usuario usuario;

    public ServicioUsuario() throws ExcepcionFicheroConfig {
        try {
            usuariDAO = new UsuarioDBDAO();
            configJSONDAO = new ConfigJSONDAO();
        } catch (ExcepcionFicheroNoEncontrado e) {
            throw new ExcepcionFicheroConfig(e);
        }
    }

    public int registrarUsua(String nombreUser, String correrElectro, String contra, String repContra) throws ExcepcionDatosIncorrectos {
        try {
            String encryptedPassword;
            if (validarCorreoElectro(correrElectro)) {
                if (contra.equals(repContra)) {
                    if (comprbarMIT(contra)) {
                        encryptedPassword = encriptarContrasena(contra);
                        if (!usuariDAO.checkUsuario(nombreUser, correrElectro)) {
                            if (usuariDAO.registrarUsuario(nombreUser, correrElectro, encryptedPassword)) {
                                return 4; // todo correcto
                            }
                            return 3;
                        }
                        return 2; // ya existe
                    }
                    return 1; // contraseña no cumple política
                } else {
                    return 0; // contraseñas no coinciden
                }
            }
            return -1; // correo no válido
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionDatosIncorrectos(e);
        }
    }


    public boolean comprbarMIT(String contra) {
        if (contra.length() < 8 || contra.length() > 50) {
            return false;
        }

        boolean mayuscula = false;
        boolean minuscula = false;
        boolean number = false;
        boolean caracter = false;

        for (int i = 0; i < contra.length(); i++) {
            if (Character.isDigit(contra.charAt(i))) {
                number = true;
            } else if (Character.isLowerCase(contra.charAt(i))) {
                minuscula = true;
            } else if (Character.isUpperCase(contra.charAt(i))) {
                mayuscula = true;
            } else {
                caracter = true;
            }
        }

        return number && mayuscula && minuscula && caracter;
    }


    public String encriptarContrasena(String contrasena) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(contrasena.getBytes());

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

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

    public boolean inicioSessionAdmin(String contra) {
        return configJSONDAO.getAdminPass().equals(contra);
    }


    public boolean inicioSession(String nombre, String contra) throws ExcepcionDatosIncorrectos {
        try {
            String correoElectro = null;
            if (validarCorreoElectro(nombre)) {
                correoElectro = nombre;
                nombre = null;
            }
            String encryptedPassword = encriptarContrasena(contra);
            usuario = usuariDAO.getUsuario(nombre, correoElectro);

            if (usuariDAO.checkUsuario(nombre, correoElectro)) {
                if (usuariDAO.getUsuarioContraseña(nombre, correoElectro).equals(encryptedPassword)) {
                    return true;
                }
            }
            return false;
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionDatosIncorrectos(e);
        }
    }

    public void registrarAdmin() throws ExcepcionDatosIncorrectos {
        try {
            String encryptedPassword = encriptarContrasena("admin");
            if (!usuariDAO.checkUsuario("admin", "admin")) {
                usuariDAO.registrarUsuario("admin", "admin", encryptedPassword);
            }
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionDatosIncorrectos(e);
        }
    }

    /**
     * Devuelve el id (clave primaria en BD) del usuario identificado por nombre o correo.
     * Retorna null si no se encuentra o hay error.
     */
    public String idUsuarioParaSesion(String nombreOCorreo) {
        if (nombreOCorreo == null || nombreOCorreo.isBlank()) return null;
        try {
            if (validarCorreoElectro(nombreOCorreo)) {
                return usuariDAO.getUsuarioId(null, nombreOCorreo);
            } else {
                return usuariDAO.getUsuarioId(nombreOCorreo, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean eliminarCuenta(String name) throws ExcepcionDatosIncorrectos {
        try {
            if (validarCorreoElectro(name)) {
                return usuariDAO.eliminarUsuario(null, name);
            } else {
                return usuariDAO.eliminarUsuario(name, null);
            }
        } catch (ExcepcionGeneralDB e) {
            throw new ExcepcionDatosIncorrectos(e);
        }
    }

}
