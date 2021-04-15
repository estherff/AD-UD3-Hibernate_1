/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gal.teis.aplicacion;

import gal.teis.excepciones.NumeroFueraRangoException;
import gal.teis.libreriadam.ControlData;
import gal.teis.libreriadam.Menu;
import gal.teis.modelo.Contacto;
import gal.teis.presistencia.HibernateUtil;
import gal.teis.presistencia.ContactosDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import org.hibernate.HibernateException;

/**
 *
 * @author Esther Ferreiro
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        //Variables de control de operaciones
        boolean finalizar = false;
        boolean correcta = false;

        //Obtenemos la lista de contactos de la base de datos
        do {
            switch (pintarMenu()) {
                case 1://Ver el contenido de la tabla
                    System.out.println("\n************************************************************\n"
                            + "LOS CONTACTOS ALMACENADOS EN LA TABLA SON: ");
                    mostrarTodos();
                    break;

                case 2://Guardar un contacto
                    System.out.println("\n************************************************************\n"
                            + "ALMACENAR CONTACTO");
                    menuGuardarElemento();
                    break;
                case 3://Buscar un contacto
                    System.out.println("\n************************************************************\n"
                            + "BUSCAR CONTACTO POR ID");
                    menuBuscarElemento();
                    break;
                case 4://Actualizar un contacto por su id
                    System.out.println("\n************************************************************\n"
                            + "ACTUALIZAR CONTACTO");
                    menuActualizarElemento();
                    break;
                case 5://Eliminar contacto
                    System.out.println("\n************************************************************\n"
                            + "ELIMINA CONTACTO");
                    menuEliminarElemento();
                    break;
                case 6:
                    System.out.println("Hasta luego!!!");
                    finalizar = true;
                    //Cierra la sesión de Hibernate
                    HibernateUtil.shutdown();

            }
        } while (!finalizar);
    }

    /**
     * Dibuja un menú en la consola a partir con unas opciones determinadas
     */
    static byte pintarMenu() {
        byte opcion = 0;
        boolean correcta;

        ArrayList<String> misOpciones = new ArrayList<String>() {
            {
                add("Listas contactos");
                add("Guardar un contacto");
                add("Buscar un contacto");
                add("Actualizar un contacto");
                add("Eliminar un contacto");
                add("Finalizar");
            }
        };

        /*La clase Menu permite imprimir el menú a partir de los datos de un ArrayList<String>
            y utilizar métodos para control de rango*/
        Menu miMenu = new Menu(misOpciones);

        System.out.println("\n\n*******************************************************************************************************");
        /* Solo sale del While cuando se selecciona una opción correcta en rango y tipo*/
        do {
            miMenu.printMenu();

            /*La clase ControlData permite hacer un control de tipo leído*/
            try {
                opcion = ControlData.lerByte(sc);
                /*miMenu.rango() lanza una excepción propia en el caso de que 
                el parámetro opcion esté fuera del rango posible */
                miMenu.rango(opcion);
                correcta = true;
            } catch (NumeroFueraRangoException e) {//Excepción personalizada
                System.out.println(e.getMessage());
                correcta = false;
            }

        } while (!correcta);

        return opcion;
    }

    /**
     * Muestra todos los elementos de una tabla
     */
    static void mostrarTodos() {
        List<Contacto> listaContactos = null;
        try {
            listaContactos = ContactosDAO.obtenListaContactos();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
        }
        if (!Objects.isNull(listaContactos)) {
            System.out.println("Hay " + listaContactos.size() + " contactos en la base de datos");
            for (Contacto c : listaContactos) {
                System.out.println("-> " + c.getNombre() + " -id- " + c.getId());
            }
        }
    }

    /**
     * Solicita los datos de un contacto y los guarda en la tabla
     */
    static void menuGuardarElemento() {
        //Pide los datos por teclado
        System.out.println("Introduce el nombre del contacto");
        String nombre = ControlData.lerNome(sc);
        System.out.println("Introduce el email del contacto");
        String email = ControlData.lerString(sc);
        System.out.println("Introduce el teléfono del contacto");
        String telefono = ControlData.lerString(sc);

        //Crea un contacto con los datos introducidos
        Contacto nuevoContacto = new Contacto(nombre, email, telefono);

        try {
            long id = ContactosDAO.guardaContacto(nuevoContacto);
            System.out.println("Se ha guardado el contacto que tendrá el id " + id);
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
        }

    }

    /**
     * Buca un contacto a partir de un id que se pide por teclado
     */
    static void menuBuscarElemento() {
        System.out.println("Introduce el id del elemento a buscar ");
        Long id = ControlData.lerLong(sc);
        try {
            Contacto contacto_localizado = ContactosDAO.obtenContacto(id);
            if (!Objects.isNull(contacto_localizado)) {
                System.out.println("El contacto ha sido localizado");
                System.out.println(contacto_localizado.toString());
            } else {
                System.out.println("El contacto no ha sido localizado");
            }
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
        }

    }

    /**
     * Actualiza un elemento de la tabla
     */
    static void menuActualizarElemento() {
        System.out.println("Introduce el id del elemento a buscar ");
        Long id = ControlData.lerLong(sc);
        boolean modificado = false;
        try {
            Contacto contacto_localizado = ContactosDAO.obtenContacto(id);
            if (!Objects.isNull(contacto_localizado)) {
                System.out.println("Confirme que el contacto a modificar es (S/N) " + contacto_localizado.toString());
                char siModificar = ControlData.lerLetra(sc);
                if (Character.toUpperCase(siModificar) == 'S') {//Realizamos la operación
                    byte opcion = pintarMenuModificar();
                    switch (opcion) {
                        case 1:
                            System.out.println("Introduce el nuevo nombre");
                            String nombre = ControlData.lerNome(sc);
                            contacto_localizado.setNombre(nombre);
                            modificado = ContactosDAO.actualizaContacto(contacto_localizado);
                            break;
                        case 2:
                            System.out.println("Introduce el nuevo email");
                            String email = ControlData.lerString(sc);
                            contacto_localizado.setEmail(email);
                            modificado = ContactosDAO.actualizaContacto(contacto_localizado);
                            break;
                        case 3:
                            System.out.println("Introduce el nuevo teléfono");
                            String telefono = ControlData.lerString(sc);
                            contacto_localizado.setTelefono(telefono);
                            modificado = ContactosDAO.actualizaContacto(contacto_localizado);
                            break;
                        default:
                            System.out.println("Operación de actualización cancelada");
                    }

                    if (modificado) {
                        System.out.println("El contacto ha sido modificado correctamente");
                    }
                } else {
                    System.out.println("Operación cancelada");
                }
            } else {
                System.out.println("El contacto no ha sido localizado");
            }
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
        }

    }

    /**
     * Pregunta qué datos se quiere modificar
     *
     * @return el dato a modificar
     */
    static byte pintarMenuModificar() {
        byte opcion = 0;
        boolean correcta;

        ArrayList<String> misOpciones = new ArrayList<String>() {
            {
                add("nombre");
                add("email");
                add("telefono");
                add("cancelar");
            }
        };

        /*La clase Menu permite imprimir el menú a partir de los datos de un ArrayList<String>
            y utilizar métodos para control de rango*/
        Menu miMenu = new Menu(misOpciones);

        System.out.println("\n\n*******************************************************************************************************");
        /* Solo sale del While cuando se selecciona una opción correcta en rango y tipo*/
        do {
            miMenu.printMenu();

            /*La clase ControlData permite hacer un control de tipo leído*/
            try {
                opcion = ControlData.lerByte(sc);
                /*miMenu.rango() lanza una excepción propia en el caso de que 
                el parámetro opcion esté fuera del rango posible */
                miMenu.rango(opcion);
                correcta = true;
            } catch (NumeroFueraRangoException e) {//Excepción personalizada
                System.out.println(e.getMessage());
                correcta = false;
            }

        } while (!correcta);

        return opcion;
    }

    /**
     * Elimina un elemento de la tabla en función de su id
     */
    static void menuEliminarElemento() {
        System.out.println("Introduce el id del elemento a eliminar ");
        Long id = ControlData.lerLong(sc);
        boolean eliminado = false;
        try {
            Contacto contacto_localizado = ContactosDAO.obtenContacto(id);
            if (!Objects.isNull(contacto_localizado)) {
                System.out.println("El contacto ha sido localizado");
                System.out.println(contacto_localizado.toString());
                System.out.println("¿Está seguro de que desea eliminarlo (S/N)?");
                char siEliminar = ControlData.lerLetra(sc);
                if (Character.toUpperCase(siEliminar) == 'S') {//Realizamos la operación
                    eliminado = ContactosDAO.eliminaContacto(contacto_localizado.getId());
                    if (eliminado) {
                        System.out.println("El objeto ha sido eliminado correctamente");
                    }
                }
            } else {
                System.out.println("El contacto no ha sido localizado");
            }
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
        }
    }

}
