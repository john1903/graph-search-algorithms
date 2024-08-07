package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Node;

import java.text.DecimalFormat;
import java.util.*;

/**
 * SolverBFS implements the Breadth-First Search (BFS) algorithm to solve board puzzles.
 */
public class SolverBFS implements BoardSolverInterface {
    private int solutionLength;
    private int visitedStates;
    private int processedStates;
    private int maxRecursionDepth;
    private long computationTime;

    /**
     * Solves the board puzzle using the Breadth-First Search (BFS) algorithm.
     *
     * @param root      The root node representing the initial state of the board.
     * @param parameter A parameter that could influence the solving process.
     * @return A list of characters representing the sequence of moves to solve the puzzle.
     */
    @Override
    public List<Character> solve(Node root, String parameter) {
        Queue<Node> queue = new LinkedList<>();
        Set<Board> visited = new HashSet<>();
        long startTime = System.nanoTime();
        queue.add(root);
        visited.add(root.getState());
        visitedStates++;
        try {
            while (!queue.isEmpty()) {
                Node currentNode = queue.poll();
                Board currentBoard = currentNode.getState();
                maxRecursionDepth = Math.max(maxRecursionDepth, currentNode.getDepth());
                processedStates++;
                if (currentBoard.isBoardSolved()) {
                    computationTime = System.nanoTime() - startTime;
                    solutionLength = currentNode.getPath().size();
                    return currentNode.getPath();
                }
                List<Node> children = currentNode.getChildren();
                sortChildren(children, parameter);
                for (Node child : children) {
                    if (!visited.contains(child.getState())) {
                        visited.add(child.getState());
                        visitedStates++;
                        queue.add(child);
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
     * Sorts the list of child nodes based on a specific order.
     *
     * @param children   The list of child nodes to be sorted.
     * @param moveOrder  The order in which to sort the child nodes.
     */
    private void sortChildren(List<Node> children, String moveOrder) {
        children.sort((node1, node2) -> {
            int index1 = moveOrder.indexOf(node1.getLastMove());
            int index2 = moveOrder.indexOf(node2.getLastMove());
            return Integer.compare(index1, index2);
        });
    }

    /**
     * Retrieves statistics about the solving process.
     *
     * @return A string containing statistics such as solution length, visited states, processed states,
     *         max recursion depth, and computation time.
     */
    public String getStats() {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        return "Solution length: " + solutionLength + "\n" +
                "Visited states: " + visitedStates + "\n" +
                "Processed states: " + processedStates + "\n" +
                "Max recursion depth: " + maxRecursionDepth + "\n" +
                "Computation time (ms): " + decimalFormat.format((double) computationTime / 1_000_000_000.0);
    }
}