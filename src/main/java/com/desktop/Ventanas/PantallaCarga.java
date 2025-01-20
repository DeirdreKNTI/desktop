package com.desktop.Ventanas;

import com.desktop.Plantillas.*;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class PantallaCarga extends Ventana {
  public PantallaCarga() {
    super("Por favor espere");

    JLabel msg = new JLabel("Cargando...", SwingConstants.CENTER);
    add(msg);

    this.setVisible(true);
  }
}
