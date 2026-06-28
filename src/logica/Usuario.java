package logica;

import java.util.HashMap;
import java.util.Scanner;

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
    
}
