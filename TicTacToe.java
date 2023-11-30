import javax.swing.*;

public class TicTacToe {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() ->  {
      GameFrame gameFrame = new GameFrame();
      gameFrame.setVisible(true);
    });
  }
}

public enum Player {
    X('X'), O('O');

    private char symbol;

    Player(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
  private JButton[][] buttons;
  public GameBoard gameBoard;

  public GameFrame() {
    setTitle("Tic Tac Toe");
    setSize(300, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    buttons = new JButton[GameBoard.getSize()][GameBoard.getSize()];
    gameBoard = new GameBoard();

    initializeButtons();
  }

  private void initializeButtons() {
    setLayout(new GridLayout(GameBoard.getSize(), GameBoard.getSize()));

    for (int i = 0; i < GameBoard.getSize(); i++) {
      for (int j = 0; j < GameBoard.getSize(); j++) {
        buttons[i][j] = new JButton();
        buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
        buttons[i][j].setFocusPainted(false);
        ButtonClickListener clickListener = new ButtonClickListener(i, j, gameBoard, this);
        buttons[i][j].addActionListener(clickListener);
        add(buttons[i][j]);
      }
    }
  }

  public void updateButton(int row, int col, char symbol) {
    buttons[row][col].setText(String.valueOf(symbol));
  }

  public void showWinnerDialog(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  public void showTieDialog(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  public void resetGame() {
    gameBoard.reset();
    for (int i = 0; i < GameBoard.getSize(); i++) {
      for (int j = 0; j < GameBoard.getSize(); j++) {
        buttons[i][j].setText("");
      }
    }
  }
}

public class GameBoard {
  private static int SIZE = 3;
  private static char EMPTY = '-';
  private char[][] board;
  public Player currentPlayer;

  public GameBoard() {
    board = new char[SIZE][SIZE];
    currentPlayer = Player.X;

    // Inisialisasi papan
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        board[i][j] = EMPTY;
      }
    }
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public boolean makeMove(int row, int col) {
    if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || board[row][col] != EMPTY) {
      return false;
    }

    board[row][col] = currentPlayer.getSymbol();
    return true;
  }

  public void switchPlayer() {
    currentPlayer = (currentPlayer == Player.X) ? Player.O : Player.X;
  }

  public void reset() {
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        board[i][j] = EMPTY;
      }
    }
    currentPlayer = Player.X;
  }

  public boolean checkWinner() {
    // Cek baris dan kolom
    for (int i = 0; i < SIZE; i++) {
      if ((board[i][0] == currentPlayer.getSymbol() && board[i][1] == currentPlayer.getSymbol()
          && board[i][2] == currentPlayer.getSymbol()) ||
          (board[0][i] == currentPlayer.getSymbol() && board[1][i] == currentPlayer.getSymbol()
              && board[2][i] == currentPlayer.getSymbol())) {
        return true;
      }
    }

    // Cek diagonal
    if ((board[0][0] == currentPlayer.getSymbol() && board[1][1] == currentPlayer.getSymbol()
        && board[2][2] == currentPlayer.getSymbol()) ||
        (board[0][2] == currentPlayer.getSymbol() && board[1][1] == currentPlayer.getSymbol()
            && board[2][0] == currentPlayer.getSymbol())) {
      return true;
    }

    return false;
  }

  public boolean isBoardFull() {
    for (int i = 0; i < SIZE; i++) {
      for (int j = 0; j < SIZE; j++) {
        if (board[i][j] == EMPTY) {
          return false;
        }
      }
    }
    return true;
  }

  public static int getSize() {
    return SIZE;
  }
}

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonClickListener implements ActionListener {
    private int row;
    private int col;
    public GameBoard gameBoard;
    public GameFrame gameFrame;

    public ButtonClickListener(int row, int col, GameBoard gameBoard, GameFrame gameFrame) {
        this.row = row;
        this.col = col;
        this.gameBoard = gameBoard;
        this.gameFrame = gameFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameBoard.makeMove(row, col)) {
            gameFrame.updateButton(row, col, gameBoard.getCurrentPlayer().getSymbol());

            if (gameBoard.checkWinner()) {
                gameFrame.showWinnerDialog("Player " + gameBoard.getCurrentPlayer().getSymbol() + " wins!");
                gameFrame.resetGame();
            } else if (gameBoard.isBoardFull()) {
                gameFrame.showTieDialog("It's a tie!");
                gameFrame.resetGame();
            } else {
                gameBoard.switchPlayer();
            }
        }
    }
}
