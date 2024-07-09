package me.jangluzniewicz.graphsearchalgorithms.model;

import me.jangluzniewicz.graphsearchalgorithms.logic.BoardController;
import me.jangluzniewicz.graphsearchalgorithms.logic.BoardValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    Board state;

    @BeforeEach
    void setUp() {
        ArrayList<Field> fieldsArray = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            fieldsArray.add(new Field(i));
        }
        fieldsArray.add(new Field(0));
        state = new Board(fieldsArray, new BoardController(), new BoardValidator());
    }

    @Test
    void testGetChildren() {
        Node node = new Node(state, null, (char) 0, new ArrayList<>());
        List<Node> children = node.getChildren();
        assertEquals(2, children.size());
        children.forEach(child -> assertEquals(node, child.getParent()));
        children.forEach(child -> assertEquals(1, child.getPath().size()));
        assertEquals('U', children.getFirst().getLastMove());
        assertEquals('L', children.getLast().getLastMove());
        assertEquals(1, children.getFirst().getPath().size());
        assertEquals(1, children.getLast().getPath().size());
    }
}