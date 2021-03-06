package vistas;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    private JPanel panel1;
    private JLabel lFondo;

    public VentanaPrincipal() {

        lFondo.setIcon(new ImageIcon("fondo.PNG"));

        add(panel1);

        crearMenu();

        setSize(700, 500);

    }

    private void crearMenu() {


        var menuBar = new JMenuBar();

        var bd = new JMenu("Base de Datos"); //Nombre del menu

        var baseDatosMenuItem = new JMenuItem("Exit"); //Nombre del boton

        baseDatosMenuItem.setToolTipText("Salir"); //Hover del boton
        baseDatosMenuItem.addActionListener((event) -> System.exit(0)); //Funcion que ejecuta el boton

        menuBar.add(bd);

        bd.add(baseDatosMenuItem);

        ///////////////////////////////////// menu proveedores ////////////////////////////////////////////////////////

        var proveedores = new JMenu("Proveedores"); //Nombre del menu

        var provGesMenuItem = new JMenuItem("Gestión de proveedores"); //Nombre del boton

        var provConsMenu = new JMenu("Consultas de proveedores"); //Nombre del menu
        var provConsMenuCodItem = new JMenuItem("Por Código"); //Nombre del boton
        var provConsMenuNomItem = new JMenuItem("Por Nombre"); //Nombre del boton
        var provConsMenuDirItem = new JMenuItem("Por Dirección"); //Nombre del boton

        proveedores.add(provGesMenuItem);

        proveedores.add(provConsMenu);
        provConsMenu.add(provConsMenuCodItem);
        provConsMenu.add(provConsMenuNomItem);
        provConsMenu.add(provConsMenuDirItem);

        menuBar.add(proveedores);

        provGesMenuItem.addActionListener((event) -> {
            VentanaProveedores vpro = new VentanaProveedores();
            vpro.setVisible(true);
        });

        provConsMenuCodItem.addActionListener((event) -> {
            VentanaConsultaProCodigo vconpro = new VentanaConsultaProCodigo();
            vconpro.setVisible(true);
        });

        provConsMenuNomItem.addActionListener((event) -> {
            VentanaConsultaProNom vconpronom = new VentanaConsultaProNom();
            vconpronom.setVisible(true);
        });

        provConsMenuDirItem.addActionListener((event) -> {
            VentanaConsultaProDir vconprodir = new VentanaConsultaProDir();
            vconprodir.setVisible(true);
        });


        //////////////////////////// menu piezas/////////////////////////////////////////////////////////////////////

        var piezas = new JMenu("Piezas"); //Nombre del menu
        var pieGesMenuItem = new JMenuItem("Gestión de piezas"); //Nombre del boton

        var pieConsMenu = new JMenu("Consultas de piezas"); //Nombre del boton
        var pieConsMenuCodItem = new JMenuItem("Por Código"); //Nombre del boton
        var pieConsMenuNomItem = new JMenuItem("Por Nombre"); //Nombre del boton

        menuBar.add(piezas);

        piezas.add(pieGesMenuItem);

        piezas.add(pieConsMenu);
        pieConsMenu.add(pieConsMenuCodItem);
        pieConsMenu.add(pieConsMenuNomItem);

        pieGesMenuItem.addActionListener((event) -> {
            VentanaPiezas vpie = new VentanaPiezas();
            vpie.setVisible(true);
        });

        pieConsMenuCodItem.addActionListener((event) -> {
            VentanaConsultaPieCodigo vpieCod = new VentanaConsultaPieCodigo();
            vpieCod.setVisible(true);
        });

        pieConsMenuNomItem.addActionListener((event) -> {
            VentanaConsultaPieNom vpieNom = new VentanaConsultaPieNom();
            vpieNom.setVisible(true);
        });

        ////////////////////////////////////// menu proyectos /////////////////////////////////////////////////////////

        var proyectos = new JMenu("Proyectos"); //Nombre del menu
        var proGesMenuItem = new JMenuItem("Gestión de proyectos"); //Nombre del boton

        var proConsMenu = new JMenu("Consultas de Proyectos"); //Nombre del boton
        var proConsMenuCodItem = new JMenuItem("Por Código"); //Nombre del boton
        var proConsMenuNomItem = new JMenuItem("Por Nombre"); //Nombre del boton
        var proConsMenuCiuItem = new JMenuItem("Por Ciudad"); //Nombre del boton

        menuBar.add(proyectos);

        proyectos.add(proGesMenuItem);

        proyectos.add(proConsMenu);
        proConsMenu.add(proConsMenuCodItem);
        proConsMenu.add(proConsMenuNomItem);
        proConsMenu.add(proConsMenuCiuItem);

        proGesMenuItem.addActionListener((event) -> {
            VentanaProyectos vproy = new VentanaProyectos();
            vproy.setVisible(true);
        });

        proConsMenuCodItem.addActionListener((event) -> {
            VentanaConsultaProyCod vproycod = new VentanaConsultaProyCod();
            vproycod.setVisible(true);
        });

        proConsMenuNomItem.addActionListener((event) -> {
            VentanaConsultaProyNom vproynom = new VentanaConsultaProyNom();
            vproynom.setVisible(true);
        });

        proConsMenuCiuItem.addActionListener((event) -> {
            VentanaConsultaProyCiu vproyciu = new VentanaConsultaProyCiu();
            vproyciu.setVisible(true);
        });

        /////////////////////////////////////// menu gestion global////////////////////////////////////////////////////

        var gestGlob = new JMenu("Gestión Global"); //Nombre del menu
        var gesPPPMenuItem = new JMenuItem("Piezas, Proveedores y Proyectos"); //Nombre del boton
        var gesSumProMenuItem = new JMenuItem("Suministros por Proveedores"); //Nombre del boton
        var gesSumPieMenuItem = new JMenuItem("Suministros por Piezas"); //Nombre del boton
        var gesEstMenuItem = new JMenuItem("Estadisticas"); //Nombre del boton

        menuBar.add(gestGlob);

        gestGlob.add(gesPPPMenuItem);
        gestGlob.add(gesSumProMenuItem);
        gestGlob.add(gesSumPieMenuItem);
        gestGlob.add(gesEstMenuItem);

        gesPPPMenuItem.addActionListener((event) -> {
            VentanaPieProvProy vpropieproy = new VentanaPieProvProy();
            vpropieproy.setVisible(true);
        });

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        var ayuda = new JMenuItem("Ayuda"); //Nombre del menu

        menuBar.add(ayuda);

        ayuda.addActionListener((event) -> {
            VentanaAyuda vAyuda = new VentanaAyuda();
            vAyuda.setVisible(true);
        });



        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


        setJMenuBar(menuBar);


    }


}
