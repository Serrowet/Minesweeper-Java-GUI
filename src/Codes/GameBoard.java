package Codes;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameBoard {
    private CellButton[][] cells;
    private JPanel boardPanel;
    private int size;
    private int mineCount;
    private int flaggedMines;
    private IconManager iconManager;

    public GameBoard(int size, int mineCount, IconManager iconManager) {
        this.size = size;
        this.mineCount = mineCount;
        this.iconManager = iconManager;
        this.flaggedMines = 0;

        initializeBoard();
        placeMines();
        calculateAdjacentMines();
    }

    private void initializeBoard() {
        cells = new CellButton[size][size];
        boardPanel = new JPanel(new GridLayout(size, size, 2, 2));
        boardPanel.setBackground(Minesweeper.BOARD_GRID_COLOR);
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col] = new CellButton(row, col, iconManager);
                boardPanel.add(cells[row][col]);
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int minesPlaced = 0;

        while (minesPlaced < mineCount) {
            int row = random.nextInt(size);
            int col = random.nextInt(size);

            if (!cells[row][col].isMine()) {
                cells[row][col].setMine(true);
                minesPlaced++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (!cells[row][col].isMine()) {
                    int count = countAdjacentMines(row, col);
                    cells[row][col].setAdjacentMines(count);
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;

                int newRow = row + i;
                int newCol = col + j;

                if (isValidPosition(newRow, newCol) && cells[newRow][newCol].isMine()) {
                    count++;
                }
            }
        }

        return count;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public void revealAllCells() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col].reveal();
            }
        }
    }

    public void updateRemainingMines() {
        flaggedMines = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (cells[row][col].isFlagged()) {
                    flaggedMines++;
                }
            }
        }
    }

    public CellButton getCell(int row, int col) {
        if (isValidPosition(row, col)) {
            return cells[row][col];
        }
        return null;
    }

    public JPanel getPanel() { return boardPanel; }
    public CellButton[][] getCells() { return cells; }
    public int getSize() { return size; }
    public int getMineCount() { return mineCount; }
    public int getFlaggedMines() { return flaggedMines; }
}