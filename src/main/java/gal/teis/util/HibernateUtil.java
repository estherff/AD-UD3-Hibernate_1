package gal.teis.util;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;



/*
 * Con esta clase podemos obtener la sesión actual desde cualquier parte de nuestra aplicación.
* El patrón Singleton  garantiza que no se crearán dos instancias de la base de datos en la aplicación
 */
/**
 *
 * @author Esther Ferreiro
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();
 
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().
                    buildSessionFactory(new StandardServiceRegistryBuilder().
                            configure().build());

        } catch (HibernateException ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
 
    public static void shutdown() {
        // Close caches and connection pools
        if (getSessionFactory().isOpen())
        getSessionFactory().close();
    }

}
