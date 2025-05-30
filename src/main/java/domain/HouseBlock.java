package domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a single block within a grid or map, identified by its x and y coordinates.
 */
public class HouseBlock implements Serializable {
    private final int x;
    private final int y;

    /**
     * Constructs a new instance of the HouseBlock class, representing a single block
     * positioned by its x and y coordinates.
     *
     * @param x     The x-coordinate of the block.
     * @param y     The y-coordinate of the block.
     */
    public HouseBlock(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retrieves the x-coordinate of the block.
     *
     * @return      The x-coordinate of the block as an integer.
     */
    public int getX() { return x; }

    /**
     * Retrieves the y-coordinate of the block.
     *
     * @return      The y-coordinate of the block as an integer.
     */
    public int getY() { return y; }

    /**
     * Compares this instance of HouseBlock with another object to determine equality.
     * Two HouseBlock objects are considered equal if they have the same x and y coordinates.
     *
     * @param o         The object to be compared for equality with this instance.
     * @return          {@code true} if the specified object is equal to this instance;
     *                  {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HouseBlock)) return false;
        HouseBlock that = (HouseBlock) o;
        return x == that.x && y == that.y;
    }

    /**
     * Computes the hash code for this instance of the class based on its x and y coordinates.
     * The hash code is computed using the {@code Objects.hash} method to ensure consistency
     * with the {@code equals} method.
     *
     * @return      The hash code value for this object.
     */
    @Override
    public int hashCode() { return Objects.hash(x, y); }
}
