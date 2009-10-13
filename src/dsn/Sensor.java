package dsn;

import yaak.concurrent.DefaultExecutor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Vector;

/**
* <p><code>Sensor</code> implements a sensor following the JavaBean
* protocol.  The implementation is a simulated sensor that represents
* only the on-board sensor functionality that would be implemented in
* hardware.  It would be easier to simulate a sensor with agent
* functionality, but doing so is inappropriate, because ultimately
* sensors will be implemented outside this software system.  An agent,
* however, will be associated with each sensor that handles all high-level,
* software-side processing.</p>
* @author Jerry Smith
* @version $Id: Sensor.java 16 2006-02-13 19:29:48Z jsmith $
*/

public class Sensor implements Serializable, CoordinatedEntity {
  private static final int RANGE = Configuration.getSensorRange();
  private static final int THRESHOLD = Configuration.getSensorThreshold();
  private Sector sector;
  private int range = RANGE;
  private int moveThreshold = THRESHOLD;
  private EntityDimension dimension;
  private Position position;
  private Vector sensorListeners = new Vector();
  private PropertyChangeSupport changes = new PropertyChangeSupport(this);
  private transient MulticastReceiver receiver = new MulticastReceiver();

  /**
  * <p>Creates a sensor, not yet associated with a sector.</p>
  */

  public Sensor() {
    this(null, new Position(0, 0), new EntityDimension(0, 0),
      RANGE, THRESHOLD);
  }

  /**
  * <p>Creates a sensor for the specified sector.</p>
  * @param sector The sector.
  */

  public Sensor(Sector sector) {
    this(sector, new Position(0, 0), new EntityDimension(0, 0), RANGE,
      THRESHOLD);
  }

  /**
  * <p>Creates a sensor with the sector-related coordinates and
  * sensor dimensions.</p>
  * @param position The position.
  * @param dimension The dimension.
  */

  public Sensor(Position position, EntityDimension dimension) {
    this(null, position, dimension, RANGE, THRESHOLD);
  }

  /**
  * <p>Creates a sensor with the sector-related coordinates and
  * sensor dimensions.</p>
  * @param sector The sector.
  * @param position The position.
  * @param dimension The dimension.
  */

  public Sensor(Sector sector, Position position, EntityDimension dimension) {
    this(sector, position, dimension, RANGE, THRESHOLD);
  }

  /**
  * <p>Creates a sensor with the sector-related coordinates, dimensions,
  * sensing range, and movement threshold.</p>
  * @param sector The sector.
  * @param position The position.
  * @param dimension The dimension.
  * @param range The sensing range.
  * @param moveThreshold The movement threshold.
  */

  public Sensor(Sector sector, Position position, EntityDimension dimension,
      int range, int moveThreshold) {
    this.sector = sector;
    this.position = position;
    this.dimension = dimension;
    this.range = range;
    this.moveThreshold = moveThreshold;
    receiver.setMulticastListener(new MReceiver()); // one receiver per sensor
  }

  /**
  * <p>Starts the sensing process (in a separate thread).  This class
  * does not implement runnable because the sensing process is
  * implemented under a separate thread, or scheduling process.</p>
  */

  public void start() {
    run();
  }

  /**
  * <p>Starts the sensing process (in a separate thread).  This class
  * does not implement runnable because the sensing process is
  * implemented under a separate thread, or scheduling process.</p>
  */

  public void run() {
    new DefaultExecutor().execute(receiver);
  }

  /**
  * <p>Stops the sensing process (running in a separate thread).</p>
  */

  public void stop() {
    receiver.stop();
  }

  /**
  * <p>Sets the sector within which this sensor operates.</p>
  * @param sector The reference sector.
  */

  void setSector(Sector sector) { // default privacy
    this.sector = sector;
  }

  /**
  * <p>Retrieves the reference sector.</p>
  * @return The reference sector.
  */

  public Sector getSector() {
    return sector;
  }

  /**
  * <p>Sets the sensing range for this sensor.</p>
  * @param range The sensing range.
  */

  protected void setRange(int range) {
    int old = this.range;
    this.range = range;
    changes.firePropertyChange("range", new Integer(old), new Integer(range));
  }

  /**
  * <p>Retrieves the sensing range.</p>
  * @return The sensing range.
  */

  public int getRange() {
    return range;
  }

  /**
  * <p>Sets the movement threshold for this sensor.</p>
  * @param moveThreshold The movement threshold.
  */

  protected void setMoveThreshold(int moveThreshold) {
    int old = this.moveThreshold;
    this.moveThreshold = moveThreshold;
    changes.firePropertyChange(
      "moveThreshold", new Integer(old), new Integer(range));
  }

  /**
  * <p>Retrieves the movement threshold.</p>
  * @return The movement threshold.
  */

  public int getMoveThreshold() {
    return moveThreshold;
  }

  /**
  * <p>Sets the coordinates for the sensor relative to the sector.</p>
  * @param position The sector-relative coordinates.
  */

  void setPosition(Position position) { // default privacy
    Position old = this.position;
    this.position = position;
    changes.firePropertyChange("position", old, position);
//    if (Math.abs(old.getX() - position.getX()) >= moveThreshold ||
//        Math.abs(old.getY() - position.getY()) >= moveThreshold) {
    if (this.position.getDistanceFromPosition(old) > moveThreshold) {
      handleMovement();
    }
  }

  /**
  * <p>Retrieves the coodinates relative to the sector.</p>
  * @return The position coordinates.
  */

  public Position getPosition() {
    return position;
  }

  private void handleMovement() {
    notifyMovement();
  }

  /**
  * <p>Sets the dimensions of the sensor.</p>
  * @param dimension The dimensions.
  */

  void setEntityDimension(EntityDimension dimension) { // default privacy
    EntityDimension old = this.dimension;
    this.dimension = dimension;
    changes.firePropertyChange("dimension", old, dimension);
  }

  /**
  * <p>Retrieves the dimensions of the sensor.</p>
  * @return The dimensions.
  */

  public EntityDimension getEntityDimension() {
    return dimension;
  }

  /**
  * <p>Adds a sensor listener for the sensor.</p>
  * @param sl The sensor listener.
  */

  public synchronized void addSensorListener(SensorListener sl) {
    sensorListeners.addElement(sl);
  }

  /**
  * <p>Removes a sensor listener for the sensor.</p>
  * @param sl The sensor listener.
  */

  public synchronized void removeSensorListener(SensorListener sl) {
    sensorListeners.removeElement(sl);
  }

  /**
  * <p>Notifies registered sensor listeners that the sensor has
  * moved/relocated within the sector.</p>
  */

  protected void notifyMovement() {
    Vector list;
    MoveEvent me = new MoveEvent(this, position);
    synchronized (this) {
      list = (Vector) sensorListeners.clone();
//      list = sensorListeners;
    }
    for (int i = 0; i < list.size(); i++) {
      SensorListener sl = (SensorListener) list.elementAt(i);
      sl.moved(me);
    }
  }

  /**
  * <p>Notifies registered sensor listeners that sensed objects have
  * moved/relocated within the sector.</p>
  */

  protected void notifySensed(String data) {
    Vector list;
    //
    // Intelligence reveals that the target is rather stupid, and
    // actually emits its own location as a string-based coordinate.
    //
    Position pos = new Position(data);
    SensingEvent se = null;
    if (pos.isLegal()) {
      se = new SensingEvent(this, pos, data);
    }
    else {
      se = new SensingEvent(this, data);
    }
    synchronized (this) {
      list = (Vector) sensorListeners.clone();
//      list = sensorListeners;
    }
    for (int i = 0; i < list.size(); i++) {
      SensorListener sl = (SensorListener) list.elementAt(i);
      sl.sensed(se);
    }
  }

  /**
  * <p>Notifies registered sensor listeners that sensed objects have
  * moved/relocated outside the sector.</p>
  */

  protected void notifyNotSensed(String data) {
    Vector list;
    //
    // Intelligence reveals that the target is rather stupid, and
    // actually emits its own location as a string-based coordinate.
    //
    Position pos = new Position(data);
    SensingEvent se = null;
    if (pos.isLegal()) {
      se = new SensingEvent(this, pos, data);
    }
    else {
      se = new SensingEvent(this, data);
    }
    synchronized (this) {
      list = (Vector) sensorListeners.clone();
//      list = sensorListeners;
    }
    for (int i = 0; i < list.size(); i++) {
      SensorListener sl = (SensorListener) list.elementAt(i);
      sl.notSensed(se);
    }
  }

  /**
  * <p>Adds a property change listener to the sensor's list.</p>
  * @param pcl The property change listener.
  */

  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    changes.addPropertyChangeListener(pcl);
  }

  /**
  * <p>Removes a property change listener from the sensor's list.</p>
  * @param pcl The property change listener.
  */

  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    changes.removePropertyChangeListener(pcl);
  }

  /**
  * <p>Determines whether or not a position is within the sensor's range
  * of detection.  The sensors aren't agents--they represent hardware or
  * firmware devices that ultimately will lie outside this software
  * system.  Thus, agent filtering isn't applicable.  So, in simulating
  * sensors, the range is handled here within the sensor.</p>
  * @return Whether or not the position is within range.
  */

  public boolean isWithinRange(Position position) {
    Position relativePosition =
      this.position.calculatePosition(sector.getReferencePosition());
    int d = relativePosition.getDistanceFromPosition(position);
    return d <= range;
  }

  /**
  * <p>Determines the sensor's position relative to its global
  * environment, not its sector.</p>
  * @return Its position in the world.
  */

  public Position getRelativePosition() {
    return position.calculatePosition(sector.getReferencePosition());
  }

  class MReceiver implements MulticastListener {
    public void received(MulticastEvent e) {
      String data = e.getData();
      Position pos = DefaultTargetSignature.getPosition(data);
      if (pos == null) { // not implemented
        return; // ??? what's the best policy here?
      }
      if (isWithinRange(pos)) {
        notifySensed(pos.toString());
      }
      else if (!isWithinRange(pos)) {
        notifyNotSensed(pos.toString());
      }
    }
  }
}
