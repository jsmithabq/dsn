package dsn;

import java.util.Vector;

import yaak.agent.AgentID;
import yaak.util.Util;

/**
* <p><code>SensorMonitor</code> maintains tracking-related sensor data.</p>
* @author Jerry Smith
* @version $Id: SensorMonitor.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class SensorMonitor extends SensorAgent implements SensorListener {
  private Sensor sensor;
  private Vector monitorListeners = new Vector();

  /**
  * <p>Not for public consumption.</p>
  */

  public SensorMonitor() {
    super();
  }

  /**
  * <p>Sets the sensor associated with this monitoring agent.</p>
  * @param sensor The sensor.
  */

  void setSensor(Sensor sensor) { // default privacy
    this.sensor = sensor;
    sensor.addSensorListener(this);
  }

  /**
  * <p>Retrieves the sensor associated with this monitoring agent.</p>
  * @return The sensor.
  */

  public Sensor getSensor() {
    return sensor;
  }

  /**
  * <p>Monitors a movement, notifying registered listeners.</p>
  * @param e The move event.
  */

  public void moved(MoveEvent e) {
    AgentID id = getAgentID();
    e.setAgentID(id);
    DSNSystem.getLogger().fine(id + "'s new location: " +
      e.getPosition() + ".");
    notifyMovement(e);
  }

  /**
  * <p>Monitors sensing operations.</p>
  * @param e The sensing event.
  */

  public void sensed(SensingEvent e) {
    Sensor sensor = (Sensor) e.getSource();
    DSNSystem.getLogger().fine(
      "Sensor at location " + sensor.getPosition() +
      " monitored by " + getAgentName() + Util.getLineSeparator() +
      "  detected entity at location " + e.getPosition() +
      " emitting: '" + e.getData() + "'.");
    notifySensing(e);
  }

  /**
  * <p>Monitors sensing operations.</p>
  * @param e The sensing event.
  */

  public void notSensed(SensingEvent e) {
    Sensor sensor = (Sensor) e.getSource();
    DSNSystem.getLogger().fine(
      "Sensor at location " + sensor.getPosition() +
      " monitored by " + getAgentName() + Util.getLineSeparator() +
      "  failed to detect entity at location " + e.getPosition() +
      " emitting: '" + e.getData() + "'.");
    notifyNotSensing(e);
  }

  /**
  * <p>Adds a listener for the monitored movement.</p>
  * @param listener The monitor listener.
  */

  public synchronized void addMonitorListener(MonitorListener listener) {
    monitorListeners.addElement(listener);
  }

  /**
  * <p>Removes a listener for the monitored movement.</p>
  * @param listener The monitor listener.
  */

  public synchronized void removeMonitorListener(MonitorListener listener) {
    monitorListeners.removeElement(listener);
  }

  /**
  * <p>Notifies registered monitors of movement.</p>
  * @param e The movement event.
  */

  protected void notifyMovement(MoveEvent e) {
    Vector list;
    synchronized (this) {
      list = (Vector) monitorListeners.clone();
//      list = monitorListeners;
    }
    for (int i = 0; i < list.size(); i++) {
      MonitorListener ml = (MonitorListener) list.elementAt(i);
      ml.moved(e);
    }
  }

  /**
  * <p>Notifies registered monitors of sensed entities.</p>
  * @param e The sensing event.
  */

  protected void notifySensing(SensingEvent e) {
    Vector list;
    synchronized (this) {
      list = (Vector) monitorListeners.clone();
//      list = monitorListeners;
    }
    for (int i = 0; i < list.size(); i++) {
      MonitorListener ml = (MonitorListener) list.elementAt(i);
      ml.sensed(e);
    }
  }

  /**
  * <p>Notifies registered monitors of failed-to-be-sensed entities.</p>
  * @param e The sensing event.
  */

  protected void notifyNotSensing(SensingEvent e) {
    Vector list;
    synchronized (this) {
      list = (Vector) monitorListeners.clone();
//      list = monitorListeners;
    }
    for (int i = 0; i < list.size(); i++) {
      MonitorListener ml = (MonitorListener) list.elementAt(i);
      ml.notSensed(e);
    }
  }
}
