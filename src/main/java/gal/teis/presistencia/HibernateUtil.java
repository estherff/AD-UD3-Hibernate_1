package gal.teis.presistencia;

import java.util.Objects;
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

    
    /**
     * Atributos privados y finales
     */
    private final SessionFactory laSessionFactory ;
    
    
    /**
     * atributo privado y estático de la misma clase
     */
    private static HibernateUtil elHibernateUtil;
 

    /**
     * Para obtener la instancia de la clase ya creada o crearla de nuevo
    * y devolver el atributo de tipo SessionFactory
     * @return Objeto de tipo SessionFactory
     */
    
    public static SessionFactory getSessionFactory() {
         if (Objects.isNull(elHibernateUtil)){
             elHibernateUtil = new HibernateUtil();
         }
        return elHibernateUtil.laSessionFactory;
    }
    
    /**
     * constructor privado que da valor a los atributos
     */
    private HibernateUtil(){
        try{
        laSessionFactory = new Configuration().configure().
                    buildSessionFactory(new StandardServiceRegistryBuilder().
                            configure().build());
        }catch (HibernateException e){
            throw new ExceptionInInitializerError(e);  
        }
    }
        
    /**
     * Close caches and connection pools
     */
    public static void shutdown() {
        if (getSessionFactory().isOpen())
        getSessionFactory().close();
    }

}
