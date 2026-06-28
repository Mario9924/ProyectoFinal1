/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.util.ArrayList;

/**
 * Esta clase permite la instanciación del usuario "normal" de nuestra aplicación
 * Los objetos de este tipo tienen asociados un ArrayList con los gastos realizados (en caso de tener)
 * 
 * @author Mario Gutiérrez González
 */
public class Normal extends Usuario {
    
    /**
     * ArrayList que contiene todos los gastos del usuario
     */
    private ArrayList<Gasto> listadoGastos = new ArrayList<>();
    
    public Normal(String nombreIn, String dniIn, String emailIn, String passIn, String fechaNacimientoIn, String telefonoIn, String rolIn, int estadoIn) {
        super(nombreIn, dniIn, emailIn, passIn, fechaNacimientoIn, telefonoIn, rolIn, estadoIn);
    }

    public ArrayList<Gasto> getListadoGastos() {
        return listadoGastos;
    }

    public void setListadoGastos(ArrayList<Gasto> listadoGastos) {
        this.listadoGastos = listadoGastos;
    }
    
}
