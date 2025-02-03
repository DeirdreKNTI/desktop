package Aplicaciones;

import Plantillas.Ventana;
import javax.swing.*;
import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;

public class Monitor extends Ventana {

    private JProgressBar cpuBar;
    private JProgressBar ramBar;
    private JProgressBar diskBar;
    private JLabel cpuLabel;
    private JLabel ramLabel;
    private JLabel diskLabel;
    private Timer timer;

    public Monitor() {
        super("Monitor de Recursos");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cpuBar = createProgressBar();
        ramBar = createProgressBar();
        diskBar = createProgressBar();

        cpuLabel = new JLabel("CPU: 0%");
        ramLabel = new JLabel("RAM: 0 MB / 0 MB");
        diskLabel = new JLabel("Disco: 0 GB libre de 0 GB");

        mainPanel.add(createResourcePanel("CPU", cpuBar, cpuLabel));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createResourcePanel("RAM", ramBar, ramLabel));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createResourcePanel("Disco", diskBar, diskLabel));

        add(mainPanel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Monitor de Recursos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Actualizar los datos cada segundo
        timer = new Timer(1000, e -> updateResources());
        timer.start();
    }

    private JProgressBar createProgressBar() {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);
        bar.setPreferredSize(new Dimension(300, 30));
        return bar;
    }

    private JPanel createResourcePanel(String title, JProgressBar bar, JLabel label) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        bar.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(bar);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(label);

        return panel;
    }

    private void updateResources() {
        // Obtener el uso de CPU
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        double cpuUsage = osBean.getSystemLoadAverage() * 100;
        cpuBar.setValue((int) cpuUsage);
        cpuLabel.setText("CPU: " + new DecimalFormat("#.##").format(cpuUsage) + "%");

        // Obtener el uso de RAM
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        int ramPercentage = (int) ((double) usedMemory / maxMemory * 100);
        ramBar.setValue(ramPercentage);
        ramLabel.setText("RAM: " + usedMemory + " MB / " + maxMemory + " MB");

        // Obtener el espacio libre en disco
        java.io.File disk = new java.io.File("/");
        long freeSpace = disk.getFreeSpace() / (1024 * 1024 * 1024);
        long totalSpace = disk.getTotalSpace() / (1024 * 1024 * 1024);
        int diskPercentage = (int) ((double) (totalSpace - freeSpace) / totalSpace * 100);
        diskBar.setValue(diskPercentage);
        diskLabel.setText("Disco: " + freeSpace + " GB libre de " + totalSpace + " GB");
    }
}

