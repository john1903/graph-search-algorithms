package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Field;
import me.jangluzniewicz.graphsearchalgorithms.model.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardFactoryTest {
    private ArrayList<Field> fieldsArray;

    @BeforeEach
    void setUp() {
        fieldsArray = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            fieldsArray.add(new Field(i));
        }
    }

    @Test
    void testGetBoardWithFieldsArray() {
        Board board = BoardFactory.getBoard(fieldsArray);

        assertNotNull(board);
        assertEquals(4, board.getRows());
        assertEquals(4, board.getColumns());

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(fieldsArray.get(i * 4 + j).getValue(), board.getFieldValue(i, j));
            }
        }
    }

    @Test
    void testGetBoardWithRowsColumnsAndFieldsArray() {
        fieldsArray = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            fieldsArray.add(new Field(i));
        }
        int rows = 3;
        int columns = 3;
        Board board = BoardFactory.getBoard(rows, columns, fieldsArray);

        assertNotNull(board);
        assertEquals(rows, board.getRows());
        assertEquals(columns, board.getColumns());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                assertEquals(fieldsArray.get(i * columns + j).getValue(), board.getFieldValue(i, j));
            }
        }
    }

    @Test
    void testGetSolvedBoard() {
        int rows = 3;
        int columns = 3;
        Board solvedBoard = BoardFactory.getSolvedBoard(rows, columns);

        assertNotNull(solvedBoard);
        assertEquals(rows, solvedBoard.getRows());
        assertEquals(columns, solvedBoard.getColumns());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int expectedValue = (i * columns + j + 1) % (rows * columns);
                assertEquals(expectedValue, solvedBoard.getFieldValue(i, j));
            }
        }
    }
    @Test
    void getSolvableBoard() {
        Board board = BoardFactory.getSolvableBoard(4, 4, 10);
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