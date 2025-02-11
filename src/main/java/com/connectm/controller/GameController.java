package com.connectm.controller;

import com.connectm.ai.AIPlayer;
import com.connectm.model.GameState;
import com.connectm.model.Move;
import com.connectm.view.ConnectMView;

import javax.swing.Timer;

public class GameController {
    private final GameState gameState;
    private final ConnectMView view;
    private final AIPlayer aiPlayer;

    public GameController(GameState gameState, ConnectMView view) {
        this.gameState = gameState;
        this.view = view;
        view.setGameController(this);
        this.aiPlayer = new AIPlayer();
    }



    // Called by the view when the falling piece animation completes.
    public void finalizeMove(int col, int player) {
        int rowLanded = gameState.getBoard().dropPiece(col, player);
        if (rowLanded == -1) {
            System.out.println("Move was invalid, skipping turn.");
            return;
        }

        System.out.println("Finalizing move: Player " + player + " at column " + col + " row " + rowLanded);

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

        // Switch player
        gameState.switchPlayer();
        view.repaint();

        // If it's AI's turn, let it make a move
        if (gameState.getCurrentPlayer() == 2) {
            handleAIMove();
        }
    }

    private void handleAIMove() {
        if (gameState.isGameOver() || aiPlayer == null) return; // Ensure AI exists

        Timer aiMoveDelay = new Timer(5, e -> { // Delay for realism
            int aiMove = aiPlayer.getBestMove(gameState.getBoard(), gameState.getDiscsToWin());

            if (aiMove != -1) {
                Move move = new Move(aiMove);
                view.animateDrop(move, gameState.getCurrentPlayer()); // AI drops a piece
            }
        });

        aiMoveDelay.setRepeats(false); // AI should move once per turn
        aiMoveDelay.start();
    }
}
