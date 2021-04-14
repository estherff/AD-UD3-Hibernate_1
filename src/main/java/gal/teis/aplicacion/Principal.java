/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gal.teis.aplicacion;

import gal.teis.modelo.Contacto;
import gal.teis.presistencia.HibernateUtil;
import gal.teis.presistencia.ContactosDAO;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author Esther Ferreiro
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ContactosDAO contactosDAO = new ContactosDAO();
        Contacto contactoRecuperado = null;
        int idAEliminar = 0;

        //Creamos tres instancias de Contacto
        Contacto contacto1 = new Contacto("Contacto1", "contacto1@contacto.com", "756438456");
        Contacto contacto2 = new Contacto("Contacto2", "contacto2@contacto.com", "756345456");
        Contacto contacto3 = new Contacto("Contacto3", "contacto3@contacto.com", "153438456");

        //Guardamos las tres instancias
        //Guardamos el id del contacto1 para usarlo posteriormente
        idAEliminar = contactosDAO.guardaContacto(contacto1);
        contactosDAO.guardaContacto(contacto2);
        contactosDAO.guardaContacto(contacto3);

        //Modificamos el contacto2 y lo actualizamos
        contacto2.setNombre("Contacto2 modificado");
        contactosDAO.actualizaContacto(contacto2);

        //Recuperamos el contacto1 de la BD
        contactoRecuperado = contactosDAO.obtenContacto(idAEliminar);
        System.out.println("El contacto recuperado es " + contactoRecuperado.getNombre());

        //Eliminamos el contactoRecuperado, el contacto3
        contactosDAO.eliminaContacto(contactoRecuperado);

        //Obtenemos la lista de contactos de la base de datos
        List<Contacto> listaContactos = contactosDAO.obtenListaContactos();
        System.out.println("Hay " + listaContactos.size() + " contactos en la base de datos");
        for (Contacto c : listaContactos) {
            System.out.println("-> " + c.getNombre());
        }
        //Cierra la sesión de Hibernate
        HibernateUtil.shutdown();
    }

}
