package Codes;

import java.util.LinkedList;
import java.util.Queue;

public class GameController {
    private GameBoard gameBoard;

    public GameController(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void revealCell(int row, int col) {
        CellButton cell = gameBoard.getCell(row, col);

        if (cell == null || cell.isRevealed() || cell.isFlagged()) {
            return;
        }

        if (cell.getAdjacentMines() > 0) {
            cell.reveal();
        } else {
            revealAdjacentCells(row, col);
        }
    }

    private void revealAdjacentCells(int startRow, int startCol) {
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[gameBoard.getSize()][gameBoard.getSize()];

        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];

            CellButton cell = gameBoard.getCell(row, col);
            if (cell != null && !cell.isRevealed() && !cell.isFlagged()) {
                cell.reveal();

                if (cell.getAdjacentMines() == 0) {
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (i == 0 && j == 0) continue;

                            int newRow = row + i;
                            int newCol = col + j;

                            if (isValidPosition(newRow, newCol) && !visited[newRow][newCol]) {
                                queue.add(new int[]{newRow, newCol});
                                visited[newRow][newCol] = true;
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean checkWinCondition() {
        CellButton[][] cells = gameBoard.getCells();
        int size = gameBoard.getSize();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                CellButton cell = cells[row][col];

                if (!cell.isMine() && !cell.isRevealed()) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < gameBoard.getSize() &&
                col >= 0 && col < gameBoard.getSize();
    }
}