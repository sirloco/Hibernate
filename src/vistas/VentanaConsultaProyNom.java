package vistas;

import accesodatos.HibernateUtil;
import accesodatos.Piezas;
import accesodatos.Proyectos;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class VentanaConsultaProyNom extends JFrame{
    private JPanel jpPrincipal;
    private JPanel jpDatos;
    private JTextField jtNom;
    private JButton bNombre;
    private JComboBox<String> cbNombre;
    private JLabel lCodigo;
    private JLabel lNombre;
    private JLabel lCiudad;

    SessionFactory sesion = HibernateUtil.getSessionFactory();
    List<Proyectos> listaProyectos = new LinkedList<>();

    public VentanaConsultaProyNom(){
        add(jpPrincipal);

        setSize(700, 400);

        bNombre.addActionListener(e -> consultaPorNombre(jtNom.getText()));
        cbNombre.addActionListener(e -> rellenaEtiquetas());

    }

    private void rellenaEtiquetas() {
        if (listaProyectos.size() > 0 && cbNombre.getSelectedIndex() > -1) {

            lCodigo.setText(listaProyectos.get(cbNombre.getSelectedIndex()).getCodigo());
            lNombre.setText(listaProyectos.get(cbNombre.getSelectedIndex()).getNombre());
            lCiudad.setText(listaProyectos.get(cbNombre.getSelectedIndex()).getCiudad());
        }
    }

    private void consultaPorNombre(String nom) {
        Session session = sesion.openSession();

        //Se crea la sentencia En realidad el like no hace falta porque al poner = hace el mismo efecto que like
        // y si se quiere que sea exacto hay que poner = 'valor a buscar' pero dejo el like para saber que tambien vale
        String hql = String.format("from Proyectos where nombre like '%%%s%%'", nom);

        listaProyectos.clear();
        for (Object o : session.createQuery(hql).list()) {
            listaProyectos.add((Proyectos) o);
        }

        if (listaProyectos.size() > 0) {

            cbNombre.removeAllItems();
            for (Proyectos proyecto : listaProyectos) {
                cbNombre.addItem(proyecto.getNombre());
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ningun Nombre coincidente",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        session.close();
    }
}
