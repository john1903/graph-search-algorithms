package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;

/**
 * Interface for validating moves and board states in a board puzzle.
 */
public interface BoardValidatorInterface {

    /**
     * Checks if a move is valid for the given board state.
     *
     * @param board    The board state.
     * @param row      The row index of the move.
     * @param column   The column index of the move.
     * @param direction The direction of the move ('U', 'D', 'L', 'R').
     * @return True if the move is valid, false otherwise.
     */
    boolean isMoveValid(Board board, int row, int column, char direction);

    /**
     * Checks if the board is in a solved state.
     *
     * @param board The board state.
     * @return True if the board is solved, false otherwise.
     */
    boolean isBoardSolved(Board board);
}
