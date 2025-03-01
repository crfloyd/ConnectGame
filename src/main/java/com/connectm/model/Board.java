package com.connectm.model;

import java.util.Arrays;

/**
 * Represents the Connect M game board as a 2D grid, managing piece placement and win conditions.
 */
public class Board {
    private static final int EMPTY_CELL = 0;      // Represents an empty cell
    private static final int[][] WIN_DIRECTIONS = {
            {1, 0},   // Vertical
            {0, 1},   // Horizontal
            {1, 1},   // Diagonal (\)
            {1, -1}   // Diagonal (/)
    };

    private final int size;        // Board size (N x N)
    private final int[][] board;   // Board state: 0 = empty, 1 = player 1, 2 = player 2

    /**
     * Constructs a new board of the specified size.
     *
     * @param size The number of rows and columns (N)
     */
    public Board(int size) {
        this.size = size;
        this.board = new int[size][size];
    }

    /**
     * Returns the size of the board (N).
     *
     * @return The number of rows and columns
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the current state of the board.
     *
     * @return The 2D array representing the board
     */
    public int[][] getState() {
        return board;
    }

    /**
     * Checks if the specified column is full.
     *
     * @param col The column index (0 to N-1)
     * @return true if the column is full, false otherwise
     */
    public boolean isColumnFull(int col) {
        return board[0][col] != EMPTY_CELL; // Top cell is not empty → column is full
    }

    /**
     * Drops a piece in the specified column, simulating gravity.
     *
     * @param col   The column index to drop into (0 to N-1)
     * @param piece The player identifier (1 or 2)
     * @return The row where the piece landed, or -1 if the column is full
     */
    public int dropPiece(int col, int piece) {
        if (isColumnFull(col)) {
            return -1;
        }
        int row = size - 1;
        while (row >= 0 && board[row][col] != EMPTY_CELL) {
            row--;
        }
        if (row >= 0) {
            board[row][col] = piece;
            return row;
        }
        return -1;
    }

    /**
     * Checks if the specified player has won by connecting the required number of discs.
     *
     * @param player     The player to check for (1 or 2)
     * @param discsToWin The number of discs required to win (M)
     * @return true if the player has won, false otherwise
     */
    public boolean checkWin(int player, int discsToWin) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == player) {
                    for (int[] dir : WIN_DIRECTIONS) {
                        int count = countConsecutive(row, col, dir[0], dir[1], player);
                        if (count >= discsToWin) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Counts consecutive pieces for a player in a given direction, including both forward and backward.
     *
     * @param row    The starting row position
     * @param col    The starting column position
     * @param rowDir The row direction to check (e.g., 1 for down, -1 for up)
     * @param colDir The column direction to check (e.g., 1 for right, -1 for left)
     * @param player The player to count for (1 or 2)
     * @return The total number of consecutive pieces in the direction
     */
    private int countConsecutive(int row, int col, int rowDir, int colDir, int player) {
        int count = 1; // Start with the current piece

        // Check forward direction
        int r = row + rowDir;
        int c = col + colDir;
        while (r >= 0 && r < size && c >= 0 && c < size && board[r][c] == player) {
            count++;
            r += rowDir;
            c += colDir;
        }

        // Check backward direction
        r = row - rowDir;
        c = col - colDir;
        while (r >= 0 && r < size && c >= 0 && c < size && board[r][c] == player) {
            count++;
            r -= rowDir;
            c -= colDir;
        }

        return count;
    }

    /**
     * Clears the board by setting all cells to empty.
     */
    public void clearBoard() {
        for (int i = 0; i < size; i++) {
            Arrays.fill(board[i], EMPTY_CELL);
        }
    }

    /**
     * Removes the topmost piece from the specified column.
     *
     * @param col The column index (0 to N-1)
     */
    public void removePiece(int col) {
        for (int row = 0; row < size; row++) {
            if (board[row][col] != EMPTY_CELL) {
                board[row][col] = EMPTY_CELL;
                return;
            }
        }
    }

    /**
     * Prints the current board state to the console for debugging purposes.
     */
    public void printBoard() {
        System.out.println("Current Board State:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}