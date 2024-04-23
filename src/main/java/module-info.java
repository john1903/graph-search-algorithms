module me.jangluzniewicz.graphsearchalgorithms {
    requires javafx.controls;
    requires javafx.fxml;


    opens me.jangluzniewicz.graphsearchalgorithms to javafx.fxml;
    exports me.jangluzniewicz.graphsearchalgorithms;
}