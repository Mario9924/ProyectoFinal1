package logica;

import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * Esta clase permite encriptar contraseñas y comprobar si son correctas mediante el uso la API BCrpyt
 * No es necesaria la instanciación de un objeto de esta clase para su funcionamiento puesto que los métodos que la componen son estáticos
 * 
 * @author Mario Gutiérrez González
 */
public class Encriptacion{
        
    /**
     * Función que realiza el hash correspondiente a la contraseña introducida
     * @param password String con la contraseña del usuario
     * @return String con el hash generado
     */
    public static String generarHash(String password){
        String hash = "";
        hash = BCrypt.hashpw(password, BCrypt.gensalt());
        return hash;
    }
    
    
    /**
     * Esta función comprueba que la contraseña introducida al iniciar sesión sea la misma que se ha almacenado en la BD.
     * Comprueba el hash de la contraseña introducida con el hash que se generó previamente
     * @param password String con la contraseña introducida
     * @param hash String con el hash almacenado en la BD
     * @return true si coinciden los hash's, false si no coinciden
     */
    public static boolean verificarHash(String password, String hash){
        boolean resultado = false;
        resultado = BCrypt.checkpw(password, hash);
        return resultado;
    }
    
}
