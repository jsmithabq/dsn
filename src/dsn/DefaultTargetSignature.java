package dsn;

import yaak.util.StringUtil;

/**
* <p><code>DefaultTargetSignature</code> provides a signature for
* extracting position information.</p>
* @author Jerry Smith
* @version $Id: DefaultTargetSignature.java 16 2006-02-13 19:29:48Z jsmith $
*/

public class DefaultTargetSignature extends TargetSignature {
  public static char DELIMITER = ':';

  /**
  * <p>Creates a target signature.</p>
  * @param signature The string content for the signature.
  */

  public DefaultTargetSignature(String signature) {
    super(signature);
  }

  /**
  * <p>Retrieves the position, if encoded.</p>
  * @param signature The signature to be parsed for positional data.
  * @return The position, or <code>null</code> if not encoded.
  */

  public static Position getPosition(String signature) {
    return parsePosition(signature);
  }

  /**
  * <p>Retrieves the position, if encoded.</p>
  * @return The position, or <code>null</code> if not encoded.
  */

  public Position getPosition() {
    return parsePosition(signature);
  }

  private static Position parsePosition(String signature) {
    if (!StringUtil.hasValue(signature)) {
      return null;
    }
    int ndx = signature.indexOf(DELIMITER);
    if (ndx == -1) {
      return null;
    }
    return new Position(signature.substring(ndx + 1));
  }
}
