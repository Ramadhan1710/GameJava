import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {

  private static final int TILE_SIZE = 20;
  private static final int GRID_SIZE = 20;
  private static final int GAME_SPEED = 150; // milliseconds per move

  private LinkedList<Point> snake;
  private Point food;
  private char direction;
  private boolean running;

  public SnakeGame() {
    setTitle("Snake Game");
    setSize(GRID_SIZE * TILE_SIZE, GRID_SIZE * TILE_SIZE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    snake = new LinkedList<>();
    direction = 'R'; // 'R' for right, 'L' for left, 'U' for up, 'D' for down
    running = true;

    initializeGame();
    addKeyListener(this);
    setFocusable(true);

    Timer timer = new Timer(GAME_SPEED, this);
    timer.start();
  }

  private void initializeGame() {
    snake.clear();
    snake.add(new Point(5, 5));
    generateFood();
  }

  private void generateFood() {
    Random rand = new Random();
    int x, y;
    do {
      x = rand.nextInt(GRID_SIZE);
      y = rand.nextInt(GRID_SIZE);
    } while (snake.contains(new Point(x, y)));
    food = new Point(x, y);
  }

  private void move() {
    Point head = snake.getFirst();
    Point newHead;

    switch (direction) {
      case 'U':
        newHead = new Point(head.x, (head.y - 1 + GRID_SIZE) % GRID_SIZE);
        break;
      case 'D':
        newHead = new Point(head.x, (head.y + 1) % GRID_SIZE);
        break;
      case 'L':
        newHead = new Point((head.x - 1 + GRID_SIZE) % GRID_SIZE, head.y);
        break;
      case 'R':
        newHead = new Point((head.x + 1) % GRID_SIZE, head.y);
        break;
      default:
        return;
    }

    if (snake.contains(newHead) && !newHead.equals(food)) {
      running = false;
      showGameOver();
    } else {
      snake.addFirst(newHead);
      if (newHead.equals(food)) {
        generateFood();
      } else {
        snake.removeLast();
      }
    }
  }

  private void showGameOver() {
    JOptionPane.showMessageDialog(this, "Game Over!\nYour score: " + (snake.size() - 1));
    initializeGame();
    running = true;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (running) {
      move();
      repaint();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_UP:
        if (direction != 'D') {
          direction = 'U';
        }
        break;
      case KeyEvent.VK_DOWN:
        if (direction != 'U') {
          direction = 'D';
        }
        break;
      case KeyEvent.VK_LEFT:
        if (direction != 'R') {
          direction = 'L';
        }
        break;
      case KeyEvent.VK_RIGHT:
        if (direction != 'L') {
          direction = 'R';
        }
        break;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // Not used
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // Not used
  }

  @Override
  public void paint(Graphics g) {
    Image image = createImage(getWidth(), getHeight());
    Graphics offscreen = image.getGraphics();
    super.paint(offscreen);
    drawGrid(offscreen);
    drawSnake(offscreen);
    drawFood(offscreen);
    g.drawImage(image, 0, 0, this);
  }

  private void drawGrid(Graphics g) {
    g.setColor(Color.BLACK);
    for (int i = 0; i < GRID_SIZE; i++) {
      for (int j = 0; j < GRID_SIZE; j++) {
        g.drawRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
      }
    }
  }

  private void drawSnake(Graphics g) {
    g.setColor(Color.GREEN);
    for (Point p : snake) {
      g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
  }

  private void drawFood(Graphics g) {
    g.setColor(Color.RED);
    g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      SnakeGame game = new SnakeGame();
      game.setVisible(true);
    });
  }
}
