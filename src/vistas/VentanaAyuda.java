package vistas;

import javax.swing.*;

public class VentanaAyuda extends JFrame{
    private JPanel jpPrincipal;
    private JLabel lFondo;

    public VentanaAyuda() {

        lFondo.setIcon(new ImageIcon("fondo.PNG"));

        add(jpPrincipal);

        setSize(700, 500);
    }
}
