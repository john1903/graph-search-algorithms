package me.jangluzniewicz.graphsearchalgorithms.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node implements Comparable<Node> {
    private final Board state;
    private final Node parent;
    private final char lastMove;
    private final List<Character> path;
    private final int depth;
    private int totalCost;

    public Node(Board state, Node parent, char lastMove, List<Character> path) {
        this.state = state;
        this.parent = parent != null ? parent : this;
        this.lastMove = lastMove;
        this.path = new ArrayList<>(path != null ? path : Collections.emptyList());
        this.depth = parent != null ? parent.getDepth() + 1 : 0;
    }

    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        List<Integer> emptyFieldPosition = state.getEmptyPosition();
        for (Character move : state.getPossibleMoves(emptyFieldPosition.get(0), emptyFieldPosition.get(1))) {
            Board newState = (Board) state.clone();
            newState.move(emptyFieldPosition.get(0), emptyFieldPosition.get(1), move);
            Node newNode = new Node(newState, this, move, path);
            newNode.path.add(move);
            children.add(newNode);
        }
        return children;
    }

    public Board getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public char getLastMove() {
        return lastMove;
    }

    public List<Character> getPath() {
        return path;
    }

    public int getDepth() {
        return depth;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.totalCost, o.totalCost);
    }
}
