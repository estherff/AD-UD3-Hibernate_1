/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gal.teis.dao;

import java.io.Serializable;

import javax.persistence.*;


/**
 *
 * @author Esther Ferreiro
 * https://www.javatutoriales.com/2009/05/hibernate-parte-1-persistiendo-objetos.html
 * http://www.mastertheboss.com/jboss-frameworks/maven-tutorials/maven-hibernate-jpa/maven-and-hibernate-4-tutorial
 */
@Entity
@Table (name = "contactos")
public class Contacto implements Serializable {

    /**
     * La propiedad "id" mantendrá un valor único que identificará a cada una de
     * las instancias de "Contacto". El uso de un identificador único para cada
     * entidad es necesario si queremos utilizar todas las funcionalidades que
     * nos ofrece Hibernate. No debemos usar directamente este idetificador,
     * sino que debe ser la base de datos quin la genere al guardar la entidad e
     * Hibernate quien lo asigne al ojeto, por ello, el setter de id debe ser
     * privador. Hibernate podrá acceder a los atributos privados
     */
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)//autoincremental
    private int id;
    @Column(name = "nombre", length = 45, nullable = false)
    private String nombre;
    @Column(name = "email", length = 45, nullable = false)
    private String email;
    @Column(name = "telefono", length = 9, nullable = false)
    private String telefono;

    /**
     * El constructor sin argumentos es obligatorio ya que Hibernate creará
     * instancias de esta clase usando reflexion cuando recupere las entidades
     * de la BD. Este constructor puede ser privado (si es que no quieren
     * permitir que alguien más lo utilice), pero usualmente el nivel de acceso
     * más restrictivo que usaremos es el de paquete (el default), ya que esto
     * hace más eficiente la creación de los objetos.
     */
    Contacto() {
    }

    public Contacto(String nombre, String email, String telefono) {

        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Contacto: " + id + ", nombre: " + nombre + ", teléfono: " + telefono + ", emal: " + email;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nombre == null) ? 0 : telefono.hashCode());
        result = prime * result + id;
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Contacto other = (Contacto) obj;
        if (telefono == null) {
            if (other.telefono != null) {
                return false;
            }
        } else if (!telefono.equals(other.telefono)) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (nombre == null) {
            if (other.nombre != null) {
                return false;
            }
        } else if (!nombre.equals(other.nombre)) {
            return false;
        }
        return true;
    }

}
