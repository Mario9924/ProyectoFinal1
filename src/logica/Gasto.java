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
    
    public Gasto(int idIn, String conceptIn, double importeIn, String fechaIn, int categoriaIn){
        this.identificador = idIn;
        this.concepto = conceptIn;
        this.importe = importeIn;
        this.fecha = fechaIn;
        this.categoria = categoriaIn;
    }

    public int getIdentificador(){
        return identificador;
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

    
    
    public void actualizarGasto(int opcionIn, String informacionIn){
        switch(opcionIn){
            case 1:
                //Actualizamos el concepto
                this.setConcepto(informacionIn);
                break;
            case 2:
                //Actualizamos el importe
                this.setImporte(Double.parseDouble(informacionIn));
                break;
            case 3:
                //Actualizamos la fecha
                this.setFecha(informacionIn);
                break;
            case 4:
                //Actualizamos la categoría
                this.setCategoria(Integer.parseInt(informacionIn));
                break;
        }
    }
    
    
}
