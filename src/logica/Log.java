/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
// https://www.w3schools.com/JAVA/java_date.asp

/**
 *
 * @author Mario_
 */
public class Log {
    
    public static void escribirLog(String errorIn) {
        // Obtenemos la fecha actual, con la hora también
        LocalDateTime myDateObj = LocalDateTime.now();
        // Damos formato a los datos anteriores
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        File fichero = new File(".\\src\\ficheros", "log.txt");
        try (
                FileWriter fw = new FileWriter(fichero, Charset.forName("ISO-8859-1"), true)
                )
        {
            fw.write(formattedDate + " " + errorIn + "\n");
        } catch (IOException ex) {
            //Si da error también a la hora de guardar el error también lo mostraremos
            System.out.println("Ha ocurrido un error al escribir en el log de errores: " + ex);
            escribirLog("" +ex);
        }
        
    }

}
