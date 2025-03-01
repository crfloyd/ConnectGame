package com.connectm.ai;

import com.connectm.model.Board;

/**
 * Implements the AI opponent for the Connect M game using minimax with alpha-beta pruning.
 */
public class AIPlayer {
    private static final int AI_PLAYER = 2;       // AI player identifier
    private static final int HUMAN_PLAYER = 1;    // Human player identifier
    private static final int MAX_DEPTH = 4;       // Search depth for minimax
    private static final int WIN_SCORE = 1000;    // Score for a winning state
    private static final int NEAR_WIN_SCORE = 50; // Score for M-1 discs in a row
    private static final int PROGRESS_SCORE = 10; // Score for M-2 discs in a row
    private static final int[][] DIRECTIONS = {
            {1, 0},  // Vertical
            {0, 1},  // Horizontal
            {1, 1},  // Diagonal (\)
            {1, -1}  // Diagonal (/)
    };

    private int discsToWin; // Number of discs needed to win (M)

    /**
     * Determines the best column for the AI to drop its piece using minimax with alpha-beta pruning.
     *
     * @param board      The current game board
     * @param discsToWin The number of discs required to win (M)
     * @return The best column index for the AI's move, or -1 if no valid move is found
     */
    public int getBestMove(Board board, int discsToWin) {
        this.discsToWin = discsToWin;
        int bestColumn = -1;
        int bestScore = Integer.MIN_VALUE;

        for (int col = 0; col < board.getSize(); col++) {
            if (!board.isColumnFull(col)) {
                int row = board.dropPiece(col, AI_PLAYER);
                if (row != -1) {
                    int score = minimax(board, discsToWin, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                    board.removePiece(col); // Undo the move
                    if (score > bestScore) {
                        bestScore = score;
                        bestColumn = col;
                    }
                }
            }
        }
        return bestColumn;
    }

    /**
     * Implements the minimax algorithm with alpha-beta pruning to evaluate the best move.
     *
     * @param board         The current game board
     * @param discsToWin    The number of discs required to win (M)
     * @param depth         The remaining depth to search
     * @param alpha         The best score for the maximizer (AI)
     * @param beta          The best score for the minimizer (human)
     * @param isMaximizing  True if maximizing (AI's turn), false if minimizing (human's turn)
     * @return The evaluated score of the board state
     */
    private int minimax(Board board, int discsToWin, int depth, int alpha, int beta, boolean isMaximizing) {
        // Base cases: win, loss, or depth limit reached
        if (board.checkWin(AI_PLAYER, discsToWin)) return WIN_SCORE;
        if (board.checkWin(HUMAN_PLAYER, discsToWin)) return -WIN_SCORE;
        if (depth == 0) return evaluateBoard(board);

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < board.getSize(); col++) {
                if (!board.isColumnFull(col)) {
                    int row = board.dropPiece(col, AI_PLAYER);
                    if (row != -1) {
                        int eval = minimax(board, discsToWin, depth - 1, alpha, beta, false);
                        board.removePiece(col); // Undo the move
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
                        board.removePiece(col); // Undo the move
                        minEval = Math.min(minEval, eval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) break; // Alpha-beta pruning
                    }
                }
            }
            return minEval;
        }
    }

    /**
     * Evaluates the board state by scoring potential winning sequences for both players.
     *
     * @param board The current game board
     * @return The heuristic score (positive for AI advantage, negative for human advantage)
     */
    private int evaluateBoard(Board board) {
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

    /**
     * Scores potential winning sequences for a player starting at a given position.
     *
     * @param board  The current game board
     * @param row    The starting row position
     * @param col    The starting column position
     * @param player The player to evaluate (1 or 2)
     * @return The score for potential sequences (e.g., M-1 or M-2 in a row)
     */
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
            if (count == discsToWin - 1) score += NEAR_WIN_SCORE;  // Near-win (M-1)
            else if (count == discsToWin - 2) score += PROGRESS_SCORE;  // Progress (M-2)
        }
        return score;
    }
}