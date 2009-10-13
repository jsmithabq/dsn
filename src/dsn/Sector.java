package dsn;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Enumeration;
import java.util.Vector;

import yaak.core.YaakSystem;
import yaak.agent.AgentID;

/**
* <p><code>Sector</code> implements a collection of sensors.</p> 
* @author Jerry Smith
* @version $Id: Sector.java 16 2006-02-13 19:29:48Z jsmith $
*/

public class Sector {
  /**
  * <p>The step size for any sector.</p>
  */
  public static int STEP_SIZE = Configuration.getSectorStepSize();
  private static int DEFAULT_SECTOR_DIMENSION = STEP_SIZE * 50;
  private String name;
  private Position referencePosition;
  private int width, height;
  private Vector sensorList = new Vector();
  private Vector sectorListeners = new Vector();
  private PropertyChangeSupport changes =
    new PropertyChangeSupport(this);

  /**
  * <p>Creates an anonymous sector with default dimensions.</p>
  */

  public Sector() {
    this("Anonymous",  DEFAULT_SECTOR_DIMENSION,  DEFAULT_SECTOR_DIMENSION);
  }

  /**
  * <p>Creates a named sector with default dimensions.</p>
  * @param name The arbitrary sector name.
  */

  public Sector(String name) {
    this(name,  DEFAULT_SECTOR_DIMENSION,  DEFAULT_SECTOR_DIMENSION);
  }

  /**
  * <p>Creates an anonymous sector.</p>
  * @param width The sector width.
  * @param height The sector height.
  */

  public Sector(int width, int height) {
    this("Anonymous", width, height);
  }

  /**
  * <p>Creates a named sector.</p>
  * @param name The arbitrary sector name.
  * @param width The sector width.
  * @param height The sector height.
  */

  public Sector(String name, int width, int height) {
    this.name = name;
    this.width = adjustDimension(width);
    this.height = adjustDimension(height);
  }

  /**
  * <p>Sets the arbitrary sector name.</p>
  * @param name The sector name.
  */

  public void setName(String name) {
    this.name = name;
  }

  /**
  * <p>Retrieves the arbitrary sector name.</p>
  * @return The sector name.
  */

  public String getName() {
    return name;
  }

  /**
  * <p>Sets the sector width.</p>
  * @param width The sector width.
  */

  public void setWidth(int width) {
    int old = this.width;
    this.width = adjustDimension(width);
    changes.firePropertyChange("width", new Integer(old), new Integer(width));
  }

  /**
  * <p>Retrieves the sector width.</p>
  * @return The sector width.
  */

  public int getWidth() {
    return width;
  }

  /**
  * <p>Sets the sector height.</p>
  * @param height The sector height.
  */

  public void setHeight(int height) {
    int old = this.height;
    this.height = adjustDimension(height);
    changes.firePropertyChange("height",
      new Integer(old), new Integer(height));
  }

  /**
  * <p>Retrieves the sector height.</p>
  * @return The sector height.
  */

  public int getHeight() {
    return height;
  }

  /**
  * <p>Retrieves the current sector step size.</p>
  * @return The sector step size.
  */

  public int getStepSize() {
    return STEP_SIZE;
  }

  /**
  * <p>Sets the position of the sector's base, that is, (0,0),
  * coodinate relative to the sector's environment.</p>
  * @param pos The reference position.
  */

  void setReferencePosition(Position referencePosition) { // default privacy
    this.referencePosition = referencePosition;
  }

  /**
  * <p>Retrieves the position of the sector's base, that is, (0,0),
  * coodinate relative to the sector's environment.</p>
  * @return The reference position.
  */

  public Position getReferencePosition() {
    return referencePosition;
  }

  /**
  * <p>Adds a sensor to the sector.</p>
  * @param sensor The sensor.
  */

  public void addSensor(Sensor sensor) {
    sensorList.addElement(sensor);
  }

  /**
  * <p>Removes a sensor from the sector.</p>
  * @param sensor The sensor.
  */

  public boolean removeSensor(Sensor sensor) {
    return sensorList.removeElement(sensor);
  }

  /**
  * <p>Retrieves the sector's sensors as an enumeration.</p>
  * @return The sensors.
  */

  public Enumeration getSensors() {
    return sensorList.elements();
  }

  /**
  * <p>Gets the number of sensors in the sector.</p>
  * @return The sensor count for the sector.
  */

  public int getSensorCount() {
    return sensorList.size();
  }

  /**
  * <p>Adds a sector listener for the sector.</p>
  * @param sl The sector listener.
  */

  public synchronized void addSectorListener(SectorListener sl) {
    sectorListeners.addElement(sl);
  }

  /**
  * <p>Removes a sector listener for the sector.</p>
  * @param sl The sector listener.
  */

  public synchronized void removeSectorListener(SectorListener sl) {
    sectorListeners.removeElement(sl);
  }

  /**
  * <p>Notifies registered sector listeners that a target has
  * entered the sector.</p>
  * @param position The horizontal and vertical entry coordinates.
  */

  protected void notifyEntered(Position position) {
    Vector list;
    TargetEvent te =
      new TargetEvent(this, position, TargetEvent.TARGET_ENTERED_SECTOR);
    synchronized (this) {
      list = (Vector) sectorListeners.clone();
//      list = sectorListeners;
    }
    for (int i = 0; i < list.size(); i++) {
      SectorListener sl = (SectorListener) list.elementAt(i);
      sl.targetEntered(te);
    }
  }

  /**
  * <p>Notifies registered sector listeners that a target has
  * exited the sector.</p>
  * @param position The horizontal and vertical exit coordinates.
  */

  protected void notifyExited(Position position) {
    Vector list;
    TargetEvent te =
      new TargetEvent(this, position, TargetEvent.TARGET_EXITED_SECTOR);
    synchronized (this) {
      list = (Vector) sectorListeners.clone();
//      list = sectorListeners;
    }
    for (int i = 0; i < list.size(); i++) {
      SectorListener sl = (SectorListener) list.elementAt(i);
      sl.targetExited(te);
    }
  }

  /**
  * <p>Adds a property change listener to the sector's list.</p>
  * @param pcl The property change listener.
  */

  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    changes.addPropertyChangeListener(pcl);
  }

  /**
  * <p>Removes a property change listener from the sector's list.</p>
  * @param pcl The property change listener.
  */

  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    changes.removePropertyChangeListener(pcl);
  }

  private int adjustDimension(int dimension) { // adjust downward
    return (dimension / STEP_SIZE) * STEP_SIZE;
  }
}
