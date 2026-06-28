package logica;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Esta clase permite realizar varias comprobaciones sobre los distintos campos
 * que se necesitan para acceder a la cuenta de usuario se validan datos como el
 * nombre, el dni del usuario, la password, la fecha o el importe
 *
 *
 * @author Mario Gutiérrez González
 */
public class Validacion {

    /**
     * Esta función permite validar si un String es vacío "" o no contiene nada (null)
     * @param datoIn String con la información a comprobar
     * @return true si hay contenido, false si no es así
     */
    public static boolean validarString(String datoIn) {
        boolean resultado = true;
        if (datoIn.isBlank() || datoIn.isEmpty()) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Valida el DNI del usuario diviendo la parte numérica del dni entre 23
     *  con el resto de la división anterior, obtenemos una letra del listado (si está todo correcto)
     * @param dniIn String con el Dni del usuario
     * @return true si el dni es correcto, false si no lo es
     */
    public static boolean validarDni(String dniIn) {
        boolean resultado = true;
        // Comprobamos que no sea vacío o la longitud distinta a 9
        if (validarString(dniIn) && dniIn.length() == 9) {
            try {
                //Comprobamos que el DNI sea correcto            
                int resto = Integer.parseInt(dniIn.substring(0, 8)) % 23;
                //Si el resto que nos devuelve no corresponde al intervalo 0-22 no seguimos comprobando
                if (resto < 0 || resto > 22) {
                    resultado = false;
                } else {
                    //Comprobamos que la letra del dni introducido sea correspondiente a la que debe
                    char[] letras = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
                    if (letras[resto] != dniIn.charAt(8)) {
                        resultado = false;
                    }
                }
            } catch (NumberFormatException nfe){
                System.err.println("El dni no puede contener caracteres a menos que sea en la última posición: " + nfe);
                Log.escribirLog("El dni no puede contener caracteres a menos que sea en la última posición: " + nfe);
                resultado = false;
            }
        } else {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Esta función permite validar que un teléfono tenga 9 números y no caracteres entre medias
     * @param telf String con el teléfono
     * @return true si sólo se incluyen números, false si se incluyen caracteres o no se cumple con la longitud
     */
    public static boolean validarTelefono(String telf) {
        boolean resultado = true;
        if (validarString(telf) && telf.length() == 9) {
            try {
                int resultadoConversion = Integer.parseInt(telf);
            } catch (NumberFormatException nfe) {
                System.err.println("El numero no puede contener caracteres: " + nfe);
                resultado = false;
            }
        } else {
            resultado = false;
        }

        return resultado;
    }

    /**
     * Esta función comprueba mediante una expresión regular que un email sea correcto.
     * Se permiten las siguientes direcciones:
     * username@domain.com
     * user.name@domain.com 
     * user-name@domain.com 
     * username@domain.co.in
     * user_name@domain.com
     * @see "https://www.baeldung.com/java-email-validation-regex"
     * @param emailIn String con la dirección de email
     * @return true si el formato es correcto, false si no lo es
     */
    public static boolean validarEmail(String emailIn) {
        boolean resultado = true;
        if (validarString(emailIn)) {
            Pattern regexPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = regexPattern.matcher(emailIn);
            if (matcher.find() == false) {
                resultado = false;
            }
        } else {
            resultado = false;
        }
        return resultado;
    }

    
    /**
     * Esta función permite comprobar que el año introducido esté en el intervalo 1950-2100
     * @param yearIn Integer con el año elegido
     * @return true si está en el intervalo, false si no lo está
     */
    public static boolean comprobarYear(int yearIn) {
        boolean resultado = true;
        if (yearIn < 1950 || yearIn > 2100) {
            resultado = false;
        }
        return resultado;
    }

    /**
     * Esta función comprueba que el año introducido sea bisiesto o no
     * @param yearIn Integer con el año elegido
     * @return true si es bisiesto, false si no lo es
     */
    public static boolean comprobarYearBisiesto(int yearIn) {
        boolean resultado = false;
        if ((yearIn % 4 == 0 && yearIn % 100 != 0) || (yearIn % 400 == 0)){
            resultado = true;
        }
        return resultado;
    }

    /**
     * Esta función comprueba que el mes introducido sea del intervalo 1-12
     * @param mesIn Integer con el mes elegido
     * @return true si pertenece al intervalo elegido, false si no pertenece
     */
    public static boolean comprobarMes(int mesIn) {
        boolean resultado = true;
        if (mesIn < 1 || mesIn >= 13) {
            resultado = false;
        }
        return resultado;
    }
    
    /**
     * Esta función comprueba el día introducido en función del mes elegido y del si el año es bisiesto o no
     * @param diaIn Integer con el día del mes, ha de pertenecer al intervalo 1-31
     * @param mesIn Integer con el mes del año
     * @param yearIn Ineteger con el año elegido
     * @return true si la fecha completa es correcta, false si no lo es
     */
    public static boolean comprobarDia(int diaIn, int mesIn, int yearIn) {
        boolean resultado = true;
        // Comprobamos que el día esté en el intervalo correcto 1-31
        if (diaIn < 1 || diaIn > 31) {
            resultado = false;
            System.out.println("El dia no puede ser menor igual a 0 o superior a 31");
        } else {
            // Generamos un Hashmap con los meses y los días por mes, por defecto es un año bisiesto
            HashMap<Integer, Integer> listadoMeses = new HashMap<>();
            listadoMeses.put(1, 31);
            listadoMeses.put(2, 29);
            listadoMeses.put(3, 31);
            listadoMeses.put(4, 30);
            listadoMeses.put(5, 31);
            listadoMeses.put(6, 33);
            listadoMeses.put(7, 31);
            listadoMeses.put(8, 31);
            listadoMeses.put(9, 30);
            listadoMeses.put(10, 31);
            listadoMeses.put(11, 30);
            listadoMeses.put(12, 31);
            if (Validacion.comprobarYearBisiesto(yearIn)) {
                if (diaIn > listadoMeses.get(mesIn)) {
                    resultado = false;
                    System.out.println("El día no puede ser superior a " + listadoMeses.get(mesIn));
                }
            } else {
                // Si el año no es bisiesto, cambiamos los días que tiene el mes de Febrero
                listadoMeses.replace(2, 28);
                if (diaIn > listadoMeses.get(mesIn)) {
                    resultado = false;
                    System.out.println("El día no puede ser superior a " + listadoMeses.get(mesIn));
                }
            }
        }
        return resultado;
    }
    
    /**
     * Esta función permite validar que el importe sea superior a 0
     * @param importeIn Double con el importe introducido
     * @return true si es > 0, false si no lo es
     */
    public static boolean validarImporte(double importeIn){
        boolean resultado = false;
        if (importeIn > 0){
            resultado = true;
        }        
        return resultado;
    }
    
}
