package com.connectm.model;

/**
 * Tracks the state of the Connect M game, including the board, current player, and win conditions.
 */
public class GameState {
    private static final int PLAYER_1 = 1; // Human player
    private static final int PLAYER_2 = 2; // AI player

    private final Board board;          // Game board
    private final int discsToWin;      // Number of discs needed to win (M)
    private int currentPlayer;         // Current player (1 or 2)
    private boolean gameOver;          // Whether the game has ended

    /**
     * Constructs a new game state with the specified parameters.
     *
     * @param boardSize      The size of the board (N x N)
     * @param discsToWin     The number of discs required to win (M)
     * @param startingPlayer The player who starts (0 for AI, 1 for human)
     */
    public GameState(int boardSize, int discsToWin, int startingPlayer) {
        this.board = new Board(boardSize);
        this.discsToWin = discsToWin;
        this.currentPlayer = startingPlayer == 0 ? PLAYER_2 : PLAYER_1;
        this.gameOver = false;
    }

    /**
     * Returns the game board.
     *
     * @return The Board instance
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the current player.
     *
     * @return The current player (1 for human, 2 for AI)
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Switches the current player (from 1 to 2 or vice versa).
     */
    public void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_1) ? PLAYER_2 : PLAYER_1;
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the game-over status.
     *
     * @param over true to mark the game as over, false otherwise
     */
    public void setGameOver(boolean over) {
        this.gameOver = over;
    }

    /**
     * Returns the number of discs required to win.
     *
     * @return The number of discs (M)
     */
    public int getDiscsToWin() {
        return discsToWin;
    }

    /**
     * Checks if the board is completely full (i.e., a draw condition).
     *
     * @return true if the board is full, false otherwise
     */
    public boolean isBoardFull() {
        for (int col = 0; col < board.getSize(); col++) {
            if (!board.isColumnFull(col)) {
                return false;
            }
        }
        return true;
    }
}