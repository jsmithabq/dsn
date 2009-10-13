package dsn;

import yaak.core.YaakException;
import yaak.core.YaakProperties;
import yaak.core.YaakSystem;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* <p><code>DSNSystem</code> provides core DSN-related services.</p>
* @author Jerry Smith
* @version $Id: DSNSystem.java 16 2006-02-13 19:29:48Z jsmith $
*/

public final class DSNSystem {
  private static final String PREFERENCES_NODE = "/dsn";
  private static final String PROPERTIES_FILE = "dsn.properties";
  private static final String DSN_LOGGING = "dsn.logging";
  private static final String DSN_LOGGING_DEFAULT = "info";
  private static Logger logger = null;
  private static String loggingLevel = null;
  private static boolean  propertiesLoaded = false;
  private static YaakProperties props = null;

  static { // default privacy
    initSystem();
  }

  private DSNSystem() {
    // nothing
  }

  /**
  * <p>Kicks off centralized initialization operations, including
  * loading properties from <code>dsn.properties</code>.</p>
  */

  public static void initSystem() {
    initProperties();
    setupLogging();
  }

  /**
  * <p>Provides application-level, property look-up services.</p>
  * @param property The property for which to search.
  * @param defaultValue The optional/alternative value.
  * @return The property value.
  * @throws DSNException The properties were not loaded.
  */

  public static String getProperty(String property, String defaultValue)
      throws DSNException {
    String propertyValue = null;
    try {
      propertyValue = props.get(property, defaultValue);
    }
    catch (YaakException e) {
      DSNSystem.getLogger().warning("DSN preferences were not available.");
      throw new DSNException("DSN preferences were not available.");
    }
    return propertyValue;
  }

  private static void initProperties() {
    if (!propertiesLoaded) { // limit to single prop set-up here only
      props = new YaakProperties(PREFERENCES_NODE, PROPERTIES_FILE);
      propertiesLoaded = props.load();
    }
  }

  private static void setupLogging() {
    if (logger != null) {
      return;
    }
    logger = Logger.getLogger(DSNSystem.class.getName());
    try {
      loggingLevel = DSNSystem.getProperty(DSN_LOGGING, DSN_LOGGING_DEFAULT);
      if (loggingLevel != null && loggingLevel.length() > 0) {
        loggingLevel = loggingLevel.toUpperCase();
        try {
          Level newLevel = Level.parse(loggingLevel);
          logger.setLevel(newLevel);
          YaakSystem.getLogger().fine(logger.getName() + 
            " logging level is: " + logger.getLevel());
        }
        catch (IllegalArgumentException e) {
          e.printStackTrace();
          throw new DSNRuntimeException("Logging not available!", e);
        }
      }
    }
    catch (DSNException e) {
      System.err.println("Exception setting up logging!");
      System.out.println("Exception setting up logging!");
      throw new DSNRuntimeException("Logging not available!", e);
    }
  }

  /**
  * <p>Provides access to framework-level logging services.</p>
  * @return The logger.
  */

  public static Logger getLogger() {
    return logger;
  }
}
