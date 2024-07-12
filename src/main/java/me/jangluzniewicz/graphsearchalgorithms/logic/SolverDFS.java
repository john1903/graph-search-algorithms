package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Node;

import java.text.DecimalFormat;
import java.util.*;

/**
 * SolverDFS implements the Depth-First Search (DFS) algorithm with depth limit to solve board puzzles.
 */
public class SolverDFS implements BoardSolverInterface {
    private static final int MAX_DEPTH = 30;
    private int solutionLength;
    private int visitedStates;
    private int processedStates;
    private int maxRecursionDepth;
    private long computationTime;

    /**
     * Solves the board puzzle using the Depth-First Search (DFS) algorithm with a depth limit.
     *
     * @param root      The root node representing the initial state of the board.
     * @param parameter A parameter that could influence the solving process.
     * @return A list of characters representing the sequence of moves to solve the puzzle.
     */
    @Override
    public List<Character> solve(Node root, String parameter) {
        Deque<Node> stack = new ArrayDeque<>();
        Set<Board> visited = new HashSet<>();
        long startTime = System.nanoTime();
        stack.push(root);
        visited.add(root.getState());
        visitedStates++;
        try {
            while (!stack.isEmpty()) {
                Node currentNode = stack.pop();
                Board currentBoard = currentNode.getState();
                maxRecursionDepth = Math.max(maxRecursionDepth, currentNode.getDepth());
                if (currentBoard.isBoardSolved()) {
                    computationTime = System.nanoTime() - startTime;
                    solutionLength = currentNode.getPath().size();
                    return currentNode.getPath();
                }
                if (currentNode.getDepth() <= MAX_DEPTH) {
                    processedStates++;
                    List<Node> children = currentNode.getChildren();
                    sortChildren(children, parameter);
                    for (Node child : children) {
                        if (!visited.contains(child.getState()) && child.getDepth() <= MAX_DEPTH) {
                            visited.add(child.getState());
                            visitedStates++;
                            stack.push(child);
                        }
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
        Collections.reverse(children);
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

