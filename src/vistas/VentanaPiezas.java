package vistas;

import accesodatos.Gestion;
import accesodatos.HibernateUtil;
import accesodatos.Piezas;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class VentanaPiezas extends JFrame {

    private JPanel jpPrincipal;
    private JTabbedPane tabbedPane1;
    private JPanel jpGesProv;
    private JPanel jpGestionProv;
    private JTextField jtCod;
    private JTextField jtNom;
    private JTextField jtPrecio;
    private JTextField jtDes;
    private JButton bLimpiar;
    private JButton bEliminar;
    private JButton bModificar;
    private JButton bInsertar;
    private JPanel jpLisProv;
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
    private JPanel jpPanel1;

    List<Piezas> piezas = new LinkedList<>();

    SessionFactory sesion = HibernateUtil.getSessionFactory();

    public VentanaPiezas() {

        add(jpPanel1);

        setSize(500, 400);

        bInsertar.addActionListener(e -> insertarPieza());// BOTON INSERTAR PIEZA //
        bLimpiar.addActionListener(e -> limpiarPieza());// BOTON LIMPIAR FORMULARIO //
        ejecutarConsultaButton.addActionListener(e -> traePiezas());// Boton Mostrar listado piezas //
        bPrimero.addActionListener(e -> primerPieza()); //Primera posicion de la lista //
        bUltimo.addActionListener(e -> ultimaPieza()); // ultimo registro de piezas //
        bEliminar.addActionListener(e -> eliminaPieza(jtCod.getText())); // ELimina pieza
        bModificar.addActionListener(e -> modificaPieza()); // Actualiza Pieza

        bBaja.addActionListener(e -> { // ELimina pieza

            eliminaPieza(jtCodigo.getText());

            traePiezas();

        });

        bAtras.addActionListener(e -> {

            int posicion = Integer.parseInt(lPrimero.getText()) - 1; // se resta uno para ir retrocediendo de 1 en 1

            if (posicion > 0) {

                ponNumeroPieza(posicion);
                ponPieza(piezas.get(posicion - 1)); // se resta 1 porque van de 0 a n
            }


        });
        bSiguiente.addActionListener(e -> {

            int posicion = Integer.parseInt(lPrimero.getText()) + 1; // se resta uno para ir avanzando de 1 en 1

            if (posicion < Integer.parseInt(lUltimo.getText()) + 1) {

                ponNumeroPieza(posicion);
                ponPieza(piezas.get(posicion - 1)); // se resta 1 porque van de 0 a n
            }

        });

    }

    private void ponPieza(Piezas pieza) {
        jtCodigo.setText(pieza.getCodigo());
        jtNombre.setText(pieza.getNombre());
        jtApe.setText(pieza.getPrecio());
        jtDireccion.setText(pieza.getDescripcion());
    }

    private void ponNumeroPieza(int numero) {
        if (piezas.size() > 0) lPrimero.setText(String.valueOf(numero));
    }

    private void modificaPieza() {

        Session session = sesion.openSession();

        Transaction tx = session.beginTransaction();

        String codigo = jtCod.getText();
        String nombre = jtNom.getText();
        String precio = jtPrecio.getText();
        String descripcion = jtDes.getText();

        Piezas pie = buscarPiezaCod(codigo);

        if (pie != null) {

            Piezas pieza = new Piezas();

            pieza.setCodigo(codigo);
            pieza.setNombre(nombre);
            pieza.setPrecio(precio);
            pieza.setDescripcion(descripcion);

            session.update(pieza);

            tx.commit();

            JOptionPane.showMessageDialog(null, "Pieza " + pieza.getNombre() + " Actiualizada",
                    "Info", JOptionPane.INFORMATION_MESSAGE);


        } else {
            JOptionPane.showMessageDialog(null, "Pieza No localizada",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
        }

        session.close();

    }

    private Piezas buscarPiezaCod(String cod) {
        Session session = sesion.openSession();

        String hql = String.format("from Piezas where codigo = '%s'", cod);

        Piezas pie = (Piezas) session.createQuery(hql).uniqueResult();

        session.close();

        return pie;
    }

    private void eliminaPieza(String codigo) {

        if (!codigo.isEmpty()) {

            Piezas pie = buscarPiezaCod(codigo);

            if (pie != null) {

                Session session = sesion.openSession();

                Transaction tx = session.beginTransaction();


                String hql = String.format("from Gestion where piezasByCodpieza.codigo = '%s'",pie.getCodigo());


                LinkedList<Gestion> gestiones = new LinkedList<>();

                for (Object o : session.createQuery(hql).list()) {
                    gestiones.add((Gestion) o);
                }

                boolean confirm;
                if (gestiones.size() > 0){
                    int confirmado = JOptionPane.showConfirmDialog(
                            null,
                            "Se eliminaran "+gestiones.size()+" filas en la tabla gestion ¿seguro que quieres continuar?");

                    if (JOptionPane.OK_OPTION == confirmado) {
                        System.out.println("confirmado");
                        confirm = true;
                    }

                    else {
                        System.out.println("vale... no borro nada...");
                        confirm = false;
                    }
                }else{
                    confirm = true;
                }

                if (confirm) {


                    //Se crea la sentencia
                    String hqlDel = String.format("delete from Piezas e where e.codigo = '%s'", pie.getCodigo());

                    session.createQuery(hqlDel).executeUpdate();

                    JOptionPane.showMessageDialog(null, "Pieza " + pie.getNombre() + " Eliminada",
                            "Info", JOptionPane.INFORMATION_MESSAGE);

                }

                tx.commit();
                session.close();

            } else {

                JOptionPane.showMessageDialog(null, "Codigo Pieza inexistente",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {

            JOptionPane.showMessageDialog(null, "Codigo Pieza necesario",
                    "Error", JOptionPane.ERROR_MESSAGE);

        }

    }

    private void ultimaPieza() {
        ponNumeroPieza(piezas.size());
        ponPieza(piezas.get(piezas.size() - 1));
    }

    private void primerPieza() {
        if (piezas.size() > 0) {
            ponNumeroPieza(1);
            ponPieza(piezas.get(0));
        }
    }

    private void traePiezas() {
        Session session = sesion.openSession();

        //Se crea la sentencia
        String hql = String.format("from %s", "Piezas");

        piezas.clear();

        // Se crea un linked list porque asi lo que devuelve la query no se queja de que lo almacena en una coleccion
        // que puede almacenar elemenos repetidos es una chorrada pero asi no me sale el warning
        for (Object o : session.createQuery(hql).list()) {
            piezas.add((Piezas) o);
        }

        ponNumeroPieza(1);

        lUltimo.setText(piezas.size() > 0 ? String.valueOf(piezas.size()) : "");

        session.close();

        primerPieza();
    }

    private void limpiarPieza() {
        jtCod.setText("");
        jtNom.setText("");
        jtPrecio.setText("");
        jtDes.setText("");
    }

    private void insertarPieza() {
        Session session = sesion.openSession();
        Transaction tx = session.beginTransaction();

        String codigo = jtCod.getText();
        String nombre = jtNom.getText();
        String precio = jtPrecio.getText();
        String descripcion = jtDes.getText();

        //todo validaciones
        Piezas p = buscarPiezaCod(codigo);

        if (p == null && !codigo.isEmpty()) {

            Piezas pie = new Piezas();

            pie.setCodigo(codigo.toUpperCase());
            pie.setNombre(nombre);
            pie.setPrecio(precio);
            pie.setDescripcion(descripcion);

            try {

                session.save(pie);

                try {

                    tx.commit();

                    JOptionPane.showMessageDialog(null, "Pieza " + pie.getNombre() + " Insertada",
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
            JOptionPane.showMessageDialog(null, "Id de pieza ya existe",
                    "Error", JOptionPane.ERROR_MESSAGE);

    }
}
