/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gal.teis.presistencia;

import gal.teis.negocio.Contacto;
import java.util.List;
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
    private Session sesion;
    private Transaction transa;

    /**
     * obtenemos una referencia a "SessionFactory" usando nuestra clase de
     * utilidad "HibernateUtil". Una vez que tenemos la "SessionFactory" creamos
     * una conexión a la base de datos e iniciamos una nueva sesión con el
     * método "openSession()". Una vez teniendo la sesión iniciamos una nueva
     * transacción y obtenemos una referencia a ella con "beginTransaction()"
     *
     */
    private void iniciaOperacion() throws HibernateException {
        sesion = HibernateUtil.getSessionFactory().openSession();
        transa = sesion.beginTransaction();
    }

    /**
     * Si se produce una excepción queremos que la transacción que se está
     * ejecutando se deshaga y se relance la excepción (podríamos lanzar una
     * excepción propia)
     */
    private void manejaExcepcion(HibernateException he) throws HibernateException {
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
     * @return
     */
    public int guardaContacto(Contacto contacto) {
        int id = 0;

        try {
            iniciaOperacion();
            //guarda el contacto en la base de datos y devuelve el id generado
            id = (int) sesion.save(contacto);
            transa.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
        return id;
    }

    /**
     * Permite actualizar un contacto en la BD
     *
     * @param contacto Contacto, elemento a actualizar
     * @throws HibernateException
     */
    public void actualizaContacto(Contacto contacto) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.update(contacto);
            transa.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
    }

    /**
     * Elimina el contacto en la BD
     *
     * @param contacto Contacto, elemento a eliminar
     * @throws HibernateException
     */
    public void eliminaContacto(Contacto contacto) throws HibernateException {
        try {
            iniciaOperacion();
            sesion.delete(contacto);
            transa.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
            throw he;
        } finally {
            sesion.close();
        }
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
    public Contacto obtenContacto(int idContacto) throws HibernateException {
        Contacto contacto = null;

        try {
            iniciaOperacion();
            contacto = (Contacto) sesion.get(Contacto.class, idContacto);
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
    public List<Contacto> obtenListaContactos() throws HibernateException {
        List<Contacto> listaContactos = null;

        try {
            iniciaOperacion();
            listaContactos = sesion.createQuery("from Contacto").list();
        } finally {
            sesion.close();
        }

        return listaContactos;
    }
}
