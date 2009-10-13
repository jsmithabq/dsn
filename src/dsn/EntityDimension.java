package dsn;

/**
* <p><code>EntityDimension</code> describes a two-dimensional entity.</p> 
* @author Jerry Smith
* @version $Id: EntityDimension.java 16 2006-02-13 19:29:48Z jsmith $
*/

public class EntityDimension {
  /**
  * <p>The width.</p>
  */
  public int width;

  /**
  * <p>The height.</p>
  */
  public int height;

  /**
  * <p>Creates a dimension instance initialized to (0,0).</p>
  */

  public EntityDimension() {
    this(0, 0);
  }

  /**
  * <p>Creates a dimension instance initialized with the specified
  * dimensions.</p>
  * @param width The width.
  * @param height The height.
  */

  public EntityDimension(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /**
  * <p>Creates a dimension instance initialized from another
  * instance.</p>
  * @param dimension The dimension instance.
  */

  public EntityDimension(EntityDimension dimension) {
    this.width = dimension.width;
    this.height = dimension.height;
  }

  /**
  * <p>Sets the width.</p>
  * @param width The width.
  */

  public void setWidth(int width) {
    this.width = width;
  }

  /**
  * <p>Retrieves the width.</p>
  * @return The width.
  */

  public int getWidth() {
    return width;
  }

  /**
  * <p>Provides the hashcode for the dimensions.</p>
  * @return The hash of the underlying dimension components.
  */

  public int hashCode() {
    return new Integer(width).hashCode() + new Integer(height).hashCode();
  }

  /**
  * <p>Tests the equality of two <code>EntityDimension</code> instances.</p>
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
    EntityDimension dimension = null;
    try {
      dimension = (EntityDimension) obj;
    }
    catch (Exception e) {
      return false;
    }
    return width == dimension.width && height == dimension.height;
  }

  /**
  * <p>Clones the <code>Position</code> instance.</p>
  * @return The cloned instance.
  */

  public Object clone() {
    return new EntityDimension(this);
  }

  /**
  * <p>Gets a string representation of the dimensions.</p>
  * @return The dimensions as a string.
  */

  public String toString() {
    return "[" + width + " X " + height + "]";
  }
}
