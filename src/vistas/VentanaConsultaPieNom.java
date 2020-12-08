package vistas;

import accesodatos.HibernateUtil;
import accesodatos.Piezas;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class VentanaConsultaPieNom extends JFrame{
    private JPanel jpPrincipal;
    private JPanel jpDatos;
    private JTextField jtNom;
    private JButton bNombre;
    private JComboBox<String> cbNombre;
    private JLabel lCodigo;
    private JLabel lNombre;
    private JLabel lPrecio;
    private JLabel lDescripcion;

    SessionFactory sesion = HibernateUtil.getSessionFactory();
    List<Piezas> listaPiezas = new LinkedList<>();

    public VentanaConsultaPieNom(){
        add(jpPrincipal);

        setSize(700, 400);

        bNombre.addActionListener(e -> consultaPorNombre(jtNom.getText()));
        cbNombre.addActionListener(e -> rellenaEtiquetas());
    }

    private void rellenaEtiquetas() {
        if (listaPiezas.size() > 0 && cbNombre.getSelectedIndex() > -1) {

            lCodigo.setText(listaPiezas.get(cbNombre.getSelectedIndex()).getCodigo());
            lNombre.setText(listaPiezas.get(cbNombre.getSelectedIndex()).getNombre());
            lPrecio.setText(listaPiezas.get(cbNombre.getSelectedIndex()).getPrecio());
            lDescripcion.setText(listaPiezas.get(cbNombre.getSelectedIndex()).getDescripcion());
        }
    }

    private void consultaPorNombre(String nom) {
        Session session = sesion.openSession();

        //Se crea la sentencia En realidad el like no hace falta porque al poner = hace el mismo efecto que like
        // y si se quiere que sea exacto hay que poner = 'valor a buscar' pero dejo el like para saber que tambien vale
        String hql = String.format("from Piezas where nombre like '%%%s%%'", nom);

        listaPiezas.clear();
        for (Object o : session.createQuery(hql).list()) {
            listaPiezas.add((Piezas) o);
        }

        if (listaPiezas.size() > 0) {

            cbNombre.removeAllItems();
            for (Piezas pieza : listaPiezas) {
                cbNombre.addItem(pieza.getNombre());
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ningun Nombre coincidente",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        session.close();
    }
}
