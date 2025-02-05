package com.connectm;

import javax.swing.*;
import java.awt.*;

public class BoardPanelWithHeaders extends JPanel {
    private final int gridSize;
    private final int cellSize;
    private final int headerSize;  // space reserved for row/column numbers

    public BoardPanelWithHeaders(int gridSize, int cellSize, int headerSize) {
        this.gridSize = gridSize;
        this.cellSize = cellSize;
        this.headerSize = headerSize;
        // Total size includes headers plus the board area.
        int width = headerSize + gridSize * cellSize;
        int height = headerSize + gridSize * cellSize;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Use Graphics2D for smoother rendering.
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- Board Grid ---
        // Increase stroke thickness to 3 pixels and use a darker gray.
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(100, 100, 100)); // medium-dark gray for grid lines

        // The board grid begins at (headerSize, headerSize)
        int boardX = headerSize;
        int boardY = headerSize;
        int boardWidth = gridSize * cellSize;
        int boardHeight = gridSize * cellSize;

        // Draw vertical grid lines.
        for (int i = 0; i <= gridSize; i++) {
            int x = boardX + i * cellSize;
            g2d.drawLine(x, boardY, x, boardY + boardHeight);
        }
        // Draw horizontal grid lines.
        for (int i = 0; i <= gridSize; i++) {
            int y = boardY + i * cellSize;
            g2d.drawLine(boardX, y, boardX + boardWidth, y);
        }

        // --- Draw the Headers ---
        g2d.setColor(Color.DARK_GRAY);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
        g2d.setFont(headerFont);
        FontMetrics fm = g2d.getFontMetrics();

        // Column headers.
        for (int col = 0; col < gridSize; col++) {
            String colText = String.valueOf(col + 1);
            int x = boardX + col * cellSize;
            int y = 0;
            int textWidth = fm.stringWidth(colText);
            int textX = x + (cellSize - textWidth) / 2;
            int textY = y + (headerSize + fm.getAscent()) / 2 - 2;
            g2d.drawString(colText, textX, textY);
        }

        // Row headers.
        for (int row = 0; row < gridSize; row++) {
            String rowText = String.valueOf(row + 1);
            int x = 0;
            int y = boardY + row * cellSize;
            int textWidth = fm.stringWidth(rowText);
            int textX = x + (headerSize - textWidth) / 2;
            int textY = y + (cellSize + fm.getAscent()) / 2 - 2;
            g2d.drawString(rowText, textX, textY);
        }

        // TEMP: Draw a Sample Game Piece ---
        g2d.setColor(new Color(220, 20, 60));
        int pieceMargin = cellSize / 10;
        int pieceSize = cellSize - 2 * pieceMargin;

        // Place a sample piece in the center cell.
        int sampleCol = gridSize / 2;
        int sampleRow = gridSize / 2;
        int sampleX = boardX + sampleCol * cellSize + pieceMargin;
        int sampleY = boardY + sampleRow * cellSize + pieceMargin;
        g2d.fillOval(sampleX, sampleY, pieceSize, pieceSize);
    }
}