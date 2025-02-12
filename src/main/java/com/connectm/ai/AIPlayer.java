package com.connectm.ai;

import com.connectm.model.Board;

public class AIPlayer {
    private final int AI_PLAYER = 2;  // AI is player 2
    private final int HUMAN_PLAYER = 1;
    private final int DEPTH = 4; // Controls difficulty (higher = smarter)
    private final int[][] DIRECTIONS = {{1, 0}, {0, 1}, {1, 1}, {1, -1}}; // Vertical, Horizontal, Diagonal (\), Diagonal (/)
    private int discsToWin;

    public int getBestMove(Board board, int discsToWin) {
        this.discsToWin = discsToWin;
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;

        for (int col = 0; col < board.getSize(); col++) {
            if (!board.isColumnFull(col)) {
                // Simulate dropping a piece in this column
                int row = board.dropPiece(col, AI_PLAYER);
                if (row != -1) {
                    int score = minimax(board, discsToWin, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                    board.removePiece(col); // Undo move

                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = col;
                    }
                }
            }
        }
        return bestMove; // Return the column with the best score
    }

    private int minimax(Board board, int discsToWin, int depth, int alpha, int beta, boolean isMaximizing) {
        // Base cases: Check for a win, loss, or depth limit
        if (board.checkWin(AI_PLAYER, discsToWin)) return 1000;
        if (board.checkWin(HUMAN_PLAYER, discsToWin)) return -1000;
        if (depth == 0) return evaluateBoard(board);

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < board.getSize(); col++) {
                if (!board.isColumnFull(col)) {
                    int row = board.dropPiece(col, AI_PLAYER);
                    if (row != -1) {
                        int eval = minimax(board, discsToWin, depth - 1, alpha, beta, false);
                        board.removePiece(col); // Undo move
                        maxEval = Math.max(maxEval, eval);
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha) break; // Alpha-beta pruning
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < board.getSize(); col++) {
                if (!board.isColumnFull(col)) {
                    int row = board.dropPiece(col, HUMAN_PLAYER);
                    if (row != -1) {
                        int eval = minimax(board, discsToWin, depth - 1, alpha, beta, true);
                        board.removePiece(col); // Undo move
                        minEval = Math.min(minEval, eval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) break; // Alpha-beta pruning
                    }
                }
            }
            return minEval;
        }
    }

    private int evaluateBoard(Board board) {
        // Basic evaluation: Count (discsToWin-1)-in-a-row for AI (+50), for human (-50)
        int score = 0;

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                if (board.getState()[row][col] == AI_PLAYER) {
                    score += checkPotential(board, row, col, AI_PLAYER);
                } else if (board.getState()[row][col] == HUMAN_PLAYER) {
                    score -= checkPotential(board, row, col, HUMAN_PLAYER);
                }
            }
        }

        return score;
    }

    private int checkPotential(Board board, int row, int col, int player) {
        int score = 0;

        for (int[] dir : DIRECTIONS) {
            int count = 1;
            int r = row + dir[0], c = col + dir[1];

            while (r >= 0 && r < board.getSize() && c >= 0 && c < board.getSize() &&
                    board.getState()[r][c] == player) {
                count++;
                r += dir[0];
                c += dir[1];
            }

            if (count == discsToWin-1) score += 50; // Give high value for near-wins
        }

        return score;
    }
}
