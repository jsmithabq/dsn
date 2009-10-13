package dsn;

import yaak.agent.Agent;
import yaak.agent.communication.Message;
import yaak.util.Util;

/**
* <p><code>Target</code> describes a target object that's being
* tracked by the distributed sensor system.</p>
* @author Jerry Smith
* @version $Id: Target.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class Target extends Agent implements CoordinatedEntity {
  /**
  * <p>The default name for a target.</p>
  */
  public static final String DEFAULT_NAME = "AnonymousTarget";
  private EntityDimension dimension =
    new EntityDimension(Configuration.getTargetWidth(),
      Configuration.getTargetHeight());
  protected int movementInterval = Configuration.getTargetMovementInterval();
  protected Position position;
  protected SensorSim sensorSim;

  /**
  * <p>Not for public consumption.</p>
  */

  public Target() {
    super();
  }

  protected void onCreation(Object args) {
    setAgentName(DEFAULT_NAME);
    sensorSim = (SensorSim) args;
  }

  /**
  * <p>Sets the target's position.</p>
  * @param position The position.
  */

  void setPosition(Position position) { // default privacy
    this.position = position;
  }

  /**
  * <p>Retrieves the target's position.</p>
  * @return The position.
  */

  public Position getPosition() {
    return position;
  }

  /**
  * <p>Sets the dimensions of the target.</p>
  * @param dimension The dimensions.
  */

  void setEntityDimension(EntityDimension dimension) { // default privacy
    this.dimension = dimension;
  }

  /**
  * <p>Retrieves the dimensions of the target.</p>
  * @return The dimensions.
  */

  public EntityDimension getEntityDimension() {
    return dimension;
  }

//  /**
//  * <p>Executes the target's rather pedestrian movement.</p>
//  * <p>A target should be scheduled/executed under a separate thread.</p>
//  */
//
//  protected void execute() {
//  }
}
