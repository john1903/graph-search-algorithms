package me.jangluzniewicz.graphsearchalgorithms.logic;

import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardFactory {
    public static Board getBoard(ArrayList<Field> fieldsArray) {
        return new Board(fieldsArray, new BoardController(), new BoardValidator());
    }

    public static Board getBoard(int rows, int columns, ArrayList<Field> fieldsArray) {
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

    public static Board getSolvableBoard(int rows, int columns, int depth) {
        ArrayList<Field> fields = new ArrayList<>();
        for (int i = 1; i < rows * columns; i++) {
            fields.add(new Field(i));
        }
        fields.add(new Field(0));
        Board board = getBoard(rows, columns, fields);
        Random random = new Random();
        List<Integer> emptyPosition;
        List<Character> possibleMoves;
        char lastMove = 'N';
        for (int i = 0; i < depth; i++) {
            emptyPosition = board.getEmptyPosition();
            possibleMoves = board.getPossibleMoves(emptyPosition.get(0), emptyPosition.get(1));
            if (!possibleMoves.isEmpty()) {
                boolean canMove;
                Character move;
                do {
                    move = possibleMoves.get(random.nextInt(0, emptyPosition.size()));
                    canMove = switch (move) {
                        case 'U' -> lastMove != 'D';
                        case 'D' -> lastMove != 'U';
                        case 'L' -> lastMove != 'R';
                        case 'R' -> lastMove != 'L';
                        default -> true;
                    };
                } while (!canMove);
                board.move(emptyPosition.get(0), emptyPosition.get(1), move);
                lastMove = move;
            }
        }
        return board;
    }
}
