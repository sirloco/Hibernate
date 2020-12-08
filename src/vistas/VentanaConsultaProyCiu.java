package vistas;

import accesodatos.HibernateUtil;
import accesodatos.Proyectos;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class VentanaConsultaProyCiu extends JFrame{
    private JPanel jpPrincipal;
    private JPanel jpDatos;
    private JTextField jtCiudad;
    private JButton bBuscar;
    private JComboBox<String> cbCiudad;
    private JLabel lCodigo;
    private JLabel lNombre;
    private JLabel lCiudad;

    SessionFactory sesion = HibernateUtil.getSessionFactory();
    List<Proyectos> listaCiudades = new LinkedList<>();

    public VentanaConsultaProyCiu(){
        add(jpPrincipal);

        setSize(700, 400);

        bBuscar.addActionListener(e -> consultaPorCiudad(jtCiudad.getText()));
        cbCiudad.addActionListener(e -> rellenaEtiquetas());
    }

    private void rellenaEtiquetas() {
        if (listaCiudades.size() > 0 && cbCiudad.getSelectedIndex() > -1) {

            lCodigo.setText(listaCiudades.get(cbCiudad.getSelectedIndex()).getCodigo());
            lNombre.setText(listaCiudades.get(cbCiudad.getSelectedIndex()).getNombre());
            lCiudad.setText(listaCiudades.get(cbCiudad.getSelectedIndex()).getCiudad());
        }
    }

    private void consultaPorCiudad(String ciu) {
        Session session = sesion.openSession();

        //Se crea la sentencia En realidad el like no hace falta porque al poner = hace el mismo efecto que like
        // y si se quiere que sea exacto hay que poner = 'valor a buscar' pero dejo el like para saber que tambien vale
        String hql = String.format("from Proyectos where ciudad like '%%%s%%'", ciu);


        listaCiudades.clear();
        for (Object o : session.createQuery(hql).list()) {
            listaCiudades.add((Proyectos) o);
        }

        if (listaCiudades.size() > 0) {

            cbCiudad.removeAllItems();
            for (Proyectos proyecto : listaCiudades) {
                cbCiudad.addItem(proyecto.getCiudad());
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ninguna ciudad coincidente",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        session.close();
    }
}
