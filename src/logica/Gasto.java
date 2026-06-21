/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.util.HashMap;

/**
 *
 * @author Mario_
 */
public class Gasto {
    
    private int identificador;
    private String concepto;
    private double importe;
    private String fecha;
    private int categoria;
    private static HashMap<Integer, String> listadoCategorias = new HashMap<>();
    
    public Gasto(int idIn, String conceptIn, double importeIn, String fechaIn, int categoriaIn){
        this.identificador = idIn;
        this.concepto = conceptIn;
        this.importe = importeIn;
        this.fecha = fechaIn;
        this.categoria = categoriaIn;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public static HashMap<Integer, String> getListadoCategorias() {
        return listadoCategorias;
    }

    public static void setListadoCategorias(HashMap<Integer, String> listadoCategorias) {
        Gasto.listadoCategorias = listadoCategorias;
    }
    
    
    
    
    
}
