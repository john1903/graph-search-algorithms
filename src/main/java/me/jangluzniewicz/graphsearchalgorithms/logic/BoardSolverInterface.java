package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Node;

import java.util.List;

public interface BoardSolverInterface {
    List<Character> solve(Node root, String parameter);

    String getStats();
}
