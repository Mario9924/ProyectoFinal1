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
    private String password;
    private String dni;
    private String rol;
    private int estado;
    private boolean userOk;
    
    
    
    
    
}
