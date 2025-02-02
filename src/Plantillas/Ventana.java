package Plantillas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

public class Ventana extends JFrame {

  public JPanel zNorte, zEste, zOeste, zCentral, zSur, zForm;

  public Ventana(String titulo) {
    setTitle(titulo);
    setSize(400, 350);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    zCentral = new JPanel();
    zNorte = new JPanel();
    zOeste = new JPanel();
    zEste = new JPanel();
    zSur = new JPanel();
    zForm = new JPanel();

    zCentral.setPreferredSize(new Dimension(100, 40));
    // zCentral.setBackground(Color.red);
    zNorte.setPreferredSize(new Dimension(100, 40));
    // zNorte.setBackground(Color.green);
    zOeste.setPreferredSize(new Dimension(100, 40));
    // zOeste.setBackground(Color.blue);
    zEste.setPreferredSize(new Dimension(100, 40));
    // zEste.setBackground(Color.yellow);
    zSur.setPreferredSize(new Dimension(100, 40));
    // zSur.setBackground(Color.darkGray);

    add(zCentral, BorderLayout.CENTER);
    add(zNorte, BorderLayout.NORTH);
    add(zOeste, BorderLayout.WEST);
    add(zEste, BorderLayout.EAST);
    add(zSur, BorderLayout.SOUTH);

  }
}
