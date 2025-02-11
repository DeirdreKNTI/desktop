package Ventanas;

import Plantillas.Ventana;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.util.Timer;
import java.util.TimerTask;

public class PantallaCarga extends Ventana {
  private JLabel msg;
  private Timer timer;
  private boolean cagando = true;

  public PantallaCarga() {
    super("Por favor espere");
    msg = new JLabel("Cargando");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    msg.setHorizontalAlignment(JLabel.CENTER);
    zCentral.setLayout(new BorderLayout());
    this.zCentral.add(msg, SwingConstants.CENTER);
    this.setVisible(true);

    LoadAnimation();
    stopAnimation();
  }

  private void LoadAnimation() {
    new Thread(() -> {
      String text = "Cargando";
      while (cagando) {
        for (int i = 0; i < 3; i++) {
          text += ".";
          msg.setText(text);
          try {
            TimeUnit.MILLISECONDS.sleep(500);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
          }
        }
        text = "Cargando";
      }
    }).start();
  }

  public void stopAnimation() {
    timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        cagando = false;
        timer.cancel();
        dispose();
        new PantallaInicio();
      }
    }, 5000);
  }
}
