package accesodatos;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Piezas {
    private String codigo;
    private String nombre;
    private String precio;
    private String descripcion;

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
    @Column(name = "precio", nullable = false, length = 20)
    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    @Basic
    @Column(name = "descripcion", nullable = false, length = -1)
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

        if (codigo != null ? !codigo.equals(piezas.codigo) : piezas.codigo != null) return false;
        if (nombre != null ? !nombre.equals(piezas.nombre) : piezas.nombre != null) return false;
        if (precio != null ? !precio.equals(piezas.precio) : piezas.precio != null) return false;
        if (descripcion != null ? !descripcion.equals(piezas.descripcion) : piezas.descripcion != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = codigo != null ? codigo.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (precio != null ? precio.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        return result;
    }
}
