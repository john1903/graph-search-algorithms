package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Node;

import java.util.List;

/**
 * Interface for solving a board puzzle using graph search algorithms.
 */
public interface BoardSolverInterface {

    /**
     * Solves the puzzle starting from the given root node.
     *
     * @param root      The root node representing the initial state of the board.
     * @param parameter An additional parameter to customize the solving process.
     * @return A list of characters representing the sequence of moves to solve the puzzle.
     */
    List<Character> solve(Node root, String parameter);

    /**
     * Gets statistics about the solving process.
     *
     * @return A string containing statistics about the solving process.
     */
    String getStats();
}
