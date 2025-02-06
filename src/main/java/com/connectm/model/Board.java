package com.connectm.model;

import java.util.Arrays;

public class Board {
    private static final int M = 4; // Number of pieces in a row to win
    private final int size;
    private final int[][] board; // 0 = empty, 1 = player piece, 2 = opponent (if needed)

    public Board(int size) {
        this.size = size;
        board = new int[size][size];
    }

    public int getSize() {
        return size;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean isColumnFull(int col) {
        return board[0][col] != 0; // top cell is not empty → column full
    }

    // Drop a piece in the given column. Returns the row at which the piece lands, or -1 if full.
    public int dropPiece(int col, int piece) {
        if (isColumnFull(col)) {
            System.out.println("Column " + col + " is full, cannot drop piece.");
            return -1;
        }
        int row = size - 1;
        while (row >= 0 && board[row][col] != 0) {
            row--;
        }
        if (row >= 0) {
            board[row][col] = piece;
//            System.out.println("Dropped piece for player " + piece + " at row " + row + ", column " + col);
//            printBoard(); // Debugging
            return row;
        }
        return -1;
    }

    // Debugging method to print the board state
    private void printBoard() {
        System.out.println("Current Board State:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }


    public boolean checkWin(int player) {
        int[][] directions = {
                {1, 0}, {0, 1}, {1, 1}, {1, -1} // Vertical, Horizontal, Diagonal (\), Diagonal (/)
        };

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == player) {
                    for (int[] dir : directions) {
                        int count = countConsecutive(row, col, dir[0], dir[1], player);
                        if (count >= M) { // Always check for 4 in a row
                            System.out.println("Win detected at row " + row + ", column " + col + " for Player " + player);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // Updated to check in both directions
    private int countConsecutive(int row, int col, int rowDir, int colDir, int player) {
        int count = 1;  // Start counting the current piece

        // Check forward direction
        int r = row + rowDir, c = col + colDir;
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


    public void clearBoard() {
        for (int i = 0; i < size; i++) {
            Arrays.fill(board[i], 0);
        }
    }

    public void removePiece(int col) {
        for (int row = 0; row < size; row++) {
            if (board[row][col] != 0) {
                board[row][col] = 0; // Reset the cell
                return;
            }
        }
    }
}
