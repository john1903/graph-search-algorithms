package me.jangluzniewicz.graphsearchalgorithms.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a node in the search tree for a graph search algorithm.
 */
public class Node implements Comparable<Node> {
    private final Board state;
    private final Node parent;
    private final char lastMove;
    private final List<Character> path;
    private final int depth;
    private int totalCost;

    /**
     * Constructs a Node with the specified state, parent, last move, and path.
     *
     * @param state    The board state associated with this node.
     * @param parent   The parent node of this node.
     * @param lastMove The last move made to reach this node.
     * @param path     The path of moves from the root to this node.
     */
    public Node(Board state, Node parent, char lastMove, List<Character> path) {
        this.state = state;
        this.parent = parent != null ? parent : this;
        this.lastMove = lastMove;
        this.path = new ArrayList<>(path != null ? path : Collections.emptyList());
        this.depth = parent != null ? parent.getDepth() + 1 : 0;
    }

    /**
     * Generates the children nodes of this node by applying all possible moves.
     *
     * @return A list of child nodes.
     */
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

    /**
     * Gets the board state of this node.
     *
     * @return The board state.
     */
    public Board getState() {
        return state;
    }

    /**
     * Gets the parent node of this node.
     *
     * @return The parent node.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Gets the last move made to reach this node.
     *
     * @return The last move.
     */
    public char getLastMove() {
        return lastMove;
    }

    /**
     * Gets the path of moves from the root node to this node.
     *
     * @return The path of moves.
     */
    public List<Character> getPath() {
        return path;
    }

    /**
     * Gets the depth of this node in the search tree.
     *
     * @return The depth.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Gets the total cost associated with this node.
     *
     * @return The total cost.
     */
    public int getTotalCost() {
        return totalCost;
    }

    /**
     * Sets the total cost associated with this node.
     *
     * @param totalCost The total cost to set.
     */
    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Compares this node with another node based on their total cost.
     *
     * @param o The node to be compared.
     * @return A negative integer, zero, or a positive integer as this node's total cost
     *         is less than, equal to, or greater than the specified node's total cost.
     */
    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.totalCost, o.totalCost);
    }
}