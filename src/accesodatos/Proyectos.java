package accesodatos;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Proyectos {
    private String codigo;
    private String nombre;
    private String ciudad;
    private Collection<Gestion> gestionsByCodigo;

    @Id
    @Column(name = "codigo", nullable = false, length = 6)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Basic
    @Column(name = "nombre", nullable = false, length = 40)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "ciudad", nullable = true, length = 40)
    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proyectos proyectos = (Proyectos) o;

        if (codigo != null ? !codigo.equals(proyectos.codigo) : proyectos.codigo != null) return false;
        if (nombre != null ? !nombre.equals(proyectos.nombre) : proyectos.nombre != null) return false;
        if (ciudad != null ? !ciudad.equals(proyectos.ciudad) : proyectos.ciudad != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = codigo != null ? codigo.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (ciudad != null ? ciudad.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "proyectosByCodproyecto")
    public Collection<Gestion> getGestionsByCodigo() {
        return gestionsByCodigo;
    }

    public void setGestionsByCodigo(Collection<Gestion> gestionsByCodigo) {
        this.gestionsByCodigo = gestionsByCodigo;
    }
}
