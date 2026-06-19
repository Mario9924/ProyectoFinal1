package logica;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Esta clase permite realizar varias comprobaciones sobre los distintos campos que se necesitan para acceder a la cuenta de usuario
 *  se validan datos como el nombre, la password o la fecha 
 * 
 * 
 * @author Mario Gutiérrez González
 */
public class Validacion {
    
    
    public static boolean validarString(String datoIn){
        boolean resultado = true;        
        if (datoIn.isBlank() || datoIn.isEmpty()){
            resultado = false;
        }        
        return resultado;
    }
    
    public static boolean validarDni(String dniIn){
        boolean resultado = true;
        // Comprobamos que no sea vacío o la longitud distinta a 9
        if (validarString(dniIn) && dniIn.length()==9){
            //Comprobamos que el DNI sea correcto            
            int resto = Integer.parseInt(dniIn.substring(0,8))%23;
            //Si el resto que nos devuelve no corresponde al intervalo 0-22 no seguimos comprobando
            if (resto < 0 || resto > 22){
                resultado = false;
            } else {
                //Comprobamos que la letra del dni introducido sea correspondiente a la que debe
                char[] letras = {'T','R','W','A','G','M','Y','F','P','D','X','B','N','J','Z','S','Q','V','H','L','C','K','E'};
                if (letras[resto] != dniIn.charAt(8)){
                    resultado = false;
                }
            }
        } else {
            resultado = false;
        }
        return resultado;
    }
    
    public static boolean validarTelefono(String telf){
        boolean resultado = true;
        if (validarString(telf) && telf.length()==9){
            try {
                int resultadoConversion = Integer.parseInt(telf);
            } catch (NumberFormatException e){
                System.err.println("El numero no puede contener caracteres");
                resultado= false;
            }
            
        } else {
            resultado = false;
        }
        
        return resultado;
    }
    
    public static boolean validarEmail(String emailIn){
        boolean resultado = true;
        if (validarString(emailIn)){
            Pattern regexPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = regexPattern.matcher(emailIn);
            if (matcher.find()==false){
                resultado = false;
            }
        } else {
            resultado = false;
        }
        return resultado;
    }
    
}
