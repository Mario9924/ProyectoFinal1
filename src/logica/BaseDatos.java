package logica;

import com.mysql.cj.jdbc.ConnectionImpl;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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
    public static boolean comprobarExistenciaUsuario(String emailIn) {
        boolean resultado = false;

        try (
                Connection conn = DriverManager.getConnection(url, user, pass); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from Usuario where Email ='" + emailIn + "'");) {
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
     * Esta función comprueba que el usuario esté activo para poder crear la
     * sesión
     *
     * @param emailIn email del usuario
     * @return True si el usuario está activo, false si no está activo
     */
    public static boolean usuarioActivo(String emailIn) {
        boolean resultado = false;

        try (
                Connection conn = DriverManager.getConnection(url, user, pass); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from Usuario where Email ='" + emailIn + "' and estado = `1`");) {
            int contador = 0;
            while (rs.next()) {
                contador++;
                if (contador > 0) {
                    resultado = true;
                    break;
                }
            }

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
                Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement pstmt = conn.prepareStatement("INSERT INTO `Usuario`"
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
    public static boolean comprobarClavesUsuario(String emailIn, String passIn) {
        boolean resultado = false;

        try (
                Connection conn = DriverManager.getConnection(url, user, pass); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from Usuario where Email ='" + emailIn + "' AND Password ='" + passIn + "'");) {
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
     * @return Objeto de tipo Usuario, has de hacer instanceof para obtener el
     * tipo correcto
     */
    public static Usuario iniciarSesion(String emailIn, String passIn) {
        Usuario userCreado = null;

        try (
                var conn = DriverManager.getConnection(url, user, BaseDatos.pass); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from Usuario where Email ='" + emailIn + "' AND Password ='" + passIn + "'");) {

            System.out.println("Patata");
            System.out.println("select * from Usuario where Email ='" + emailIn + "' AND Password ='" + passIn + "'");
            while (rs.next()) {
                System.out.println(rs.getString("email") + " " + rs.getString("Nombre") + " " + rs.getString("FechaNacimiento"));
                if (rs.getString("rol").equalsIgnoreCase("administrador")) {
                    userCreado = new Administrador(rs.getString("Nombre"), rs.getString("Dni"), rs.getString("email"), rs.getString("password"), rs.getString("fechaNacimiento"), rs.getString("telefono"), rs.getString("rol"), Integer.parseInt(rs.getString("activo")));
                } else {
                    userCreado = new Normal(rs.getString("Nombre"), rs.getString("Dni"), rs.getString("email"), rs.getString("password"), rs.getString("fechaNacimiento"), rs.getString("telefono"), rs.getString("rol"), Integer.parseInt(rs.getString("activo")));
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
     * Esta función permite crear una usuario mediante un array de String que
     * contiene los datos
     *
     * @param datosIn String[] con los datos necesarios para el usuario
     * @return devuelve un objeto de tipo Usuario que luego se ha de castear al
     * tipo correcto, en principio Normal
     */
    public static Usuario iniciarSesion(String[] datosIn) {
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

        userCreado = new Normal(datosIn[1], datosIn[0], datosIn[2], datosIn[3], datosIn[4], datosIn[7], datosIn[6], 1);

        return userCreado;
    }

    /**
     * Esta función actuaiza la línea en la que se encuentre el gasto realizado,
     * actualizará TODA la línea con los datos actuales del Gasto
     *
     * @param gastoIn Objeto de tipo Gasto, contiene toda la información del
     * gasto
     */
    public static void modificarGasto(Gasto gastoIn) {

        try (
                Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement pstmt = conn.prepareStatement("UPDATE `Gasto` "
                + "SET `Nombre` = ?, `Importe` = ?, `Fecha` = ?, `ID_categoria` = ? WHERE `Gasto`.`ID` = " + gastoIn.getIdentificador());) {
            pstmt.setString(1, gastoIn.getConcepto());
            pstmt.setDouble(2, gastoIn.getImporte());
            LocalDate date = LocalDate.parse(gastoIn.getFecha(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            pstmt.setDate(3, Date.valueOf(date)); // fechanacimiento
            pstmt.setInt(4, gastoIn.getIdentificador());
            int resultado = pstmt.executeUpdate();
            if (resultado == 1) {
                System.out.println("Actualización completada");
            } else {
                System.err.println("Algo ha salido mal con la actualización");
                Log.escribirLog("Algo ha salido mal con la actualización de datos para la sentencia: \"UPDATE `Gasto` \"\n"
                        + "                        + \"SET `Nombre` = ?, `Importe` = ?, `Fecha` = ?, `ID_categoria` = ? WHERE `Gasto`.`ID` = \" " + gastoIn.getIdentificador());
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }

    }

    /**
     * Esta función permite eliminar un gasto, realmente modifica el campo
     * activo de 1 a 0 en la BD
     *
     * @param identificadorGasto ID del gasto
     */
    public static void eliminarGasto(int identificadorGasto) {
        try (
                Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement pstmt = conn.prepareStatement("UPDATE `Gasto` "
                + "SET `activo`= `0` WHERE `Gasto`.`ID` = " + identificadorGasto);) {
            int resultado = pstmt.executeUpdate();
            if (resultado == 1) {
                System.out.println("Registro eliminado");
            } else {
                System.err.println("Algo ha salido mal con el borrado");
                Log.escribirLog("Algo ha salido mal con la actualización de datos para la sentencia: "
                        + "\"SET `activo`= `0` WHERE `Gasto`.`ID` = \"" + identificadorGasto);
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
    }

    /**
     * Esta función permite obtener un listado de las categorías con su ID y
     * nombre
     *
     * @return HashMap con la información de las categorías, siendo la clave el
     * ID numérico de la tabla
     */
    public static HashMap<Integer, String> obtenerCategorias() {
        HashMap<Integer, String> listadoCategorias = new HashMap<>();

        try (
                Connection conn = DriverManager.getConnection(url, user, pass); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("select * from Categoria");) {
            while (rs.next()) {
                listadoCategorias.put(rs.getInt("ID"), rs.getString("Nombre"));
            }

        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
        return listadoCategorias;
    }

    public static void actualizarDatosUsuario(Usuario usuarioIn) {
        try (
                Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement pstmt = conn.prepareStatement("UPADTE USUARIO set nombre = ?, Email = ?, Password = ?,"
                + " FechaNacimiento = ?, Telefono = ? where Dni = " + usuarioIn.getDni());) {
            pstmt.setString(1, usuarioIn.getNombre());
            pstmt.setString(2, usuarioIn.getEmail());
            pstmt.setString(3, usuarioIn.getPassword());
            pstmt.setString(4, usuarioIn.getFechaNacimiento());
            pstmt.setString(5, usuarioIn.getTelefono());
            int resultado = pstmt.executeUpdate();
            if (resultado > 0) {
                System.out.println("La actualización en la BD ha sido correcta");
            } else {
                System.err.println("Ha ocurrido un error a la hora de actualizar la información");
                Log.escribirLog("Ha ocurrido un error a la hora de actualizar la información UPADTE USUARIO set nombre = ?, Email = ?, Password = ?,"
                        + " FechaNacimiento = ?, Telefono = ? where Dni = " + usuarioIn.getDni());
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
    }

    /**
     * Esta función pasa de activo a no activo tanto al usuario como a los
     * gastos que haya realizado
     *
     * @param userIn Objeto de tipo Usuario sobre el que se va a modificar la
     * información
     */
    public static void eliminarUsuario(Usuario userIn) {
        try (
                Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement pstmt = conn.prepareStatement("UPDATE Usuario set activo='0' where email=?"); PreparedStatement pstmt2 = conn.prepareStatement("UPDATE Gasto set activo='0' where dni_usuario=?");) {
            pstmt.setString(1, userIn.getEmail());
            int resultado = pstmt.executeUpdate();
            if (resultado > 0) {
                System.out.println("El usuario ha sido eliminado de la BD");
            } else {
                System.err.println("Ha ocurrido un error a la hora de eliminar al usuario");
                Log.escribirLog("Ha ocurrido un error a la hora de eliminar al usuario en la consulta: "
                        + "UPDATE Usuario set activo='0' where email= " + userIn.getEmail());
            }
            pstmt2.setString(2, userIn.getDni());

            resultado = pstmt2.executeUpdate();

            if (resultado > 0) {
                System.out.println("Los gastos asociados del usuario han sido eliminados");
            } else {
                System.err.println("Ha ocurrido un error a la hora de eliminar los gastos usuario");
                Log.escribirLog("Ha ocurrido un error a la hora de eliminar los gastos del usuario en la consulta: "
                        + "UPDATE Usuario set activo='0' where email= " + userIn.getEmail());
            }

        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
    }

    /**
     * Esta función permite la creación de una categoría indicando solamente su
     * nombre
     *
     * @param nombreCategoria String con el nombre de la categoría
     */
    public static void crearCategoria(String nombreCategoria) {
        try (
                Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement pstmt = conn.prepareStatement("Insert into Categoria (`Nombre`) VALUES (?)");) {
            pstmt.setString(1, nombreCategoria);
            int resultado = pstmt.executeUpdate();
            if (resultado > 0) {
                System.out.println("La categoría ha sido creada");
            } else {
                System.err.println("Ha ocurrido un error a la hora de añadir la categoría");
                Log.escribirLog("Ha ocurrido un error a la hora de añadir la categoría en la consulta: "
                        + "Insert into Categoria (`Nombre`) VALUES (" + nombreCategoria + ")");
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
    }

    /**
     * Esta función permite modificar una categoría, su nombre, a partir del
     * identificador de dicha categoría.
     *
     * @param identificadorCategoria Integer identificador de la categoría
     * @param nuevoNombreCategoria String con el nuevo nombre de la categoría
     */
    public static void modificarCategoria(int identificadorCategoria, String nuevoNombreCategoria) {
        try (
                Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement pstmt = conn.prepareStatement("UPDATE Categoria set nombre= ? where id = " + identificadorCategoria);) {
            pstmt.setString(1, nuevoNombreCategoria);
            int resultado = pstmt.executeUpdate();
            if (resultado > 0) {
                System.out.println("La categoría ha sido modificada");
            } else {
                System.err.println("Ha ocurrido un error a la hora de añadir la categoría");
                Log.escribirLog("Ha ocurrido un error a la hora de modificar la categoría en la consulta: "
                        + "UPDATE Categoria set nombre= ? where id = " + identificadorCategoria);
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
    }

    /**
     * Esta función permite a los administradores visualizar el histórico de los
     * gastos por cada usuario
     */
    public static void mostrarHistoricoGastos() {
        try (
                Connection conn = DriverManager.getConnection(url, user, pass); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("Select email, dni from Usuario where activo='1'");) {
            while (rs.next()) {
                System.out.println("Para el usuario de email " + rs.getString("Email"));
                try (
                        Statement stmt2 = conn.createStatement(); 
                        ResultSet rs2 = stmt2.executeQuery("Select DATE_FORMAT(Fecha, '%d/%m/%Y') as Fecha, nombre, importe,"
                        + "(select nombre from Categoria where id = g1.ID) as categoria "
                        + "FROM Gasto g1 WHERE Activo = '1' AND Dni_usuario ='" + rs.getString("Dni") + "'");
                        ) 
                {
                    System.out.printf("%-10s | %-20s | %-10s | %-10s%n", "Fecha", "Concepto", "Importe", "Categoria");
                    while (rs2.next()) {
                        System.out.printf("%-10s | %-20s | %-10s | %-10s%n", rs2.getString("Fecha"), rs2.getString("nombre"), rs2.getString("Importe"), rs2.getString("Categoria"));
                    }
                    System.out.println("");
                } catch (SQLException sqlex) {
                    System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
                    Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
                } catch (Exception e) {
                    System.out.println("Error en la BD: " + e);
                    Log.escribirLog("Error en la BD: " + e);
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

    /**
     * Permite ver los gastos históricos a partir de una categoría
     *
     * @param categoria Integer con el id de la categoría
     */
    public static void mostrarHistoricoGastosCategoria(int categoria) {
        try (
                Connection conn = DriverManager.getConnection(url, user, pass); 
                Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery("Select * from Categoria where ID="+categoria);
                ) 
        {
            while (rs.next()) {
                System.out.println("Para la categoría: " + rs.getString("Nombre"));               
                double totalGastado = 0;
                try (
                        Statement stmt2 = conn.createStatement();
                        ResultSet rs2 = stmt2.executeQuery("Select DATE_FORMAT(Fecha, '%d/%m/%Y') as Fecha, nombre, importe "
                        + "FROM Gasto WHERE Activo = '1' AND ID_categoria =" + rs.getInt("ID"));
                    ) 
                {
                    System.out.printf("%-10s | %-20s | %-10s %n", "Fecha", "Concepto", "Importe");
                    while (rs2.next()) {
                        System.out.printf("%-10s | %-20s | %-10s %n", rs2.getString("Fecha"), rs2.getString("nombre"), rs2.getString("Importe"));
                        totalGastado += rs2.getDouble("Importe");
                    }
                    System.out.println("Siendo el total de todo ello: " + totalGastado + " euros");
                    System.out.println("");
                } catch (SQLException sqlex) {
                    System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
                    Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
                } catch (Exception e) {
                    System.out.println("Error en la BD: " + e);
                    Log.escribirLog("Error en la BD: " + e);
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
    
    /**
     * Esta función sin parámetros, permite al administrador obtener el histórico de gastos por categoría, la cantidad
     *  que se ha gastado por categoría y el total de todas las categorías
     */
    public static void mostrarHistoricoGastosCategoria(){
        try (
                Connection conn = DriverManager.getConnection(url, user, pass);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("Select * from Categoria");
            )
        {
            double gastadoTramo = 0;
            double totalGastado = 0;
            while (rs.next()){
                System.out.println("Para la categoría : " + rs.getString("Nombre"));
                try (
                        Statement stmt2 = conn.createStatement();
                        ResultSet rs2 = stmt2.executeQuery("Select DATE_FORMAT(Fecha, '%d/%m/%Y') as Fecha, nombre, importe "
                        + "FROM Gasto WHERE Activo = '1' AND ID_categoria =" + rs.getInt("ID"));
                    ) 
                {
                    System.out.printf("%-10s | %-20s | %-10s %n", "Fecha", "Concepto", "Importe");
                    while (rs2.next()) {
                        System.out.printf("%-10s | %-20s | %-10s %n", rs2.getString("Fecha"), rs2.getString("nombre"), rs2.getString("Importe"));
                        gastadoTramo += rs2.getDouble("Importe");
                    }
                    System.out.println("Siendo el total de todo ello: " + gastadoTramo + " euros");
                    System.out.println("");
                    totalGastado += gastadoTramo;
                    gastadoTramo = 0;
                } catch (SQLException sqlex) {
                    System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
                    Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
                } catch (Exception e) {
                    System.out.println("Error en la BD: " + e);
                    Log.escribirLog("Error en la BD: " + e);
                }
            }
            System.out.println("Todas las categorías han sumado un total de: " + totalGastado + " euros");
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.out.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
    }
}
