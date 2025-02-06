package com.connectm.view;

import com.connectm.controller.GameController;
import com.connectm.model.Board;
import com.connectm.model.GameState;
import com.connectm.model.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ConnectMView extends JPanel {
    private final int gridSize;
    private final int cellSize;
    private final int headerSize; // Total header size (e.g. 100 = 50px preview + 50px column numbers)
    private GameController controller;
    private final GameState gameState;

    // Colors for the two players.
    private final Color redColor = new Color(220, 20, 60);
    private final Color yellowColor = new Color(255, 215, 0);

    // Hover state.
    private int hoveredColumn = -1;

    // Animation state for the falling piece.
    private boolean isAnimating = false;
    private int animColumn = -1;
    private int animTargetRow = -1;
    private double animCurrentRow = -1;
    private final int animDelay = 20; // milliseconds between animation updates
    private final double animStep = 0.3; // row increment per tick
    private Color fallingPieceColor;

    private java.util.List<int[]> winningPositions = new ArrayList<int[]>();


    public ConnectMView(GameState gameState, int cellSize, int headerSize) {
        this.gameState = gameState;
        this.gridSize = gameState.getBoard().getSize();
        this.cellSize = cellSize;
        this.headerSize = headerSize;
        int width = headerSize + gridSize * cellSize;
        int height = headerSize + gridSize * cellSize;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);

        // Add mouse listener for hover and click.
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (isAnimating) return;
                int boardX = headerSize/2;
                int boardY = headerSize;
                int boardWidth = gridSize * cellSize;
                int boardHeight = gridSize * cellSize;
                Point p = e.getPoint();
                if (p.x >= boardX && p.x < boardX + boardWidth &&
                        p.y >= boardY && p.y < boardY + boardHeight) {
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
                if (!isAnimating) {
                    hoveredColumn = -1;
                    repaint();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (isAnimating) return;
                if (hoveredColumn >= 0 && hoveredColumn < gridSize && controller != null) {
                    controller.handleColumnClick(hoveredColumn);
                }
            }
        };
        addMouseMotionListener(mouseHandler);
        addMouseListener(mouseHandler);
    }

    public void setGameController(GameController controller) {
        this.controller = controller;
    }

    // Called by the controller to animate the drop of a piece.
    public void animateDrop(Move move, int player) {
        fallingPieceColor = (player == 1) ? this.redColor : this.yellowColor;
        Board board = gameState.getBoard();

        int targetRow = -1;
        for (int row = gridSize - 1; row >= 0; row--) {
            if (board.getBoard()[row][move.getColumn()] == 0) {
                targetRow = row;
                break;
            }
        }
        if (targetRow == -1) return;

        isAnimating = true;
        animColumn = move.getColumn();
        animTargetRow = targetRow;
        animCurrentRow = -1; // Start above the board

        final double gravity = 0.1;   // Acceleration factor (lower = smoother)
        final double maxSpeed = 1.0;  // Maximum fall speed (keep it reasonable)

        final double[] speed = {0.1}; // Initial slow speed

        Timer timer = new Timer(animDelay, e -> {
            if (animCurrentRow < animTargetRow) {
                speed[0] = Math.min(speed[0] + gravity, maxSpeed); // Accelerate smoothly
                animCurrentRow += speed[0]; // Apply updated speed

                if (animCurrentRow >= animTargetRow) { // Smooth landing
                    animCurrentRow = animTargetRow;
                    isAnimating = false;
                    controller.finalizeMove(animColumn, animTargetRow, player);
                    animColumn = -1;
                    animTargetRow = -1;
                    animCurrentRow = -1;
                    ((Timer)e.getSource()).stop();
                }
            }
            repaint();
        });

        timer.start();
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Use Graphics2D for smooth rendering.
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Define header areas.
        int previewAreaHeight = headerSize / 2;      // Top half for preview disk.
        int columnHeaderAreaHeight = headerSize / 2;   // Bottom half for column numbers.
        int boardX = headerSize/2;
        int boardY = headerSize;
        int boardWidth = gridSize * cellSize;
        int boardHeight = gridSize * cellSize;

        // --- Draw Left Header (Row Numbers) ---
        g2d.setColor(Color.DARK_GRAY);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
        g2d.setFont(headerFont);

        // (A) Draw the Preview Disk in the top half (if not animating).
        if (hoveredColumn >= 0 && hoveredColumn < gridSize && !isAnimating) {
            int holeMargin = cellSize / 10;
            int holeDiameter = cellSize - 2 * holeMargin;
            int previewDiameter = (int)(holeDiameter * 0.8);
            int previewX = boardX + hoveredColumn * cellSize + (cellSize - previewDiameter) / 2;
            int previewY = (previewAreaHeight - previewDiameter) / 2;
            int currentPlayer = gameState.getCurrentPlayer();
//            Color previewColor = (currentPlayer == 1) ? new Color(220, 20, 60) : new Color(255, 215, 0);
            if (currentPlayer == 1) {
                g2d.setColor(redColor);
                g2d.fillOval(previewX, previewY, previewDiameter, previewDiameter);
                g2d.setColor(new Color(100, 100, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(previewX, previewY, previewDiameter, previewDiameter);
            }
        }

        // --- Draw Board Background (Connect 4 Style) ---
        Color boardBlue = new Color(0, 0, 150);
        g2d.setColor(boardBlue);
        g2d.fillRect(boardX, boardY, boardWidth, boardHeight);

        // --- Highlight Hovered Column (if not animating) ---
        if (hoveredColumn >= 0 && hoveredColumn < gridSize && !isAnimating) {
            Color highlight = new Color(255, 255, 255, 100);
            int highlightX = boardX + hoveredColumn * cellSize;
            g2d.setColor(highlight);
            g2d.fillRect(highlightX, boardY, cellSize, boardHeight);
        }

        // --- Draw Circular Cutouts (Holes) ---
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

        // --- Draw Pieces from the Board Model ---
        Board boardModel = gameState.getBoard();
        int[][] boardArray = boardModel.getBoard();
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (boardArray[row][col] != 0) {
                    int x = boardX + col * cellSize + holeMargin;
                    int y = boardY + row * cellSize + holeMargin;
                    if (boardArray[row][col] == 1) {
                        g2d.setColor(new Color(220, 20, 60));
                    } else {
                        g2d.setColor(new Color(255, 215, 0));
                    }
                    g2d.fillOval(x, y, holeDiameter, holeDiameter);
                    g2d.setColor(new Color(100, 100, 100));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(x, y, holeDiameter, holeDiameter);
                }
            }
        }

        // --- Draw the Falling (Animated) Piece ---
        if (isAnimating && animColumn >= 0) {
            int holeMarginAnim = cellSize / 10;
            int holeDiameterAnim = cellSize - 2 * holeMarginAnim;
            int pieceX = boardX + animColumn * cellSize + holeMarginAnim;
            int pieceY = boardY + (int)(animCurrentRow * cellSize) + holeMarginAnim;
            g2d.setColor(fallingPieceColor);
            g2d.fillOval(pieceX, pieceY, holeDiameterAnim, holeDiameterAnim);
            g2d.setColor(new Color(100, 100, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(pieceX, pieceY, holeDiameterAnim, holeDiameterAnim);
        }
    }

    public void showGameOverDialog(String message) {
        SwingUtilities.invokeLater(() -> {
            int option = JOptionPane.showOptionDialog(
                    this,
                    message + "\nWould you like to play again?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new Object[]{"Play Again", "Exit"},
                    "Play Again"
            );

            if (option == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        });
    }

    public void showWinningMove(java.util.List<int[]> positions) {
        this.winningPositions = positions;
        repaint(); // Redraw the board to show highlights
    }

    private void resetGame() {
        gameState.getBoard().clearBoard(); // Implement a method in Board.java to reset the grid
        gameState.setGameOver(false);
        gameState.switchPlayer(); // Optionally reset to player 1
        repaint();
    }
}
