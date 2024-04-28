package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Field;

import java.util.ArrayList;

public class BoardFactory {
    public Board getBoard(ArrayList<Field> fieldsArray) {
        return new Board(fieldsArray, new BoardController(), new BoardValidator());
    }

    public Board getBoard(int rows, int columns, ArrayList<Field> fieldsArray) {
        return new Board(rows, columns, fieldsArray, new BoardController(), new BoardValidator());
    }

    public static Board getSolvedBoard(int rows, int columns) {
        ArrayList<Field> fields = new ArrayList<>();
        for (int i = 1; i < rows * columns; i++) {
            fields.add(new Field(i));
        }
        fields.add(new Field(0));
        return new Board(rows, columns, fields, new BoardController(), new BoardValidator());
    }
}
