package Aplicaciones;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import Plantillas.Ventana;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TaskManager extends Ventana {
    private JTable table;
    private ProcessTableModel tableModel;
    private JButton refreshButton;
    private JButton endTaskButton;

    public TaskManager(JFrame parent) {
        super("Administrador de tareas");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
                    String pid = (String) tableModel.getValueAt(selectedRow, 0);
                    endProcess(pid);
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
        try {
            Process process = Runtime.getRuntime().exec("tasklist /fo csv /nh");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String name = parts[0].replaceAll("\"", "");
                    String pid = parts[1].replaceAll("\"", "");
                    String memUsage = parts[4].replaceAll("\"", "").trim();
                    processes.add(new ProcessInfo(name, pid, "N/A", memUsage));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return processes;
    }

    private void endProcess(String pid) {
        try {
            Runtime.getRuntime().exec("taskkill /F /PID " + pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ProcessInfo {
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

class ProcessTableModel extends AbstractTableModel {
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

