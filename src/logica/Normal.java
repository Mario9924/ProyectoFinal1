/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.util.ArrayList;
import java.util.InputMismatchException;
import static logica.Main.pedirFecha;
import static logica.Main.reader;

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
    
    
     /**
     * Esta función permite a un usuario normal el registro de un gasto en la BD
     */
    public void registrarGasto() {
        int opcionIdCategoria;
        String[] datosGasto = new String[5];
        // Registrar el gasto, necesitamos: Concepto Importe Fecha Dni_usuario ID_categoria
        do {
            System.out.println("Introduce el concepto del gasto por favor: ");
            datosGasto[0] = reader.nextLine();
        } while (Validacion.validarString(datosGasto[0]) == false);
        double importe = 0;
        do {
            try {
                System.out.println("Introduce el importe, recuerda que ha de ser superior a 0");
                importe = reader.nextDouble();
            } catch (InputMismatchException ime) {
                System.err.println("Has introducido un dato incorrecto, necesito un número con decimales (separado por comas): " + ime);
                reader.nextLine();
                Log.escribirLog("El usuario ha introducido un valor no decimal para el registro del gasto" + ime);
            }
        } while (Validacion.validarImporte(importe) == false);
        datosGasto[1] = importe + "";
        datosGasto[2] = pedirFecha();
        datosGasto[3] = this.getDni();
        do {
            System.out.println("Elige una categoría para el gasto");
            for (Integer indice : this.getListadoCategorias().keySet()) {
                System.out.println(indice + this.getListadoCategorias().get(indice));
            }
            opcionIdCategoria = reader.nextInt();
        } while (opcionIdCategoria <= 0 || this.getListadoCategorias().get(opcionIdCategoria) == null);
        datosGasto[4] = opcionIdCategoria+"";
        int identificadorGasto = BaseDatos.registrarGasto(datosGasto);
        this.getListadoGastos().add(new Gasto(identificadorGasto, datosGasto));
    }
}
