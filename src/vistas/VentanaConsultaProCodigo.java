package vistas;

import accesodatos.HibernateUtil;
import accesodatos.Proveedores;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VentanaConsultaProCodigo  extends JFrame{
    private JPanel jpPrincipal;
    private JTextField jtCod;
    private JPanel jpDatos;
    private JButton buscarProveedorButton;
    private JComboBox comboBox1;
    private JLabel lDireccion;
    private JLabel lApellidos;
    private JLabel lNombre;
    private JLabel lCodigo;

    SessionFactory sesion = HibernateUtil.getSessionFactory();

    public VentanaConsultaProCodigo() {

        add(jpPrincipal);

        setSize(700, 400);

        buscarProveedorButton.addActionListener(e -> consultaPorCodigo(jtCod.getText()));
    }

    private void consultaPorCodigo(String cod) {

        Session session = sesion.openSession();


        //Se crea la sentencia
        String hql = "from Proveedores where codigo like '%"+cod+"%'";

        //Se lanza la sentencia
        Query q = session.createQuery(hql);

        List<Proveedores> lista = q.list();

        for (Proveedores proveedores : lista) {
            System.out.println(proveedores.getNombre());
        }

        // va a devolver un único registro
        //System.out.format(" %s, %s \n", pro.getNombre(), pro.getApellidos());

        session.close();

    }

}
