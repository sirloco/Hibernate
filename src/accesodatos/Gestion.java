package accesodatos;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Gestion {
    private int id;
    private String codproveedor;
    private String codpieza;
    private String codproyecto;
    private Double cantidad;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "codproveedor", nullable = false, length = 6)
    public String getCodproveedor() {
        return codproveedor;
    }

    public void setCodproveedor(String codproveedor) {
        this.codproveedor = codproveedor;
    }

    @Basic
    @Column(name = "codpieza", nullable = false, length = 6)
    public String getCodpieza() {
        return codpieza;
    }

    public void setCodpieza(String codpieza) {
        this.codpieza = codpieza;
    }

    @Basic
    @Column(name = "codproyecto", nullable = false, length = 6)
    public String getCodproyecto() {
        return codproyecto;
    }

    public void setCodproyecto(String codproyecto) {
        this.codproyecto = codproyecto;
    }

    @Basic
    @Column(name = "cantidad", nullable = true, precision = 0)
    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gestion gestion = (Gestion) o;

        if (id != gestion.id) return false;
        if (codproveedor != null ? !codproveedor.equals(gestion.codproveedor) : gestion.codproveedor != null)
            return false;
        if (codpieza != null ? !codpieza.equals(gestion.codpieza) : gestion.codpieza != null) return false;
        if (codproyecto != null ? !codproyecto.equals(gestion.codproyecto) : gestion.codproyecto != null) return false;
        if (cantidad != null ? !cantidad.equals(gestion.cantidad) : gestion.cantidad != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (codproveedor != null ? codproveedor.hashCode() : 0);
        result = 31 * result + (codpieza != null ? codpieza.hashCode() : 0);
        result = 31 * result + (codproyecto != null ? codproyecto.hashCode() : 0);
        result = 31 * result + (cantidad != null ? cantidad.hashCode() : 0);
        return result;
    }
}
