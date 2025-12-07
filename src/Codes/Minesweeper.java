package Codes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Minesweeper implements MouseListener {
    private JFrame frame;
    private GameBoard gameBoard;
    private GameController gameController;
    private IconManager iconManager;
    private GameStatus gameStatus;
    private JLabel mineCounter;
    private int boardSize = 10;
    private int mineCount = 10;

    public static final Color BACKGROUND_COLOR = Color.BLACK;
    public static final Color BUTTON_COLOR = Color.WHITE;
    public static final Color REVEALED_COLOR = Color.BLACK;
    public static final Color EMPTY_COLOR = Color.BLACK;
    public static final Color MINE_COLOR = Color.RED;
    public static final Color BOARD_GRID_COLOR = Color.BLACK;

    public Minesweeper() {
        initializeGame();
    }

    private void initializeGame() {
        frame = new JFrame("Minesweeper");
        frame.setSize(500, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        iconManager = new IconManager(40);
        gameBoard = new GameBoard(boardSize, mineCount, iconManager);
        gameController = new GameController(gameBoard);
        gameStatus = GameStatus.PLAYING;

        setupFrame();
        frame.setVisible(true);
    }

    private void setupFrame() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        mainPanel.add(gameBoard.getPanel(), BorderLayout.CENTER);

        frame.add(mainPanel);

        for (CellButton[] row : gameBoard.getCells()) {
            for (CellButton cell : row) {
                cell.addMouseListener(this);
            }
        }
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setBackground(BACKGROUND_COLOR);

        JButton restartButton = new JButton("🔄 New Game");
        restartButton.addActionListener(e -> restartGame());

        mineCounter = new JLabel("💣 Mines: " + mineCount);
        mineCounter.setFont(new Font("Arial", Font.BOLD, 14));
        mineCounter.setForeground(Color.WHITE);

        topPanel.add(restartButton);
        topPanel.add(mineCounter);

        return topPanel;
    }

    private void updateMineCounter() {
        int remaining = mineCount - gameBoard.getFlaggedMines();
        mineCounter.setText("💣 Mines: " + remaining);
    }

    private void restartGame() {
        frame.dispose();
        new Minesweeper();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameStatus != GameStatus.PLAYING) return;

        CellButton cell = (CellButton) e.getComponent();

        if (SwingUtilities.isLeftMouseButton(e)) {
            handleLeftClick(cell);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            handleRightClick(cell);
        }

        checkGameStatus();
    }

    private void handleLeftClick(CellButton cell) {
        if (cell.isFlagged()) return;

        if (cell.isMine()) {
            gameOver(false);
        } else {
            gameController.revealCell(cell.getRow(), cell.getCol());
        }
    }

    private void handleRightClick(CellButton cell) {
        if (!cell.isRevealed()) {
            cell.toggleFlag();
            gameBoard.updateRemainingMines();
            updateMineCounter();
        }
    }

    private void checkGameStatus() {
        if (gameController.checkWinCondition()) {
            gameOver(true);
        }
    }

    private void gameOver(boolean won) {
        gameStatus = won ? GameStatus.WON : GameStatus.LOST;

        gameBoard.revealAllCells();

        String message = won ? "🎉 Congratulations! You won!" : "💥 Game Over! You hit a mine!";
        int option = JOptionPane.showConfirmDialog(frame,
                message + "\nDo you want to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Minesweeper());
    }
}