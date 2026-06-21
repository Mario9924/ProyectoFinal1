package logica;

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

    
    public void modificarDatoUsuario(int opcionIn, String datoIn){
        /*
        pstmt.setString(1,usuarioIn.getNombre());
            pstmt.setString(2, usuarioIn.getEmail());
            pstmt.setString(3, usuarioIn.getPassword());
            pstmt.setString(4, usuarioIn.getFechaNacimiento());
            pstmt.setString(5, usuarioIn.getTelefono());
       */
        
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
    
    
}
