package dsn;

import yaak.agent.behavior.AgentBehavior;

/**
* <p><code>TargetBehavior</code> implements target trekking operations.</p>
* @author Jerry Smith
* @version $Id: TargetBehavior.java 4 2005-06-08 16:58:29Z jsmith $
*/

abstract public class TargetBehavior implements AgentBehavior {
  /**
  * <p>The target that exhibits this behavior.</p>
  */

  protected Target target;

  /**
  * <p>Creates a target behavior.</p>
  * @param target The target.
  */

  public TargetBehavior(Target target) {
    this.target = target;
  }

  /**
  * <p>Executes the specific target behavior.</p>
  */

  abstract public void execute();

  /**
  * <p>Interrupts the specific target behavior.</p>
  */

  abstract public void interrupt();
}
