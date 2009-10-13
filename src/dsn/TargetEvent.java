package dsn;

import java.util.EventObject;

import yaak.agent.AgentID;

/**
* <p><code>TargetEvent</code> describes target-related escapades.</p> 
* @author Jerry Smith
* @version $Id: TargetEvent.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class TargetEvent extends EventObject {
  /**
  * <p>The event ID associated with a target entering a sector.</p>
  */

  public static final int TARGET_ENTERED_SECTOR = 1;

  /**
  * <p>The event ID associated with a target exiting a sector.</p>
  */

  public static final int TARGET_EXITED_SECTOR = 2;
  private Position position;
  private int eventID;
  private AgentID agentID;

  TargetEvent(Object source) { // default privacy
    super(source);
  }

  TargetEvent(Object source, Position position) { // default privacy
    this(source, position, -1, null);
  }

  TargetEvent(Object source, Position position,
      int eventID) { // default privacy
    this(source, position, eventID, null);
  }

  TargetEvent(Object source, Position position,
      AgentID agentID) { // default privacy
    this(source, position, -1, agentID);
  }

  TargetEvent(Object source, Position position, int eventID,
      AgentID agentID) { // default privacy
    super(source);
    this.position = position;
    this.agentID = agentID;
    this.eventID = eventID;
  }

  void setPosition(Position position) { // default privacy
    this.position = position;
  }

  /**
  * <p>Retrieves the position.</p>
  * @return The position.
  */

  public Position getPosition() {
    return position;
  }

   void setAgentID(AgentID agentID) { // default privacy
    this.agentID = agentID;
  }

  /**
  * <p>Retrieves the agent ID associated with the target.</p>
  * @return The agent ID.
  */

  public AgentID getAgentID() {
    return agentID;
  }

  void setEventID(int eventID) { // default privacy
    this.eventID = eventID;
  }

  /**
  * <p>Retrieves the event ID for this event.</p>
  * @return The event ID.
  */

  public int getEventID() {
    return eventID;
  }
}
