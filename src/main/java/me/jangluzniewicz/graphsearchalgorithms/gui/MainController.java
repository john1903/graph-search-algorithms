package me.jangluzniewicz.graphsearchalgorithms.gui;

import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import me.jangluzniewicz.graphsearchalgorithms.data.BoardGenerator;
import me.jangluzniewicz.graphsearchalgorithms.logic.*;
import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Node;

import java.util.List;

public class MainController {
    private BoardWrapper boardWrapper;
    @FXML
    private GridPane gridPane;
    @FXML
    private ComboBox<String> algorithmComboBox;
    @FXML
    private ComboBox<String> heuristicComboBox;
    @FXML
    private Button playButton;
    @FXML
    private Button generateButton;

    @FXML
    public void initialize() {
        Board board = BoardFactory.getSolvedBoard(4, 4);
        boardWrapper = new BoardWrapper(board);
        bindGridToBoard();
        algorithmComboBox.getItems().addAll("BFS", "DFS", "A-star");

        playButton.disableProperty().bind(
                algorithmComboBox.valueProperty().isNull()
                        .or(heuristicComboBox.valueProperty().isNull())
        );

        heuristicComboBox.setDisable(true);
        algorithmComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("BFS") || newValue.equals("DFS")) {
                    heuristicComboBox.getItems().setAll("RDLU", "DRUL", "DRLU", "LUDR", "LURD", "ULDR", "ULRD");
                } else if (newValue.equals("A-star")) {
                    heuristicComboBox.getItems().setAll("MANH", "HAMM");
                }
                heuristicComboBox.setDisable(false);
            } else {
                heuristicComboBox.getItems().clear();
                heuristicComboBox.setDisable(true);
            }
        });
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

    @FXML
    public void solveBoard() {
        String selectedAlgorithm = algorithmComboBox.getSelectionModel().getSelectedItem();
        List<Character> result = getResult(selectedAlgorithm);

        if (result == null || result.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Solution Not Found");
            alert.setHeaderText(null);
            alert.setContentText("Solution not found for the selected algorithm and heuristic.");
            alert.showAndWait();
            return;
        }

        setButtonsDisabled(true);
        gridPane.setDisable(true);
        PauseTransition pause = getTransition(result);
        pause.play();
    }

    private PauseTransition getTransition(List<Character> result) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.8));
        pause.setOnFinished(event -> {
            if (!result.isEmpty()) {
                Character move = result.removeFirst();
                boardWrapper.moveTile(boardWrapper.getBoard().getEmptyPosition().get(0),
                        boardWrapper.getBoard().getEmptyPosition().get(1), move);
                pause.playFromStart();
            } else {
                setButtonsDisabled(false);
                gridPane.setDisable(false);
            }
        });
        return pause;
    }

    private void setButtonsDisabled(boolean disabled) {
        playButton.disableProperty().unbind();
        generateButton.disableProperty().unbind();

        playButton.setDisable(disabled);
        generateButton.setDisable(disabled);
    }

    private List<Character> getResult(String selectedAlgorithm) {
        String selectedHeuristic = heuristicComboBox.getSelectionModel().getSelectedItem();
        BoardSolverInterface boardSolver = switch (selectedAlgorithm) {
            case "BFS" -> new SolverBFS();
            case "DFS" -> new SolverDFS();
            case "A-star" -> new SolverASTR();
            default -> null;
        };
        List<Character> result = null;
        if (boardSolver != null) {
            Node root = new Node(boardWrapper.getBoard(), null, 'N', null);
            result = boardSolver.solve(root, selectedHeuristic);
        }
        return result;
    }

    private void bindGridToBoard() {
        int rows = boardWrapper.getRows();
        int columns = boardWrapper.getColumns();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Text text = new Text();
                text.getStyleClass().add("grid-cell");

                text.textProperty().bind(
                        Bindings.when(boardWrapper.tileProperty(row, col).isEqualTo(0))
                                .then("")
                                .otherwise(boardWrapper.tileProperty(row, col).asString())
                );

                StackPane stackPane = new StackPane(text);
                stackPane.getStyleClass().add("grid-cell");

                stackPane.styleProperty().bind(
                        Bindings.when(boardWrapper.tileProperty(row, col).isEqualTo(0))
                                .then("-fx-background-color: lightgreen;")
                                .otherwise(
                                        Bindings.when(
                                                        boardWrapper.tileProperty(row, col).isEqualTo(row * columns + col + 1)
                                                ).then("-fx-background-color: transparent;")
                                                .otherwise("-fx-background-color: lightcoral;")
                                )
                );

                gridPane.add(stackPane, col, row);
            }
        }
    }
}