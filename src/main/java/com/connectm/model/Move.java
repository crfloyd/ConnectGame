package com.connectm.model;

/**
 * A record representing a player's move in the Connect M game, specifying the column to drop a piece into.
 *
 * @param column The column index where the piece will be dropped (0 to N-1)
 */
public record Move(int column) {
}