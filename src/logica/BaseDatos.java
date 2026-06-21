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
     * Esta función permite comprobar la existencia de un usuario, a partir de
     * su email, en la BD
     *
     * @param emailIn dirección de email del usuario
     * @return true si existe en la BD, false si no existe
     */
    public static boolean comprobarUsuario(String emailIn) {
        boolean resultado = false;

        try (
                Connection conn = DriverManager.getConnection(url, user, pass); 
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from Usuario where Email ='" + emailIn + "'");) {
            int contador = 0;
            while (rs.next()) {
                contador++;
                if (contador > 0) {
                    resultado = true;
                    break;
                }
            }
//            System.out.println("Ya hay un usuario registrado con ese email");
//            Log.escribirLog("El usuario con email \"" + emailIn+"\" ya existe en la BD");
        } catch (SQLException ex) {
            System.err.println("Error al trabajar con la BD: " + ex);
            Log.escribirLog("Error al trabajar con la BD: " + ex);
        }
        return resultado;
    }

    /**
     * Esta función estática permite a un usuario darse de alta en la BD, se
     * necesitan una serie de datos que serán recibidos por parámetro con un
     * String[] El orde que ha de seguirse es el siguiente:
     *
     * 0 - Dni 1 - Nombre 2 - Email 3 - Password 4 - FechaNacimiento 5 - Rol
     * (por defecto usuario normal) 6 - Activo (por defecto 1) 7 - Telefono
     *
     * @param datosUsuario String[] con toda la información necesaria
     */
    public static void registroUsuario(String[] datosUsuario) {
        /*
        Formato que tiene nuestro String[]
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
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");) {
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

        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }

    }

    /**
     * Esta función comprueba que los datos de inicio de sesión sean correctos,
     * se usa tras comprobar la existencia del email
     *
     * @param emailIn String email
     * @param passIn String password
     * @return true si el email y contraseña son correctos, false si la
     * contraseña es incorrecta
     */
    public static boolean iniciarSesion(String emailIn, String passIn) {
        boolean resultado = false;

        try (
                Connection conn = DriverManager.getConnection(url, user, pass); 
                Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery("select * from Usuario where Email ='" + emailIn + "' AND Password ='" + passIn + "'");
                ) 
        {
            int contador = 0;
            while (rs.next()) {
                contador++;
                if (contador > 0) {
                    resultado = true;
                    break;
                }
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
        return resultado;
    }

    /**
     * Permite crear la sesión del usuario, siempre se utiliza tras comprobar si
     * el email y contraseña son correctos
     *
     * @param emailIn email del usuario
     * @param passIn password del usuario
     * @return Objeto de tipo Usuario, has de hacer instanceof para obtener el tipo correcto
     */
    public static Usuario crearSesion(String emailIn, String passIn) {
        Usuario userCreado = null;
        
        try (
                var conn = DriverManager.getConnection(url, user, BaseDatos.pass);
                Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery("select * from Usuario where Email ='" + emailIn + "' AND Password ='" + passIn + "'");
            ) 
        {

            System.out.println("Patata");
            System.out.println("select * from Usuario where Email ='" + emailIn + "' AND Password ='" + passIn + "'");
            while (rs.next()) {
                System.out.println(rs.getString("email") + " " + rs.getString("Nombre") + " " + rs.getString("FechaNacimiento"));
                if (rs.getString("rol").equalsIgnoreCase("administrador")) {
                    userCreado = new Administrador(rs.getString("Dni"), rs.getString("email"), rs.getString("password"), rs.getString("fechaNacimiento"), rs.getString("telefono"), rs.getString("rol"), Integer.parseInt(rs.getString("activo")));
                } else {
                    userCreado = new Normal(rs.getString("Dni"), rs.getString("email"), rs.getString("password"), rs.getString("fechaNacimiento"), rs.getString("telefono"), rs.getString("rol"), Integer.parseInt(rs.getString("activo")));
                }
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
        return userCreado;
    }
    
    /**
     * Esta función permite crear una usuario mediante un array de String que contiene los datos 
     * @param datosIn String[] con los datos necesarios para el usuario
     * @return devuelve un objeto de tipo Usuario que luego se ha de castear al tipo correcto, en principio Normal
     */
    public static Usuario crearSesion(String[] datosIn){
        Usuario userCreado = null;
        /*
        Formato que tiene nuestro String[]
            0 - Dni
            1 - Nombre
            2 - Email
            3 - Password
            4 - FechaNacimiento
            5 - Rol (por defecto usuario normal)
            6 - Activo (por defecto 1)
            7 - Telefono
         */
        
        userCreado = new Normal(datosIn[0], datosIn[2], datosIn[3], datosIn[4], datosIn[7], datosIn[6], 1);
        
        return userCreado;
    }

}
