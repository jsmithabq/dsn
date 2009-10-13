package dsn;

import yaak.util.StringUtil;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
* <p><code>Position</code> describes a two-dimensional coordinate
* location.</p> 
* @author Jerry Smith
* @version $Id: Position.java 16 2006-02-13 19:29:48Z jsmith $
*/

public class Position implements Serializable {
  private int x, y;

  /**
  * <p>Creates a position instance initialized to (0,0).</p>
  */

  public Position() {
    this(0, 0);
  }

  /**
  * <p>Creates a position instance initialized with the specified
  * coordinates.</p>
  * @param x The x coordinate.
  * @param y The y coordinate.
  */

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
  * <p>Creates a position instance initialized from another
  * position.</p>
  * @param pos The existing position instance.
  */

  public Position(Position pos) {
    this.x = pos.getX();
    this.y = pos.getY();
  }

  /**
  * <p>Creates a position instance initialized by parsing a
  * string-based coordinate specification, e.g., <code>"(3,4)"</code>.</p>
  * @param coordinate The two-dimensional coordinate.
  */

  public Position(String coordinate) {
    if (!StringUtil.hasValue(coordinate)) {
      return;
    }
    StringTokenizer st = new StringTokenizer(coordinate, "(,)");
    try {
      x = Integer.parseInt(st.nextToken());
      y = Integer.parseInt(st.nextToken());
    }
    catch (NoSuchElementException e) {
      x = y = -1;
    }
    catch (NumberFormatException e) {
      x = y = -1;
    }
  }

  /**
  * <p>Sets the "x" (horizontal) coordinate.</p>
  * @param x The horizontal coordinate.
  */

  public void setX(int x) {
    this.x = x;
  }

  /**
  * <p>Increments the "x" (horizontal) coordinate by the specified amount.</p>
  * @param increment The increment.
  * @return The (new) horizontal coordinate.
  */

  public int incrementX(int increment) {
    return x += increment;
  }

  /**
  * <p>Decrements the "x" (horizontal) coordinate by the specified amount.</p>
  * @param decrement The decrement.
  * @return The (new) horizontal coordinate.
  */

  public int decrementX(int decrement) {
    return x += incrementX(-decrement);
  }

  /**
  * <p>Retrieves the "x" (horizontal) coordinate.</p>
  * @return The horizontal coordinate.
  */

  public int getX() {
    return x;
  }

  /**
  * <p>Sets the "y" (vertical) coordinate.</p>
  * @param y The vertical coordinate.
  */

  public void setY(int y) {
    this.y = y;
  }

  /**
  * <p>Increments the "y" (vertical) coordinate by the specified amount.</p>
  * @param increment The increment.
  * @return The (new) vertical coordinate.
  */

  public int incrementY(int increment) {
    return y += increment;
  }

  /**
  * <p>Decrements the "y" (vertical) coordinate by the specified amount.</p>
  * @param decrement The increment.
  * @return The (new) vertical coordinate.
  */

  public int decrementY(int decrement) {
    return incrementY(-decrement);
  }

  /**
  * <p>Retrieves the "y" (vertical) coordinate.</p>
  * @return The vertical coordinate.
  */

  public int getY() {
    return y;
  }

  /**
  * <p>Augments the current position by the specified position offset.
  * There are no tests for legitimate values.</p>
  * @param position The position offset.
  */

  public void augmentPosition(Position position) {
    x += position.getX();
    y += position.getY();
  }

  /**
  * <p>Calculates a new position relative to this instance, offset by
  * the specified position.  This method does not modify the current
  * instance.</p>
  * @param position The position offset.
  * @return The calculated position.
  */

  public Position calculatePosition(Position position) {
    return new Position(x + position.getX(), y + position.getY());
  }

  /**
  * <p>Tests the legitimacy of the coordinates.</p>
  * @return Whether or not the coordinates are valid (nonnegative).
  */

  public boolean isLegal() {
    return x > -1 && y > -1;
  }

  /**
  * <p>Provides the hashcode for the coordinates.</p>
  * @return The hash of the underlying (x,y).
  */

  public int hashCode() {
    return new Integer(x).hashCode() + new Integer(y).hashCode();
  }

  /**
  * <p>Tests the equality of two <code>Position</code> instances.</p>
  * @param obj The comparison object.
  * @return Whether or not the comparison instance equals this instance.
  */

  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    Position pos = null;
    try {
      pos = (Position) obj;
    }
    catch (Exception e) {
      return false;
    }
    return x == pos.getX() && y == pos.getY();
  }

  /**
  * <p>Determines the distance between two <code>Position</code>
  * instances.  The current implementation supports a two-dimensional
  * coordinate system.  Distances are maximized; that is, fractional
  * distance is rounded up.</p>
  * @param pos The "other" instance.
  * @return The distance between this instance and the referenced
  * instance, or -1 if the argument is null.
  */

  public int getDistanceFromPosition(Position pos) {
    if (pos == null) {
      return -1; // ??? Should this raise an exception, or not?
    }
    if (pos == this) {
      return 0;
    }
    return (int)
      Math.ceil(
        Math.sqrt(
          Math.pow(Math.abs(pos.getX() - x), 2) +
          Math.pow(Math.abs(pos.getY() - y), 2)
        )
      )
    ;
  }

  /**
  * <p>Clones the <code>Position</code> instance.</p>
  * @return The cloned instance.
  */

  public Object clone() {
    return new Position(this);
  }

  /**
  * <p>Gets a string representation of the coordinates.</p>
  * @return The coordinates as a string.
  */

  public String toString() {
    return "(" + x + "," + y + ")";
  }
}
