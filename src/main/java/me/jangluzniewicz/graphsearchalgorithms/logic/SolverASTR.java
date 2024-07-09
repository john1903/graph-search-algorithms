package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Node;

import java.text.DecimalFormat;
import java.util.*;

public class SolverASTR implements BoardSolverInterface {
    private Board solvedBoard;
    private int solutionLength;
    private int visitedStates;
    private int processedStates;
    private int maxRecursionDepth;
    private long computationTime;

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
                    if (!openList.contains(child)){
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

    private int calculateManhattanError(Board currentBoard) {
        int manhError = 0;
        for (int i = 0; i < currentBoard.getRows(); i++) {
            for (int j = 0; j < currentBoard.getColumns(); j++) {
                int value = currentBoard.getFieldValue(i, j);
                if (value != 0) {
                    List<Integer> targetPosition = findPosition(solvedBoard, value);
                    assert targetPosition != null;
                    manhError += Math.abs(i - targetPosition.getFirst()) + Math.abs(j - targetPosition.getLast());
                }
            }
        }
        return manhError;
    }

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

    @Override
    public String getStats() {
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        return "Solution length: " + solutionLength + "\n" + "Visited states: " + visitedStates + "\n"
                + "Processed states: " + processedStates + "\n" + "Max recursion depth: " + maxRecursionDepth + "\n"
                + "Computation time (ms): " + decimalFormat.format((double)computationTime / 1_000_000_000.0);
    }
}
