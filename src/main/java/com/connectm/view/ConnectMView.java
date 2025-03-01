package com.connectm.view;

import com.connectm.controller.GameController;
import com.connectm.model.Board;
import com.connectm.model.GameState;
import com.connectm.model.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Displays the Connect M game board graphically, handles user interactions, and animates piece drops.
 */
public class ConnectMView extends JPanel {
    private final int gridSize;
    private final int cellSize;
    private final int headerSize; // Total header size (e.g. 100 = 50px preview + 50px column numbers)
    private GameController controller;
    private final GameState gameState;
    private JLabel statusLabel; // Label for game status messages

    // Colors for the two players
    private final Color player1Color = new Color(220, 20, 60);  // Red for Player 1 (human)
    private final Color player2Color = new Color(255, 215, 0);  // Yellow for Player 2 (AI)
    private final Color boardBlue = new Color(0, 0, 150);       // Board background color
    private final Color highlightColor = new Color(255, 255, 255, 100); // Column highlight

    // Hover state
    private int hoveredColumn = -1;

    // Animation state for the falling piece
    private boolean isAnimating = false;
    private int animColumn = -1;
    private int animTargetRow = -1;
    private double animCurrentRow = -1;
    private final double animStep = 0.3; // Row increment per tick
    private Color fallingPieceColor;

    /**
     * Constructs the game view with the given game state and dimensions.
     *
     * @param gameState  The game state to display
     * @param cellSize   The pixel size of each grid cell
     * @param headerSize The pixel size of the header area above the board
     */
    public ConnectMView(GameState gameState, int cellSize, int headerSize) {
        this.gameState = gameState;
        this.gridSize = gameState.getBoard().getSize();
        this.cellSize = cellSize;
        this.headerSize = headerSize;

        // Set panel dimensions
        int width = gridSize * cellSize;
        int height = headerSize + gridSize * cellSize + 30; // Extra space for status label
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);

        // Set up layout to include status label at the bottom
        setLayout(new BorderLayout());
        statusLabel = new JLabel("Player 1's Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(statusLabel, BorderLayout.SOUTH);

        // Add mouse listener for hover and click
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (isAnimating) return;
                int boardX = 0;
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
                    handleColumnClick(hoveredColumn);
                }
            }
        };
        addMouseMotionListener(mouseHandler);
        addMouseListener(mouseHandler);
    }

    /**
     * Handles a user click on the specified column, initiating a piece drop if valid.
     *
     * @param col The column index clicked (0 to gridSize-1)
     */
    public void handleColumnClick(int col) {
        if (gameState.getBoard().isColumnFull(col)) {
            return;
        }
        Move move = new Move(col);
        animateDrop(move, gameState.getCurrentPlayer());
    }

    /**
     * Sets the game controller for this view to delegate move handling.
     *
     * @param controller The GameController instance
     */
    public void setGameController(GameController controller) {
        this.controller = controller;
    }

    /**
     * Animates the dropping of a piece in the specified column for the given player.
     *
     * @param move   The move containing the column to drop into
     * @param player The player making the move (1 or 2)
     */
    public void animateDrop(Move move, int player) {
        fallingPieceColor = (player == 1) ? player1Color : player2Color;
        Board board = gameState.getBoard();

        int targetRow = -1;
        for (int row = gridSize - 1; row >= 0; row--) {
            if (board.getState()[row][move.column()] == 0) {
                targetRow = row;
                break;
            }
        }
        if (targetRow == -1) return;

        isAnimating = true;
        animColumn = move.column();
        animTargetRow = targetRow;
        animCurrentRow = -1; // Start above the board

        getDropTimer(player).start();
    }

    /**
     * Updates the status message displayed below the board.
     *
     * @param message The message to display
     */
    public void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private Timer getDropTimer(int player) {
        final double gravity = 0.07;   // Acceleration factor (lower = smoother)
        final double maxSpeed = 0.8;   // Maximum fall speed
        final double[] speed = {0.08}; // Initial slow speed

        int animDelay = 25; // Milliseconds between animation updates
        return new Timer(animDelay, e -> {
            if (animCurrentRow < animTargetRow) {
                speed[0] = Math.min(speed[0] + gravity, maxSpeed); // Accelerate smoothly
                animCurrentRow += speed[0]; // Apply updated speed

                if (animCurrentRow >= animTargetRow) { // Smooth landing
                    animCurrentRow = animTargetRow;
                    isAnimating = false;
                    controller.finalizeMove(animColumn, player);
                    animColumn = -1;
                    animTargetRow = -1;
                    animCurrentRow = -1;
                    ((Timer)e.getSource()).stop();
                }
            }
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Define header areas
        int previewAreaHeight = headerSize / 2;      // Top half for preview disk
        int columnHeaderAreaHeight = headerSize / 2; // Bottom half for column numbers
        int boardX = 0;
        int boardY = headerSize;
        int boardWidth = gridSize * cellSize;
        int boardHeight = gridSize * cellSize;

        // Draw column numbers in the header area
        g2d.setColor(Color.DARK_GRAY);
        Font headerFont = new Font("Segoe UI", Font.BOLD, 14);
        g2d.setFont(headerFont);
        for (int col = 0; col < gridSize; col++) {
            String colNum = String.valueOf(col + 1); // Columns are 1 to N
            int textX = boardX + col * cellSize + cellSize / 2 - g2d.getFontMetrics().stringWidth(colNum) / 2;
            int textY = previewAreaHeight + columnHeaderAreaHeight / 2 + g2d.getFontMetrics().getAscent() / 2;
            g2d.drawString(colNum, textX, textY);
        }

        // Draw the preview disc in the top half (if not animating)
        if (hoveredColumn >= 0 && hoveredColumn < gridSize && !isAnimating) {
            int holeMargin = cellSize / 10;
            int holeDiameter = cellSize - 2 * holeMargin;
            int previewDiameter = (int)(holeDiameter * 0.8);
            int previewX = boardX + hoveredColumn * cellSize + (cellSize - previewDiameter) / 2;
            int previewY = (previewAreaHeight - previewDiameter) / 2;
            int currentPlayer = gameState.getCurrentPlayer();

            // Draw a more prominent shadow
            int shadowOffset = 4; // Increased offset for better visibility
            g2d.setColor(new Color(0, 0, 0, 100)); // Increased alpha for more contrast
            g2d.fillOval(previewX + shadowOffset, previewY + shadowOffset, previewDiameter, previewDiameter);

            // Draw the preview disc
            g2d.setColor(currentPlayer == 1 ? player1Color : player2Color);
            g2d.fillOval(previewX, previewY, previewDiameter, previewDiameter);
            g2d.setColor(new Color(100, 100, 100));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(previewX, previewY, previewDiameter, previewDiameter);
        }

        // Draw board background (Connect 4 style)
        g2d.setColor(boardBlue);
        g2d.fillRect(boardX, boardY, boardWidth, boardHeight);

        // Highlight hovered column (if not animating)
        if (hoveredColumn >= 0 && hoveredColumn < gridSize && !isAnimating) {
            int highlightX = boardX + hoveredColumn * cellSize;
            g2d.setColor(highlightColor);
            g2d.fillRect(highlightX, boardY, cellSize, boardHeight);
        }

        // Draw circular cutouts (holes)
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

        // Draw pieces from the board model
        Board boardModel = gameState.getBoard();
        int[][] boardArray = boardModel.getState();
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (boardArray[row][col] != 0) {
                    int x = boardX + col * cellSize + holeMargin;
                    int y = boardY + row * cellSize + holeMargin;
                    g2d.setColor(boardArray[row][col] == 1 ? player1Color : player2Color);
                    g2d.fillOval(x, y, holeDiameter, holeDiameter);
                    g2d.setColor(new Color(100, 100, 100));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(x, y, holeDiameter, holeDiameter);
                }
            }
        }

        // Draw the falling (animated) piece
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

    /**
     * Displays a game-over dialog with the result and an option to play again.
     *
     * @param message The game-over message (e.g., "Player 1 wins!")
     */
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
                controller.resetGame();
            } else {
                System.exit(0);
            }
        });
    }
}