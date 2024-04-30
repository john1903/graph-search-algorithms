package me.jangluzniewicz.graphsearchalgorithms.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {
    Field field;

    @BeforeEach
    void setUp() {
        field = new Field(1);
    }

    @Test
    void getValue() {
        assertEquals(1, field.getValue());
    }

    @Test
    void setValue() {
        field.setValue(2);
        assertEquals(2, field.getValue());
    }

    @Test
    void compareTo() {
        Field field2 = new Field(2);
        assertEquals(-1, field.compareTo(field2));
        Field field3 = new Field(1);
        assertEquals(0, field.compareTo(field3));
        Field field4 = new Field(0);
        assertEquals(1, field.compareTo(field4));
    }

    @Test
    void testToString() {
        assertEquals("1", field.toString());
    }

    @Test
    void testEquals() {
        Field field2 = new Field(1);
        assertEquals(field, field2);
        Field field3 = new Field(2);
        assertNotEquals(field, field3);
    }
}