package logica;


/**
 * Esta clase permite el volcado de los gastos de un usuario concreto a un objeto con el que poder trabajar.
 * Todos los objetos son instanciados con los datos disponibles en la BD del proyecto o tras registrar el mismo usando los datos
 * necesarios para darle de alta
 * 
 * @author Mario Gutiérrez González
 */
public class Gasto {
    
    /**
     * Este atributo FINAL se utiliza para identificar al gasto individualmente y proviene de la BD
     * No se permite su modificación pero si que se puede obtener con el get correspondiente
     */
    private final int identificador;
    private String concepto;
    private double importe;
    private String fecha;
    private int categoria;
    
    /**
     * Este constructor permite instanciar un objeto de tipo Gasto "manualmente" proporcionando todos los datos necesarios;
     * @param idIn Int con el identificador
     * @param conceptIn String con el nombre
     * @param importeIn Double con el importe
     * @param fechaIn String con la fecha 
     * @param categoriaIn String con el nombre de la categoría
     */
    public Gasto(int idIn, String conceptIn, double importeIn, String fechaIn, int categoriaIn){
        this.identificador = idIn;
        this.concepto = conceptIn;
        this.importe = importeIn;
        this.fecha = fechaIn;
        this.categoria = categoriaIn;
    }
    
    /**
     * Este constructor permite la instanciación de un Gasto tras registrarle, de este modo evitamos una consulta innecesaria a la BD
     *  puesto que ya disponemos de todos los datos necesarios a excepción del identificador. El identificador se obtiene tras
     *  la insercción del gasto en la BD.
     * @param identificadorIn Int con el id del identificador en la BD
     * @param datosGastoIn String[] con los datos del gasto
     */
    public Gasto(int identificadorIn, String[] datosGastoIn){
        this.identificador = identificadorIn;
        this.concepto = datosGastoIn[0];
        this.importe = Double.parseDouble(datosGastoIn[1]);
        this.fecha = datosGastoIn[2];
        this.categoria = Integer.parseInt(datosGastoIn[4]);
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

    
    /**
     * Esta función permite la actualización de los atributos de un gasto. 
     * Recibe una opción y el dato a cambiar,tras ello, se usará el setter correspondiente para modificar un atributo u otro
     * @param opcionIn int con la opcion elegida
     * @param informacionIn String con la informacion a modificar
     */
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

    @Override
    public String toString() {
        return "Gasto{" + "concepto=" + concepto + ", importe=" + importe + ", fecha=" + fecha;
    }
    
}
