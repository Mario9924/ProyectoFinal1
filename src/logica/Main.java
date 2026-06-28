package logica;

import java.util.InputMismatchException;
import java.util.Scanner;
import mail.EnvioJakartaMail;

/**
 *
 * Clase principal del proyecto, la cuál combina todas las clases generadas para su uso en la aplicación real
 * 
 * @author Mario Gutiérrez González
 */
public class Main {

    /**
     * Se define como público y estático un Scanner para que sea utilizable
     *  por todos los métodos y secciones de esta clase
     */
    public static Scanner reader = new Scanner(System.in);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Declaración de variables
        String[] datosGasto = new String[5];
        String emailUser = "";
        String password = "";
        String informacion = "";
        int opcionIdCategoria = 0;
        int opcionMenu = 0;
        int opcionMenuGastos = 0;
        boolean continuar = true;
        boolean userOk = false; // Variable que comprueba que el usuario haya podido acceder correctamente a su cuenta
        // Objetos que vamos a poder crear, a partir de los datos de la BD
        Normal userNormal = null;
        Administrador userAdmin = null;
        // Este objeto de tipo EnvioJakartaMail es necesario para el correcto envío de correos desde la aplicación
        EnvioJakartaMail correo = null;
        

        while (continuar) {
            try {
                System.out.println("Opciones del menú:"
                        + "\n1-Registro de usuario"
                        + "\n2-Iniciar sesión"
                        + "\n3-Salir");

                System.out.println("Introduce la opción que quieras");
                opcionMenu = reader.nextInt();
                switch (opcionMenu) {
                    case 1:
                        Usuario.registroUsuario();
                        break;
                    case 2:
                        // Inicio de sesión
                        for (int i = 0; i < 3; i++) {
                            System.out.println("Tienes 3 intentos para indicar un email válido");
                            System.out.println("Introduce tu email por favor");
                            emailUser = reader.next();
                            if (Validacion.validarEmail(emailUser) && BaseDatos.comprobarExistenciaUsuario(emailUser)) {
                                if (BaseDatos.usuarioActivo(emailUser)){
                                    i = 4;
                                userOk = true;
                                System.out.println("Email correcto!");
                                } else {
                                    System.out.println("El usuario está desactivado, ponte en contacto con soporte");
                                    i = 4;
                                    userOk = false;
                                    Log.escribirLog("El usuario " + emailUser + " ha intentado acceder siendo estando desactivado");
                                    continuar = false;
                                }
                            }
                        }

                        if (userOk) {
                            userOk = false;
                            // El usuario ahora necesita poner la contraseña
                            for (int i = 0; i < 3; i++) {
                                System.out.println("Introduce tu contraseña por favor, tienes un máximo de 3 intentos");
                                password = reader.next();
                                if (BaseDatos.comprobarClavesUsuario(emailUser, password)) {
                                    i = 4;
                                    userOk = true;
                                }
                            }

                            if (userOk) {
                                System.out.println("Sesión inicada correctamente");
                                correo = new EnvioJakartaMail();
                                if (BaseDatos.iniciarSesion(emailUser) instanceof Normal) {
                                    userNormal = (Normal) BaseDatos.iniciarSesion(emailUser);
                                } else {
                                    userAdmin = (Administrador) BaseDatos.iniciarSesion(emailUser);
                                }
                                continuar = false;
                            } else {
                                System.err.println("Lo sentimos pero la contraseña indicada es incorrecta");
                            }
                        } else {
                            System.err.println("Lo sentimos pero la dirección de email indicada es incorrecta");
                        }
                        break;
                    case 3:
                        // Salir del menú
                        System.out.println("Hasta luego");
                        continuar = false;
                        break;
                    default:
                        System.out.println("Opción inválida");
                        break;
                }
            } catch (InputMismatchException ime) {
                System.err.println("El dato introducido es incorrecto: " + ime);
                Log.escribirLog("Se ha introducido un dato erróneo en el menú de inicio/registro de sesión " + ime);
                reader.nextLine();
            }
        }

        continuar = true;
        if (userOk) {
            //Mostramos el menú correspondiente a cada tipo de usuario

            if (userNormal != null) {
                while (continuar) {
                    try {
                        System.out.println("Bienvenid@ a la aplicación de gestión de gastos, por favor, elige una opción:"
                                + "\n1-Registrar gasto"
                                + "\n2-Modificar gasto"
                                + "\n3-Eliminar gasto"
                                + "\n4-Consultar gastos"
                                + "\n5-Consultar gastos por categoría"
                                + "\n6-Salir");
                        System.out.println("Introduce una opción por favor:");
                        opcionMenu = reader.nextInt();

                        userNormal.setListadoGastos(BaseDatos.gastosUsuario(userNormal.getDni()));
                        userNormal.setListadoCategorias(BaseDatos.obtenerCategorias());
                        switch (opcionMenu) {
                            case 1:
                                userNormal.registrarGasto();
                                break;
                            case 2:
                                // Modificar gasto
                                do {
                                    try {
                                        System.out.println("Elige un gasto por favor, indicando el número del mismo:");
                                        for (int i = 0; i < userNormal.getListadoGastos().size(); i++) {
                                            System.out.println((i + 1) + " " + userNormal.getListadoGastos().get(i));
                                        }
                                        opcionMenu = reader.nextInt();
                                        opcionMenu--;
                                    } catch (InputMismatchException ime) {
                                        System.err.println("El usuario ha introducido un valor incorrecto!!" + ime);
                                        reader.nextLine();
                                        Log.escribirLog("El usuario ha introducido un valor incorrecto al elegir un gasto " + ime);
                                    } catch (Exception ex) {
                                        System.err.println("Ha ocurrido un error inesperado: " + ex);
                                        reader.nextLine();
                                        Log.escribirLog("Ha ocurrido un error inesperado: " + ex);
                                    }
                                } while (opcionMenu < 0 || opcionMenu > userNormal.getListadoGastos().size());

                                // Damos a elegir una opción para cambiar un dato u otro
                                do {
                                    try {
                                        System.out.println("Elige una opción por favor:"
                                                + "\n1 - Modificar concepto"
                                                + "\n2 - Modificar importe"
                                                + "\n3 - Modificar fecha"
                                                + "\n4 - Modificar categoria");
                                        opcionMenuGastos = reader.nextInt();
                                    } catch (InputMismatchException ime) {
                                        System.err.println("El usuario ha introducido un valor incorrecto!!" + ime);
                                        reader.nextLine();
                                        Log.escribirLog("El usuario ha introducido un valor incorrecto en el menú de sus opciones " + ime);
                                    } catch (Exception ex) {
                                        System.err.println("Ha ocurrido un error inesperado: " + ex);
                                        reader.nextLine();
                                        Log.escribirLog("Ha ocurrido un error inesperado: " + ex);
                                    }
                                } while (opcionMenuGastos <= 0 || opcionMenuGastos > 4);

                                switch (opcionMenuGastos) {
                                    case 1:
                                        // Modificamos el concepto
                                        do {
                                            System.out.println("Introduce el nuevo concepto por favor: ");
                                            informacion = reader.nextLine();
                                        } while (Validacion.validarString(informacion) == false);
                                        break;
                                    case 2:
                                        // Modificamos el importe
                                        do {
                                            System.out.println("Introduce el importe por favor:");
                                            informacion = reader.next().replace(".", ",");
                                        } while (Validacion.validarImporte(Double.parseDouble(informacion)));
                                        break;
                                    case 3:
                                        // Modificamos la fecha
                                        informacion = pedirFecha();
                                        break;
                                    case 4:
                                        // Modificamos la categoría
                                        do {
                                            System.out.println("Elige la nueva categoría del gasto por favor");
                                            for (Integer indice : userNormal.getListadoCategorias().keySet()) {
                                                System.out.println(indice + " - " + userNormal.getListadoCategorias().get(indice));
                                            }
                                            informacion = reader.nextInt() + "";
                                        } while (userNormal.getListadoCategorias().containsValue(informacion));
                                        break;
                                }

                                userNormal.getListadoGastos().get(opcionMenu).actualizarGasto(opcionMenuGastos, informacion);
                                BaseDatos.modificarGasto(userNormal.getListadoGastos().get(opcionMenu));
                                break;
                            case 3:
                                // Eliminar gasto
                                do {
                                    try {
                                        System.out.println("Elige un gasto a eliminar: ");
                                        for (int i = 0; i < userNormal.getListadoGastos().size(); i++) {
                                            System.out.println((i + 1) + " " + userNormal.getListadoGastos().get(i));
                                        }
                                        opcionMenuGastos = reader.nextInt();
                                        opcionMenuGastos--;
                                    } catch (InputMismatchException ime) {
                                        System.err.println("El dato introducido para eliminar el gasto es incorrecto: " + ime);
                                        Log.escribirLog("El dato introducido para eliminar el gasto es incorrecto: " + ime);
                                    }
                                } while (opcionMenuGastos < 0 || opcionMenuGastos > userNormal.getListadoGastos().size());

                                // Actualizamos en la base de datos
                                BaseDatos.eliminarGasto(userNormal.getListadoGastos().get(opcionMenuGastos).getIdentificador());
                                // Eliminamos del listado de gastos
                                userNormal.getListadoGastos().remove(opcionMenuGastos);

                                System.out.println("Se ha eliminado el gasto correctamente");
                                break;
                            case 4:
                                // Mostrar gastos, si no hay registros no se mostrará nada
                                if (userNormal.getListadoGastos() == null || userNormal.getListadoGastos().isEmpty()) {
                                    System.out.println("No hay nignún gasto por el momento, por favor, introduce uno");
                                } else {
                                    for (int i = 0; i < userNormal.getListadoGastos().size(); i++) {
                                        String categoriaGasto = userNormal.getListadoCategorias().get(userNormal.getListadoGastos().get(i).getCategoria());
                                        System.out.println((i + 1) + " " + userNormal.getListadoGastos().get(i) + " - " + categoriaGasto + '}');
                                    }
                                }
                                break;
                            case 5:
                                //Consultar gasto por categoría
                                do {
                                    System.out.println("Elige la categoría quer quieres consultar por favor");
                                    for (Integer indice : userNormal.getListadoCategorias().keySet()) {
                                        System.out.println(indice + " - " + userNormal.getListadoCategorias().get(indice));
                                    }
                                    informacion = reader.nextInt() + "";
                                } while (userNormal.getListadoCategorias().containsValue(informacion));

                                BaseDatos.mostrarHistoricoGastosUsuarioCategoria(Integer.parseInt(informacion), userNormal.getDni());

                                break;
                            case 6:
                                System.out.println("Hasta luego!!");
                                continuar = false;
                                break;
                            default:
                                System.out.println("Opción inválida");
                                break;
                        }
                    } catch (InputMismatchException ime) {
                        System.err.println("El usario ha introducido un valor incorrecto!!" + ime);
                        reader.nextLine();
                        Log.escribirLog("El usuario ha introducido un valor incorrecto en el menú de sus opciones " + ime);
                    } catch (Exception ex) {
                        System.err.println("Ha ocurrido un error inesperado: " + ex);
                        reader.nextLine();
                        Log.escribirLog("Ha ocurrido un error inesperado: " + ex);
                    }
                }
            } else if (userAdmin != null) {
                while (continuar) {
                    try {
                        System.out.println("Bienvenido administrador"
                                + "\n"
                                + "\n1-Registrar a un usuario"
                                + "\n2-Eliminar a un usuario (no administrador)"
                                + "\n3-Crear categoría"
                                + "\n4-Modificar categoría"
                                + "\n5-Salir");
                        opcionMenu = reader.nextInt();
                        userAdmin.setListadoCategorias(BaseDatos.obtenerCategorias());
                        switch (opcionMenu) {
                            case 1:
                                Usuario.registroUsuario();
                                break;
                            case 2:
                                //Eliminar usuario
                                BaseDatos.mostrarUsuarios();
                                System.out.println("Introduce el email del usuario que quieras eliminar:");
                                emailUser = reader.nextLine();
                                if (BaseDatos.comprobarExistenciaUsuario(emailUser)) {
                                    System.out.println("Usuario encontrador, procedemos al borrado");
                                    BaseDatos.eliminarUsuario(emailUser);
                                } else {
                                    System.out.println("Lo sentimos, pero el email es incorrecto, comprueba que hayas escrito bien la dirección");
                                }
                                break;
                            case 3:
                                //Crear categoría
                                reader.nextLine();
                                do {
                                    System.out.println("A continuación mostraremos las categorías existentes: ");
                                    for (Integer indice : userAdmin.getListadoCategorias().keySet()) {
                                        System.out.println(indice + " - " + userAdmin.getListadoCategorias().get(indice));
                                    }
                                    System.out.println("Introduce la nueva categoría, no se puede repetir:");
                                    informacion = reader.nextLine().toLowerCase();
                                    char primeraLetra = informacion.charAt(0);
                                    primeraLetra -= 32;
                                    informacion = primeraLetra + informacion.substring(1, informacion.length());
                                } while (userAdmin.getListadoCategorias().containsValue(informacion));
                                int identificadorCategoria = BaseDatos.crearCategoria(informacion);
                                Usuario.addCategoria(identificadorCategoria, informacion);
                                System.out.println("Se ha añadido correctamente la categoría");
                                break;
                            case 4:
                                // Modificar categoría
                                int clave = 0;
                                do {
                                    try {
                                        System.out.println("A continuación mostraremos las categorías existentes: ");
                                        for (Integer indice : userAdmin.getListadoCategorias().keySet()) {
                                            System.out.println(indice + " - " + userAdmin.getListadoCategorias().get(indice));
                                        }
                                        System.out.println("Indica el número de la categoría a modificar");
                                        clave = reader.nextInt();
                                    } catch (InputMismatchException ime) {
                                        System.err.println("El usario administrador ha introducido un valor incorrecto!!" + ime);
                                        reader.nextLine();
                                        Log.escribirLog("El usuario administrador ha introducido un valor incorrecto en el menú de sus opciones " + ime);
                                    } catch (Exception ex) {
                                        System.err.println("Ha ocurrido un error inesperado: " + ex);
                                        reader.nextLine();
                                        Log.escribirLog("Ha ocurrido un error inesperado: " + ex);
                                    }
                                } while (userAdmin.getListadoCategorias().containsKey(clave) == false);
                                
                                do {
                                    System.out.println("Introduce el nuevo nombre de la categoría por favor, recuerda que no se puede repetir");
                                    informacion = reader.nextLine();
                                    char primeraLetra = informacion.charAt(0);
                                    primeraLetra -= 32;
                                    informacion = primeraLetra + informacion.substring(1, informacion.length());
                                } while(userAdmin.getListadoCategorias().containsValue(informacion));
                                
                                userAdmin.getListadoCategorias().replace(clave, informacion);
                                BaseDatos.modificarCategoria(clave, informacion);
                                break;
                            case 5:
                                System.out.println("Hasta luego!!");
                                continuar = false;
                                break;
                            default:
                                System.out.println("Opción incorrecta, elige de nuevo");
                                break;
                        }
                    } catch (InputMismatchException ime) {
                        System.err.println("El usario administrador ha introducido un valor incorrecto!!" + ime);
                        reader.nextLine();
                        Log.escribirLog("El usuario administrador ha introducido un valor incorrecto en el menú de sus opciones " + ime);
                    } catch (Exception ex) {
                        System.err.println("Ha ocurrido un error inesperado: " + ex);
                        reader.nextLine();
                        Log.escribirLog("Ha ocurrido un error inesperado: " + ex);
                    }
                }
            }
        }

    }

   

    /**
     * Esta función permite la petición de una fecha para poder optimizar el código
     * @return 
     */
    public static String pedirFecha() {
        String fecha = "";
        int year = 0;
        int mes = 0;
        int dia = 0;
        do {
            System.out.println("Introduce el año, recuerda que ha de ser en el intervalo 1995-2100");
            year = reader.nextInt();
        } while (Validacion.comprobarYear(year) == false);

        do {
            System.out.println("Introduce el mes por favor");
            mes = reader.nextInt();
        } while (Validacion.comprobarMes(mes) == false);

        
        do {
            System.out.println("Introduce el día por favor, ten en cuenta que los años bisiesto tienen 29 días en febrero");
            dia = reader.nextInt();
        } while (Validacion.comprobarDia(dia, mes, year) == false);
        return fecha = year + "-" + mes + "-" + dia;
    }

}
