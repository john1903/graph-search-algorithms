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

/**
 * MainController manages the interaction between the GUI elements and the puzzle-solving logic,
 * facilitating board generation, solving, and displaying the solution process.
 */
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

    /**
     * Initializes the controller, setting up the initial board state and binding UI elements.
     */
    @FXML
    public void initialize() {
        Board board = BoardFactory.getSolvedBoard(4, 4);
        boardWrapper = new BoardWrapper(board);
        bindGridToBoard();
        algorithmComboBox.getItems().addAll("BFS", "DFS", "A-star");
        bindUIElements();
    }

    /**
     * Binds various UI elements' properties to enable/disable them based on selection.
     */
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
                    getSolvedBoard();
                } else {
                    depthComboBox.getItems().setAll("7", "12", "15", "18");
                }

                if (newValue.equals("BFS") || newValue.equals("DFS")) {
                    heuristicComboBox.getItems().setAll("RDLU", "DRUL", "DRLU", "LUDR", "LURD", "ULDR", "ULRD");
                } else if (newValue.equals("A-star")) {
                    heuristicComboBox.getItems().setAll("MANH", "HAMM");
                }
            } else {
                depthComboBox.getItems().clear();
                heuristicComboBox.getItems().clear();
            }
        });
    }

    /**
     * Generates a solvable board based on selected depth and updates the board display.
     */
    @FXML
    public void generateBoard() {
        int depth = Integer.parseInt(depthComboBox.getSelectionModel().getSelectedItem());
        Board board = BoardFactory.getSolvableBoard(4, 4, depth);
        tilePropertyUpdate(board);
    }

    /**
     * Generates a solved board and updates the board display.
     */
    @FXML
    public void getSolvedBoard() {
        Board board = BoardFactory.getSolvedBoard(4, 4);
        tilePropertyUpdate(board);
    }

    /**
     * Updates the tile properties of the board based on the specified board.
     *
     * @param board The board to update the tile properties.
     */
    private void tilePropertyUpdate(Board board) {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                boardWrapper.tileProperty(i, j).set(board.getFieldValue(i, j));
            }
        }
        boardWrapper.isBoardSolvedProperty().set(board.isBoardSolved());
    }

    /**
     * Initiates the board solving process using the selected algorithm and heuristic.
     */
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

    /**
     * Creates a Task to perform the solving operation asynchronously.
     *
     * @param boardSolver The solver algorithm to use.
     * @return A Task that performs the solving operation.
     */
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

    /**
     * Creates a PauseTransition to animate the board solution process.
     *
     * @param result      The list of moves representing the solution.
     * @param boardSolver The solver algorithm used to find the solution.
     * @return A PauseTransition for animating the solution.
     */
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

    /**
     * Displays an alert dialog with the specified title, header text, and content text.
     *
     * @param title       The title of the alert dialog.
     * @param headerText  The header text of the alert dialog.
     * @param contentText The content text of the alert dialog.
     */
    private void showAlertDialog(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     * Enables or disables UI elements based on the provided flag.
     *
     * @param disabled Flag indicating whether to disable or enable UI elements.
     */
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

    /**
     * Retrieves the solution path from the specified board solver algorithm.
     *
     * @param boardSolver The board solver algorithm to use.
     * @return The list of moves representing the solution path.
     */
    private List<Character> getResult(BoardSolverInterface boardSolver) {
        String selectedHeuristic = heuristicComboBox.getSelectionModel().getSelectedItem();
        List<Character> result = null;
        if (boardSolver != null) {
            Node root = new Node(boardWrapper.getBoard(), null, 'N', null);
            result = boardSolver.solve(root, selectedHeuristic);
        }
        return result;
    }

    /**
     * Binds the JavaFX GridPane to the current board state for display.
     */
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