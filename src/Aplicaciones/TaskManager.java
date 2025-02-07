package Aplicaciones;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

import Ventanas.PantallaEscritorio.ManagedApplication;

public class TaskManager extends JFrame {
    private JTable table;
    private ProcessTableModel tableModel;
    private JButton refreshButton;
    private JButton endTaskButton;
    private List<ManagedApplication> managedApplications;

    public TaskManager(List<ManagedApplication> managedApplications) {
        this.managedApplications = managedApplications;
        setTitle("Administrador de tareas");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new ProcessTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        refreshButton = new JButton("Actualizar");
        endTaskButton = new JButton("Finalizar tarea");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(endTaskButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshProcessList();
            }
        });

        endTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    ManagedApplication app = managedApplications.get(selectedRow);
                    app.close();
                    managedApplications.remove(app);
                    refreshProcessList();
                }
            }
        });

        refreshProcessList();
    }

    private void refreshProcessList() {
        List<ProcessInfo> processes = getProcessList();
        tableModel.setProcesses(processes);
    }

    private List<ProcessInfo> getProcessList() {
        List<ProcessInfo> processes = new ArrayList<>();
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        for (ManagedApplication app : managedApplications) {
            String name = app.getName();
            long memoryUsage = app.getMemoryUsage();
            double cpuUsage = app.getCpuUsage(osBean);
            processes.add(new ProcessInfo(name, String.valueOf(app.hashCode()), 
                String.format("%.2f%%", cpuUsage * 100), 
                String.format("%.2f MB", memoryUsage / (1024.0 * 1024.0))));
        }
        return processes;
    }

    private class ProcessTableModel extends AbstractTableModel {
        private List<ProcessInfo> processes = new ArrayList<>();
        private String[] columnNames = {"Nombre", "PID", "Uso de CPU", "Uso de memoria"};

        public void setProcesses(List<ProcessInfo> processes) {
            this.processes = processes;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return processes.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ProcessInfo process = processes.get(rowIndex);
            switch (columnIndex) {
                case 0: return process.name;
                case 1: return process.pid;
                case 2: return process.cpuUsage;
                case 3: return process.memoryUsage;
                default: return null;
            }
        }
    }

    private static class ProcessInfo {
        String name;
        String pid;
        String cpuUsage;
        String memoryUsage;

        public ProcessInfo(String name, String pid, String cpuUsage, String memoryUsage) {
            this.name = name;
            this.pid = pid;
            this.cpuUsage = cpuUsage;
            this.memoryUsage = memoryUsage;
        }
    }
}

