package vistas;

import accesodatos.HibernateUtil;
import accesodatos.Proveedores;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;
import java.util.List;

public class VentanaConsultaProDir extends JFrame{
    private JPanel jpPrincipal;
    private JPanel jpDatos;
    private JTextField jtDir;
    private JButton bDireccion;
    private JComboBox<String> cbDireccion;
    private JLabel lCodigo;
    private JLabel lNombre;
    private JLabel lApellidos;
    private JLabel lDireccion;

    SessionFactory sesion = HibernateUtil.getSessionFactory();
    List<Proveedores> listaProveedores = new LinkedList<>();

    public VentanaConsultaProDir(){

        add(jpPrincipal);

        setSize(700, 400);

        bDireccion.addActionListener(e -> consultaPorDireccion(jtDir.getText()));
        cbDireccion.addActionListener(e -> rellenaEtiquetas());

        cbDireccion.addMouseMotionListener(new MouseMotionAdapter() {
        });

    }

    private void rellenaEtiquetas() {

        if (listaProveedores.size() > 0 && cbDireccion.getSelectedIndex() > -1) {

            lCodigo.setText(listaProveedores.get(cbDireccion.getSelectedIndex()).getCodigo());
            lNombre.setText(listaProveedores.get(cbDireccion.getSelectedIndex()).getNombre());
            lApellidos.setText(listaProveedores.get(cbDireccion.getSelectedIndex()).getApellidos());
            lDireccion.setText(listaProveedores.get(cbDireccion.getSelectedIndex()).getDireccion());
        }
    }

    private void consultaPorDireccion(String dir) {

        Session session = sesion.openSession();

        //Se crea la sentencia En realidad el like no hace falta porque al poner = hace el mismo efecto que like
        // y si se quiere que sea exacto hay que poner = 'valor a buscar' pero dejo el like para saber que tambien vale
        String hql = String.format("from Proveedores where direccion like '%%%s%%'", dir);

        listaProveedores.clear();
        for (Object o : session.createQuery(hql).list()) {
            listaProveedores.add((Proveedores) o);
        }

        if (listaProveedores.size() > 0) {

            cbDireccion.removeAllItems();
            for (Proveedores proveedores : listaProveedores) {
                cbDireccion.addItem(proveedores.getDireccion());
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ninguna Dirección coincidente",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        session.close();

    }

}
