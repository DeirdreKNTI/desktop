package Ventanas;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

    logIn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        checkLogin();
      }
    });

    // Add KeyListener for Enter key press
    logIn.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          checkLogin();
        }
      }
    });

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

  private void checkLogin() {
    String u = user.getText();
    char[] p = psw.getPassword();
    String user = "Carlos";
    char[] password = { '1', '9', '8', '7' };
    if (u.equals(user) && Arrays.equals(p, password)) {
      dispose();
      new PantallaEscritorio();
      System.out.println("LogIn");
    } else {
      JOptionPane.showMessageDialog(this, "Inicio de sesión fallido", "Introduzca los datos correctos",
          JOptionPane.WARNING_MESSAGE);
      System.out.println("LogFailed");
    }
  }
}
