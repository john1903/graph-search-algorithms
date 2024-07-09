package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Field;
import me.jangluzniewicz.graphsearchalgorithms.model.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolverDFSTest {
    Board board;
    Board board3_4;

    @BeforeEach
    void setUp() {
        ArrayList<Field> fieldsArray = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            fieldsArray.add(new Field(i));
        }
        fieldsArray.add(new Field(0));
        ArrayList<Field> fieldsArray3_4 = new ArrayList<>();
        for (int i = 1; i < 12; i++) {
            fieldsArray3_4.add(new Field(i));
        }
        fieldsArray3_4.add(new Field(0));
        board = BoardFactory.getBoard(fieldsArray);
        board3_4 = BoardFactory.getBoard(3, 4, fieldsArray3_4);
    }

    @Test
    void testSolve() {
        SolverDFS solverDFS = new SolverDFS();
        assertTrue(board.move(3, 3, 'L'));
        board.move(3, 2, 'L');
        board.move(3, 1, 'L');
        board.move(3, 0, 'U');
        board.move(2, 0, 'U');
        board.move(1, 0, 'U');
        board.move(0, 0, 'R');
        board.move(0, 1, 'R');
        assertTrue(board.move(0, 2, 'R'));
        Node root = new Node(board, null, 'N', null);
        List<Character> result = solverDFS.solve(root, "LDRU");
        assertFalse(result.isEmpty());
        List<Integer> position;
        for (Character c : result) {
            position  = board.getEmptyPosition();
            board.move(position.get(0), position.get(1), c);
        }
        assertTrue(board.isBoardSolved());
    }

    @Test
    void testSolve3_4() {
        SolverDFS solverDFS = new SolverDFS();
        assertTrue(board3_4.move(2, 3, 'L'));
        board3_4.move(2, 2, 'L');
        board3_4.move(2, 1, 'L');
        board3_4.move(2, 0, 'U');
        board3_4.move(1, 0, 'U');
        board3_4.move(0, 0, 'R');
        board3_4.move(0, 1, 'R');
        assertTrue(board3_4.move(0, 2, 'R'));
        Node root = new Node(board3_4, null, 'N', null);
        List<Character> result = solverDFS.solve(root, "RDUL");
        assertFalse(result.isEmpty());
        List<Integer> position;
        for (Character c : result) {
            position  = board3_4.getEmptyPosition();
            board3_4.move(position.get(0), position.get(1), c);
        }
        assertTrue(board3_4.isBoardSolved());
    }
}