package me.jangluzniewicz.graphsearchalgorithms.gui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import me.jangluzniewicz.graphsearchalgorithms.logic.*;
import me.jangluzniewicz.graphsearchalgorithms.model.Board;
import me.jangluzniewicz.graphsearchalgorithms.model.Node;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController {
    private BoardWrapper boardWrapper;
    @FXML
    private GridPane gridPane;
    @FXML
    private ComboBox<String> algorithmComboBox;
    @FXML
    private ComboBox<String> heuristicComboBox;
    @FXML
    private ComboBox<String> depthComboBox;
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
        bindUIElements();
    }

    private void bindUIElements() {
        generateButton.disableProperty().bind(
                depthComboBox.valueProperty().isNull()
        );
        depthComboBox.disableProperty().bind(
                algorithmComboBox.valueProperty().isNull()
        );
        playButton.disableProperty().bind(
                algorithmComboBox.valueProperty().isNull()
                        .or(heuristicComboBox.valueProperty().isNull())
                        .or(boardWrapper.isBoardSolvedProperty())
        );
        heuristicComboBox.disableProperty().bind(
                algorithmComboBox.valueProperty().isNull()
        );
        algorithmComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("DFS")) {
                    depthComboBox.getItems().setAll("7");
                    depthComboBox.getSelectionModel().selectFirst();
                } else {
                    depthComboBox.getItems().setAll("7", "12", "15", "18");
                }

                if (newValue.equals("BFS") || newValue.equals("DFS")) {
                    heuristicComboBox.getItems().setAll("RDLU", "DRUL", "DRLU", "LUDR", "LURD", "ULDR", "ULRD");
                } else if (newValue.equals("A-star")) {
                    heuristicComboBox.getItems().setAll("MANH", "HAMM");
                }
            }
        });
    }

    @FXML
    public void generateBoard() {
        int depth = Integer.parseInt(depthComboBox.getSelectionModel().getSelectedItem());
        Board board = BoardFactory.getSolvableBoard(4, 4, depth);
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                boardWrapper.tileProperty(i, j).set(board.getFieldValue(i, j));
            }
        }
        boardWrapper.isBoardSolvedProperty().set(board.isBoardSolved());
    }

    @FXML
    public void solveBoard() {
        String selectedAlgorithm = algorithmComboBox.getSelectionModel().getSelectedItem();
        BoardSolverInterface boardSolver = switch (selectedAlgorithm) {
            case "BFS" -> new SolverBFS();
            case "DFS" -> new SolverDFS();
            case "A-star" -> new SolverASTR();
            default -> null;
        };

        setUIElementsDisabled(true);

        Task<List<Character>> task = getTask(boardSolver);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(task);
        executorService.shutdown();
    }

    private Task<List<Character>> getTask(BoardSolverInterface boardSolver) {
        Task<List<Character>> task = new Task<>() {
            @Override
            protected List<Character> call() {
                return getResult(boardSolver);
            }
        };

        task.setOnSucceeded(event -> {
            List<Character> result = task.getValue();
            if (result == null || result.isEmpty()) {
                showAlertDialog("Solution Not Found", null,
                        "Solution not found for the selected algorithm and heuristic.");
                setUIElementsDisabled(false);
                return;
            }
            gridPane.setDisable(true);
            PauseTransition pause = getTransition(result, boardSolver);
            pause.play();
        });

        task.setOnFailed(event -> {
            showAlertDialog("Error", null, "An error occurred while solving the board.");
            setUIElementsDisabled(false);
        });
        return task;
    }

    private PauseTransition getTransition(List<Character> result, BoardSolverInterface boardSolver) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.8));
        pause.setOnFinished(event -> {
            if (!result.isEmpty()) {
                Character move = result.removeFirst();
                boardWrapper.moveTile(boardWrapper.getBoard().getEmptyPosition().get(0),
                        boardWrapper.getBoard().getEmptyPosition().get(1), move);
                pause.playFromStart();
            } else {
                setUIElementsDisabled(false);
                gridPane.setDisable(false);
                if (boardSolver != null) {
                    Platform.runLater(() ->
                            showAlertDialog("Solution Found", "Statistics:", boardSolver.getStats()));
                }
            }
        });
        return pause;
    }

    private void showAlertDialog(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void setUIElementsDisabled(boolean disabled) {
        playButton.disableProperty().unbind();
        generateButton.disableProperty().unbind();
        depthComboBox.disableProperty().unbind();
        algorithmComboBox.disableProperty().unbind();
        heuristicComboBox.disableProperty().unbind();
        playButton.setDisable(disabled);
        generateButton.setDisable(disabled);
        depthComboBox.setDisable(disabled);
        algorithmComboBox.setDisable(disabled);
        heuristicComboBox.setDisable(disabled);
        if (!disabled) {
            bindUIElements();
        }
    }

    private List<Character> getResult(BoardSolverInterface boardSolver) {
        String selectedHeuristic = heuristicComboBox.getSelectionModel().getSelectedItem();
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
