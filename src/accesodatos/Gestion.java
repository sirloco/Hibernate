package accesodatos;

import javax.persistence.*;

@Entity
public class Gestion {
    private int id;
    private double cantidad;
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
    @Column(name = "cantidad", nullable = false, precision = 0)
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gestion gestion = (Gestion) o;

        if (id != gestion.id) return false;
        if (Double.compare(gestion.cantidad, cantidad) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        temp = Double.doubleToLongBits(cantidad);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
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
