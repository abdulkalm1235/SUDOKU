import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class SlidingPuzzleGame extends JFrame {
    private static final int GRID_SIZE = 3;
    private final JPanel boardPanel;
    private final JButton[][] tiles;
    private Point emptySpot;

    public SlidingPuzzleGame() {
        setTitle("3x3 Sliding Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null);

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        tiles = new JButton[GRID_SIZE][GRID_SIZE];

        initializeBoard();
        add(boardPanel);

        setVisible(true);
    }

    private void initializeBoard() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE * GRID_SIZE - 1; i++) {
            numbers.add(i + 1);
        }
        Collections.shuffle(numbers);
        numbers.add(0); // The empty spot

        int count = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JButton button = new JButton();
                int number = numbers.get(count);
                if (number == 0) {
                    emptySpot = new Point(j, i);
                    button.setEnabled(false);
                } else {
                    button.setText(String.valueOf(number));
                }
                button.addActionListener(new TileActionListener());
                tiles[i][j] = button;
                boardPanel.add(button);
                count++;
            }
        }
    }

    private class TileActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            Point clickedPoint = findButton(clickedButton);
            if (isAdjacent(clickedPoint, emptySpot)) {
                swapTiles(clickedPoint, emptySpot);
                emptySpot = clickedPoint;
                checkWinCondition();
            }
        }
    }

    private Point findButton(JButton button) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (tiles[i][j] == button) {
                    return new Point(j, i);
                }
            }
        }
        return null;
    }

    private boolean isAdjacent(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) == 1;
    }

    private void swapTiles(Point a, Point b) {
        JButton buttonA = tiles[a.y][a.x];
        JButton buttonB = tiles[b.y][b.x];
        String text = buttonA.getText();
        buttonA.setText(buttonB.getText());
        buttonB.setText(text);
        buttonA.setEnabled(false);
        buttonB.setEnabled(true);
    }

    private void checkWinCondition() {
        boolean won = true;
        int count = 1;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                String text = tiles[i][j].getText();
                if (!text.isEmpty() && Integer.parseInt(text) != count) {
                    won = false;
                    break;
                }
                if (count == GRID_SIZE * GRID_SIZE - 1) break;
                count++;
            }
            if (!won) break;
        }
        if (won) {
            JOptionPane.showMessageDialog(this, "Congratulations! You've won!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SlidingPuzzleGame());
    }
}
