package dsn;

import java.util.Vector;

import yaak.agent.behavior.AgentBehavior;
import yaak.core.YaakSystem;
import yaak.model.ModelExecutive;
import yaak.model.SimAction;
import yaak.sim.schedule.Action;
import yaak.sim.schedule.ActionScheduler;

/**
* <p><code>DistributedSensorExecutive</code> implements a simple simulation
* of a <code>SensorSim</code> distributed sensor network.  The simulation,
* <em>but not the implementation and core frameworks</em>, is modeled after
* <a href="http://dis.cs.umass.edu/~bhorling/papers/00-49/">SPT</a>.
* This implementation uses the Yaak agent framework for distributed
* agent services.</p>
* @author Jerry Smith
* @version $Id: DistributedSensorExecutive.java 16 2006-02-13 19:29:48Z jsmith $
*/

public class DistributedSensorExecutive extends ModelExecutive {
  private ActionScheduler targetScheduler = new ActionScheduler();
  private Target target;
  private boolean hasBeenStarted = false;

  static {
    YaakSystem.initFramework();
    DSNSystem.initSystem();
  }

  /**
  * <p>Creates a simulation run.</p>
  */

  public DistributedSensorExecutive() {
  }

  /**
  * <p>Runs the operations.</p>
  */

  public void startOperations() {
    if (hasBeenStarted) {
      return;
    }
    hasBeenStarted = true;
    target = ((SensorSim) world).getTarget();
    targetScheduler.addAction(new SimAction(target) {
      public void executeAction() {
        agent.run();
      }
    }, 1);
    activateSensorWorld();
    targetScheduler.start();
  }

  /**
  * <p>Stop the operations.</p>
  */

  public void stopOperations() {
    if (!hasBeenStarted) {
      return;
    }
    //
    // Stop current target operations:
    //
    TargetBehavior targetBehavior =
      (TargetBehavior) target.getAgentBehavior();
    targetBehavior.interrupt();
    //
    // Stop scheduler (and other actions, if any):
    //
    targetScheduler.stop();
    deactivateSensorWorld();
  }

  private void activateSensorWorld() {
    ((SensorSim) world).activate();
  }

  private void deactivateSensorWorld() {
    ((SensorSim) world).deactivate();
  }
}
