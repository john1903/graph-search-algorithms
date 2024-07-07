module me.jangluzniewicz.graphsearchalgorithms {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;

    exports me.jangluzniewicz.graphsearchalgorithms.gui;
    exports me.jangluzniewicz.graphsearchalgorithms.logic;
    exports me.jangluzniewicz.graphsearchalgorithms.model;
    opens me.jangluzniewicz.graphsearchalgorithms.gui to javafx.fxml;
}