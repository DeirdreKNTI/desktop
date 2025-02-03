package Aplicaciones;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import Plantillas.Ventana;

import java.awt.*;
import java.io.File;

public class FileExplorer extends Ventana {

    private JTree tree;
    private DefaultTreeModel treeModel;

    public FileExplorer() {
        super("Explorador de Archivos");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el árbol de archivos
        treeModel = new DefaultTreeModel(new DefaultMutableTreeNode());
        tree = new JTree(treeModel);
        tree.setShowsRootHandles(true);
        JScrollPane scrollPane = new JScrollPane(tree);

        // Botón para seleccionar directorio
        JButton openButton = new JButton("Abrir Directorio");
        openButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedDir = fileChooser.getSelectedFile();
                loadDirectory(selectedDir);
            }
        });

        // Panel para el botón
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openButton);

        // Añadir componentes al frame
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadDirectory(File dir) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(dir.getName());
        treeModel.setRoot(root);
        listFiles(dir, root);
    }

    private void listFiles(File dir, DefaultMutableTreeNode parent) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(file.getName());
                parent.add(node);
                if (file.isDirectory()) {
                    listFiles(file, node);
                }
            }
        }
    }
}

