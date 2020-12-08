package vistas;

import accesodatos.HibernateUtil;
import accesodatos.Proyectos;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class VentanaConsultaProyCod extends JFrame {
    private JPanel jpPrincipal;
    private JPanel jpDatos;
    private JTextField jtCod;
    private JButton bBuscar;
    private JComboBox<String> cbCodigo;
    private JLabel lCodigo;
    private JLabel lNombre;
    private JLabel lCiudad;

    SessionFactory sesion = HibernateUtil.getSessionFactory();
    List<Proyectos> listaCodigos = new LinkedList<>();

    public VentanaConsultaProyCod() {
        add(jpPrincipal);

        setSize(700, 400);

        bBuscar.addActionListener(e -> consultaPorCodigo(jtCod.getText()));
        cbCodigo.addActionListener(e -> rellenaEtiquetas());

    }

    private void rellenaEtiquetas() {
        if (listaCodigos.size() > 0 && cbCodigo.getSelectedIndex() > -1) {

            lCodigo.setText(listaCodigos.get(cbCodigo.getSelectedIndex()).getCodigo());
            lNombre.setText(listaCodigos.get(cbCodigo.getSelectedIndex()).getNombre());
            lCiudad.setText(listaCodigos.get(cbCodigo.getSelectedIndex()).getCiudad());
        }
    }

    private void consultaPorCodigo(String cod) {
        Session session = sesion.openSession();

        //Se crea la sentencia En realidad el like no hace falta porque al poner = hace el mismo efecto que like
        // y si se quiere que sea exacto hay que poner = 'valor a buscar' pero dejo el like para saber que tambien vale
        String hql = String.format("from Proyectos where codigo like '%%%s%%'", cod);


        listaCodigos.clear();
        for (Object o : session.createQuery(hql).list()) {
            listaCodigos.add((Proyectos) o);
        }

        if (listaCodigos.size() > 0) {

            cbCodigo.removeAllItems();
            for (Proyectos proyecto : listaCodigos) {
                cbCodigo.addItem(proyecto.getCodigo());
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ningun codigo coincidente",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        session.close();
    }
}
