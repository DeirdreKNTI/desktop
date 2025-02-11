package Ventanas;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.File;
import javax.imageio.ImageIO;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;

import Aplicaciones.Calculadora;
import Aplicaciones.Editor;
import Aplicaciones.FileExplorer;
import Aplicaciones.SnakeGame;
import Aplicaciones.TaskManager;

import Plantillas.Ventana;

public class PantallaEscritorio extends Ventana {
    JPanel ico1, ico2, ico3, ico4, ico5;
    Dimension iconD = new Dimension(35, 35);
    JLabel clockLabel;
    JButton calendarButton, wallpaperButton;
    JPanel contentPanel, wallpaperPanel;
    Image backgroundImage;
    JComboBox<String> wallpaperComboBox;
    JButton applyWallpaperButton;

    private List<ManagedApplication> openApplications;
    private Thread resourceMonitorThread;
    private volatile boolean isMonitoring;
    private static final int MAX_MEMORY_USAGE = 100 * 1024 * 1024; // 100 MB en bytes
    private static final double MAX_CPU_USAGE = 0.8; // 80% de uso de CPU

    public PantallaEscritorio() {
        super("Escritorio");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        openApplications = new ArrayList<>();
        initComponents();
        layoutComponents();
        startResourceMonitoring();

        setVisible(true);
    }

    private void initComponents() {
        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPanel.setLayout(new BorderLayout());

        setupIcons();
        setupClock();
        setupCalendar();
        setupWallpaperButton();
        setupWallpaperPanel();
    }

    private void layoutComponents() {
        setContentPane(contentPanel);

        zSur.setBackground(new Color(0, 0, 0, 150)); // Fondo oscuro semi-transparente
        zSur.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        leftPanel.setOpaque(false);
        leftPanel.add(ico1);
        leftPanel.add(ico2);
        leftPanel.add(ico3);
        leftPanel.add(ico4);
        leftPanel.add(ico5);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        rightPanel.setOpaque(false);
        rightPanel.add(clockLabel);
        rightPanel.add(calendarButton);
        rightPanel.add(wallpaperButton);

        zSur.add(leftPanel, BorderLayout.WEST);
        zSur.add(rightPanel, BorderLayout.EAST);

        zNorte.setOpaque(false);
        zCentral.setLayout(new BorderLayout());
        zCentral.setOpaque(false);

        zOeste.setPreferredSize(new Dimension(200, 0));
        zOeste.setOpaque(false);

        zEste.setPreferredSize(new Dimension(200, 0));
        zEste.setOpaque(false);
        zEste.add(wallpaperPanel, BorderLayout.NORTH);

        contentPanel.add(zNorte, BorderLayout.NORTH);
        contentPanel.add(zSur, BorderLayout.SOUTH);
        contentPanel.add(zCentral, BorderLayout.CENTER);
        contentPanel.add(zOeste, BorderLayout.WEST);
        contentPanel.add(zEste, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    private void setupIcons() {
        ico1 = createIcon("desktop\\assets\\snake.png", new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openManagedApplication(new SnakeGame(PantallaEscritorio.this), "SnakeGame");
            }
        });
        ico2 = createIcon("desktop\\assets\\calc.png", new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openManagedApplication(new Calculadora(PantallaEscritorio.this), "Calculadora");
            }
        });
        ico3 = createIcon("desktop\\assets\\folder.png", new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openManagedApplication(new FileExplorer(PantallaEscritorio.this), "FileExplorer");
            }
        });
        ico4 = createIcon("desktop\\assets\\monitor.png", new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SwingUtilities.invokeLater(() -> openTaskManager());
            }
        });
        ico5 = createIcon("desktop\\assets\\text.png", new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openManagedApplication(new Editor(PantallaEscritorio.this), "Editor");
            }
        });
    }

    private JPanel createIcon(String imagePath, MouseAdapter listener) {
        JPanel icon = new JPanel();
        icon.setPreferredSize(iconD);
        icon.setOpaque(false);
        icon.setLayout(new BorderLayout());

        if (imagePath != null) {
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(iconD.width, iconD.height, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            JLabel iconLabel = new JLabel(resizedIcon);
            icon.add(iconLabel, BorderLayout.CENTER);
        }

        icon.addMouseListener(listener);
        return icon;
    }

    private void setupClock() {
        clockLabel = new JLabel();
        clockLabel.setForeground(Color.WHITE);
        clockLabel.setFont(new Font("Arial", Font.BOLD, 14));
        updateClock();

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClock();
            }
        });
        timer.start();
    }

    private void updateClock() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        clockLabel.setText(sdf.format(new Date()));
    }

    private void setupCalendar() {
        calendarButton = new JButton("Calendario");
        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCalendar();
            }
        });
    }

    private void showCalendar() {
        JFrame calendarFrame = new JFrame("Calendario");
        calendarFrame.setSize(300, 200);
        calendarFrame.setLocationRelativeTo(this);

        JPanel calendarPanel = new JPanel(new BorderLayout());

        JLabel monthYearLabel = new JLabel("", SwingConstants.CENTER);
        updateMonthYearLabel(monthYearLabel);

        JPanel daysPanel = new JPanel(new GridLayout(0, 7));
        updateDaysPanel(daysPanel);

        calendarPanel.add(monthYearLabel, BorderLayout.NORTH);
        calendarPanel.add(daysPanel, BorderLayout.CENTER);

        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar.getInstance().add(Calendar.MONTH, -1);
                updateMonthYearLabel(monthYearLabel);
                updateDaysPanel(daysPanel);
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar.getInstance().add(Calendar.MONTH, 1);
                updateMonthYearLabel(monthYearLabel);
                updateDaysPanel(daysPanel);
            }
        });

        JPanel navigationPanel = new JPanel(new FlowLayout());
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);

        calendarPanel.add(navigationPanel, BorderLayout.SOUTH);

        calendarFrame.add(calendarPanel);
        calendarFrame.setVisible(true);
    }

    private void updateMonthYearLabel(JLabel label) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        label.setText(sdf.format(Calendar.getInstance().getTime()));
    }

    private void updateDaysPanel(JPanel panel) {
        panel.removeAll();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        String[] weekdays = { "Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb" };
        for (String day : weekdays) {
            panel.add(new JLabel(day, SwingConstants.CENTER));
        }

        for (int i = 1; i < firstDayOfWeek; i++) {
            panel.add(new JLabel(""));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            panel.add(new JLabel(String.valueOf(day), SwingConstants.CENTER));
        }

        panel.revalidate();
        panel.repaint();
    }

    private void setupWallpaperButton() {
        wallpaperButton = new JButton("Cambiar Fondo");
        wallpaperButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleWallpaperPanel();
            }
        });
    }

    private void setupWallpaperPanel() {
        wallpaperPanel = new JPanel();
        wallpaperPanel.setLayout(new BoxLayout(wallpaperPanel, BoxLayout.Y_AXIS));
        wallpaperPanel.setBorder(BorderFactory.createTitledBorder("Cambiar Fondo de Pantalla"));
        wallpaperPanel.setBackground(new Color(255, 255, 255, 200));

        String[] options = { "Color Sólido", "Imagen" };
        wallpaperComboBox = new JComboBox<>(options);

        applyWallpaperButton = new JButton("Aplicar");
        applyWallpaperButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyWallpaper();
            }
        });

        wallpaperPanel.add(wallpaperComboBox);
        wallpaperPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        wallpaperPanel.add(applyWallpaperButton);

        wallpaperPanel.setVisible(false);
    }

    private void toggleWallpaperPanel() {
        wallpaperPanel.setVisible(!wallpaperPanel.isVisible());
        revalidate();
        repaint();
    }

    private void applyWallpaper() {
        String selected = (String) wallpaperComboBox.getSelectedItem();
        if ("Color Sólido".equals(selected)) {
            Color color = JColorChooser.showDialog(this, "Elige un color", Color.WHITE);
            if (color != null) {
                backgroundImage = null;
                contentPanel.setBackground(color);
            }
        } else if ("Imagen".equals(selected)) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "png", "gif"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    backgroundImage = ImageIO.read(selectedFile);
                    contentPanel.setBackground(null);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        contentPanel.repaint();
    }

    private void openManagedApplication(JFrame app, String appName) {
        app.setVisible(true);
        ManagedApplication managedApp = new ManagedApplication(app, appName);
        openApplications.add(managedApp);
    }

    private void startResourceMonitoring() {
        isMonitoring = true;
        resourceMonitorThread = new Thread(() -> {
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            while (isMonitoring) {
                for (ManagedApplication app : new ArrayList<>(openApplications)) {
                    long appMemoryUsage = app.getMemoryUsage();
                    double appCpuUsage = app.getCpuUsage(osBean);

                    if (appMemoryUsage > MAX_MEMORY_USAGE || appCpuUsage > MAX_CPU_USAGE) {
                        SwingUtilities.invokeLater(() -> handleResourceOverflow(app));
                    }
                }

                try {
                    Thread.sleep(5000); // Verificar cada 5 segundos
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        resourceMonitorThread.start();
    }

    private void handleResourceOverflow(ManagedApplication app) {
        openApplications.remove(app);
        app.close();
        System.gc(); // Sugerir al recolector de basura que se ejecute
        JOptionPane.showMessageDialog(this,
            "Se ha cerrado la aplicación " + app.getName() + " debido al alto consumo de recursos.",
            "Aviso de Desbordamiento",
            JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void dispose() {
        isMonitoring = false;
        if (resourceMonitorThread != null) {
            resourceMonitorThread.interrupt();
        }
        for (ManagedApplication app : openApplications) {
            app.close();
        }
        super.dispose();
    }

    private void openTaskManager() {
        TaskManager taskManager = new TaskManager(openApplications);
        taskManager.setVisible(true);
    }

    public static class ManagedApplication {
        private JFrame application;
        private String name;
        private long lastCpuTime;
        private long lastSystemTime;

        public ManagedApplication(JFrame application, String name) {
            this.application = application;
            this.name = name;
            this.lastCpuTime = 0;
            this.lastSystemTime = System.nanoTime();
        }

        public String getName() {
            return name;
        }

        public long getMemoryUsage() {
            return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        }

        public double getCpuUsage(OperatingSystemMXBean osBean) {
            long systemTime = System.nanoTime();
            long processCpuTime = osBean.getProcessCpuTime();

            double cpuUsage = (double)(processCpuTime - lastCpuTime) / (systemTime - lastSystemTime);

            lastSystemTime = systemTime;
            lastCpuTime = processCpuTime;

            return cpuUsage;
        }

        public void close() {
            application.dispose();
        }
    }
}

