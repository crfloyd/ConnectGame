package com.connectm.model;

public class GameState {
    private final Board board;
    private final int discsToWin;
    private int currentPlayer;
    private boolean gameOver;

    public GameState(int boardSize, int discsToWin, int startingPlayer) {
        board = new Board(boardSize);
        this.discsToWin = discsToWin;
        this.currentPlayer = startingPlayer;
        gameOver = false;
    }

    public Board getBoard() {
        return board;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean over) {
        gameOver = over;
    }

    public int getDiscsToWin() {
        return discsToWin;
    }

    public boolean isBoardFull() {
        for (int col = 0; col < board.getSize(); col++) {
            if (!board.isColumnFull(col)) {
                return false;
            }
        }
        return true;
    }
}
