package dsn;

import java.io.Serializable;

/**
* <p><code>TargetEmission</code> describes a target's emitted
* information, that is, its simulation signature.</p>
* @author Jerry Smith
* @version $Id: TargetEmission.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class TargetEmission implements Serializable {
  private String id;
  private Position position;

  TargetEmission(String id, Position position) {
    this.id = id;
    this.position = position;
  }

  /**
  * <p>Sets the target's ID.</p>
  * @param id The ID.
  */

  public void setID(String id) {
    this.id = id;
  }

  /**
  * <p>Retrieves the target's ID.</p>
  * @return The ID.
  */

  public String getID() {
    return id;
  }

  /**
  * <p>Sets the target's position.</p>
  * @param position The position.
  */

  public void setPosition(Position position) {
    this.position = position;
  }

  /**
  * <p>Retrieves the target's position.</p>
  * @return The position.
  */

  public Position getPosition() {
    return position;
  }
}
