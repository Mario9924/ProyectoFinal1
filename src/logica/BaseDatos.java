/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Mario_
 */
public class BaseDatos {
    private static String url = "jdbc:mysql://192.168.1.21:3306/proyecto";
    private static String user = "root";
    private static String pass = "root";
    
    public static void registroUsuario(String[] datosUsuario){
        /*
            0 - Dni
            1 - Nombre
            2 - Email
            3 - Password
            4 - FechaNacimiento
            5 - Rol (por defecto usuario normal)
            6 - Activo (por defecto 1)
            7 - Telefono
        */
        try (
               
                Connection conn = DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO `Usuario`"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                
            ){
            /*            
            INSERT INTO `Usuario` (`Dni`, `Nombre`, `Email`, `Password`, `FechaNacimiento`, `Rol`, `Activo`, `Telefono`) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            */
            
            pstmt.setString(1, datosUsuario[0]); // dni
            pstmt.setString(2, datosUsuario[1]); // nombre
            pstmt.setString(3, datosUsuario[2]); // email
            pstmt.setString(4, datosUsuario[3]); // password
            
            // Tenemos que parsear primero la fecha desde string al tipo Date para que no haya problemas
            LocalDate date = LocalDate.parse(datosUsuario[4], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            pstmt.setDate(5, Date.valueOf(date)); // fechanacimiento
            
            pstmt.setString(6, datosUsuario[5]); // rol
            pstmt.setString(7, datosUsuario[6]); // activo
            pstmt.setString(8, datosUsuario[7]); // telefono
            pstmt.executeUpdate();
            System.out.println("Alta de usuario correcta");
        }  catch(SQLException sqlex){
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        }
        catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
        
        
        
        
    
    }
    
}
