package me.jangluzniewicz.graphsearchalgorithms.data;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;

public interface BoardGeneratorInterface {
    Board getSolvableBoard(int rows, int columns, int depth);
}
