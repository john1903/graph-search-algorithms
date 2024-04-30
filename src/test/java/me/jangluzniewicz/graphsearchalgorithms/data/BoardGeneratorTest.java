package me.jangluzniewicz.graphsearchalgorithms.data;

import me.jangluzniewicz.graphsearchalgorithms.logic.BoardSolverInterface;
import me.jangluzniewicz.graphsearchalgorithms.logic.SolverBFS;
import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Node;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardGeneratorTest {
    @Test
    void getSolvableBoard() {
        Board board = new BoardGenerator().getSolvableBoard(4, 4, 10);
        assertFalse(board.isBoardSolved());
        BoardSolverInterface boardSolver = new SolverBFS();
        List<Character> solution = boardSolver.solve(new Node(board, null,
                'N', null), "LURD");
        List<Integer> position;
        for (Character c : solution) {
            position = board.getEmptyPosition();
            board.move(position.get(0), position.get(1), c);
        }
        assertTrue(board.isBoardSolved());
    }
}