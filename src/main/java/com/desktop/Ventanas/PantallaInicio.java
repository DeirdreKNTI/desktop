package com.desktop.Ventanas;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.desktop.Plantillas.Ventana;

public class PantallaInicio extends Ventana {

  JTextField user;
  JPasswordField psw;
  JLabel userL;
  JLabel pswL;

  public PantallaInicio() {
    super("Ingreso");

    userL = new JLabel("Usuario");
    userL.setBounds(70, 80, 80, 20);
    pswL = new JLabel("Contraseña");
    pswL.setBounds(70, 110, 80, 20);
    user = new JTextField("");
    user.setBounds(150, 80, 80, 20);
    psw = new JPasswordField("");
    psw.setBounds(150, 110, 80, 20);

    add(user);
    add(psw);
    add(pswL);
    add(userL);
    this.setVisible(true);
  }
}
