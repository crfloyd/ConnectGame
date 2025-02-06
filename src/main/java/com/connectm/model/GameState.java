package com.connectm.model;

public class GameState {
    private final Board board;
    private final int discsToWin;
    private int currentPlayer;
    private boolean gameOver;

    public GameState(int boardSize, int discsToWin) {
        board = new Board(boardSize);
        this.discsToWin = discsToWin;
        currentPlayer = 1; // 1 for player, 2 for opponent (if you add AI later)
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
}
