package dsn;

import java.util.Vector;

import yaak.agent.communication.Message;
import yaak.agent.communication.MessageListener;

/**
* <p><code>TrackingManager</code> coordinates all sector-level operations.</p>
* @author Jerry Smith
* @version $Id: TrackingManager.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class TrackingManager extends SensorAgent {
  private Vector targetListeners = new Vector();
  /**
  * <p>The default name for a tracking manager.</p>
  */

  public static final String DEFAULT_NAME = "AnonymousTrackingManager";

  /**
  * <p>Not for public consumption.</p>
  */

  public TrackingManager() {
    super();
  }

  protected void onCreation(Object args) {
    setAgentName(DEFAULT_NAME);
    registerForMessages(new MessageListener() {
      public void onMessage(Message message) {
//        Position position = (Position) message.getObject();
        TargetEmission emission = (TargetEmission) message.getObject();
        DSNSystem.getLogger().fine("Target " + emission.getID() + " @ " +
          emission.getPosition() + ".");
        MoveEvent me = new MoveEvent(this, emission.getPosition());
        notifyMovement(me);
      }
    });
  }

  private void notifyMovement(MoveEvent e) {
    Vector list;
    synchronized (this) {
      list = (Vector) targetListeners.clone();
//      list = targetListeners;
    }
    for (int i = 0; i < list.size(); i++) {
      TargetListener tl = (TargetListener) list.elementAt(i);
      tl.moved(e);
    }
  }

  /**
  * <p>Adds a listener for the target movement.</p>
  * @param listener The target listener.
  */

  public synchronized void addTargetListener(TargetListener listener) {
    targetListeners.addElement(listener);
  }

  /**
  * <p>Removes a listener for the target movement.</p>
  * @param listener The target listener.
  */

  public synchronized void removeTargetListener(TargetListener listener) {
    targetListeners.removeElement(listener);
  }
}
