package dsn;

import yaak.agent.behavior.AgentBehavior;
import yaak.agent.communication.Message;
import yaak.util.Util;

/**
* <p><code>BilliardBall</code> implements target trekking
* operations.</p>
* @author Jerry Smith
* @version $Id: BilliardBall.java 16 2006-02-13 19:29:48Z jsmith $
*/

public class BilliardBall extends TargetBehavior {
  private MulticastTransmitter transmitter;
  private boolean interrupted = false;
  //
  // To simplify movement-oriented expressions, it is (deemed)
  // sufficient to capture (reset) the simulation environment's
  // width and height at the beginning of each execution phase.
  //
  private int simWidth, simHeight;
  private int targetWidth, targetHeight;

  /**
  * <p>Creates a target behavior.</p>
  * @param target The target.
  */

  public BilliardBall(Target target) {
    super(target);
    transmitter = new MulticastTransmitter();
  }

  /**
  * <p>Executes the specific target behavior.</p>
  */

  public void execute() {
    simWidth = target.sensorSim.getWidth();
    simHeight = target.sensorSim.getHeight();
    targetWidth = target.getEntityDimension().width;
    targetHeight = target.getEntityDimension().height;
    MoveManager moveManager =
      new MoveManager(new Position((int) (Math.random() * simWidth), 0));
    while (!interrupted) {
      while (!interrupted &&
          target.sensorSim.isTargetWithinGrid(target.position)) {
        moveManager.moveNext();
        transmitter.emit(
          target.sensorSim.getTargetSignature().getSignature());
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
      moveManager.updateDirection();
      moveManager.moveNext();
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
  // Here, movement is not bound to grid dimensions:
  //
  class MoveManager {
    Mover mover;
    MoveManager(Position startingPosition) {
      target.position = startingPosition;
      updateDirection();
    }
    void moveNext() {
      mover.move();
    }
    void updateDirection() {
      int haphazardDirectionChange =
        1 - (int) (System.currentTimeMillis() % 3);
      int increment = Sector.STEP_SIZE;
      int relativeX = increment, relativeY = increment;
      if (target.sensorSim.isBeyondWestBoundary(target.position)) {
        relativeX = increment;
        relativeY = increment * haphazardDirectionChange;
      }
      else if (target.sensorSim.isBeyondEastBoundary(target.position)) {
        relativeX = -increment;
        relativeY = increment * haphazardDirectionChange;
      }
      else if (target.sensorSim.isBeyondNorthBoundary(target.position)) {
        relativeX = increment * haphazardDirectionChange;
        relativeY = -increment;
      }
      else if (target.sensorSim.isBeyondSouthBoundary(target.position)) {
        relativeX = increment * haphazardDirectionChange;
        relativeY = increment;
      }
      else {
        // ???
      }
      mover = new Mover(new Position(relativeX, relativeY));
    }
    boolean isInNorthHalf(Position position) {
      return position.getY() > (simHeight / 2);
    }
    boolean isInWestHalf(Position position) {
      return position.getX() < (simWidth / 2);
    }
    class Mover {
      Position relativeChange;
      Mover(Position relativeChange) {
        this.relativeChange = relativeChange;
      }
      void move() {
        target.position.augmentPosition(relativeChange);
      }
    }
  }
}
