package dsn;

/**
* <p><code>MulticastListener</code> allows an observer to monitor
* transmissions.</p>
*/

public interface MulticastListener extends java.util.EventListener {
  /**
  * <p>Reports a transmission.</p>
  * @param e The transmission event.
  */

  void received(MulticastEvent e);
}
