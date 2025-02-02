package Aplicaciones;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Plantillas.Ventana;

public class Calculadora extends Ventana implements ActionListener {
  private JTextField pantalla;
  private double resultado;
  private String operador;

  public Calculadora() {
    super("calculadora"); // Llama al constructor de Ventanas
    inicializarCalculadora();
  }

  private void inicializarCalculadora() {
    // Pantalla de la calculadora
    pantalla = new JTextField();
    add(pantalla, BorderLayout.NORTH);

    // Panel de botones
    JPanel panelBotones = new JPanel();
    panelBotones.setLayout(new GridLayout(4, 4));

    // Crear botones
    String[] botones = {
        "7", "8", "9", "/",
        "4", "5", "6", "*",
        "1", "2", "3", "-",
        "0", "C", "=", "+"
    };

    for (String texto : botones) {
      JButton boton = new JButton(texto);
      boton.addActionListener(this);
      panelBotones.add(boton);
    }

    add(panelBotones, BorderLayout.CENTER);
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String comando = e.getActionCommand();

    switch (comando) {
      case "C":
        pantalla.setText("");
        resultado = 0;
        operador = "";
        break;
      case "=":
        calcular();
        break;
      default:
        if ("+-*/".contains(comando)) {
          operador = comando;
          resultado = Double.parseDouble(pantalla.getText());
          pantalla.setText("");
        } else {
          pantalla.setText(pantalla.getText() + comando);
        }
        break;
    }
  }

  private void calcular() {
    double numeroActual = Double.parseDouble(pantalla.getText());

    switch (operador) {
      case "+":
        resultado += numeroActual;
        break;
      case "-":
        resultado -= numeroActual;
        break;
      case "*":
        resultado *= numeroActual;
        break;
      case "/":
        if (numeroActual != 0) {
          resultado /= numeroActual;
        } else {
          pantalla.setText("Error");
          return;
        }
        break;
    }

    pantalla.setText(String.valueOf(resultado));
  }
}
