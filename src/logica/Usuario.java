package logica;

import java.util.HashMap;
import java.util.Scanner;
import static logica.Main.pedirFecha;
import static logica.Main.reader;
import mail.EnvioJakartaMail;

/**
 * Esta clase abstracta permite almacenar los datos de los dos posibles usuarios del proyecto:
 *  usuario normal
 *  usuario administrador
 * 
 * Esta clase tiene varios métodos disponibles para sus hijas:
 *  Iniciar Sesión
 *  Cerrar Sesión
 *  Registro en la aplicación
 * 
 * Todos los usuarios tienen una serie de atributos en común:
 *  nombre
 *  dni
 *  password
 *  rol (administrador o usuario normal)
 *  estado (0,1)
 * 
 * 
 * @author Mario Gutiérrez González
 */
public abstract class Usuario {
    private static Scanner reader = new Scanner(System.in);
    private String nombre;
    private String email;
    private String password;
    private String fechaNacimiento;
    private String telefono;
    private String dni;
    private String rol;
    private int estado;
    /**
     * Este atributo permite a todos los usuarios hijos el acceso a un listado de las categorías de la BD
     *  se almacena la información en un HashMap<Integer, String>  siendo la clave el ID de la categoría
     *  y el value el nombre de la misma.
     */
    private static  HashMap<Integer, String> listadoCategorias = new HashMap<>();
    
    public Usuario(String nombreIn, String dniIn, String emailIn, String passIn, String fechaNacimientoIn ,String telefonoIn, String rolIn , int estadoIn){
        this.nombre = nombreIn;
        this.dni = dniIn;
        this.email = emailIn;
        this.password = passIn;
        this.fechaNacimiento = fechaNacimientoIn;
        this.telefono = telefonoIn;
        this.rol = rolIn;
        this.estado = estadoIn;        
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }

   
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    

    public String getDni() {
        return dni;
    }

    public String getRol() {
        return rol;
    }

    public int getEstado() {
        return estado;
    }

    public HashMap<Integer, String> getListadoCategorias() {
        return listadoCategorias;
    }

    public void setListadoCategorias(HashMap<Integer, String> listadoCategorias) {
        this.listadoCategorias = listadoCategorias;
    }
    
    /**
     * Esta función permite de forma rápida actualizar los atributos del objeto Usuario mediante una serie de opciones:
     * 1- Modificar el nombre
     * 2- Modificar el email
     * 3- Modificar la contraseña
     * 4- Modificar la fecha de nacimiento
     * 5- Modificar el teléfono
     * @param opcionIn Int con la opción elegida
     * @param datoIn String con la información que se va a modificar
     */
    public void modificarDatoUsuario(int opcionIn, String datoIn){        
        switch(opcionIn){
            case 1:
                //Modificamos el nombre del usuario
                this.setNombre(datoIn);
                break;
            case 2:
                //Modificamos el email del usuario
                this.setEmail(datoIn);
                break;
            case 3:
                // Modificamos la contraseña del usuario
                this.setPassword(datoIn);
                break;
            case 4:
                // Modificamos la fecha de nacimiento
                this.setFechaNacimiento(datoIn);
                break;
            case 5:
                //Modificamos el teléfono
                this.setTelefono(datoIn);
                break;
        }
    }
    
    public static void addCategoria(int identificador, String nuevaCategoria){
        listadoCategorias.put(identificador, nuevaCategoria);
    }
    
    
    /**
     * Esta función permite registrar a un usuario en la BD, pueden usarlo tanto los administradores como los usuarios normales.
     * No es necesaria la isntanciación de un objeto para acceder a ello.
     */
    public static void registroUsuario() {
        String[] datosUsuario = new String[8];
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
        // Registro de usuario
        boolean emailRepetido = false;
        do {
            do {
                System.out.println("Introduce tu email por favor");
                datosUsuario[2] = reader.nextLine().toLowerCase();
            } while (Validacion.validarEmail(datosUsuario[2]) == false);
            if (BaseDatos.comprobarExistenciaUsuario(datosUsuario[2])) {
                System.out.println("Lo sentimos, pero el email ya está asociado a un usuario");
                emailRepetido = true;
            } else {
                emailRepetido = false;
            }
        } while (emailRepetido);
        do {
            System.out.println("Introduce tu dni, recuerda que ha de contener 8 números y una letra");
            datosUsuario[0] = reader.next().toUpperCase();
        } while (Validacion.validarDni(datosUsuario[0]) == false);
        do {
            System.out.println("Introduce tu nombre por favor");
            datosUsuario[1] = reader.nextLine();
        } while (Validacion.validarString(datosUsuario[1]) == false);
        do {
            System.out.println("Introduce tu contraseña por favor");
            datosUsuario[3] = reader.nextLine();
        } while (Validacion.validarString(datosUsuario[3]) == false);
        String passHash = Encriptacion.generarHash(datosUsuario[3]);
        datosUsuario[3] = passHash;
        // fecha nacimiento
        datosUsuario[4] = pedirFecha();
        datosUsuario[5] = "normal";
        datosUsuario[6] = "1";
        do {
            System.out.println("Introduce el número de teléfono por favor, no permitimos el formato +34, así que sólo usa números");
            datosUsuario[7] = reader.nextLine();
        } while (Validacion.validarTelefono(datosUsuario[7]) == false);
        for (String datosUsuario1 : datosUsuario) {
            System.out.println(datosUsuario1);
        }
        BaseDatos.registroUsuario(datosUsuario);
        System.out.println("Registro completado correctamente!! Tienes que iniciar sesión por primera vez");
        EnvioJakartaMail correo = new EnvioJakartaMail();
        correo.correoBienvenida(datosUsuario[2]);
    }
}
