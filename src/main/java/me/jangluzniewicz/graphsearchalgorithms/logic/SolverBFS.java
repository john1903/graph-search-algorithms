package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Node;

import java.text.DecimalFormat;
import java.util.*;

public class SolverBFS implements BoardSolverInterface {
    private int solutionLength;
    private int visitedStates;
    private int processedStates;
    private int maxRecursionDepth;
    private long computationTime;

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
                processedStates++;
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

    private void sortChildren(List<Node> children, String moveOrder) {
        children.sort((node1, node2) -> {
            int index1 = moveOrder.indexOf(node1.getLastMove());
            int index2 = moveOrder.indexOf(node2.getLastMove());
            return Integer.compare(index1, index2);
        });
    }

    public String getStats() {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        return solutionLength + "\n" + visitedStates + "\n"
                + processedStates + "\n" + maxRecursionDepth + "\n" +
                decimalFormat.format((double)computationTime / 1_000_000_000.0);
    }
}
