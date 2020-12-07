package vistas;

import accesodatos.HibernateUtil;
import accesodatos.Proveedores;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class VentanaConsultaProCodigo extends JFrame {
    private JPanel jpPrincipal;
    private JTextField jtCod;
    private JPanel jpDatos;
    private JButton buscarProveedorButton;
    private JComboBox<String> cbCodigo;
    private JLabel lDireccion;
    private JLabel lApellidos;
    private JLabel lNombre;
    private JLabel lCodigo;

    SessionFactory sesion = HibernateUtil.getSessionFactory();
    List<Proveedores> listaCodigos = new LinkedList<>();

    public VentanaConsultaProCodigo() {

        add(jpPrincipal);

        setSize(700, 400);

        buscarProveedorButton.addActionListener(e -> consultaPorCodigo(jtCod.getText()));
        cbCodigo.addActionListener(e -> rellenaEtiquetas());
    }

    private void rellenaEtiquetas() {

        if (listaCodigos.size() > 0 && cbCodigo.getSelectedIndex() > -1){

            lCodigo.setText(listaCodigos.get(cbCodigo.getSelectedIndex()).getCodigo());
            lNombre.setText(listaCodigos.get(cbCodigo.getSelectedIndex()).getNombre());
            lApellidos.setText(listaCodigos.get(cbCodigo.getSelectedIndex()).getApellidos());
            lDireccion.setText(listaCodigos.get(cbCodigo.getSelectedIndex()).getDireccion());
        }
    }

    private void consultaPorCodigo(String cod) {

        Session session = sesion.openSession();


        //Se crea la sentencia En realidad el like no hace falta porque al poner = hace el mismo efecto que like
        // y si se quiere que sea exacto hay que poner = 'valor a buscar' pero dejo el like para saber que tambien vale
        String hql = String.format("from Proveedores where codigo like '%%%s%%'", cod);


        listaCodigos.clear();
        for (Object o : session.createQuery(hql).list()) {
            listaCodigos.add((Proveedores) o);
        }

        if (listaCodigos.size() > 0) {

            cbCodigo.removeAllItems();
            for (Proveedores proveedores : listaCodigos) {
                cbCodigo.addItem(proveedores.getCodigo());
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ningun codigo coincidente",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        session.close();

    }

}
