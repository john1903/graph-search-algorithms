package me.jangluzniewicz.graphsearchalgorithms.gui;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import me.jangluzniewicz.graphsearchalgorithms.data.BoardGenerator;
import me.jangluzniewicz.graphsearchalgorithms.logic.BoardFactory;
import me.jangluzniewicz.graphsearchalgorithms.model.Board;

public class MainController {
    private BoardWrapper boardWrapper;

    @FXML
    private GridPane gridPane;

    @FXML
    public void initialize() {
        Board board = BoardFactory.getSolvedBoard(4, 4);
        boardWrapper = new BoardWrapper(board);
        bindGridToBoard();
    }

    @FXML
    public void generateBoard() {
        Board board = new BoardGenerator().getSolvableBoard(4, 4, 7);
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                boardWrapper.tileProperty(i, j).set(board.getFieldValue(i, j));
            }
        }
    }

    private void bindGridToBoard() {
        int rows = boardWrapper.getRows();
        int columns = boardWrapper.getColumns();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Text text = new Text();
                text.getStyleClass().add("grid-cell");
                text.textProperty().bind(boardWrapper.tileProperty(row, col).asString());

                StackPane stackPane = new StackPane(text);
                stackPane.getStyleClass().add("grid-cell");

                gridPane.add(stackPane, col, row);
            }
        }
    }
}
