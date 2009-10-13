package dsn;

import yaak.util.StringUtil;

import java.awt.Color;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
* <p><code>Configuration</code> manages DSN application configuration
* data.</p>
* @author Jerry Smith
* @version $Id: Configuration.java 16 2006-02-13 19:29:48Z jsmith $
*/

public final class Configuration {
  private static final String DSN_MULTICAST_NET = "dsn.multicast.net";
  private static final String DSN_MULTICAST_PORT = "dsn.multicast.port";
  private static final String DSN_MULTICAST_TTL = "dsn.multicast.ttl";
  private static final String DSN_SECTOR_DIMENSION = "dsn.sector.dimension";
  private static final String DSN_SECTOR_ROWS = "dsn.sector.rows";
  private static final String DSN_SECTOR_COLUMNS = "dsn.sector.columns";
  private static final String DSN_SECTOR_STEP_SIZE = "dsn.sector.stepSize";
  private static final String DSN_SENSORS_PER_SECTOR =
    "dsn.sensors.per.sector";
  private static final String DSN_SENSOR_RANGE = "dsn.sensor.range";
  private static final String DSN_SENSOR_THRESHOLD = "dsn.sensor.range";
  private static final String DSN_TARGET_MOVEMENT_INTERVAL_MS =
    "dsn.target.movement.interval";
  private static final String DSN_TARGET_WIDTH = "dsn.target.width";
  private static final String DSN_TARGET_HEIGHT = "dsn.target.height";
  private static final String DSN_TARGET_BEHAVIOR = "dsn.target.behavior";
  private static final String DSN_VISUAL_TARGET_WIDTH =
    "dsn.visual.target.width";
  private static final String DSN_VISUAL_TARGET_HEIGHT =
    "dsn.visual.target.height";
  private static final String DSN_VISUAL_SENSOR_WIDTH =
    "dsn.visual.sensor.width";
  private static final String DSN_VISUAL_SENSOR_HEIGHT =
    "dsn.visual.sensor.height";
  private static final String DSN_VISUAL_APP_WINDOW_WIDTH =
    "dsn.visual.app.window.width";
  private static final String DSN_VISUAL_APP_WINDOW_HEIGHT =
    "dsn.visual.app.window.height";
  private static final String DSN_VISUAL_GRID_COLOR = "dsn.visual.grid.color";
  private static final String DSN_VISUAL_SENSOR_COLOR =
    "dsn.visual.sensor.color";
  private static final String DSN_VISUAL_ALERTED_SENSOR_COLOR =
    "dsn.visual.alerted.sensor.color";
  private static final String DSN_VISUAL_TARGET_COLOR =
    "dsn.visual.target.color";
  private static final String MULTICAST_NET = "224.0.1.20";
  private static final String MULTICAST_PORT = "8181";
  private static final String MULTICAST_TTL = "0"; // affects cross-channeling
  private static final String SECTOR_DIMENSION = "250";
  private static final String SECTOR_ROWS = "2";
  private static final String SECTOR_COLUMNS = "2";
  private static final String SECTOR_STEP_SIZE = "10";
  private static final String NUMBER_SENSORS_PER_SECTOR =
    "" + SensorSim.MIN_SENSORS;
  private static final String SENSOR_RANGE = "100";
  private static final String SENSOR_THRESHOLD = "10";
  private static final String TARGET_MOVEMENT_INTERVAL_MS = "500";
  private static final String TARGET_WIDTH = "10";
  private static final String TARGET_HEIGHT= "10";
  private static final String TARGET_BEHAVIOR = "dsn.SouthwestToNortheastTrek";
  private static final String VISUAL_TARGET_WIDTH = "10";
  private static final String VISUAL_TARGET_HEIGHT= "10";
  private static final String VISUAL_SENSOR_WIDTH = "10";
  private static final String VISUAL_SENSOR_HEIGHT= "10";
  private static final String VISUAL_APP_WINDOW_WIDTH = "300";
  private static final String VISUAL_APP_WINDOW_HEIGHT= "200";
  private static final String VISUAL_GRID_COLOR = "184,167,138";
  private static final String VISUAL_SENSOR_COLOR = "255,255,0";
  private static final String VISUAL_ALERTED_SENSOR_COLOR = "255,0,0";
  private static final String VISUAL_TARGET_COLOR = "64,64,64";

/*
  private static boolean booleanVariable =
    new Boolean(stringValue).booleanValue();
*/
  private static String multicastNet = MULTICAST_NET;
  private static int multicastPort = new Integer(MULTICAST_PORT).intValue();
  private static int multicastTTL = new Integer(MULTICAST_TTL).intValue();
  private static int sectorDimension =
    new Integer(SECTOR_DIMENSION).intValue();
  private static int sectorRows = new Integer(SECTOR_ROWS).intValue();
  private static int sectorColumns = new Integer(SECTOR_COLUMNS).intValue();
  private static int sectorStepSize =
    new Integer(SECTOR_STEP_SIZE).intValue();
  private static int numberSensorsPerSector =
    new Integer(NUMBER_SENSORS_PER_SECTOR).intValue();
  private static int sensorRange = new Integer(SENSOR_RANGE).intValue();
  private static int sensorThreshold =
    new Integer(SENSOR_THRESHOLD).intValue();
  private static int targetMovementIntervalMS =
    new Integer(TARGET_MOVEMENT_INTERVAL_MS).intValue();
  private static int targetWidth = new Integer(TARGET_WIDTH).intValue();
  private static int targetHeight = new Integer(TARGET_HEIGHT).intValue();
  private static String targetBehaviorClassname = TARGET_BEHAVIOR;
  private static int visualTargetWidth =
    new Integer(VISUAL_TARGET_WIDTH).intValue();
  private static int visualTargetHeight =
    new Integer(VISUAL_TARGET_HEIGHT).intValue();
  private static int visualSensorWidth =
    new Integer(VISUAL_SENSOR_WIDTH).intValue();
  private static int visualSensorHeight =
    new Integer(VISUAL_SENSOR_HEIGHT).intValue();
  private static int appWindowWidth =
    new Integer(VISUAL_APP_WINDOW_WIDTH).intValue();
  private static int appWindowHeight =
    new Integer(VISUAL_APP_WINDOW_HEIGHT).intValue();
  private static Color gridColor = stringToColor(VISUAL_GRID_COLOR);
  private static Color sensorColor = stringToColor(VISUAL_SENSOR_COLOR);
  private static Color alertedSensorColor =
    stringToColor(VISUAL_ALERTED_SENSOR_COLOR);
  private static Color targetColor = stringToColor(VISUAL_TARGET_COLOR);

  static {
    try {
/*
      booleanVariable =
        new Boolean(DSNSystem.getProperty(, )
        ).booleanValue();
*/
      multicastNet = DSNSystem.getProperty(DSN_MULTICAST_NET, MULTICAST_NET);
      multicastPort = new Integer(
        DSNSystem.getProperty(DSN_MULTICAST_PORT, MULTICAST_PORT)
      ).intValue();
      multicastTTL = new Integer(
        DSNSystem.getProperty(DSN_MULTICAST_TTL, MULTICAST_TTL)
      ).intValue();
      sectorDimension = new Integer(
        DSNSystem.getProperty(DSN_SECTOR_DIMENSION, SECTOR_DIMENSION)
      ).intValue();
      sectorRows = new Integer(
        DSNSystem.getProperty(DSN_SECTOR_ROWS, SECTOR_ROWS)
      ).intValue();
      sectorColumns = new Integer(
        DSNSystem.getProperty(DSN_SECTOR_COLUMNS, SECTOR_COLUMNS)
      ).intValue();
      sectorStepSize = new Integer(
        DSNSystem.getProperty(DSN_SECTOR_STEP_SIZE, SECTOR_STEP_SIZE)
      ).intValue();
      numberSensorsPerSector = new Integer(
        DSNSystem.getProperty(DSN_SENSORS_PER_SECTOR,
        NUMBER_SENSORS_PER_SECTOR)
      ).intValue();
      sensorRange = new Integer(
        DSNSystem.getProperty(DSN_SENSOR_RANGE, SENSOR_RANGE)
      ).intValue();
      sensorThreshold = new Integer(
        DSNSystem.getProperty(DSN_SENSOR_THRESHOLD, SENSOR_THRESHOLD)
      ).intValue();
      targetMovementIntervalMS = new Integer(
        DSNSystem.getProperty(DSN_TARGET_MOVEMENT_INTERVAL_MS,
          TARGET_MOVEMENT_INTERVAL_MS)
      ).intValue();
      targetWidth = new Integer(
        DSNSystem.getProperty(DSN_TARGET_WIDTH, TARGET_WIDTH)
      ).intValue();
      targetHeight = new Integer(
        DSNSystem.getProperty(DSN_TARGET_HEIGHT, TARGET_HEIGHT)
      ).intValue();
      targetBehaviorClassname =
        DSNSystem.getProperty(DSN_TARGET_BEHAVIOR, TARGET_BEHAVIOR);
      visualTargetWidth = new Integer(
        DSNSystem.getProperty(DSN_VISUAL_TARGET_WIDTH, VISUAL_TARGET_WIDTH)
      ).intValue();
      visualTargetHeight = new Integer(
        DSNSystem.getProperty(DSN_VISUAL_TARGET_HEIGHT, VISUAL_TARGET_HEIGHT)
      ).intValue();
      visualSensorWidth = new Integer(
        DSNSystem.getProperty(DSN_VISUAL_SENSOR_WIDTH, VISUAL_SENSOR_WIDTH)
      ).intValue();
      visualSensorHeight = new Integer(
        DSNSystem.getProperty(DSN_VISUAL_SENSOR_HEIGHT, VISUAL_SENSOR_HEIGHT)
      ).intValue();
      appWindowWidth = new Integer(
        DSNSystem.getProperty(DSN_VISUAL_APP_WINDOW_WIDTH,
          VISUAL_APP_WINDOW_WIDTH)
      ).intValue();
      appWindowHeight = new Integer(
        DSNSystem.getProperty(DSN_VISUAL_APP_WINDOW_HEIGHT,
          VISUAL_APP_WINDOW_HEIGHT)
      ).intValue();
      gridColor = stringToColor(
        DSNSystem.getProperty(DSN_VISUAL_GRID_COLOR, VISUAL_GRID_COLOR));
      sensorColor = stringToColor(
        DSNSystem.getProperty(DSN_VISUAL_SENSOR_COLOR, VISUAL_SENSOR_COLOR));
      alertedSensorColor = stringToColor(
        DSNSystem.getProperty(DSN_VISUAL_ALERTED_SENSOR_COLOR,
          VISUAL_ALERTED_SENSOR_COLOR));
      targetColor = stringToColor(
        DSNSystem.getProperty(DSN_VISUAL_TARGET_COLOR, VISUAL_TARGET_COLOR));
    }
    catch (DSNException e) {
      DSNSystem.getLogger().warning("cannot load properties: ");
      e.printStackTrace();
    }
  }

  private Configuration() {
  }

  /**
  * <p>Creates a color object from a string with comma-separated
  * RGB values, e.g., "100, 100, 100".</p>
  * @param str The color specification string, e.g., "100, 100, 100".
  * @return The color object.
  */

  public static Color stringToColor(String str) {
    if (!StringUtil.hasValue(str)) {
      return new Color(0, 0, 0);
    }
    StringTokenizer st = new StringTokenizer(str, ", ");
    int r = 0, g = 0, b = 0;
    try {
      r = Integer.parseInt(st.nextToken());
      g = Integer.parseInt(st.nextToken());
      b = Integer.parseInt(st.nextToken());
    }
    catch (NoSuchElementException e) {
      r = g = b = 0;
    }
    catch (NumberFormatException e) {
      r = g = b = 0;
    }
    return new Color(r, g, b);
  }

  /**
  * <p>Determines the multicast network, or group, as a string.</p>
  * @return The multicast network (group).
  */

  public static String getMulticastNet() {
    return multicastNet;
  }

  /**
  * <p>Determines the multicast port.</p>
  * @return The multicast port.
  */

  public static int getMulticastPort() {
    return multicastPort;
  }

  /**
  * <p>Determines the multicast TTL.</p>
  * @return The multicast TTL.
  */

  public static int getMulticastTTL() {
    return multicastTTL;
  }

  /**
  * <p>Determines the sector side dimension--sectors are square.</p>
  * @return The dimension of a sector side.
  */

  public static int getSectorDimension() {
    return sectorDimension;
  }

  /**
  * <p>Determines the number of rows of sectors.</p>
  * @return The row count.
  */

  public static int getSectorRows() {
    return sectorRows;
  }

  /**
  * <p>Determines the number of columns of sectors.</p>
  * @return The column count.
  */

  public static int getSectorColumns() {
    return sectorColumns;
  }

  /**
  * <p>Determines the step size for sector operations, for example,
  * position changes.</p>
  * @return The step size.
  */

  public static int getSectorStepSize() {
    return sectorStepSize;
  }

  /**
  * <p>Determines the number of sensors per sector.</p>
  * @return The column count.
  */

  public static int getNumberSensorsPerSector() {
    return numberSensorsPerSector;
  }

  /**
  * <p>Determines the sensor range.</p>
  * @return The sensor range.
  */

  public static int getSensorRange() {
    return sensorRange;
  }

  /**
  * <p>Determines the sensor threshold.</p>
  * @return The sensor threshold.
  */

  public static int getSensorThreshold() {
    return sensorThreshold;
  }

  /**
  * <p>Determines the target movement interval in milliseconds.</p>
  * @return The target movement interval.
  */

  public static int getTargetMovementInterval() {
    return targetMovementIntervalMS;
  }

  /**
  * <p>Determines the target width.</p>
  * @return The target width.
  */

  public static int getTargetWidth() {
    return targetWidth;
  }

  /**
  * <p>Determines the target height.</p>
  * @return The target height.
  */

  public static int getTargetHeight() {
    return targetHeight;
  }

  /**
  * <p>Determines the target behavior, as a string-specified Java
  * classname.</p>
  * @return The target behavior (class).
  */

  public static String getTargetBehaviorClassname() {
    return targetBehaviorClassname;
  }

  /**
  * <p>Determines the visual target width.</p>
  * @return The visual target width.
  */

  public static int getVisualTargetWidth() {
    return visualTargetWidth;
  }

  /**
  * <p>Determines the visual target height.</p>
  * @return The visual target height.
  */

  public static int getVisualTargetHeight() {
    return visualTargetHeight;
  }

  /**
  * <p>Determines the visual sensor width.</p>
  * @return The visual sensor width.
  */

  public static int getVisualSensorWidth() {
    return visualSensorWidth;
  }

  /**
  * <p>Determines the visual sensor height.</p>
  * @return The visual sensor height.
  */

  public static int getVisualSensorHeight() {
    return visualSensorHeight;
  }

  /**
  * <p>Determines the DSN application window width.</p>
  * @return The window width.
  */

  public static int getAppWindowWidth() {
    return appWindowWidth;
  }

  /**
  * <p>Determines the DSN application window height.</p>
  * @return The window height.
  */

  public static int getAppWindowHeight() {
    return appWindowHeight;
  }

  /**
  * <p>Determines the DSN simulation window grid color.</p>
  * @return The grid color.
  */

  public static Color getGridColor() {
    return gridColor;
  }

  /**
  * <p>Determines the DSN simulation window sensor color.</p>
  * @return The sensor color.
  */

  public static Color getSensorColor() {
    return sensorColor;
  }

  /**
  * <p>Determines the DSN simulation window alerted sensor color.</p>
  * @return The alerted sensor color.
  */

  public static Color getAlertedSensorColor() {
    return alertedSensorColor;
  }

  /**
  * <p>Determines the DSN simulation window target color.</p>
  * @return The target color.
  */

  public static Color getTargetColor() {
    return targetColor;
  }
}
