package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Node;

import java.text.DecimalFormat;
import java.util.*;

/**
 * A* Search algorithm implementation for solving board puzzles.
 */
public class SolverASTR implements BoardSolverInterface {
    private Board solvedBoard;
    private int solutionLength;
    private int visitedStates;
    private int processedStates;
    private int maxRecursionDepth;
    private long computationTime;

    /**
     * Solves the board puzzle using the A* search algorithm.
     *
     * @param root      The root node representing the initial state of the board.
     * @param parameter A parameter to customize the solving process ("MANH" for Manhattan distance,
     *                  "HAMM" for Hamming distance).
     * @return A list of characters representing the sequence of moves to solve the puzzle.
     */
    @Override
    public List<Character> solve(Node root, String parameter) {
        solvedBoard = BoardFactory.getSolvedBoard(root.getState().getRows(), root.getState().getColumns());
        PriorityQueue<Node> openList = new PriorityQueue<>(Node::compareTo);
        long startTime = System.nanoTime();
        openList.add(root);
        visitedStates++;
        try {
            while (!openList.isEmpty()) {
                Node currentNode = openList.poll();
                maxRecursionDepth = Math.max(maxRecursionDepth, currentNode.getDepth());
                processedStates++;
                if (currentNode.getState().isBoardSolved()) {
                    computationTime = System.nanoTime() - startTime;
                    solutionLength = currentNode.getPath().size();
                    return currentNode.getPath();
                }
                List<Node> children = currentNode.getChildren();
                for (Node child : children) {
                    if (!openList.contains(child)) {
                        int error;
                        if (parameter.equals("MANH")) {
                            error = calculateManhattanError(child.getState());
                        } else if (parameter.equals("HAMM")) {
                            error = calculateHammingError(child.getState());
                        } else {
                            throw new IllegalArgumentException("Invalid parameter");
                        }
                        child.setTotalCost(child.getDepth() + error);
                        openList.add(child);
                        visitedStates++;
                    }
                }
            }
        } catch (OutOfMemoryError e) {
            computationTime = System.nanoTime() - startTime;
            solutionLength = -1;
            return Collections.emptyList();
        }
        computationTime = System.nanoTime() - startTime;
        solutionLength = -1;
        return Collections.emptyList();
    }

    /**
     * Calculates the Manhattan error (sum of distances) between the current board state and the solved board state.
     *
     * @param currentBoard The current board state.
     * @return The Manhattan error.
     */
    private int calculateManhattanError(Board currentBoard) {
        int manhError = 0;
        for (int i = 0; i < currentBoard.getRows(); i++) {
            for (int j = 0; j < currentBoard.getColumns(); j++) {
                int value = currentBoard.getFieldValue(i, j);
                if (value != 0) {
                    List<Integer> targetPosition = findPosition(solvedBoard, value);
                    assert targetPosition != null;
                    manhError += Math.abs(i - targetPosition.get(0)) + Math.abs(j - targetPosition.get(1));
                }
            }
        }
        return manhError;
    }

    /**
     * Calculates the Hamming error (number of misplaced tiles) between the current board state and the solved board state.
     *
     * @param currentBoard The current board state.
     * @return The Hamming error.
     */
    private int calculateHammingError(Board currentBoard) {
        int hammError = 0;
        for (int i = 0; i < currentBoard.getRows(); i++) {
            for (int j = 0; j < currentBoard.getColumns(); j++) {
                if (currentBoard.getFieldValue(i, j) != 0) {
                    if (currentBoard.getFieldValue(i, j) != solvedBoard.getFieldValue(i, j)) {
                        hammError++;
                    }
                }
            }
        }
        return hammError;
    }

    /**
     * Finds the position (row and column indices) of a specific value on the board.
     *
     * @param board The board to search.
     * @param value The value to find.
     * @return A list containing the row and column indices of the value, or null if not found.
     */
    private List<Integer> findPosition(Board board, int value) {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                if (board.getFieldValue(i, j) == value) {
                    return new ArrayList<>(List.of(i, j));
                }
            }
        }
        return null;
    }

    /**
     * Retrieves statistics about the solving process.
     *
     * @return A string containing statistics such as solution length, visited states, processed states,
     *         max recursion depth, and computation time.
     */
    @Override
    public String getStats() {
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        return "Solution length: " + solutionLength + "\n" +
                "Visited states: " + visitedStates + "\n" +
                "Processed states: " + processedStates + "\n" +
                "Max recursion depth: " + maxRecursionDepth + "\n" +
                "Computation time (ms): " + decimalFormat.format((double)computationTime / 1_000_000_000.0);
    }
}
