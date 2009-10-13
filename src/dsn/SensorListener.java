package dsn;

/**
* <p><code>SensorListener</code> prescribes sensor-related notifications.</p> 
* @author Jerry Smith
* @version $Id: SensorListener.java 4 2005-06-08 16:58:29Z jsmith $
*/

public interface SensorListener extends java.util.EventListener {
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
  * <p>Reports an "unsensible" entity.  That is,  it senses that it
  * is there, but, technically, it cannot sense the entity!</p>
  * @param e The sensing event.
  */

  void notSensed(SensingEvent e);
}
