package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;

public interface BoardControllerInterface {
    boolean moveTile(Board board, int row, int column, char direction);
}
