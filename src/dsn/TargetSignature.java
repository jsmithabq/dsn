package dsn;

/**
* <p><code>TargetSignature</code> provides a base-level signature.</p>
* @author Jerry Smith
* @version $Id: TargetSignature.java 4 2005-06-08 16:58:29Z jsmith $
*/

abstract public class TargetSignature {
  /**
  * <p>The target signature.</p>
  */

  protected String signature = "(signature has not been set)";

  /**
  * <p>Creates a target signature.</p>
  * @param signature The string content for the signature.
  */

  public TargetSignature(String signature) {
    this.signature = signature;
  }

  /**
  * <p>Retrieves the signature.</p>
  * @return The signature.
  */

  public String getSignature() {
    return signature;
  }

  /**
  * <p>Retrieves the position, if encoded.</p>
  * @return The position, or <code>null</code> if not encoded.
  */

  abstract public Position getPosition();
}
