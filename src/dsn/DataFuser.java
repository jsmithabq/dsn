package dsn;

/**
* <p><code>DataFuser</code> coordinates all sector-level
* data fusion operations.</p>
* @author Jerry Smith
* @version $Id: DataFuser.java 16 2006-02-13 19:29:48Z jsmith $
*/

public class DataFuser extends SensorAgent {
  /**
  * <p>Not for public consumption.</p>
  */

  public DataFuser() {
    super();
  }

  /**
  * <p>The default name for a data fuser.</p>
  */

  public static final String DEFAULT_NAME = "AnonymousDataFuser";

  protected void onCreation(Object args) {
    setAgentName(DEFAULT_NAME);
  }
}
