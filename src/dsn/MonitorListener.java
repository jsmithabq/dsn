package dsn;

/**
* <p><code>MonitorListener</code> allows an agent to monitor movement
* events.</p>
*/

public interface MonitorListener extends java.util.EventListener {
  /**
  * <p>Reports a movement.</p>
  * @param e The move event.
  */

  void moved(MoveEvent e);

  /**
  * <p>Reports a sensed entity.</p>
  * @param e The sensing event.
  */

  void sensed(SensingEvent e);

  /**
  * <p>Reports a failed-to-be-sensed entity.</p>
  * @param e The sensing event.
  */

  void notSensed(SensingEvent e);
}
