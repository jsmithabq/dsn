package dsn;

/**
* <p><code>TargetListener</code> prescribes target-related notifications.</p> 
* @author Jerry Smith
* @version $Id: TargetListener.java 4 2005-06-08 16:58:29Z jsmith $
*/

public interface TargetListener extends java.util.EventListener {
  /**
  * <p>Reports a movement.</p>
  * @param e The move event.
  */

  void moved(MoveEvent e);
}
