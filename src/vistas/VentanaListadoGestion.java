package vistas;

import accesodatos.Gestion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class VentanaListadoGestion extends JFrame{
    private JPanel jpPrincipal;
    private JTable tGestion;

    public VentanaListadoGestion(LinkedList<Gestion>listaGestion) {

        add(jpPrincipal);

        setTitle("Lista Gestiones");

        setSize(1000,500);

        DefaultTableModel modeloTablaGestion = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloTablaGestion.setColumnIdentifiers(new Object[]{
                "Id",
                "Proveedor",
                "Pieza",
                "Proyecto",
                "Cantidad"
        });

        for (Gestion gestion : listaGestion) {

            modeloTablaGestion.addRow(new Object[]{
                    gestion.getId(),
                    gestion.getProveedoresByCodproveedor().getNombre(),
                    gestion.getPiezasByCodpieza().getNombre(),
                    gestion.getProyectosByCodproyecto().getNombre(),
                    gestion.getCantidad()
            });

            tGestion.setModel(modeloTablaGestion);

        }
    }
}
