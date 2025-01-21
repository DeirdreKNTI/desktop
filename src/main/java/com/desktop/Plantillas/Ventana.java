package com.desktop.Plantillas;

import javax.swing.*;

public class Ventana extends JFrame {

  public Ventana(String titulo) {
    setTitle(titulo);
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

  }

  public void cerrar() {
    this.dispose();
  }
}
