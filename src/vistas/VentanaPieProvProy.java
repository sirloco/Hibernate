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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    private JComboBox<String> cbGestion;

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
            modificaGestion();
        });
        cbGestion.addActionListener(e -> {
            rellenaGestion();
        });
        bEliminar.addActionListener(e -> {
            eliminaGestion(Objects.requireNonNull(cbGestion.getSelectedItem()).toString());
        });
        bListado.addActionListener(e -> {
            listadoGestiones();
        });
    }

    private void listadoGestiones() {

        Session session = sesion.openSession();

        String hql = String.format("from %s", "Gestion");

        LinkedList<Gestion> listGestion = new LinkedList<>();

        for (Object o : session.createQuery(hql).list()) {
            listGestion.add((Gestion) o);
        }

        if (listGestion.size() > 0) {

            VentanaListadoGestion vlg = new VentanaListadoGestion(listGestion);

            vlg.setVisible(true);


        } else {
            JOptionPane.showMessageDialog(null, "No hay Gestiones",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        session.close();

    }

    private void eliminaGestion(String cod) {

        int codigo = Integer.parseInt(cod);

        if (codigo > 0) {

            Gestion ges = buscarGestionCod(codigo);

            if (ges != null) {

                Session session = sesion.openSession();

                Transaction tx = session.beginTransaction();

                //Se crea la sentencia
                String hqlDel = String.format("delete from Gestion e where e.id = '%s'", ges.getId());

                session.createQuery(hqlDel).executeUpdate();

                JOptionPane.showMessageDialog(null, "Gestion " + ges.getId() + " Eliminada",
                        "Info", JOptionPane.INFORMATION_MESSAGE);

                tx.commit();
                session.close();

                actualizaComboGestion();
            } else {

                JOptionPane.showMessageDialog(null, "Codigo Gestion inexistente",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {

            JOptionPane.showMessageDialog(null, "Codigo Gestion necesario",
                    "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    private Gestion buscarGestionCod(int cod) {
        Session session = sesion.openSession();

        String hql = String.format("from Gestion where id = '%d'", cod);

        Gestion ges = (Gestion) session.createQuery(hql).uniqueResult();

        session.close();

        return ges;
    }

    private void modificaGestion() {

        Session session = sesion.openSession();

        Transaction tx = session.beginTransaction();

        String codigo = String.valueOf(cbGestion.getSelectedIndex());

        System.out.println(codigo);

        String cantidad = jtCantidad.getText();

        Gestion ges = buscarGestionCod(codigo);

        double cant;
        try {
            cant = Double.parseDouble(cantidad);


            if (ges != null && cant > 0) {

                Proveedores pro = listaProveedores.get(cbCodigoProv.getSelectedIndex() - 1);
                Piezas pie = listaPiezas.get(cbPiezas.getSelectedIndex() - 1);
                Proyectos proy = listaProyectos.get(cbProyecto.getSelectedIndex() - 1);


                ges.setPiezasByCodpieza(pie);
                ges.setProveedoresByCodproveedor(pro);
                ges.setProyectosByCodproyecto(proy);

                ges.setCantidad(cant);

                session.update(ges);

                tx.commit();

                JOptionPane.showMessageDialog(null, "Gestión " + ges.getId() + " Actiualizada",
                        "Info", JOptionPane.INFORMATION_MESSAGE);

                actualizaComboGestion();


            } else {
                JOptionPane.showMessageDialog(null, "Gestión No localizada",
                        "Error", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (NumberFormatException e) {
            cant = -1;
            JOptionPane.showMessageDialog(null, "Solo cantidades numericas",
                    "Error", JOptionPane.INFORMATION_MESSAGE);

        }

        session.close();


    }

    private Gestion buscarGestionCod(String cod) {
        Session session = sesion.openSession();

        String hql = String.format("from Gestion where id = '%s'", cod);

        Gestion ges = (Gestion) session.createQuery(hql).uniqueResult();

        session.close();

        return ges;
    }

    private void rellenaGestion() {

        int ngestion = cbGestion.getSelectedIndex();

        if (ngestion > 0) {
            Gestion g = gestiones.get(ngestion-1);


            for (int i = 0; i < listaProveedores.size(); i++) {
                if (listaProveedores.get(i).getCodigo().equals(g.getProveedoresByCodproveedor().getCodigo())) {
                    cbCodigoProv.setSelectedIndex(i+1);
                }

            }

            jtProvLinea1.setText(g.getProveedoresByCodproveedor().getNombre() + " " + g.getProveedoresByCodproveedor().getApellidos());
            jtProvLinea2.setText(g.getProveedoresByCodproveedor().getDireccion());


            for (int i = 0; i < listaProyectos.size(); i++) {
                if (listaProyectos.get(i).getCodigo().equals(g.getProyectosByCodproyecto().getCodigo())) {
                    cbProyecto.setSelectedIndex(i+1);
                }

            }

            jtProyLinea1.setText(g.getProyectosByCodproyecto().getNombre());
            jtProyLinea2.setText(g.getProyectosByCodproyecto().getCiudad());


            for (int i = 0; i < listaPiezas.size(); i++) {
                if (listaPiezas.get(i).getCodigo().equals(g.getPiezasByCodpieza().getCodigo())) {
                    cbPiezas.setSelectedIndex(i+1);
                }

            }

            jtPieLinea1.setText(g.getPiezasByCodpieza().getNombre() + " " + g.getPiezasByCodpieza().getPrecio());
            jtPieLinea2.setText(g.getPiezasByCodpieza().getDescripcion());


            jtCantidad.setText(String.valueOf(g.getCantidad()));

        }else{
            limpiaDatos();
        }

    }

    private void limpiaDatos() {
        cbGestion.setSelectedIndex(0);
        cbCodigoProv.setSelectedIndex(0);
        cbPiezas.setSelectedIndex(0);
        cbProyecto.setSelectedIndex(0);
        jtCantidad.setText("");
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

                        actualizaComboGestion();

                    } catch (ConstraintViolationException ignored) {

                    }

                } catch (TransientPropertyValueException ignored) {

                }
                session.close();

            } else {
                JOptionPane.showMessageDialog(null, "Solo Cantidades numericas",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios para insertar",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizaComboGestion() {
        cbGestion.removeAllItems();
        cbCodigoProv.removeAllItems();
        cbProyecto.removeAllItems();
        cbPiezas.removeAllItems();
        cargarListas();
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

        cbGestion.addItem("Elige Codigo");
        for (Gestion gestion : gestiones) {
            cbGestion.addItem(String.valueOf(gestion.getId()));
        }

        jtCantidad.setText("");

    }
}