package com.connectm.controller;

import com.connectm.ai.AIPlayer;
import com.connectm.model.Board;
import com.connectm.model.GameState;
import com.connectm.model.Move;
import com.connectm.view.ConnectMView;

import javax.swing.Timer;

public class GameController {
    private final GameState gameState;
    private final ConnectMView view;
    private final boolean isSinglePlayer;
    private AIPlayer aiPlayer;

    public GameController(GameState gameState, ConnectMView view, boolean isSinglePlayer) {
        this.gameState = gameState;
        this.view = view;
        this.isSinglePlayer = isSinglePlayer;
        view.setGameController(this);
        this.aiPlayer = isSinglePlayer ? new AIPlayer() : null;
    }

    // Called by the view when the user clicks a column.
    public void handleColumnClick(int col) {
        if (gameState.getBoard().isColumnFull(col)) {
            // Could signal an error or ignore.
            return;
        }
        // Create a move (for now, only the column is needed).
        Move move = new Move(col);
        // Tell the view to animate the drop of the current player's piece.
        view.animateDrop(move, gameState.getCurrentPlayer());
    }

    // Called by the view when the falling piece animation completes.
    public void finalizeMove(int col, int row, int player) {
        int rowLanded = gameState.getBoard().dropPiece(col, player);
        if (rowLanded == -1) {
            System.out.println("Move was invalid, skipping turn.");
            return;
        }

        System.out.println("Finalizing move: Player " + player + " at column " + col + " row " + rowLanded);

        // Check for a win
        if (gameState.getBoard().checkWin(player, gameState.getDiscsToWin())) {
            gameState.setGameOver(true);
//            view.showWinningMove(gameState.getBoard().getWinningPositions());
            view.showGameOverDialog("Player " + player + " wins!");
            return;
        }

        // Check for a draw
        if (isBoardFull()) {
            gameState.setGameOver(true);
            view.showGameOverDialog("It's a draw!");
            return;
        }

        // Switch player
        gameState.switchPlayer();
        view.repaint();

        // If it's AI's turn, let it make a move
        if (isSinglePlayer && gameState.getCurrentPlayer() == 2) {
            handleAIMove();
        }
    }

    private boolean isBoardFull() {
        Board board = gameState.getBoard();
        for (int col = 0; col < board.getSize(); col++) {
            if (!board.isColumnFull(col)) {
                return false;
            }
        }
        return true;
    }

    private void handleAIMove() {
        if (gameState.isGameOver() || aiPlayer == null) return; // Ensure AI exists

        Timer aiMoveDelay = new Timer(500, e -> { // Delay for realism
            int aiMove = aiPlayer.getBestMove(gameState.getBoard(), gameState.getDiscsToWin());

            if (aiMove != -1) {
                Move move = new Move(aiMove);
                view.animateDrop(move, gameState.getCurrentPlayer()); // AI drops a piece
            }
        });

        aiMoveDelay.setRepeats(false); // AI should move once per turn
        aiMoveDelay.start();
    }

    public GameState getGameState() {
        return gameState;
    }
}
