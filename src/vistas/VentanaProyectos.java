package vistas;

import accesodatos.Gestion;
import accesodatos.HibernateUtil;
import accesodatos.Proyectos;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class VentanaProyectos extends JFrame {
    private JPanel jpPanel1;
    private JPanel jpPrincipal;
    private JTabbedPane tabbedPane1;
    private JPanel jpGesProv;
    private JPanel jpGestionProv;
    private JTextField jtCod;
    private JTextField jtNom;
    private JTextField jtCiudad;
    private JButton bLimpiar;
    private JButton bEliminar;
    private JButton bModificar;
    private JButton bInsertar;
    private JPanel jpLisProv;
    private JPanel jpListaPro;
    private JTextField jtCodigo;
    private JTextField jtNombre;
    private JTextField jtCiu;
    private JButton bSiguiente;
    private JButton bBaja;
    private JButton bPrimero;
    private JButton bAtras;
    private JButton bUltimo;
    private JLabel lPrimero;
    private JLabel lUltimo;
    private JButton ejecutarConsultaButton;

    List<Proyectos> proyectos = new LinkedList<>();

    SessionFactory sesion = HibernateUtil.getSessionFactory();

    public VentanaProyectos() {
        add(jpPanel1);

        setSize(500, 400);

        bInsertar.addActionListener(e -> insertarProyecto());// BOTON INSERTAR PROYECTO //
        bLimpiar.addActionListener(e -> limpiarProyecto());// BOTON LIMPIAR FORMULARIO //
        ejecutarConsultaButton.addActionListener(e -> traeProyecto());// Boton Mostrar listado proyecto //
        bPrimero.addActionListener(e -> primerProyecto()); //Primera posicion de la lista //
        bUltimo.addActionListener(e -> ultimaProyecto()); // ultimo registro de proyectos //
        bEliminar.addActionListener(e -> eliminaProyecto(jtCod.getText())); // ELimina proyecto
        bModificar.addActionListener(e -> modificaProyecto()); // Actualiza Pieza

        bBaja.addActionListener(e -> { // ELimina proyecto

            eliminaProyecto(jtCodigo.getText());

            traeProyecto();

        });

        bAtras.addActionListener(e -> {

            int posicion = Integer.parseInt(lPrimero.getText()) - 1; // se resta uno para ir retrocediendo de 1 en 1

            if (posicion > 0) {

                ponNumeroProyecto(posicion);
                ponProyecto(proyectos.get(posicion - 1)); // se resta 1 porque van de 0 a n
            }


        });
        bSiguiente.addActionListener(e -> {

            int posicion = Integer.parseInt(lPrimero.getText()) + 1; // se resta uno para ir avanzando de 1 en 1

            if (posicion < Integer.parseInt(lUltimo.getText()) + 1) {

                ponNumeroProyecto(posicion);
                ponProyecto(proyectos.get(posicion - 1)); // se resta 1 porque van de 0 a n
            }

        });

    }

    private void ponProyecto(Proyectos proyecto) {
        jtCodigo.setText(proyecto.getCodigo());
        jtNombre.setText(proyecto.getNombre());
        jtCiu.setText(proyecto.getCiudad());
    }

    private void ponNumeroProyecto(int numero) {
        if (proyectos.size() > 0) lPrimero.setText(String.valueOf(numero));
    }

    private void modificaProyecto() {
        Session session = sesion.openSession();

        Transaction tx = session.beginTransaction();

        String codigo = jtCod.getText();
        String nombre = jtNom.getText();
        String ciudad = jtCiudad.getText();

        Proyectos pro = buscarProyectoCod(codigo);

        if (pro != null) {

            Proyectos proyecto = new Proyectos();

            proyecto.setCodigo(codigo);
            proyecto.setNombre(nombre);
            proyecto.setCiudad(ciudad);

            session.update(proyecto);

            tx.commit();

            JOptionPane.showMessageDialog(null, "Proyecto " + proyecto.getNombre() + " Actiualizado",
                    "Info", JOptionPane.INFORMATION_MESSAGE);


        } else {
            JOptionPane.showMessageDialog(null, "Proyecto No localizado",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
        }

        session.close();
    }

    private Proyectos buscarProyectoCod(String cod) {
        Session session = sesion.openSession();

        String hql = String.format("from Proyectos where codigo = '%s'", cod);

        Proyectos pro = (Proyectos) session.createQuery(hql).uniqueResult();

        session.close();

        return pro;
    }

    private void eliminaProyecto(String codigo) {
        if (!codigo.isEmpty()) {

            Proyectos pro = buscarProyectoCod(codigo);

            if (pro != null) {

                Session session = sesion.openSession();

                Transaction tx = session.beginTransaction();


                String hql = String.format("from Gestion where proyectosByCodproyecto.codigo = '%s'",pro.getCodigo());


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
                    String hqlDel = String.format("delete from Proyectos e where e.codigo = '%s'", pro.getCodigo());

                    session.createQuery(hqlDel).executeUpdate();

                    JOptionPane.showMessageDialog(null, "Proyecto " + pro.getNombre() + " Eliminado",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                }

                tx.commit();
                session.close();

            } else {

                JOptionPane.showMessageDialog(null, "Codigo Proyecto inexistente",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {

            JOptionPane.showMessageDialog(null, "Codigo Proyecto necesario",
                    "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void ultimaProyecto() {
        ponNumeroProyecto(proyectos.size());
        ponProyecto(proyectos.get(proyectos.size() - 1));
    }

    private void primerProyecto() {
        if (proyectos.size() > 0) {
            ponNumeroProyecto(1);
            ponProyecto(proyectos.get(0));
        }
    }

    private void traeProyecto() {
        Session session = sesion.openSession();

        //Se crea la sentencia
        String hql = String.format("from %s", "Proyectos");

        proyectos.clear();
        // Se crea un linked list porque asi lo que devuelve la query no se queja de que lo almacena en una coleccion
        // que puede almacenar elemenos repetidos es una chorrada pero asi no me sale el warning
        for (Object o : session.createQuery(hql).list()) {
            proyectos.add((Proyectos) o);
        }

        ponNumeroProyecto(1);

        lUltimo.setText(proyectos.size() > 0 ? String.valueOf(proyectos.size()) : "");

        session.close();

        primerProyecto();
    }

    private void limpiarProyecto() {
        jtCod.setText("");
        jtNom.setText("");
        jtCiudad.setText("");
    }

    private void insertarProyecto() {
        Session session = sesion.openSession();
        Transaction tx = session.beginTransaction();

        String codigo = jtCod.getText();
        String nombre = jtNom.getText();
        String ciudad = jtCiudad.getText();

        //todo validaciones
        Proyectos p = buscarProyectoCod(codigo);

        if (p == null && !codigo.isEmpty()) {

            Proyectos pro = new Proyectos();

            pro.setCodigo(codigo.toUpperCase());
            pro.setNombre(nombre);
            pro.setCiudad(ciudad);

            try {

                session.save(pro);

                try {

                    tx.commit();

                    JOptionPane.showMessageDialog(null, "Proyecto " + pro.getNombre() + " Insertado",
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
            JOptionPane.showMessageDialog(null, "Id de proyecto ya existe",
                    "Error", JOptionPane.ERROR_MESSAGE);

    }
}
