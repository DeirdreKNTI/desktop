package Aplicaciones;

import javax.swing.*;

import Plantillas.Ventana;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class Snake extends Ventana {
  private static final int TILE_SIZE = 20;
  private static final int WIDTH = 20;
  private static final int HEIGHT = 20;
  private LinkedList<Point> snake;
  private Point food;
  private char direction;
  private boolean gameOver;

  public Snake() {
    super("Snake");
    snake = new LinkedList<>();
    snake.add(new Point(WIDTH / 2, HEIGHT / 2));
    direction = 'U'; // U: Up, D: Down, L: Left, R: Right
    spawnFood();
    gameOver = false;

    // Configuración del panel de juego
    GamePanel gamePanel = new GamePanel();
    add(gamePanel);

    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_UP:
            if (direction != 'D')
              direction = 'U';
            break;
          case KeyEvent.VK_DOWN:
            if (direction != 'U')
              direction = 'D';
            break;
          case KeyEvent.VK_LEFT:
            if (direction != 'R')
              direction = 'L';
            break;
          case KeyEvent.VK_RIGHT:
            if (direction != 'L')
              direction = 'R';
            break;
        }
      }
    });

    Timer timer = new Timer(100, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
          moveSnake();
          checkCollision();
          gamePanel.repaint(); // Repaint the game panel
        }
      }
    });
    timer.start();
  }

  private void spawnFood() {
    Random rand = new Random();
    int x = rand.nextInt(WIDTH);
    int y = rand.nextInt(HEIGHT);
    food = new Point(x, y);
  }

  private void moveSnake() {
    Point head = snake.getFirst();
    Point newHead = new Point(head);

    switch (direction) {
      case 'U':
        newHead.y--;
        break;
      case 'D':
        newHead.y++;
        break;
      case 'L':
        newHead.x--;
        break;
      case 'R':
        newHead.x++;
        break;
    }

    snake.addFirst(newHead);

    // Check if the snake has eaten the food
    if (newHead.equals(food)) {
      spawnFood(); // Spawn new food
    } else {
      snake.removeLast(); // Remove tail
    }
  }

  private void checkCollision() {
    Point head = snake.getFirst();

    // Check wall collision
    if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
      gameOver = true;
      JOptionPane.showMessageDialog(this, "Game Over! Score: " + (snake.size() - 1));
      System.exit(0);
    }

    // Check self collision
    for (int i = 1; i < snake.size(); i++) {
      if (head.equals(snake.get(i))) {
        gameOver = true;
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + (snake.size() - 1));
        System.exit(0);
      }
    }
  }

  class GamePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g); // Llama al método de la superclase para limpiar el panel

      // Dibuja la serpiente
      g.setColor(Color.GREEN);
      for (Point p : snake) {
        g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
      }

      // Dibuja la comida
      g.setColor(Color.RED);
      g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
  }

}
