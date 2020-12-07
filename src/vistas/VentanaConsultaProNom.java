package vistas;

import accesodatos.HibernateUtil;
import accesodatos.Proveedores;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VentanaConsultaProNom extends JFrame {
    private JPanel jpPrincipal;
    private JPanel jpDatos;
    private JTextField jtNom;
    private JButton bNombre;
    private JComboBox<String> cbNombre;
    private JLabel lCodigo;
    private JLabel lNombre;
    private JLabel lApellidos;
    private JLabel lDireccion;

    SessionFactory sesion = HibernateUtil.getSessionFactory();
    List<Proveedores> listaProveedores = new LinkedList<>();

    public VentanaConsultaProNom() {

        add(jpPrincipal);

        setSize(700, 400);

        bNombre.addActionListener(e -> consultaPorNombre(jtNom.getText()));
        cbNombre.addActionListener(e -> rellenaEtiquetas());

    }


    private void rellenaEtiquetas() {

        if (listaProveedores.size() > 0 && cbNombre.getSelectedIndex() > -1) {

            lCodigo.setText(listaProveedores.get(cbNombre.getSelectedIndex()).getCodigo());
            lNombre.setText(listaProveedores.get(cbNombre.getSelectedIndex()).getNombre());
            lApellidos.setText(listaProveedores.get(cbNombre.getSelectedIndex()).getApellidos());
            lDireccion.setText(listaProveedores.get(cbNombre.getSelectedIndex()).getDireccion());
        }
    }

    private void consultaPorNombre(String nom) {

        Session session = sesion.openSession();

        //Se crea la sentencia En realidad el like no hace falta porque al poner = hace el mismo efecto que like
        // y si se quiere que sea exacto hay que poner = 'valor a buscar' pero dejo el like para saber que tambien vale
        String hql = String.format("from Proveedores where nombre like '%%%s%%'", nom);

        listaProveedores.clear();
        for (Object o : session.createQuery(hql).list()) {
            listaProveedores.add((Proveedores) o);
        }

        if (listaProveedores.size() > 0) {

            cbNombre.removeAllItems();
            for (Proveedores proveedores : listaProveedores) {
                cbNombre.addItem(proveedores.getNombre());
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ningun Nombre coincidente",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        session.close();

    }

}
