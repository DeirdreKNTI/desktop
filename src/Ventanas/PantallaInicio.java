package Ventanas;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import Plantillas.Ventana;

public class PantallaInicio extends Ventana {

  JTextField user;
  JPasswordField psw;
  JLabel userL;
  JLabel pswL;
  JPanel zForm;
  JButton logIn;

  public PantallaInicio() {
    super("Ingreso");

    zForm = new JPanel();
    userL = new JLabel("Usuario");
    pswL = new JLabel("Contraseña");
    user = new JTextField("");
    user.setColumns(10);
    psw = new JPasswordField("");
    psw.setColumns(10);
    logIn = new JButton("Ingresar");

    zForm.setPreferredSize(new Dimension(120, 260));
    zForm.setLayout(new FlowLayout());
    zForm.add(userL);
    zForm.add(user);
    zForm.add(pswL);
    zForm.add(psw);
    zForm.add(logIn);
    zCentral.add(zForm);
    this.setVisible(true);
  }

  private void checkLogin(){
    String u = user.getText();
    char[] p = psw.getPassword();
    char[] password = {'1','9','8','7'};
    if (u.equals("Carlos") && Arrays.equals(p, password))  {
      
    }
  }
}