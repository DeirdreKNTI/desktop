package Ventanas;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.File;
import javax.imageio.ImageIO;

import Aplicaciones.Calculadora;
import Aplicaciones.FileExplorer;
import Aplicaciones.Monitor;
import Aplicaciones.SnakeGame;

import Plantillas.Ventana;

public class PantallaEscritorio extends Ventana {
    JPanel ico1, ico2, ico3, ico4, ico5, ico6;
    Dimension iconD = new Dimension(30, 30);
    JLabel clockLabel;
    JButton calendarButton, wallpaperButton;
    JPanel contentPanel, wallpaperPanel;
    Image backgroundImage;
    JComboBox<String> wallpaperComboBox;
    JButton applyWallpaperButton;

    public PantallaEscritorio() {
        super("Escritorio");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        layoutComponents();

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

        zSur.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent dark background
        zSur.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        leftPanel.setOpaque(false);
        leftPanel.add(ico1);
        leftPanel.add(ico2);
        leftPanel.add(ico3);
        leftPanel.add(ico4);

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
        ico1 = createIcon(Color.BLACK, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new SnakeGame().setVisible(true);
            }
        });
        ico2 = createIcon(Color.lightGray, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Calculadora().setVisible(true);
            }
        });
        ico3 = createIcon(null, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new FileExplorer().setVisible(true);
            }
        });
        ico4 = createIcon(Color.blue, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Monitor().setVisible(true);
            }
        });
    }

    private JPanel createIcon(Color color, MouseAdapter listener) {
        JPanel icon = new JPanel();
        icon.setPreferredSize(iconD);
        if (color != null) icon.setBackground(color);
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

        String[] weekdays = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
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

        String[] options = {"Color Sólido", "Imagen"};
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
                    JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        contentPanel.repaint();
    }
}

