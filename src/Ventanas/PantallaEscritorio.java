package Ventanas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Aplicaciones.Calculadora;
import Aplicaciones.Snake;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Plantillas.Ventana;

public class PantallaEscritorio extends Ventana {
  JPanel ico1, ico2, ico3, ico4, ico5, ico6;
  Dimension iconD = new Dimension(30, 30);

  public PantallaEscritorio() {
    super("Escritorio");
    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    ico1 = new JPanel();
    ico1.setBackground(Color.BLACK);
    ico1.setPreferredSize(iconD);
    ico2 = new JPanel();
    ico2.setPreferredSize(iconD);
    ico2.setBackground(Color.lightGray);
    ico3 = new JPanel();
    ico3.setPreferredSize(iconD);

    ico2.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // Acción a realizar al hacer clic
        new Calculadora();
      }
    });

    ico3.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // Acción a realizar al hacer clic
        new Snake();
      }
    });
    zSur.setBackground(Color.darkGray);

    zSur.setLayout(new FlowLayout(FlowLayout.LEADING));
    zSur.add(ico1);
    zSur.add(ico2);
    zSur.add(ico3);
    this.setVisible(true);
  }
}
