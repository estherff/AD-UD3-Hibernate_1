/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gal.teis.presistencia;

import gal.teis.modelo.Contacto;
import java.util.List;
import java.util.Objects;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Contiene los métodos para realizar las siguientes operaciones con Hibernate:
 * abrir sesión, crear transacción, agregar, eliminar, actualizar, obtener y
 * listaR elementos de la BB
 *
 * @author Esther Ferreiro
 */
public class ContactosDAO {

    /**
     * Objeto Session y Transaction para porder realizar operaciones sobre la BD
     */
    private static Session sesion;
    private static Transaction transa;

    /**
     * obtenemos una referencia a "SessionFactory" usando nuestra clase de
     * utilidad "HibernateUtil". Una vez que tenemos la "SessionFactory" creamos
     * una conexión a la base de datos e iniciamos una nueva sesión con el
     * método "openSession()". Una vez teniendo la sesión iniciamos una nueva
     * transacción y obtenemos una referencia a ella con "beginTransaction()"
     *
     */
    private static void iniciaOperacion() throws HibernateException {
        try {
            sesion = HibernateUtil.getSessionFactory().openSession();
            transa = sesion.beginTransaction();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Si se produce una excepción queremos que la transacción que se está
     * ejecutando se deshaga y se relance la excepción (podríamos lanzar una
     * excepción propia)
     */
    private static void manejaExcepcion(HibernateException he) {
        transa.rollback();
        throw new HibernateException("Ha sucedido un error en la capa de acceso a datos", he);
    }

    /*
    Ahora crearemos los métodos que nos permitirán realizar las tareas de persistencia 
    de una entidad "Contacto", conocidas en lenguaje de base de datos como CRUD: guardarla, 
    actualizarla, eliminarla, buscar un entidad "Contacto" y obtener todas los contactos 
    que existen en la base de datos
     */
    /**
     * Permite guardar un contacto en la BD
     *
     * @param contacto Contacto, elemento a insertar en la BD
     * @return devuelve el id generado para el elemento guardado o -1 si la
     * operación no se ha podido realizar
     */
    public static long guardaContacto(Contacto contacto) {
        long id = -1;
        try {
            //abre la sesión e inicia la transición
            iniciaOperacion();
            /*guarda el contacto en la base de datos y devuelve el id generado
            aquí no se usa, pero se podría utilizar*/
            id = (long) sesion.save(contacto);
            transa.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            sesion.close();
        }
        return id;
    }

    /**
     * Permite actualizar un contacto en la BD a partir del id que lo identifica
     *
     * @param contacto Contacto, elemento a actualizar
     * @throws HibernateException
     */
    public static boolean actualizaContacto(Contacto contacto) {
        boolean actualizado = false;

        try {
            //abre la sesión e inicia la transición
            iniciaOperacion();

            //Actualiza el contacto 
            sesion.update(contacto);
            transa.commit();
            actualizado = true;

        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            sesion.close();
        }
        return actualizado;
    }

    /**
     * Elimina el contacto en la BD Previamente lo obtiene de la BD para
     * confirmar que existe
     *
     * @param contacto Contacto, elemento a eliminar
     * @return si la operación ha sido realizada con éxito, devuelve true
     * @throws HibernateException
     */
    public static boolean eliminaContacto(long id) {

        boolean eliminado = false;
        try {
            //abre la sesión e inicia la transición
            iniciaOperacion();

            //Antes de eliminar el objeto debe ser recuperado de la BD con get
            Contacto contacto_get = sesion.get(Contacto.class, id);

            //Elimina el contacto persistente si existe
            if (!Objects.isNull(contacto_get)) {
                sesion.delete(contacto_get);
                transa.commit();
                eliminado = true;
            }

        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            sesion.close();
        }
        return eliminado;
    }

    /**
     * Obtiene un contacto de la BD Para ejecutar las búsquedas se puede usar
     * get o load. La diferencia es el comportamiento cuando no se encuentra la
     * entidad, mientras get() devuelve null, load() lanza una excepción
     *
     * @param idContacto
     * @return
     * @throws HibernateException
     */
    public static Contacto obtenContacto(long id) {
        Contacto contacto = null;
        boolean obtenido = false;

        try {
            //abre la sesión e inicia la transición
            iniciaOperacion();

            contacto = sesion.get(Contacto.class, id);
            transa.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            sesion.close();
        }
        return contacto;
    }

    /**
     * Recupera todos los contactos
     *
     * @return devuleve un ojeto List con todos los contactos de la BD
     * @throws HibernateException
     */
    public static List<Contacto> obtenListaContactos() {
        List<Contacto> listaContactos = null;

        try {
            //abre la sesión e inicia la transición
            iniciaOperacion();
            listaContactos = sesion.createQuery("from Contacto").list();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            sesion.close();
        }

        return listaContactos;
    }
}
