package vistas;

import accesodatos.HibernateUtil;
import accesodatos.Proveedores;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaProveedores extends JFrame {
    private JPanel panel1;
    private JPanel jpPrincipal;
    private JTabbedPane tabbedPane1;
    private JPanel jpGesProv;
    private JPanel jpLisProv;
    private JPanel jpGestionProv;
    private JTextField jtCod;
    private JTextField jtNom;
    private JTextField jtApellidos;
    private JTextField jtDir;
    private JButton bLimpiar;
    private JButton bEliminar;
    private JButton bModificar;
    private JButton bInsertar;
    private JTable tListado;

    // TODO Auto-generated method stub
    SessionFactory sesion = HibernateUtil.getSessionFactory();

    public VentanaProveedores() {

        add(panel1);

        setSize(500, 300);

        bInsertar.addActionListener(e -> insertarProveedor());// BOTON INSERTAR PROVEEDOR //
        bLimpiar.addActionListener(e -> limpiarProveedor());// BOTON LIMPIAR FORMULARIO //

    }

    private Proveedores buscarProveedorCod(String cod) {

        Session session = sesion.openSession();

        //Muestra el apellido y oficio del empleado con número 7369

        //Se crea la sentencia
        String hql = "from Proveedores where codigo = :cod";

        //Se lanza la sentencia
        Query q = session.createQuery(hql);

        //Se pasa el valor recibido
        q.setParameter("cod", cod);

        Proveedores pro = (Proveedores) q.uniqueResult(); //uniqueResult() se utiliza cuando sabemos que nos

        // va a devolver un único registro
        //System.out.format(" %s, %s \n", pro.getNombre(), pro.getApellidos());

        session.close();

        return pro;

    }

    private void limpiarProveedor() {

        jtCod.setText("");
        jtNom.setText("");
        jtApellidos.setText("");
        jtDir.setText("");

    }

    private void insertarProveedor() {

        Session session = sesion.openSession();
        Transaction tx = session.beginTransaction();

        //System.out.println("Inserto un EMPLEADO EN EL DEPARTAMENTO 10.");

        String codigo = jtCod.getText();
        String nombre = jtNom.getText();
        String apellidos = jtApellidos.getText();
        String direccion = jtDir.getText();

        //todo validaciones
        Proveedores p = buscarProveedorCod(codigo);

        if (p == null) {

            Proveedores pro = new Proveedores();

            pro.setCodigo(codigo);
            pro.setNombre(nombre);
            pro.setApellidos(apellidos);
            pro.setDireccion(direccion);

            try {

                session.save(pro);

                try {

                    tx.commit();

                    JOptionPane.showMessageDialog(null, "Proveedor " + pro.getNombre() + " Insertado",
                            "Info", JOptionPane.INFORMATION_MESSAGE);

                } catch (ConstraintViolationException e) {
                    System.out.println("EMPLEADO DUPLICADO");
                    System.out.printf("MENSAJE:%s%n", e.getMessage());
                    System.out.printf("COD ERROR:%d%n", e.getErrorCode());
                    System.out.printf("ERROR SQL:%s%n", e.getSQLException().getMessage());
                }

            } catch (TransientPropertyValueException e) {
                System.out.println("EL DEPARTAMENTO NO EXISTE");
                System.out.printf("MENSAJE:%s%n", e.getMessage());
                System.out.printf("Propiedad:%s%n", e.getPropertyName());
            }
            session.close();
            //System.exit(0);
        }
        else{

            JOptionPane.showMessageDialog(null, "Id de proveedor ya existe",
                    "Error", JOptionPane.ERROR_MESSAGE);

        }
    }
}
