package dsn;

import yaak.agent.behavior.AgentBehavior;
import yaak.agent.communication.Message;
import yaak.util.Util;

/**
* <p><code>SouthwestToNortheastTrek</code> implements target trekking
* operations.</p>
* @author Jerry Smith
* @version $Id: SouthwestToNortheastTrek.java 16 2006-02-13 19:29:48Z jsmith $
*/

public class SouthwestToNortheastTrek extends TargetBehavior {
  private MulticastTransmitter transmitter;
  private boolean interrupted = false;

  /**
  * <p>Creates a target behavior.</p>
  * @param target The target.
  */

  public SouthwestToNortheastTrek(Target target) {
    super(target);
    transmitter = new MulticastTransmitter();
  }

  /**
  * <p>Executes the specific target behavior.</p>
  */

  public void execute() {
    setStartPosition();
    while (!interrupted &&
        target.sensorSim.isTargetWithinGrid(target.position)) {
      setNextPosition();
      transmitter.emit(target.sensorSim.getTargetSignature().getSignature());
      try {
        target.publishMessage("",
          new Message(new TargetEmission(
            target.getAgentID().toString(), target.position)));
      }
      catch (Exception e) {
        // gulp, for now
      }
      Util.sleepMilliseconds(target.movementInterval);
    }
  }

  /**
  * <p>Interrupts the target's movement.</p>
  */

  public void interrupt() {
    transmitter.stop();
    interrupted = true;
  }

  //
  // Move upward in a northeasterly direction:
  //

  private void setNextPosition() {
    target.position.incrementX(Sector.STEP_SIZE);
    target.position.incrementY(Sector.STEP_SIZE);
  }

  //
  // Get a start position at the bottom of the grid:
  //

  private void setStartPosition() {
    target.position = new Position(
      (int) (Math.random() * (target.sensorSim.getWidth() / 2)), 0);
  }
}
