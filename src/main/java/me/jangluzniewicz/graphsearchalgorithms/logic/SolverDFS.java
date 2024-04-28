package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Node;

import java.text.DecimalFormat;
import java.util.*;

public class SolverDFS implements BoardSolverInterface {
    private static final int MAX_DEPTH = 30;
    private int solutionLength;
    private int visitedStates;
    private int processedStates;
    private int maxRecursionDepth;
    private long computationTime;

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
                long MAX_COMPUTATION_TIME = 90000000000L;
                maxRecursionDepth = Math.max(maxRecursionDepth, currentNode.getDepth());
                if (System.nanoTime() - startTime > MAX_COMPUTATION_TIME) {
                    break;
                }
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

    private void sortChildren(List<Node> children, String moveOrder) {
        children.sort((node1, node2) -> {
            int index1 = moveOrder.indexOf(node1.getLastMove());
            int index2 = moveOrder.indexOf(node2.getLastMove());
            return Integer.compare(index1, index2);
        });
        Collections.reverse(children);
    }

    public String getStats() {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        return solutionLength + "\n" + visitedStates + "\n"
                + processedStates + "\n" + maxRecursionDepth + "\n" +
                decimalFormat.format((double)computationTime / 1_000_000_000.0);
    }
}
