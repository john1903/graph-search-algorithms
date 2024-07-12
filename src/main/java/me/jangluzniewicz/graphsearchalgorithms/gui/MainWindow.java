package me.jangluzniewicz.graphsearchalgorithms.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * MainWindow is the main entry point for the JavaFX application,
 * initializing and displaying the GUI for the 15 Puzzle Solver.
 */
public class MainWindow extends Application {
    /**
     * Starts the JavaFX application by loading the main window from FXML.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.resizableProperty().set(false);
        stage.setTitle("15 Puzzle Solver");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch();
    }
}
