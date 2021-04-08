package gal.teis.presistencia;

import java.util.Objects;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Esta clase permite arrancar Hibernate y crear una instancia de
 * SessionFactory que utiliza el fichero de configuración (hibernate.cfg.xml)
 * donde está la información de la conexión con nuestra base de datos. A partir
 * de este objeto se puede abrir una sesión y crear transacciones.
 * El patrón Singleton garantiza que no se crearán dos de SessionFactory
 */
/**
 *
 * @author Esther Ferreiro
 */
public class HibernateUtil {

    /**
     * Atributos privados y finales
     */
    private final SessionFactory laSessionFactory;

    /**
     * atributo privado y estático de la misma clase
     */
    private static HibernateUtil elHibernateUtil;

    /**
     * Para obtener la instancia de la clase ya creada o crearla de nuevo y
     * devolver el atributo de tipo SessionFactory
     *
     * @return Objeto de tipo SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (Objects.isNull(elHibernateUtil)) {
            elHibernateUtil = new HibernateUtil();
        }
        return elHibernateUtil.laSessionFactory;
    }

    /**
     * constructor privado que da valor a los atributos
     */
    private HibernateUtil() {
        try {
            // carga el fichero de configuración hibernate.cfg.xml y crea un objeto SessionFactory
            laSessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (HibernateException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Close caches and connection pools
     */
    public static void shutdown() {
        if (getSessionFactory().isOpen()) {
            getSessionFactory().close();
        }
    }

}
