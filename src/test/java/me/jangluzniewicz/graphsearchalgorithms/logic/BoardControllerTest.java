package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardControllerTest {
    Board board;

    @BeforeEach
    void setUp() {
        ArrayList<Field> fieldsArray = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            fieldsArray.add(new Field(i));
        }
        fieldsArray.add(new Field(0));
        board = BoardFactory.getBoard(fieldsArray);
    }

    @Test
    void moveTile() {
        assertTrue(board.move(3, 3, 'U'));
        assertEquals(0, board.getFieldValue(2, 3));
        assertEquals(12, board.getFieldValue(3, 3));
        assertTrue(board.move(2, 3, 'L'));
        assertEquals(0, board.getFieldValue(2, 2));
        assertEquals(11, board.getFieldValue(2, 3));
        assertTrue(board.move(2, 2, 'U'));
        assertEquals(0, board.getFieldValue(1, 2));
        assertEquals(7, board.getFieldValue(2, 2));
        assertTrue(board.move(1, 2, 'R'));
        assertEquals(0, board.getFieldValue(1, 3));
        assertEquals(8, board.getFieldValue(1, 2));
        assertTrue(board.move(1, 3, 'D'));
        assertEquals(0, board.getFieldValue(2, 3));
        assertEquals(11, board.getFieldValue(1, 3));
    }

    @Test
    void moveTileInvalidIndex() {
        assertThrows(IllegalArgumentException.class, () -> board.move(4, 4, 'U'));
    }

    @Test
    void moveTileInvalidDirection() {
        assertThrows(IllegalArgumentException.class, () -> board.move(3, 3, 'X'));
    }

    @Test
    void canNotMoveTile() {
        assertFalse(board.move(3, 3, 'D'));
        assertFalse(board.move(3, 3, 'R'));
        assertFalse(board.move(0, 0, 'U'));
        assertFalse(board.move(0, 0, 'L'));
        assertFalse(board.move(0, 3, 'U'));
        assertFalse(board.move(2, 2, 'L'));
    }
}