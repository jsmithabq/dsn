package dsn;

import java.util.EventObject;

import yaak.agent.AgentID;

/**
* <p><code>SensingEvent</code> describes sensor-related escapades.</p> 
* @author Jerry Smith
* @version $Id: SensingEvent.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class SensingEvent extends EventObject {
  private Position position;
  private String data;

  SensingEvent(Object source) { // default privacy
    this(source, null, null);
  }

  SensingEvent(Object source, String data) { // default privacy
    this(source, null, data);
  }

  SensingEvent(Object source, Position position) { // default privacy
    this(source, position, null);
  }

  SensingEvent(Object source, Position position,
      String data) { // default privacy
    super(source);
    this.position = position;
    this.data = data;
  }

  /**
  * <p>Retrieves the position.</p>
  * @return The position.
  */

  public Position getPosition() {
    return position;
  }

  void setPosition(Position position) { // default privacy
    this.position = position;
  }

  /**
  * <p>Retrieves the data for the sensing operation.</p>
  * @return The data.
  */

  public String getData() {
    return data;
  }

  void setData(String data) { // default privacy
    this.data = data;
  }
}
