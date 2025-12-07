package Codes;

import javax.swing.*;
import java.awt.*;

public class CellButton extends JButton {
    private int row;
    private int col;
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int adjacentMines;
    private IconManager iconManager;

    public CellButton(int row, int col, IconManager iconManager) {
        this.row = row;
        this.col = col;
        this.iconManager = iconManager;
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.adjacentMines = 0;

        setPreferredSize(new Dimension(40, 40));
        setFont(new Font("Arial", Font.BOLD, 16));
        setFocusPainted(false);
        setContentAreaFilled(true);
        setBackground(Minesweeper.BUTTON_COLOR);
        setBorder(BorderFactory.createRaisedBevelBorder());
    }

    public void reveal() {
        if (isRevealed) return;

        isRevealed = true;
        setEnabled(false);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if (isMine) {
            setIcon(iconManager.getMineIcon());
            setBackground(Minesweeper.MINE_COLOR);
        } else if (adjacentMines > 0) {
            setText(String.valueOf(adjacentMines));
            setForeground(getNumberColor(adjacentMines));
            setBackground(Minesweeper.REVEALED_COLOR);
        } else {
            setBackground(Minesweeper.EMPTY_COLOR);
        }
    }

    public void toggleFlag() {
        if (isRevealed) return;

        isFlagged = !isFlagged;

        if (isFlagged) {
            setIcon(iconManager.getFlagIcon());
        } else {
            setIcon(null);
        }
    }

    private Color getNumberColor(int number) {
        switch (number) {
            case 1: return Color.BLUE;
            case 2: return new Color(0, 128, 0);
            case 3: return Color.RED;
            case 4: return new Color(128, 0, 128);
            case 5: return new Color(128, 0, 0);
            case 6: return new Color(64, 224, 208);
            case 7: return Color.BLACK;
            case 8: return Color.GRAY;
            default: return Color.BLACK;
        }
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean isMine() { return isMine; }
    public boolean isRevealed() { return isRevealed; }
    public boolean isFlagged() { return isFlagged; }
    public int getAdjacentMines() { return adjacentMines; }

    public void setMine(boolean mine) { this.isMine = mine; }
    public void setAdjacentMines(int count) { this.adjacentMines = count; }
}