package vistas;

import accesodatos.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class VentanaPieProvProy extends JFrame {
    private JPanel jpPrincipal;
    private JButton bInsertar;
    private JButton bModificar;
    private JButton bEliminar;
    private JButton bListado;
    private JPanel jpRelacion;
    private JComboBox<String> cbCodigoProv;
    private JTextField jtProvLinea1;
    private JTextField jtProvLinea2;
    private JPanel jpProveedor;
    private JPanel jpPieza;
    private JComboBox<String> cbPiezas;
    private JTextField jtPieLinea1;
    private JTextField jtPieLinea2;
    private JComboBox<String> cbProyecto;
    private JTextField jtProyLinea1;
    private JTextField jtProyLinea2;
    private JTextField jtCantidad;
    private JPanel jpProyecto;
    private JComboBox<Integer> cbGestion;

    SessionFactory sesion = HibernateUtil.getSessionFactory();
    List<Proyectos> listaProyectos = new LinkedList<>();
    List<Proveedores> listaProveedores = new LinkedList<>();
    List<Piezas> listaPiezas = new LinkedList<>();
    List<Gestion> gestiones = new LinkedList<>();

    public VentanaPieProvProy() {

        add(jpPrincipal);

        setSize(800, 400);

        cargarListas();

        //////////////////////////////////// Evento seleccion combobox ////////////////////////////////////////////////
        cbCodigoProv.addActionListener(e -> {

            if (cbCodigoProv.getSelectedIndex() > 0) {
                Proveedores pro = listaProveedores.get(cbCodigoProv.getSelectedIndex() - 1);
                jtProvLinea1.setText(pro.getNombre() + " " + pro.getApellidos());
                jtProvLinea2.setText(pro.getDireccion());
            } else {
                jtProvLinea1.setText("");
                jtProvLinea2.setText("");
            }


        });

        cbPiezas.addActionListener(e -> {
            if (cbPiezas.getSelectedIndex() > 0) {
                Piezas pie = listaPiezas.get(cbPiezas.getSelectedIndex() - 1);
                jtPieLinea1.setText(pie.getNombre() + " " + pie.getPrecio());
                jtPieLinea2.setText(pie.getDescripcion());
            } else {
                jtPieLinea1.setText("");
                jtPieLinea2.setText("");
            }
        });

        cbProyecto.addActionListener(e -> {
            if (cbProyecto.getSelectedIndex() > 0) {
                Proyectos proy = listaProyectos.get(cbProyecto.getSelectedIndex() - 1);
                jtProyLinea1.setText(proy.getNombre());
                jtProyLinea2.setText(proy.getCiudad());
            } else {
                jtProyLinea1.setText("");
                jtProyLinea2.setText("");
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        bInsertar.addActionListener(e -> {
            insertaGestion();
        });
        bModificar.addActionListener(e -> {
            //todo pulsa boton modificar
        });
        cbGestion.addActionListener(e -> {
            modificaGestion();
        });
    }

    private void modificaGestion() {

        int ngestion = cbGestion.getSelectedIndex();

        System.out.println(ngestion);
       if (ngestion > -1){
           Gestion g = gestiones.get(ngestion);


           for (int i = 0; i < listaProveedores.size(); i++) {
               if (listaProveedores.get(i).getCodigo().equals(g.getProveedoresByCodproveedor().getCodigo())) {
                   cbCodigoProv.setSelectedIndex(i);
               }

           }

           jtProvLinea1.setText(g.getProveedoresByCodproveedor().getNombre() + " " + g.getProveedoresByCodproveedor().getApellidos());
           jtProvLinea2.setText(g.getProveedoresByCodproveedor().getDireccion());


       }


    }

    private void insertaGestion() {
        if (cbCodigoProv.getSelectedIndex() > 0 && cbPiezas.getSelectedIndex() > 0 &&
                cbProyecto.getSelectedIndex() > 0 && !jtCantidad.getText().isEmpty()) {

            Gestion ges = new Gestion();

            Proveedores pro = listaProveedores.get(cbCodigoProv.getSelectedIndex() - 1);
            Piezas pie = listaPiezas.get(cbPiezas.getSelectedIndex() - 1);
            Proyectos proy = listaProyectos.get(cbProyecto.getSelectedIndex() - 1);


            ges.setPiezasByCodpieza(pie);
            ges.setProveedoresByCodproveedor(pro);
            ges.setProyectosByCodproyecto(proy);

            boolean falloCantidad = false;
            double cant = 0;
            try {
                cant = Double.parseDouble(jtCantidad.getText());
            } catch (NumberFormatException e) {
                falloCantidad = true;
            }

            if (!falloCantidad) {

                ges.setCantidad(cant);

                Session session = sesion.openSession();
                Transaction tx = session.beginTransaction();

                try {

                    session.save(ges);

                    try {

                        tx.commit();

                        JOptionPane.showMessageDialog(null, "Gestion Insertada",
                                "Info", JOptionPane.INFORMATION_MESSAGE);

                    } catch (ConstraintViolationException ignored) {

                    }

                } catch (TransientPropertyValueException ignored) {

                }
                session.close();

            }else {
                JOptionPane.showMessageDialog(null, "Solo Cantidades con decimales",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios para insertar",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarListas() {

        Session session = sesion.openSession();

        //Se crea la sentencia
        String hql = String.format("from %s", "Proveedores");

        listaProveedores.clear();

        // Se crea un linked list porque asi lo que devuelve la query no se queja de que lo almacena en una coleccion
        // que puede almacenar elemenos repetidos es una chorrada pero asi no me sale el warning
        for (Object o : session.createQuery(hql).list()) {
            listaProveedores.add((Proveedores) o);
        }

        //Se crea la sentencia
        hql = String.format("from %s", "Proyectos");

        listaProyectos.clear();

        // Se crea un linked list porque asi lo que devuelve la query no se queja de que lo almacena en una coleccion
        // que puede almacenar elemenos repetidos es una chorrada pero asi no me sale el warning
        for (Object o : session.createQuery(hql).list()) {
            listaProyectos.add((Proyectos) o);
        }

        //Se crea la sentencia
        hql = String.format("from %s", "Piezas");

        listaPiezas.clear();

        // Se crea un linked list porque asi lo que devuelve la query no se queja de que lo almacena en una coleccion
        // que puede almacenar elemenos repetidos es una chorrada pero asi no me sale el warning
        for (Object o : session.createQuery(hql).list()) {
            listaPiezas.add((Piezas) o);
        }

        //Se crea la sentencia
        hql = String.format("from %s", "Gestion");

        gestiones.clear();

        // Se crea un linked list porque asi lo que devuelve la query no se queja de que lo almacena en una coleccion
        // que puede almacenar elemenos repetidos es una chorrada pero asi no me sale el warning
        for (Object o : session.createQuery(hql).list()) {
            gestiones.add((Gestion) o);
        }

        session.close();

        cbCodigoProv.addItem("Elige Codigo");
        for (Proveedores listaProveedore : listaProveedores) {
            cbCodigoProv.addItem(listaProveedore.getCodigo());
        }
        cbCodigoProv.setSelectedIndex(0);

        cbProyecto.addItem("Elige Codigo");
        for (Proyectos listaProyecto : listaProyectos) {
            cbProyecto.addItem(listaProyecto.getCodigo());
        }
        cbProyecto.setSelectedIndex(0);

        cbPiezas.addItem("Elige Codigo");
        for (Piezas listaPieza : listaPiezas) {
            cbPiezas.addItem(listaPieza.getCodigo());
        }
        cbPiezas.setSelectedIndex(0);

        for (Gestion gestion : gestiones) {
            cbGestion.addItem(gestion.getId());
        }

    }
}