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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


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
    private JPanel jpListaPro;
    private JTextField jtCodigo;
    private JTextField jtNombre;
    private JTextField jtApe;
    private JTextField jtDireccion;
    private JButton bSiguiente;
    private JButton bBaja;
    private JButton bPrimero;
    private JButton bAtras;
    private JButton bUltimo;
    private JLabel lPrimero;
    private JLabel lUltimo;
    private JButton ejecutarConsultaButton;

    List<Proveedores> proveedores;


    // TODO Auto-generated method stub
    SessionFactory sesion = HibernateUtil.getSessionFactory();

    public VentanaProveedores() {

        add(panel1);

        setSize(500, 400);


        bInsertar.addActionListener(e -> insertarProveedor());// BOTON INSERTAR PROVEEDOR //
        bLimpiar.addActionListener(e -> limpiarProveedor());// BOTON LIMPIAR FORMULARIO //
        ejecutarConsultaButton.addActionListener(e -> traeProveedores());// Boton Mostrar listado proveedores //
        bPrimero.addActionListener(e -> primerProveedor()); //Primera posicion de la lista //
        bUltimo.addActionListener(e -> ultimoProveedor()); // ultimo registro de proveedores //
        bEliminar.addActionListener(e -> eliminaProveedor(jtCod.getText())); // ELimina proveedor

        bBaja.addActionListener(e -> { // ELimina proveedor

            eliminaProveedor(jtCodigo.getText());

            traeProveedores();
        /*    if (proveedores.size() > 0)
                ponProveedor(proveedores.get(0));
            else
                limpiaVentanaIterador();*/

        });

        bAtras.addActionListener(e -> {

            int posicion = Integer.parseInt(lPrimero.getText()) - 1; // se resta uno para ir retrocediendo de 1 en 1

            if (posicion > 0) {

                ponNumeroProveedor(posicion);
                ponProveedor(proveedores.get(posicion - 1)); // se resta 1 porque van de 0 a n
            }


        });
        bSiguiente.addActionListener(e -> {

            int posicion = Integer.parseInt(lPrimero.getText()) + 1; // se resta uno para ir avanzando de 1 en 1

            if (posicion < Integer.parseInt(lUltimo.getText()) + 1) {

                ponNumeroProveedor(posicion);
                ponProveedor(proveedores.get(posicion - 1)); // se resta 1 porque van de 0 a n
            }

        });

    }

    private void limpiaVentanaIterador() {
        jtCodigo.setText("");
        jtNombre.setText("");
        jtApe.setText("");
        jtDireccion.setText("");
        lPrimero.setText("");
        lUltimo.setText("");
    }

    private void eliminaProveedor(String codigo) {

        if (!codigo.isEmpty()) {

            Proveedores pro = buscarProveedorCod(codigo);

            if (pro != null) {

                Session session = sesion.openSession();

                Transaction tx = session.beginTransaction();

                String hqlDel = "delete from Proveedores e where e.codigo = :dep";

                Query q = session.createQuery(hqlDel);

                q.setParameter("dep", pro.getCodigo());

                int filasDel = q.executeUpdate();

                JOptionPane.showMessageDialog(null, "Proveedor " + pro.getNombre() + " Eliminado",
                        "Info", JOptionPane.INFORMATION_MESSAGE);


                tx.commit();
                session.close();

            } else {

                JOptionPane.showMessageDialog(null, "Codigo Proveedor inexistente",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }


        } else {

            JOptionPane.showMessageDialog(null, "Codigo Proveedor necesario",
                    "Error", JOptionPane.ERROR_MESSAGE);

        }

    }

    private void ultimoProveedor() {
        ponNumeroProveedor(proveedores.size());
        ponProveedor(proveedores.get(proveedores.size() - 1));
    }

    private void primerProveedor() {
        
        if (proveedores.size() > 0 ) {
            ponNumeroProveedor(1);
            ponProveedor(proveedores.get(0));
        }
    }

    private void ponProveedor(Proveedores pro) {

        jtCodigo.setText(pro.getCodigo());
        jtNombre.setText(pro.getNombre());
        jtApe.setText(pro.getApellidos());
        jtDireccion.setText(pro.getDireccion());
    }


    private void traeProveedores() {

        Session session = sesion.openSession();

        Query q = session.createQuery("from Proveedores");

        proveedores = q.list();

        ponNumeroProveedor(1);

        lUltimo.setText(proveedores.size() > 0 ? String.valueOf(proveedores.size()) : "");

        session.close();

        primerProveedor();
    }

    private void ponNumeroProveedor(int numero) {// Numerito de identificador -> 1 /// 7

        if (proveedores.size() > 0) lPrimero.setText(String.valueOf(numero));
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

        if (p == null || !codigo.isEmpty()) {

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

        if (codigo.isEmpty())

            JOptionPane.showMessageDialog(null, "Id Vacio",
                    "Error", JOptionPane.ERROR_MESSAGE);

        if (p != null)
            JOptionPane.showMessageDialog(null, "Id de proveedor ya existe",
                    "Error", JOptionPane.ERROR_MESSAGE);

    }
}

