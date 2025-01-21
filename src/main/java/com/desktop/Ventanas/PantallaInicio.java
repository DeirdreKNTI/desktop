package com.desktop.Ventanas;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.desktop.Plantillas.Ventana;

public class PantallaInicio extends Ventana {

  JTextField user;
  JPasswordField psw;
  JLabel userL;
  JLabel pswL;

  public PantallaInicio() {
    super("Ingreso");

    userL = new JLabel("Usuario");
    pswL = new JLabel("Contraseña", SwingConstants.CENTER);
    user = new JTextField("", SwingConstants.CENTER);
    user.setSize(80, 20);
    psw = new JPasswordField("", SwingConstants.CENTER);

    add(user);
    add(psw);
    add(pswL);
    this.setVisible(true);
  }
}
