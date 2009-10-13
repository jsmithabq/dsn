package dsn;

import yaak.agent.Agent;
import yaak.agent.AgentContext;
import yaak.agent.AgentException;
import yaak.agent.AgentSystem;
import yaak.agent.behavior.AgentBehavior;
import yaak.core.YaakSystem;
import yaak.model.SimAction;
import yaak.model.World;
import yaak.model.service.ServiceArena;
import yaak.model.service.ServiceArenaFactory;
import yaak.util.StringUtil;

import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Vector;

/**
* <p><code>SensorSim</code> implements a simple simulation world for
* a distributed sensor network.  The simulation, <i>but not the
* implementation and core frameworks</i>, is modeled after
* <a href="http://dis.cs.umass.edu/~bhorling/papers/00-49/">SPT</a>.
* This implementation uses the Yaak agent framework for distributed
* agent services.</p>
* <p>Sectors are organized as rows and columns (row-major layout).
* Sectors are equal-sized partitions of a larger geographic area;
* the latter may be rectangular.  Coordinates (positions) within
* the sectors and/or grid are based on (0,0) as the southwest corner.</p>
* @author Jerry Smith
* @version $Id: SensorSim.java 16 2006-02-13 19:29:48Z jsmith $
*/

public class SensorSim extends World {
  /**
  * <p>The minimum number of sensors per sector.</p>
  */
  public static final int MIN_SENSORS = 10;
  /**
  * <p>The maximum number of sensors per sector.</p>
  */
  public static final int MAX_SENSORS = 50;
  private int sectorDimension = Configuration.getSectorDimension();
  private int numRows = Configuration.getSectorRows();
  private int numColumns = Configuration.getSectorColumns();
  private int numSectors = numRows * numColumns;
  private int numSensors = Configuration.getNumberSensorsPerSector();
  private ServiceArena arena;
  private TrackingManager trackingManager;
  private DataFuser dataFuser;
  private Target target;
  private AgentContext mainContext;
  private AgentContext[] sectorContexts;
  private Sector[] sectors;
  private SectorManager[] sectorManagers;

  /**
  * <p>Creates a sensor simulation environment with the default/minimum
  * number of sensors per sector.</p>
  */

  public SensorSim() {
    this(Configuration.getNumberSensorsPerSector());
  }

  /**
  * <p>Creates a sensor simulation environment with the specified
  * number of sensors per sector.</p>
  * @param numSensors The number of sensors per sector.
  */

  public SensorSim(int numSensors) {
    this.numSensors = numSensors;
    if (numSensors > MAX_SENSORS || numSensors < MIN_SENSORS) {
      this.numSensors = (MIN_SENSORS + MAX_SENSORS) / 2;
    }
    init();
    initWorldAndRegistrations();
    createSectorsAndSensors();
    createSectorAndSensorAgents();
  }

  private void init() {
    DSNSystem.getLogger().fine("Initializing DSN simulation...");
    sectors = new Sector[numSectors];
    sectorManagers = new SectorManager[numSectors];
  }

  /**
  * <p>Provides access to the environment's (geographic area's)
  * width.</p>
  * @return The environment's width.
  */

  public int getWidth() {
    return sectors[0].getWidth() * numColumns;
  }

  /**
  * <p>Provides access to the environment's (geographic area's)
  * height.</p>
  * @return The environment's height.
  */

  public int getHeight() {
    return sectors[0].getHeight() * numRows;
  }

  /**
  * <p>Provides access to the environment's target.</p>
  * @return The target.
  */

  public Target getTarget() {
    return target;
  }

  /**
  * <p>Provides access to the environment's target behavior
  * (classname only).</p>
  * @return The target behavior classname.
  */

  public String getTargetBehavior() {
    return target.getAgentBehavior().getClass().getName();
  }

  /**
  * <p>Sets the target behavior object, instantiated from the specified
  * classname.  Note that the target-behavior class must have a
  * constructor with the following signature:</p>
  * <pre>
  *   SpecialTargetBehavior stb = new SpecialTargetBehavior(Target target);
  * </pre>
  * @param classname The classname for the target behavior.
  */

  public void setTargetBehavior(String classname) {
    AgentBehavior behavior = null;
    try {
      behavior = instantiateTargetBehavior(classname);
      if (behavior == null) {
        DSNSystem.getLogger().warning(
          "cannot get/set target behavior (null).");
        return;
      }
    }
    catch (RuntimeException e) {
      throw new DSNRuntimeException("Cannot get target behavior", e);
    }
    try {
      target.setAgentBehavior(behavior);
      DSNSystem.getLogger().fine(
        "setting target behavior to: " + classname + ".");
    }
    catch (Exception e) {
      throw new DSNRuntimeException("Cannot set target behavior", e);
    }
  }

  /**
  * <p>Gets the target signature used by this sensor environment.</p>
  * @return The target's signature.
  */

  public TargetSignature getTargetSignature() {
    return new DefaultTargetSignature(
      target.getAgentID().toString() +
      DefaultTargetSignature.DELIMITER +
      target.position.toString());
  }

  /**
  * <p>Provides access to the tracking manager.</p>
  * @return The tracking manager.
  */

  public TrackingManager getTrackingManager() {
    return trackingManager;
  }

  /**
  * <p>Provides access to the environment's sectors.</p>
  * @return The array of sectors.
  */

  public Sector[] getSectors() {
    return sectors;
  }

  /**
  * <p>Determines whether or not a position is within the sensor grid.</p>
  * @param position The grid coordinate.
  * @return Whether or not the position is inside the sensor grid.
  */

  public boolean isWithinGrid(Position position) {
    return position.getX() < getWidth() && position.getY() < getHeight();
  }

  /**
  * <p>Determines whether or not the target is within the sensor grid.</p>
  * @param position The grid coordinate.
  * @return Whether or not the position is inside the sensor grid.
  */

  public boolean isTargetWithinGrid(Position position) {
/*
    //
    // This approach minimizes/optimizes calculations:
    //
    int x = position.getX();
    int y = position.getY();
    int htw = target.getEntityDimension().width / 2;
    int hth = target.getEntityDimension().height / 2;
    return
      ((x - htw) < getWidth()) &&
      ((x + htw) > 0) &&
      ((y - hth) < getHeight()) &&
      ((y + hth) > 0)
      ;
*/
/**/
    //
    // This approach isolates certain calculations, at the expense
    // of not sharing the dimension and position calculations:
    //
    return
      !isBeyondEastBoundary(position) &&
      !isBeyondWestBoundary(position) &&
      !isBeyondNorthBoundary(position) &&
      !isBeyondSouthBoundary(position)
      ;
/**/
  }

  /**
  * <p>Determines whether or not the target is beyond the west boundary.</p>
  * @param position The grid coordinate.
  * @return Whether or not the position is beyond the boundary.
  */

  public boolean isBeyondWestBoundary(Position position) {
    int x = position.getX();
//    int y = position.getY();
    int htw = target.getEntityDimension().width / 2;
//    int hth = target.getEntityDimension().height / 2;
    return !((x + htw) > 0);
  }

  /**
  * <p>Determines whether or not the target is beyond the east boundary.</p>
  * @param position The grid coordinate.
  * @return Whether or not the position is beyond the boundary.
  */

  public boolean isBeyondEastBoundary(Position position) {
    int x = position.getX();
//    int y = position.getY();
    int htw = target.getEntityDimension().width / 2;
//    int hth = target.getEntityDimension().height / 2;
    return !((x - htw) < getWidth());
  }

  /**
  * <p>Determines whether or not the target is beyond the north boundary.</p>
  * @param position The grid coordinate.
  * @return Whether or not the position is beyond the boundary.
  */

  public boolean isBeyondNorthBoundary(Position position) {
//    int x = position.getX();
    int y = position.getY();
//    int htw = target.getEntityDimension().width / 2;
    int hth = target.getEntityDimension().height / 2;
    return !((y - hth) < getHeight());
  }

  /**
  * <p>Determines whether or not the target is beyond the south boundary.</p>
  * @param position The grid coordinate.
  * @return Whether or not the position is beyond the boundary.
  */

  public boolean isBeyondSouthBoundary(Position position) {
//    int x = position.getX();
    int y = position.getY();
//    int htw = target.getEntityDimension().width / 2;
    int hth = target.getEntityDimension().height / 2;
    return !((y + hth) > 0);
  }

  /**
  * <p>Gets the row of the sector within the grid.  (The grid has row-major
  * organization).</p>
  * @param index The specified sector index.
  * @return The grid row.
  */

  public int getRow(int index) {
    return index / numColumns;
  }

  /**
  * <p>Gets the row of the sector within the grid.  (The grid has row-major
  * organization).</p>
  * @param index The specified sector index.
  * @return The grid row.
  */

  public int getColumn(int index) {
    return index % numColumns;
  }

  /**
  * <p>Activates the simulation environment.</p>
  */

  public void activate() {
    super.activate();
    activateSensors();
  }

  /**
  * <p>Deactivates the simulation environment.</p>
  */

  public void deactivate() {
    deactivateSensors(); // order not important; can be first
    for (int i = 0; i < numSectors; i++) {
      sectorContexts[i].dispose();
    }
    super.deactivate();
  }

  private void activateSensors() {
    for (int i = 0; i < numSectors; i++) {
      Enumeration enum = sectors[i].getSensors();
      for (int j = 0; enum.hasMoreElements(); j++) {
        ((Sensor) enum.nextElement()).start();
      }
    }
  }

  private void deactivateSensors() {
    MulticastTransmitter transmitter = new MulticastTransmitter();
    transmitter.emit(MulticastReceiver.DEFAULT_STOP_SIGNAL);
    transmitter.stop();
/*************** handled by transmitting stop signal ***************
    for (int i = 0; i < numSectors; i++) {
      Enumeration enum = sectors[i].getSensors();
      for (int j = 0; enum.hasMoreElements(); j++) {
        ((Sensor) enum.nextElement()).stop();
      }
    }
*************** handled by transmitting stop signal ***************/
  }

  private void initWorldAndRegistrations() {
    arena = initServices("MainContext", "ServiceArena");
    mainContext = getAgentContext();
    try {
      trackingManager = (TrackingManager) mainContext.createAgent(
        TrackingManager.class, TrackingManager.DEFAULT_NAME);
      dataFuser = (DataFuser) mainContext.createAgent(
        DataFuser.class, DataFuser.DEFAULT_NAME);
      target = (Target) mainContext.createAgent(
        Target.class, this, "SittingDuck");
/******************************
      TargetBehavior tb = new TargetBehavior();
      target.setAgentBehavior(tb);
******************************/
      target.setAgentBehavior(getDefaultTargetBehavior());
    }
    catch (AgentException e) {
      DSNSystem.getLogger().warning("exception creating main agents.");
      e.printStackTrace();
    }
  }

  private AgentBehavior getDefaultTargetBehavior() {
    return instantiateTargetBehavior(
      Configuration.getTargetBehaviorClassname());
  }

  private AgentBehavior instantiateTargetBehavior(String classname) {
//    if (!StringUtil.hasValue(classname)) {
//      return null;
//    }
    try {
      Class c = Class.forName(classname);
      Class params[] = {Target.class};
      Constructor ctor = c.getConstructor(params);
      Object args[] = {target};
      return (AgentBehavior) ctor.newInstance(args);
    }
    catch (ClassNotFoundException e) {
      DSNSystem.getLogger().warning(
        "Cannot access target behavior: '" + classname + "'.");
      DSNSystem.getLogger().warning(
        "Are all implementation-related files available?");
      throw new DSNRuntimeException(
        "Cannot access target behavior", e);
    }
    catch (Exception e) {
      DSNSystem.getLogger().warning(
        "Cannot instantiate target behavior: '" + classname + "'.");
      throw new DSNRuntimeException(
        "Cannot instantiate target behavior", e);
    }
  }

  //
  // Application characteristic:  all sectors are square, but the
  // number of sectors per grid row and/or column can be rectangular.
  // Also, all sectors are the same size.
  // Note:  sector sizes are adjusted based on minimal size restrictions.
  //

  private void createSectorsAndSensors() {
    for (int i = 0; i < numSectors; i++) {
      sectors[i] =
        new Sector("Sector" + i, sectorDimension, sectorDimension);
      int x = getColumn(i) * sectors[i].getWidth();
      int y = getRow(i) * sectors[i].getHeight();
      sectors[i].setReferencePosition(new Position(x, y));
//      System.out.println("Sector = " + i + ", Reference Position = " +
//        sectors[i].getReferencePosition() + ".");
      for (int j = 0; j < numSensors; j++) {
        sectors[i].addSensor(new Sensor(sectors[i]));
      }
      SensorDistributor.distributeRandomly(sectors[i]);
    }
  }

  private void createSectorAndSensorAgents() {
    sectorContexts = new AgentContext[numSectors];
    for (int i = 0; i < numSectors; i++) {
      String sectorName = sectors[i].getName();
      sectorContexts[i] = AgentSystem.createAgentContext(sectorName);
      try {
        sectorManagers[i] = (SectorManager) sectorContexts[i].createAgent(
          SectorManager.class, "SectorManager" + i);
        sectorManagers[i].setSector(sectors[i]);
      }
      catch (AgentException e) {
        DSNSystem.getLogger().warning("exception creating sector managers.");
        e.printStackTrace();
      }
      Enumeration enum = sectors[i].getSensors();
      for (int j = 0; enum.hasMoreElements(); j++) {
        Sensor aSensor = (Sensor) enum.nextElement();
        try {
          SensorMonitor agent = (SensorMonitor) sectorContexts[i].createAgent(
            SensorMonitor.class, "Agent" + j + "@" + sectorName);
          aSensor.addSensorListener(agent);
        }
        catch (AgentException e) {
          DSNSystem.getLogger().warning(
            "exception creating sensor monitors.");
          e.printStackTrace();
        }
      }
    }
  }

  //
  // Sectors are equal-sized and square, but environments
  // may be rectaungular.
  //

  private int getSectorFromVirtualCoordinate(int x, int y) {
    if (x >= getWidth()) {
      return -1;
    }
    if (y >= getHeight()) {
      return -1;
    }
    int rowPos = y / sectors[0].getHeight();
    int colPos = x / sectors[0].getWidth();
    return (rowPos * numColumns) + colPos;
  }

  /**
  * <p>Not for public consumption.</p>
  */

  public static void main(String[] args) {
    SensorSim sensorSim = new SensorSim();
    DistributedSensorExecutive exec = new DistributedSensorExecutive();
    exec.setWorld(sensorSim);
    exec.startOperations();
  }
}
