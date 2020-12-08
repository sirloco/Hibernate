package vistas;

import accesodatos.HibernateUtil;
import accesodatos.Proveedores;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import javax.swing.*;
import java.util.LinkedList;
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
        bModificar.addActionListener(e -> modificaProveedor()); // Actualiza Proveedor

        bBaja.addActionListener(e -> { // ELimina proveedor

            eliminaProveedor(jtCodigo.getText());

            traeProveedores();

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

    private void modificaProveedor() {

        Session session = sesion.openSession();

        Transaction tx = session.beginTransaction();

        String codigo = jtCod.getText();
        String nombre = jtNom.getText();
        String apellidos = jtApellidos.getText();
        String direccion = jtDir.getText();

        Proveedores pro = buscarProveedorCod(codigo);

        if (pro != null) {

            Proveedores proveedor = new Proveedores();

            proveedor.setCodigo(codigo);
            proveedor.setNombre(nombre);
            proveedor.setApellidos(apellidos);
            proveedor.setDireccion(direccion);

            session.update(proveedor);

            tx.commit();

            JOptionPane.showMessageDialog(null, "Proveedor " + proveedor.getNombre() + " Actiualizado",
                    "Info", JOptionPane.INFORMATION_MESSAGE);


        }else{
            JOptionPane.showMessageDialog(null, "Proveedor No localizado",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
        }

        session.close();

    }

    private void eliminaProveedor(String codigo) {

        if (!codigo.isEmpty()) {

            Proveedores pro = buscarProveedorCod(codigo);

            if (pro != null) {

                Session session = sesion.openSession();

                Transaction tx = session.beginTransaction();

                //Se crea la sentencia
                String hqlDel = String.format("delete from Proveedores e where e.codigo = '%s'",pro.getCodigo());

                session.createQuery(hqlDel).executeUpdate();

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

        if (proveedores.size() > 0) {
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

        //Se crea la sentencia
        String hql = String.format("from %s","Proveedores");

        List<Proveedores> listaProveedores = new LinkedList<>();

        // Se crea un linked list porque asi lo que devuelve la query no se queja de que lo almacena en una coleccion
        // que puede almacenar elemenos repetidos es una chorrada pero asi no me sale el warning
        for (Object o : session.createQuery(hql).list()) {
            listaProveedores.add((Proveedores) o );
        }

        proveedores = listaProveedores;

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

        String hql = String.format("from Proveedores where codigo = '%s'",cod);

        Proveedores pro = (Proveedores) session.createQuery(hql).uniqueResult();

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

        if (p == null && !codigo.isEmpty()) {

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

