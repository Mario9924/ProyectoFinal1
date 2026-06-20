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

    
    /**
     * Esta función permite comprobar la existencia de un usuario, a partir de su email, en la BD
     * @param emailIn dirección de email del usuario
     * @return true si no existe en la BD, false si no existe
     */
    public static boolean comprobarUsuario(String emailIn){
        boolean resultado = false;
        
         try (
                Connection conn = DriverManager.getConnection(url, user, pass);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Usuario where Email = " + emailIn);
                ) 
        {
            int contador = 0;
            while(rs.next()){
                contador++;
                if (contador > 0){
                    resultado = true;
                 break;
                }
            }
            System.out.println("Ya hay un usuario registrado con ese email");
        } catch (SQLException ex) {
            System.err.println("Error al trabajar con la BD: " +ex);
        }
        return resultado;
    }
    
    
    
    /**
     * Esta función estática permite a un usuario darse de alta en la BD, se
     * necesitan una serie de datos
     *
     * @param datosUsuario String[] con toda la información necesaria
     */
    public static void registroUsuario(String[] datosUsuario) {
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
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("Select * from `Usuario` where email = '" + datosUsuario[2] + "'");
                ) 
        {

            if (rs.isFirst?()) {
                System.out.println("El usuario ya está registrado con ese email, por favor, inténtalo de nuevo");
                Log.escribirLog("El usuario ya está registrado con ese email " + datosUsuario[2]);
            } else {
                try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO `Usuario`"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                        ) 
                {
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
                }
            }

        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }

    }

}
