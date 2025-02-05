package com.connectm.controller;

import com.connectm.model.Board;
import com.connectm.model.GameState;
import com.connectm.model.Move;
import com.connectm.view.ConnectMView;

public class GameController {
    private final GameState gameState;
    private final ConnectMView view;

    public GameController(GameState gameState, ConnectMView view) {
        this.gameState = gameState;
        this.view = view;
        view.setGameController(this);
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
        System.out.println("Player " + player + " dropped a piece in column " + col + " at row " + rowLanded);

        // Check if the game is won
        if (gameState.getBoard().checkWin(player)) {
            gameState.setGameOver(true);
            view.showGameOverDialog("Player " + player + " wins!");
            return;
        }

        // Check for a draw (board full)
        if (isBoardFull()) {
            gameState.setGameOver(true);
            view.showGameOverDialog("It's a draw!");
            return;
        }

        // Switch players if no win/draw
        gameState.switchPlayer();
        view.repaint();
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

    public GameState getGameState() {
        return gameState;
    }
}
