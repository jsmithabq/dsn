package dsn;

import yaak.core.YaakException;

/**
* <p><code>DSNException</code> is the base exception class.</p>
* @author Jerry Smith
* @version $Id: DSNException.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class DSNException extends YaakException {
  /**
  * <p>Instantiates an exception.</p>
  */

  public DSNException() {
    super();
  }

  /**
  * <p>Instantiates an exception.</p>
  * @param str The specified exception string.
  */

  public DSNException(String str) {
    super(str);
  }

  /**
  * <p>Instantiates an exception.</p>
  * @param throwable The nested exception.
  */

  public DSNException(Throwable throwable) {
    super(throwable);
  }

  /**
  * <p>Instantiates an exception.</p>
  * @param str The specified exception string.
  * @param throwable The nested exception.
  */

  public DSNException(String str, Throwable throwable) {
    super(str, throwable);
  }
}
