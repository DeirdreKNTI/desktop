package Aplicaciones;

import javax.swing.*;

import Plantillas.Ventana;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculadora extends Ventana implements ActionListener {

    private JTextField pantalla;
    private JButton[] botones;
    private String[] etiquetas = {
        "C", "←", "%", "/",
        "7", "8", "9", "*",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "±", "0", ".", "="
    };
    private double num1 = 0, num2 = 0, resultado = 0;
    private char operacion;

    public Calculadora(JFrame Parent) {
        super("Calculadora");
        setSize(300, 400);
        setLayout(new BorderLayout());
        setAlwaysOnTop(true);


        pantalla = new JTextField();
        pantalla.setEditable(false);
        pantalla.setBackground(new Color(230, 230, 230));
        pantalla.setFont(new Font("Arial", Font.BOLD, 24));
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        add(pantalla, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(5, 4, 5, 5));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        botones = new JButton[etiquetas.length];
        for (int i = 0; i < etiquetas.length; i++) {
            botones[i] = new JButton(etiquetas[i]);
            botones[i].addActionListener(this);
            botones[i].setFont(new Font("Arial", Font.BOLD, 18));
            if (i == 0) {
                botones[i].setBackground(new Color(255, 153, 153)); // Rojo claro para C
            } else if (i % 4 == 3 || i == etiquetas.length - 1) {
                botones[i].setBackground(new Color(153, 204, 255)); // Azul claro para operaciones
            } else {
                botones[i].setBackground(new Color(240, 240, 240)); // Gris claro para números
            }
            botones[i].setFocusPainted(false);
            panelBotones.add(botones[i]);
        }

        add(panelBotones, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if (comando.charAt(0) >= '0' && comando.charAt(0) <= '9' || comando.equals(".")) {
            if (pantalla.getText().equals("0")) {
                pantalla.setText(comando);
            } else {
                pantalla.setText(pantalla.getText() + comando);
            }
        } else if (comando.equals("C")) {
            pantalla.setText("0");
            num1 = num2 = resultado = 0;
            operacion = '\0';
        } else if (comando.equals("←")) {
            String str = pantalla.getText();
            if (str.length() > 1) {
                str = str.substring(0, str.length() - 1);
                pantalla.setText(str);
            } else {
                pantalla.setText("0");
            }
        } else if (comando.equals("±")) {
            if (!pantalla.getText().equals("0")) {
                double num = Double.parseDouble(pantalla.getText());
                num = -num;
                pantalla.setText(String.valueOf(num));
            }
        } else if (comando.equals("=")) {
            num2 = Double.parseDouble(pantalla.getText());

            switch (operacion) {
                case '+':
                    resultado = num1 + num2;
                    break;
                case '-':
                    resultado = num1 - num2;
                    break;
                case '*':
                    resultado = num1 * num2;
                    break;
                case '/':
                    if (num2 != 0) {
                        resultado = num1 / num2;
                    } else {
                        JOptionPane.showMessageDialog(this, "No se puede dividir por cero", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
                case '%':
                    resultado = num1 % num2;
                    break;
            }

            pantalla.setText(String.valueOf(resultado));
            num1 = resultado;
        } else {
            num1 = Double.parseDouble(pantalla.getText());
            operacion = comando.charAt(0);
            pantalla.setText("0");
        }
    }
}