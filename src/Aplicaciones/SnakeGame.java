package Aplicaciones;

import Plantillas.Ventana;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends Ventana implements KeyListener, ActionListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 20;
    private static final int DELAY = 100;

    private final ArrayList<Point> snake;
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private JButton startButton, exitButton;
    private JPanel menuPanel, gamePanel, gameOverPanel;
    private JLabel titleLabel, scoreLabel;
    private int score = 0;

    public SnakeGame(JFrame Parent) {
        super("Snake Game");
        snake = new ArrayList<>();
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        addKeyListener(this);
        setFocusable(true);
        setAlwaysOnTop(true);

        createMenu();
        createGamePanel();
        add(menuPanel);
        setVisible(true);
    }

    private void createMenu() {
        menuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(50, 205, 50));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        titleLabel = new JLabel("Snake Game");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);

        startButton = createStyledButton("Comenzar Juego");
        exitButton = createStyledButton("Salir");

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        menuPanel.add(startButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(exitButton);
        menuPanel.add(Box.createVerticalGlue());
    }

    private void createGamePanel() {
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                draw(g);
            }
        };
        gamePanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        gamePanel.setBackground(Color.BLACK);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 100, 0));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.addActionListener(this);
        return button;
    }

    private void startGame() {
        snake.clear();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        spawnFood();
        running = true;
        score = 0;

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        
        remove(menuPanel);
        add(scoreLabel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        revalidate();
        repaint();

        new Thread(() -> {
            while (running) {
                move();
                checkCollisions();
                checkFood();
                gamePanel.repaint();
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void spawnFood() {
        Random random = new Random();
        int x = random.nextInt((WIDTH - UNIT_SIZE) / UNIT_SIZE) * UNIT_SIZE;
        int y = random.nextInt((HEIGHT - UNIT_SIZE) / UNIT_SIZE) * UNIT_SIZE;
        food = new Point(x, y);
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case 'U': newHead.y -= UNIT_SIZE; break;
            case 'D': newHead.y += UNIT_SIZE; break;
            case 'L': newHead.x -= UNIT_SIZE; break;
            case 'R': newHead.x += UNIT_SIZE; break;
        }

        snake.add(0, newHead);
        if (!checkFood()) {
            snake.remove(snake.size() - 1);
        }
    }

    private boolean checkFood() {
        if (snake.get(0).equals(food)) {
            spawnFood();
            score += 10;
            scoreLabel.setText("Score: " + score);
            return true;
        }
        return false;
    }

    private void checkCollisions() {
        Point head = snake.get(0);

        // Colisión con los bordes
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            running = false;
        }

        // Colisión con el cuerpo
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
                break;
            }
        }

        if (!running) {
            gameOver();
        }
    }

    private void gameOver() {
        remove(scoreLabel);
        remove(gamePanel);
        gameOverPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(178, 34, 34));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));

        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 48));
        gameOverLabel.setForeground(Color.WHITE);

        JLabel finalScoreLabel = new JLabel("Final Score: " + score);
        finalScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        finalScoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        finalScoreLabel.setForeground(Color.WHITE);

        JButton restartButton = createStyledButton("Reiniciar");
        restartButton.addActionListener(e -> {
            remove(gameOverPanel);
            startGame();
        });

        gameOverPanel.add(Box.createVerticalGlue());
        gameOverPanel.add(gameOverLabel);
        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameOverPanel.add(finalScoreLabel);
        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        gameOverPanel.add(restartButton);
        gameOverPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        gameOverPanel.add(exitButton);
        gameOverPanel.add(Box.createVerticalGlue());

        add(gameOverPanel);
        revalidate();
        repaint();
    }

    private void draw(Graphics g) {
        if (running) {
            // Dibujar bordes
            g.setColor(Color.WHITE);
            g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

            // Dibujar comida
            g.setColor(Color.RED);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

            // Dibujar serpiente
            for (int i = 0; i < snake.size(); i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                Point p = snake.get(i);
                g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (direction != 'U') direction = 'D';
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                if (direction != 'L') direction = 'R';
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startGame();
        } else if (e.getSource() == exitButton) {
            dispose();
        }
    }
}


