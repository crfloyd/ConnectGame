package com.connectm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanelWithHeaders extends JPanel {
    private final int gridSize;
    private final int cellSize;
    private final int headerSize;  // space reserved for row/column numbers
    // Stores the column index currently hovered over (-1 means none)
    private int hoveredColumn = -1;

    public BoardPanelWithHeaders(int gridSize, int cellSize, int headerSize) {
        this.gridSize = gridSize;
        this.cellSize = cellSize;
        this.headerSize = headerSize;
        // Total size includes headers plus the board area.
        int width = headerSize + gridSize * cellSize;
        int height = headerSize + gridSize * cellSize;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);

        // Add a mouse listener to track movement for column highlighting.
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Determine if the mouse is within the board area.
                int boardX = headerSize;
                int boardY = headerSize;
                int boardWidth = gridSize * cellSize;
                int boardHeight = gridSize * cellSize;
                Point p = e.getPoint();

                if (p.x >= boardX && p.x < boardX + boardWidth && p.y >= boardY && p.y < boardY + boardHeight) {
                    int col = (p.x - boardX) / cellSize;
                    if (col != hoveredColumn) {
                        hoveredColumn = col;
                        repaint();
                    }
                } else {
                    if (hoveredColumn != -1) {
                        hoveredColumn = -1;
                        repaint();
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoveredColumn = -1;
                repaint();
            }
        };
        addMouseMotionListener(mouseHandler);
        addMouseListener(mouseHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Use Graphics2D for smoother rendering.
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Define board area (offset by headerSize)
        int boardX = headerSize;
        int boardY = headerSize;
        int boardWidth = gridSize * cellSize;
        int boardHeight = gridSize * cellSize;

        // --- Draw Headers (Row and Column Numbers) ---
        g2d.setColor(Color.DARK_GRAY);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
        g2d.setFont(headerFont);
        FontMetrics fm = g2d.getFontMetrics();

        // Draw column headers.
        for (int col = 0; col < gridSize; col++) {
            String colText = String.valueOf(col + 1);
            int x = boardX + col * cellSize;
            int y = 0;
            int textWidth = fm.stringWidth(colText);
            int textX = x + (cellSize - textWidth) / 2;
            int textY = y + (headerSize + fm.getAscent()) / 2 - 2;
            g2d.drawString(colText, textX, textY);
        }
        // Draw row headers.
        for (int row = 0; row < gridSize; row++) {
            String rowText = String.valueOf(row + 1);
            int x = 0;
            int y = boardY + row * cellSize;
            int textWidth = fm.stringWidth(rowText);
            int textX = x + (headerSize - textWidth) / 2;
            int textY = y + (cellSize + fm.getAscent()) / 2 - 2;
            g2d.drawString(rowText, textX, textY);
        }

        // --- Draw the Board Background (Connect 4 style) ---
        Color boardBlue = new Color(0, 0, 150);
        g2d.setColor(boardBlue);
        g2d.fillRect(boardX, boardY, boardWidth, boardHeight);

        // --- Highlight the Hovered Column ---
        if (hoveredColumn >= 0 && hoveredColumn < gridSize) {
            // Create a semi-transparent white highlight.
            Color highlight = new Color(255, 255, 255, 100);
            int highlightX = boardX + hoveredColumn * cellSize;
            g2d.setColor(highlight);
            g2d.fillRect(highlightX, boardY, cellSize, boardHeight);
        }

        // --- Draw the Circular Cutouts (Holes) ---
        int holeMargin = cellSize / 10;
        int holeDiameter = cellSize - 2 * holeMargin;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int x = boardX + col * cellSize + holeMargin;
                int y = boardY + row * cellSize + holeMargin;
                g2d.setColor(Color.WHITE);
                g2d.fillOval(x, y, holeDiameter, holeDiameter);
                g2d.setColor(new Color(100, 100, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(x, y, holeDiameter, holeDiameter);
            }
        }

        // --- (Optional) Draw a Sample Game Piece ---
        // For demonstration, we draw a crimson disc in the center cell.
        int sampleCol = gridSize / 2;
        int sampleRow = gridSize / 2;
        int sampleX = boardX + sampleCol * cellSize + holeMargin;
        int sampleY = boardY + sampleRow * cellSize + holeMargin;
        g2d.setColor(new Color(220, 20, 60)); // crimson
        g2d.fillOval(sampleX, sampleY, holeDiameter, holeDiameter);
    }
}
