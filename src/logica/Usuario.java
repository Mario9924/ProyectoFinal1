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
    private String email;
    private String password;
    private String dni;
    private String rol;
    private int estado;
    /**
     * Si el usuario ha podido logearse será true
     */
    private boolean userOk;
    
    
    public Usuario(String emailIn, String passIn){
        this.email = emailIn;
        this.password = passIn;
    }

    public String getEmail() {
        return email;
    }

   
    public String getPassword() {
        return password;
    }

    

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public boolean isUserOk() {
        return userOk;
    }

    public void setUserOk(boolean userOk) {
        this.userOk = userOk;
    }
    
    
    
}
