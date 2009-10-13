package dsn;

import java.util.EventObject;

import yaak.agent.AgentID;

/**
* <p><code>MoveEvent</code> describes sensor-related escapades.</p> 
* @author Jerry Smith
* @version $Id: MoveEvent.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class MoveEvent extends EventObject {
  private Position position;
  private AgentID id;

  MoveEvent(Object source) { // default privacy
    super(source);
  }

  MoveEvent(Object source, Position position) { // default privacy
    this(source, position, null);
  }

  MoveEvent(Object source, Position position, AgentID id) { // default privacy
    super(source);
    this.position = position;
    this.id = id;
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
  * <p>Retrieves the agent ID associated with the movement-tracked object.</p>
  * @return The agent ID.
  */

  public AgentID getAgentID() {
    return id;
  }

  void setAgentID(AgentID id) { // default privacy
    this.id = id;
  }
}
