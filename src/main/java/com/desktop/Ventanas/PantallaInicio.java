package com.desktop.Ventanas;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.desktop.Plantillas.Ventana;

public class PantallaInicio extends Ventana {

  JTextField user;
  JPasswordField psw;
  JLabel userL;
  JLabel pswL;
  JPanel zForm;

  public PantallaInicio() {
    super("Ingreso");

    zForm = new JPanel();
    userL = new JLabel("Usuario");
    pswL = new JLabel("Contraseña");
    user = new JTextField("");
    user.setColumns(10);
    psw = new JPasswordField("");
    psw.setColumns(10);
    zCentral.setLayout(new FlowLayout(FlowLayout.CENTER));
    zForm.setPreferredSize(new Dimension(120, 250));
    zForm.setLayout(new FlowLayout());
    zForm.add(userL);
    zForm.add(user);
    zForm.add(pswL);
    zForm.add(psw);
    zCentral.add(zForm);
    this.setVisible(true);
  }
}
