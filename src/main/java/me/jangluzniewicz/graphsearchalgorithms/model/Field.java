package me.jangluzniewicz.graphsearchalgorithms.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a field on the board with a specific value.
 */
public class Field implements Comparable<Field> {
    private int value;

    /**
     * Constructs a Field with the specified value.
     *
     * @param value The value of the field.
     */
    public Field(int value) {
        this.value = value;
    }

    /**
     * Gets the value of the field.
     *
     * @return The value of the field.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the field.
     *
     * @param value The new value to set for the field.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Compares this field with another field based on their values.
     *
     * @param o The field to be compared.
     * @return A negative integer, zero, or a positive integer as this field's value
     *         is less than, equal to, or greater than the specified field's value.
     */
    @Override
    public int compareTo(Field o) {
        return Integer.compare(this.value, o.value);
    }

    /**
     * Returns a string representation of the field's value.
     *
     * @return A string representing the field's value.
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Computes the hash code for the field.
     *
     * @return The hash code of the field.
     */
    @Override
    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(value);
        return hashCodeBuilder.toHashCode();
    }

    /**
     * Checks if this field is equal to another object.
     *
     * @param obj The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        Field rhs = (Field) obj;
        equalsBuilder.append(value, rhs.value);
        return equalsBuilder.isEquals();
    }
}
