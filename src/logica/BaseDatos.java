package logica;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase desde la que se va a gestionar la BD, ya sea el inicio de sesión, la gestión de usuarios o la gestión de gastos.
 * Se pone a disposición del usuario (normal o administrador) todos los métodos necesarios para poder trabajar con el programa
 * 
 * Esta clase tiene una serie de datos como la url de conexión a la BD, el usuario administrador y la contraseña de este
 * 
 * @author Mario Gutiérrez González
 */
public class BaseDatos {

    /**
     * Este atributo almacena la url de acceso a la BD
     */
    private static String url = "jdbc:mysql://192.168.1.21:3306/proyecto";
    /**
     * Este atributo almacena el user administrador de la BD
     */
    private static String user = "root";
    /**
     * Este atributo almacena la contraseña del usuario administrador de la BD
     */
    private static String pass = "root";

    public static void setUrl(String url) {
        BaseDatos.url = url;
    }

    public static void setUser(String user) {
        BaseDatos.user = user;
    }

    public static void setPass(String pass) {
        BaseDatos.pass = pass;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPass() {
        return pass;
    }

    
    
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
                Connection conn = DriverManager.getConnection(url, user, pass); 
                Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery("select * from Usuario where Email ='" + emailIn + "' and activo ='1'");) {
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

            pstmt.setString(1, datosUsuario[0]); // dni
            pstmt.setString(2, datosUsuario[1]); // nombre
            pstmt.setString(3, datosUsuario[2]); // email
            pstmt.setString(4, datosUsuario[3]); // password

            // Tenemos que parsear primero la fecha desde string al tipo Date para que no haya problemas
            LocalDate date = LocalDate.parse(datosUsuario[4], DateTimeFormatter.ofPattern("yyyy-M-d"));
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
                Connection conn = DriverManager.getConnection(url, user, pass); 
                Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery("select * from Usuario where Email ='" + emailIn + "'");) {
            int contador = 0;
            while (rs.next()) {
                contador++;
                if (contador > 0 && Encriptacion.verificarHash(passIn, rs.getString("password"))) {
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
     * @return Objeto de tipo Usuario, has de hacer instanceof para obtener el
     * tipo correcto
     */
    public static Usuario iniciarSesion(String emailIn) {
        Usuario userCreado = null;

        try (
                var conn = DriverManager.getConnection(url, user, BaseDatos.pass); 
                Statement stmt = conn.createStatement(); 
                ResultSet rs = stmt.executeQuery("select * from Usuario where Email ='" + emailIn + "'");) {

            while (rs.next()) {
                if (rs.getString("rol").equalsIgnoreCase("administrador")) {
                    userCreado = new Administrador(rs.getString("Nombre"), rs.getString("Dni"), rs.getString("email"), rs.getString("password"), rs.getString("fechaNacimiento"), rs.getString("telefono"), rs.getString("rol"), Integer.parseInt(rs.getString("activo")));
                } else if (rs.getString("rol").equalsIgnoreCase("normal")){
                    userCreado = new Normal(rs.getString("Nombre"), rs.getString("Dni"), rs.getString("email"), rs.getString("password"), rs.getString("fechaNacimiento"), rs.getString("telefono"), rs.getString("rol"), Integer.parseInt(rs.getString("activo")));
                }
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.err.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
        return userCreado;
    }

    /**
     * Esta función permite crear una usuario mediante un array de String que
     * contiene los datos del usuario
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
     * Esta función permite dar de alta un gasto indicando en un String[] los
     * siguientes datos: Nombre Importe Fecha Dni_usuario ID_categoria Al
     * terminar y si todo es correcto devolverá un número con el ID del último
     * Gasto creado para poder usarlo luego
     *
     * @param datosGasto String[] con los datos mencionados anteriormente
     * @return 0 si no encuentra nada, N si encuentra el identificador del
     * último gasto creado
     */
    public static int registrarGasto(String[] datosGasto) {
        int identificadorGasto = 0;
        try (
                Connection conn = DriverManager.getConnection(url, user, pass);
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Gasto (Nombre, Importe, Fecha, Dni_Usuario, ID_Categoria)"
                + "VALUES(?,?,?,?,?)");) {
            pstmt.setString(1, datosGasto[0]); //Nombre
            pstmt.setDouble(2, Double.parseDouble(datosGasto[1])); // Importe
            LocalDate date = LocalDate.parse(datosGasto[2], DateTimeFormatter.ofPattern("yyyy-M-d"));
            pstmt.setDate(3, Date.valueOf(date));
            pstmt.setString(4, datosGasto[3]); // Dni_Usuario
            pstmt.setInt(5, Integer.parseInt(datosGasto[4])); // ID_Categoria
            int resultado = pstmt.executeUpdate();
            if (resultado > 0) {
                System.out.println("Se ha realizado el registro del gasto correctamente");
                //Ahora necesito obtener el objeto 'Gasto' completo, de modo que el usuario lo añada a su lista

                try (
                        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                        ResultSet rs = stmt.executeQuery("select ID from Gasto where ID = (select max(ID) from Gasto)");
                        ) {
                    identificadorGasto = rs.getInt("ID");
                } catch (SQLException sqlex) {
                    System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
                    Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
                } catch (Exception e) {
                    System.err.println("Error en la BD: " + e);
                    Log.escribirLog("Error en la BD: " + e);
                }

            } else {
                System.err.println("Ha ocurrido un error a la hora de registrar el gasto");
                Log.escribirLog("Ha ocurrido un error al registrar un nuevo gasto para la instrucción:\n "
                        + "INSERT INTO Gasto (Nombre, Importe, Fecha, Dni_Usuario, ID_Categoria) VALUES(" + datosGasto[0] + " , " + datosGasto[1] + " , " + datosGasto[2] + " , " + datosGasto[3] + " , " + datosGasto[4] + ")");
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.err.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
        return identificadorGasto;
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
            pstmt.setDate(3, Date.valueOf(LocalDate.parse(gastoIn.getFecha(), DateTimeFormatter.ofPattern("yyyy-M-d"))));
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
            System.err.println("Error en la BD: " + e);
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
                Connection conn = DriverManager.getConnection(url, user, pass); 
                PreparedStatement pstmt = conn.prepareStatement("UPDATE `Gasto` "
                + "SET `activo`= '0' WHERE `Gasto`.`ID` = " + identificadorGasto);) {
            int resultado = pstmt.executeUpdate();
            if (resultado == 1) {
                System.err.println("Registro eliminado");
            } else {
                System.err.println("Algo ha salido mal con el borrado");
                Log.escribirLog("Algo ha salido mal con la actualización de datos para la sentencia: "
                        + "\"SET `activo`= `0` WHERE `Gasto`.`ID` = \"" + identificadorGasto);
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.err.println("Error en la BD: " + e);
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
            System.err.println("Error en la BD: " + e);
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
            System.err.println("Error en la BD: " + e);
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
                Connection conn = DriverManager.getConnection(url, user, pass); 
                PreparedStatement pstmt = conn.prepareStatement("UPDATE Usuario set activo='0' where email=?"); 
                PreparedStatement pstmt2 = conn.prepareStatement("UPDATE Gasto set activo='0' where dni_usuario=?");
            ) 
        {
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
            System.err.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
    }
    
    /**
     * Esta función permite a los administradores eliminar a un usuario mediante su dirección de email, no necesitan más información
     * @param email String con la dirección de email del usuario
     */
    public static void eliminarUsuario(String email){
        try (
                Connection conn = DriverManager.getConnection(url, user, pass); 
                PreparedStatement pstmt = conn.prepareStatement("UPDATE Usuario set activo='0' where email=?"); 
                PreparedStatement pstmt2 = conn.prepareStatement("UPDATE Gasto set activo='0' where dni_usuario=(select dni from Usuario where email = `?`)");) 
        {
            pstmt.setString(1, email);
            int resultado = pstmt.executeUpdate();
            if (resultado > 0){
                System.err.println("Se han eliminado los gastos asociados al usuario");
                pstmt2.setString(1,email);
                resultado = pstmt2.executeUpdate();
                if (resultado > 0){
                    System.out.println("Se ha eliminado al usuario");
                } else {
                    System.err.println("Algo ha salido mal y no hemos podido eliminar al usuario");
                }
            } else {
                System.err.println("Algo ha salido mal y no hemos podido eliminar los gastos del usuario");
            }
        }  catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.err.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
    }
    

    /**
     * Esta función permite a los administradores visualizar todos los usuarios del proyecto que se encuentre en activo y NO sean administradores
     * Devuelve por pantalla el nombre y el email de los usuarios, no tiene acceso a ningún otro dato
     */
    public static void mostrarUsuarios(){
        int contador = 0;
                try (
                Connection conn = DriverManager.getConnection(url, user, pass);
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = stmt.executeQuery("Select nombre, email from Usuario where activo = '1' and rol='normal'");
            )
        {
            if (rs.next()){
                rs.beforeFirst();
                while (rs.next()){
                    contador++;
                    System.out.println(contador + " " + rs.getString("Nombre") + " - " + rs.getString("Email"));
                }
            } else {
                System.out.println("No hay usuarios NO administradores por el momento");
            }
        }  catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.err.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
    }
    
    /**
     * Esta función permite la creación de una categoría indicando solamente su
     * nombre
     *
     * @param nombreCategoria String con el nombre de la categoría
     * @return Int con el identificador de la categoría creada
     */
    public static int crearCategoria(String nombreCategoria) {
        int identificador = 0;
        try (
                Connection conn = DriverManager.getConnection(url, user, pass); 
                PreparedStatement pstmt = conn.prepareStatement("Insert into Categoria (`Nombre`) VALUES (?)");) {
            pstmt.setString(1, nombreCategoria);
            int resultado = pstmt.executeUpdate();
            if (resultado > 0) {
                System.out.println("La categoría ha sido creada");
                try (
                     Statement stmt = conn.createStatement();
                        ResultSet rs  = stmt.executeQuery("Select max(ID) as ID from Categoria");
                        )
                {
                    identificador = rs.getInt("ID");
                }
            } else {
                System.err.println("Ha ocurrido un error a la hora de añadir la categoría");
                Log.escribirLog("Ha ocurrido un error a la hora de añadir la categoría en la consulta: "
                        + "Insert into Categoria (`Nombre`) VALUES (" + nombreCategoria + ")");
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.err.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
        return identificador;
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
                Connection conn = DriverManager.getConnection(url, user, pass); 
                PreparedStatement pstmt = conn.prepareStatement("UPDATE Categoria set nombre= ? where id = " + identificadorCategoria);) {
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
            System.err.println("Error en la BD: " + e);
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
            double gastadoTramo = 0;
            double totalGastado = 0;
            while (rs.next()) {
                System.out.println("Para el usuario de email " + rs.getString("Email"));
                try (
                        Statement stmt2 = conn.createStatement(); ResultSet rs2 = stmt2.executeQuery("Select DATE_FORMAT(Fecha, '%d/%m/%Y') as Fecha, nombre, importe,"
                        + "(select nombre from Categoria where id = g1.ID) as categoria "
                        + "FROM Gasto g1 WHERE Activo = '1' AND Dni_usuario ='" + rs.getString("Dni") + "'");) {
                    System.out.printf("%-10s | %-20s | %-10s | %-10s%n", "Fecha", "Concepto", "Importe", "Categoria");
                    while (rs2.next()) {
                        System.out.printf("%-10s | %-20s | %-10s | %-10s%n", rs2.getString("Fecha"), rs2.getString("nombre"), rs2.getString("Importe"), rs2.getString("Categoria"));
                        gastadoTramo += rs2.getDouble("Importe");
                    }
                    System.out.println("El usuario ha gastado un total de: " + gastadoTramo);
                    System.out.println("");
                    totalGastado += gastadoTramo;
                    gastadoTramo = 0;
                } catch (SQLException sqlex) {
                    System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
                    Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
                } catch (Exception e) {
                    System.err.println("Error en la BD: " + e);
                    Log.escribirLog("Error en la BD: " + e);
                }
            }
            System.out.println("El gasto total de todos los usuarios es de: " + totalGastado + " euros");
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.err.println("Error en la BD: " + e);
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
                Connection conn = DriverManager.getConnection(url, user, pass); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("Select * from Categoria where ID=" + categoria);) {
            while (rs.next()) {
                System.out.println("Para la categoría: " + rs.getString("Nombre"));
                double totalGastado = 0;
                try (
                        Statement stmt2 = conn.createStatement(); ResultSet rs2 = stmt2.executeQuery("Select DATE_FORMAT(Fecha, '%d/%m/%Y') as Fecha, nombre, importe "
                        + "FROM Gasto WHERE Activo = '1' AND ID_categoria =" + rs.getInt("ID"));) {
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
                    System.err.println("Error en la BD: " + e);
                    Log.escribirLog("Error en la BD: " + e);
                }
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.err.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
    }

    /**
     * Esta función permite ver los gastos del usuario, filtrando por una
     * categoría
     *
     * @param identificadorCategoria Integer con el id de la categoría
     * @param dniUsuario String con el dni del usuario
     */
    public static void mostrarHistoricoGastosUsuarioCategoria(int identificadorCategoria, String dniUsuario) {
        try (
                Connection conn = DriverManager.getConnection(url, user, pass); Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE); ResultSet rs = stmt.executeQuery("Select fecha, nombre, importe, (select nombre from Categoria where id = g1.ID_categoria) as Categoria"
                        + " from Gasto g1 where activo ='1' AND Dni_usuario='" + dniUsuario + "' and ID_categoria=" + identificadorCategoria);) {

            if (rs.next()) {
                double totalGasto = 0;
                String categoria = "";
                rs.beforeFirst();
                System.out.printf("%-10s | %-20s | %-10s | %-10s %n", "Fecha", "Concepto", "Importe", "Categoria");
                while (rs.next()) {
                    System.out.printf("%-10s | %-20s | %-10s | %-10s %n", rs.getString("Fecha"), rs.getString("Nombre"), rs.getString("Importe"), rs.getString("Categoria"));
                    totalGasto += rs.getDouble("importe");
                    categoria = rs.getString("Categoria");
                }
                System.out.println("El gasto total  de la categoría " + categoria + " es de " + totalGasto);
            } else {
                System.out.println("No hay gastos para la categoría elegida");
            }

        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos, usando la consulta: \n"
                    + "Select * from Gasto where activo ='1' AND Dni_usuario=" + dniUsuario + " and ID_categoria=" + identificadorCategoria);
        } catch (Exception e) {
            System.err.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
    }

    /**
     * Esta función sin parámetros, permite al administrador obtener el
     * histórico de gastos por categoría, la cantidad que se ha gastado por
     * categoría y el total de todas las categorías
     */
    public static void mostrarHistoricoGastosCategoria() {
        try (
                Connection conn = DriverManager.getConnection(url, user, pass); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("Select * from Categoria");) {
            double gastadoTramo = 0;
            double totalGastado = 0;
            while (rs.next()) {
                System.out.println("Para la categoría : " + rs.getString("Nombre"));
                try (
                        Statement stmt2 = conn.createStatement(); ResultSet rs2 = stmt2.executeQuery("Select DATE_FORMAT(Fecha, '%d/%m/%Y') as Fecha, nombre, importe "
                        + "FROM Gasto WHERE Activo = '1' AND ID_categoria =" + rs.getInt("ID"));) {
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
                    System.err.println("Error en la BD: " + e);
                    Log.escribirLog("Error en la BD: " + e);
                }
            }
            System.out.println("Todas las categorías han sumado un total de: " + totalGastado + " euros");
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.err.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }
    }

    /**
     * Esta función permite obtener un listado con los gastos que haya realizado
     * el usuario
     *
     * @param userDni String con el dni del usuario
     * @return ArrayList(Gasto) con los gastos que haya realizado el usuario
     */
    public static ArrayList gastosUsuario(String userDni) {
        ArrayList<Gasto> listadoGastos = new ArrayList<>();

        try (
                Connection conn = DriverManager.getConnection(url, user, pass); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * from Gasto where activo = '1' AND DNI_Usuario ='" + userDni + "'");) {
            while (rs.next()) {
                listadoGastos.add(new Gasto(rs.getInt("ID"), rs.getString("Nombre"), rs.getDouble("Importe"), rs.getString("Fecha"), rs.getInt("ID_categoria")));
            }
        } catch (SQLException sqlex) {
            System.err.println("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
            Log.escribirLog("Ha habido un error a la hora de trabajar con la base de datos: " + sqlex);
        } catch (Exception e) {
            System.err.println("Error en la BD: " + e);
            Log.escribirLog("Error en la BD: " + e);
        }

        return listadoGastos;
    }
    
    
}
