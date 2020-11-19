package accesodatos;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Piezas {
    private String codigo;
    private String nombre;
    private double precio;
    private String descripcion;
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
    @Column(name = "nombre", nullable = false, length = 20)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "precio", nullable = false, precision = 0)
    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Basic
    @Column(name = "descripcion", nullable = true, length = -1)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Piezas piezas = (Piezas) o;

        if (Double.compare(piezas.precio, precio) != 0) return false;
        if (codigo != null ? !codigo.equals(piezas.codigo) : piezas.codigo != null) return false;
        if (nombre != null ? !nombre.equals(piezas.nombre) : piezas.nombre != null) return false;
        if (descripcion != null ? !descripcion.equals(piezas.descripcion) : piezas.descripcion != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = codigo != null ? codigo.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        temp = Double.doubleToLongBits(precio);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "piezasByCodpieza")
    public Collection<Gestion> getGestionsByCodigo() {
        return gestionsByCodigo;
    }

    public void setGestionsByCodigo(Collection<Gestion> gestionsByCodigo) {
        this.gestionsByCodigo = gestionsByCodigo;
    }
}
