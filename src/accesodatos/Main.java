package accesodatos;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Iterator;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        listadoDepartamento();
    }

    private static void listadoDepartamento() {
        // TODO Auto-generated method stub
        SessionFactory sesion = HibernateUtil.getSessionFactory();
        Session session = sesion.openSession();


        Piezas pie = session.load(Piezas.class, "1");



        System.out.println("Codigo:" + pie.getCodigo());
        System.out.println("Nombre:" + pie.getNombre());
        System.out.println("==============================");
        System.out.println("Piezas");


        System.out.println("==============================");
        session.close();
        System.exit(0);

    }
}
