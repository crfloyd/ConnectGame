package com.connectm.controller;

import com.connectm.ai.AIPlayer;
import com.connectm.model.GameState;
import com.connectm.model.Move;
import com.connectm.view.ConnectMView;

import javax.swing.Timer;

/**
 * Manages the game flow, coordinating between the model (GameState) and view (ConnectMView).
 * Handles user and AI moves, checks for win/draw conditions, and updates the UI.
 */
public class GameController {
    private static final int AI_MOVE_DELAY_MS = 5; // Delay in milliseconds for AI moves

    private final GameState gameState;
    private final ConnectMView view;
    private final AIPlayer aiPlayer;

    /**
     * Constructs the controller with the given game state and view.
     *
     * @param gameState The game state to manage
     * @param view      The view to update
     */
    public GameController(GameState gameState, ConnectMView view) {
        this.gameState = gameState;
        this.view = view;
        this.aiPlayer = new AIPlayer();
        view.setGameController(this);
        updateStatus(); // Set initial status
    }

    /**
     * Finalizes a move after the animation completes, checks for win/draw, and switches players.
     *
     * @param column The column where the piece was dropped (0 to N-1)
     * @param player The player who made the move (1 for human, 2 for AI)
     */
    public void finalizeMove(int column, int player) {
        int rowLanded = gameState.getBoard().dropPiece(column, player);
        if (rowLanded == -1) {
            view.updateStatus("Invalid move: Column " + (column + 1) + " is full.");
            return;
        }

        // Check for a win
        if (gameState.getBoard().checkWin(player, gameState.getDiscsToWin())) {
            gameState.setGameOver(true);
            view.showGameOverDialog("Player " + player + " wins!");
            return;
        }

        // Check for a draw
        if (gameState.isBoardFull()) {
            gameState.setGameOver(true);
            view.showGameOverDialog("It's a draw!");
            return;
        }

        // Switch player and update the view
        gameState.switchPlayer();
        updateStatus();
        view.repaint();

        // If it's the AI's turn, let it make a move
        if (gameState.getCurrentPlayer() == 2) {
            handleAIMove();
        }
    }

    /**
     * Initiates an AI move by computing the best column and animating the drop.
     */
    private void handleAIMove() {
        if (gameState.isGameOver() || aiPlayer == null) return;

        view.updateStatus("AI is thinking...");
        Timer aiMoveTimer = new Timer(AI_MOVE_DELAY_MS, e -> {
            int aiColumn = aiPlayer.getBestMove(gameState.getBoard(), gameState.getDiscsToWin());
            if (aiColumn != -1) {
                Move move = new Move(aiColumn);
                view.animateDrop(move, gameState.getCurrentPlayer());
            } else {
                view.updateStatus("AI cannot make a move.");
            }
        });
        aiMoveTimer.setRepeats(false);
        aiMoveTimer.start();
    }

    /**
     * Updates the view's status message based on the current player.
     */
    private void updateStatus() {
        String playerName = gameState.getCurrentPlayer() == 1 ? "Player 1" : "AI (Player 2)";
        view.updateStatus(playerName + "'s Turn");
    }
}