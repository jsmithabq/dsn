package dsn;

/**
* <p><code>CoordinatedEntity</code> prescribes coordinate-related
* functionality.</p>
* @author Jerry Smith
* @version $Id: CoordinatedEntity.java 4 2005-06-08 16:58:29Z jsmith $
*/

public interface CoordinatedEntity {
  /**
  * <p>Retrieves the entity's width.</p>
  * @return The width.
  */

//  int getWidth();

  /**
  * <p>Retrieves the entity's height.</p>
  * @return The height.
  */

//  int getHeight();

  /**
  * <p>Retrieves the entity's dimensions.</p>
  * @return The entity's dimensions.
  */

  EntityDimension getEntityDimension();

  /**
  * <p>Retrieves the entity's position.</p>
  * @return The entity's position.
  */

  Position getPosition();
}
