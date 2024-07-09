package me.jangluzniewicz.graphsearchalgorithms.model;

import me.jangluzniewicz.graphsearchalgorithms.logic.BoardFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;
    ArrayList<Field> fieldsArray;

    @BeforeEach
    void setUp() {
        fieldsArray = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            fieldsArray.add(new Field(random.nextInt(16)));
        }
        fieldsArray.get(random.nextInt(16)).setValue(0);
        board = BoardFactory.getBoard(fieldsArray);
    }

    @Test
    void getFieldValue() {
        ArrayList<Field> fieldsSecondArray = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            fieldsSecondArray.add(new Field(i));
        }
        Board board2 = BoardFactory.getBoard(fieldsSecondArray);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(i * 4 + j, board2.getFieldValue(i, j));
            }
        }
    }

    @Test
    void setFieldValue() {
        for (int i = 0; i < 16; i++) {
            board.setFieldValue(i / 4, i % 4, i);
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(i * 4 + j, board.getFieldValue(i, j));
            }
        }
    }

    @Test
    void testToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            stringBuilder.append(fieldsArray.get(i).getValue());
            if (i % 4 == 3) {
                stringBuilder.append("\n");
            } else {
                stringBuilder.append(" ");
            }
        }
        assertEquals(stringBuilder.toString(), board.toString());
    }

    @Test
    void testEquals() {
        ArrayList<Field> fieldsSecondArray = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            fieldsSecondArray.add(new Field(i));
        }
        Board board3 = BoardFactory.getBoard(fieldsArray);
        assertEquals(board, board3);
        board.setFieldValue(0, 0, 2);
        Board board2 = BoardFactory.getBoard(fieldsSecondArray);
        assertNotEquals(board, board2);
    }

    @Test
    void testGetEmptyPosition() {
        ArrayList<Field> fieldsArray2 = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            fieldsArray2.add(new Field(i));
        }
        fieldsArray2.getFirst().setValue(14);
        fieldsArray2.get(10).setValue(0);
        Board board2 = BoardFactory.getBoard(fieldsArray2);
        assertEquals(2, board2.getEmptyPosition().getFirst());
        assertEquals(2, board2.getEmptyPosition().getLast());
    }

    @Test
    void testGetPossibleMoves() {
        ArrayList<Field> fieldsArray2 = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            fieldsArray2.add(new Field(i));
        }
        fieldsArray2.getFirst().setValue(10);
        fieldsArray2.get(10).setValue(0);
        Board board2 = BoardFactory.getBoard(fieldsArray2);
        assertEquals(4, board2.getPossibleMoves(2,2).size());
    }
}