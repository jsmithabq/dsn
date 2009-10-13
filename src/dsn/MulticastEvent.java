package dsn;

import java.util.EventObject;

import yaak.agent.AgentID;

/**
* <p><code>MulticastEvent</code> describes sensor-related escapades.</p> 
* @author Jerry Smith
* @version $Id: MulticastEvent.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class MulticastEvent extends EventObject {
  private String data;

  MulticastEvent(Object source) { // default privacy
    super(source);
  }

  MulticastEvent(Object source, String data) { // default privacy
    super(source);
    this.data = data;
  }

  /**
  * <p>Retrieves the data.</p>
  * @return The data.
  */

  public String getData() {
    return data;
  }

  void setData(String data) { // default privacy
    this.data = data;
  }
}
