package dsn;

/**
* <p><code>SectorListener</code> prescribes sector-related notifications.</p> 
* @author Jerry Smith
* @version $Id: SectorListener.java 4 2005-06-08 16:58:29Z jsmith $
*/

public interface SectorListener extends java.util.EventListener {
  /**
  * <p>Reports a target entry event.</p>
  * @param e The entry event.
  */

  void targetEntered(TargetEvent e);

  /**
  * <p>Reports a target exit event.</p>
  * @param e The exit event.
  */

  void targetExited(TargetEvent e);
}
