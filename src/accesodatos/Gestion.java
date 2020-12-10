package accesodatos;

import javax.persistence.*;

@Entity
public class Gestion {
    private int id;
    private Double cantidad;
    private Proveedores proveedoresByCodproveedor;
    private Piezas piezasByCodpieza;
    private Proyectos proyectosByCodproyecto;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        if (cantidad != null ? !cantidad.equals(gestion.cantidad) : gestion.cantidad != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (cantidad != null ? cantidad.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "codproveedor", referencedColumnName = "codigo", nullable = false)
    public Proveedores getProveedoresByCodproveedor() {
        return proveedoresByCodproveedor;
    }

    public void setProveedoresByCodproveedor(Proveedores proveedoresByCodproveedor) {
        this.proveedoresByCodproveedor = proveedoresByCodproveedor;
    }

    @ManyToOne
    @JoinColumn(name = "codpieza", referencedColumnName = "codigo", nullable = false)
    public Piezas getPiezasByCodpieza() {
        return piezasByCodpieza;
    }

    public void setPiezasByCodpieza(Piezas piezasByCodpieza) {
        this.piezasByCodpieza = piezasByCodpieza;
    }

    @ManyToOne
    @JoinColumn(name = "codproyecto", referencedColumnName = "codigo", nullable = false)
    public Proyectos getProyectosByCodproyecto() {
        return proyectosByCodproyecto;
    }

    public void setProyectosByCodproyecto(Proyectos proyectosByCodproyecto) {
        this.proyectosByCodproyecto = proyectosByCodproyecto;
    }
}
