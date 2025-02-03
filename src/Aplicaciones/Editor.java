package Aplicaciones;

import javax.swing.*;

import Plantillas.Ventana;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Editor extends Ventana implements ActionListener {
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newMenuItem, openMenuItem, saveMenuItem, exitMenuItem;

    public Editor() {
        // Configuración de la ventana
        super("Editor de Texto Simple");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear el área de texto
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Crear la barra de menú
        menuBar = new JMenuBar();

        // Crear el menú "Archivo"
        fileMenu = new JMenu("Archivo");

        // Crear los elementos del menú
        newMenuItem = new JMenuItem("Nuevo");
        openMenuItem = new JMenuItem("Abrir");
        saveMenuItem = new JMenuItem("Guardar");
        exitMenuItem = new JMenuItem("Salir");

        // Añadir los listeners a los elementos del menú
        newMenuItem.addActionListener(this);
        openMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);

        // Añadir los elementos al menú "Archivo"
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        // Añadir el menú "Archivo" a la barra de menú
        menuBar.add(fileMenu);

        // Añadir la barra de menú a la ventana
        setJMenuBar(menuBar);

        // Hacer visible la ventana
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newMenuItem) {
            // Limpiar el área de texto
            textArea.setText("");
        } else if (e.getSource() == openMenuItem) {
            // Abrir un archivo
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    textArea.read(reader, null);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == saveMenuItem) {
            // Guardar el archivo
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    textArea.write(writer);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == exitMenuItem) {
            // Salir de la aplicación
            System.exit(0);
        }
    }
}