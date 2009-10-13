package dsn;

/**
* <p><code>DSNRuntimeException</code> extends
* <code>RuntimeException</code>,
* as a base for reporting unchecked, DSN-related exceptions.</p>
* @author Jerry Smith
* @version $Id: DSNRuntimeException.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class DSNRuntimeException extends RuntimeException {
  /**
  * <p>Instantiates an exception.</p>
  */

  public DSNRuntimeException() {
    super();
  }

  /**
  * <p>Instantiates an exception.</p>
  * @param str The specified exception string.
  */

  public DSNRuntimeException(String str) {
    super(str);
  }

  /**
  * <p>Instantiates an exception.</p>
  * @param throwable The nested exception.
  */

  public DSNRuntimeException(Throwable throwable) {
    super(throwable);
  }

  /**
  * <p>Instantiates an exception.</p>
  * @param str The specified exception string.
  * @param throwable The nested exception.
  */

  public DSNRuntimeException(String str, Throwable throwable) {
    super(str, throwable);
  }
}
