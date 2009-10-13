package dsn;

import java.util.Enumeration;

/**
* <p><code>SensorDistributor</code> provides functionality for
* distributing sensors within a sector.</p>
* @author Jerry Smith
* @version $Id: SensorDistributor.java 4 2005-06-08 16:58:29Z jsmith $
*/

class SensorDistributor {
  //
  // Sensors are distributed (their (x,y) coordinates are set) in
  // a random fashion.  No two sensors can have the same location,
  // but there is no preceived need to accommodate sensor size in
  // layout; hence, sensors can be (mostly) overlapping.
  //

  static void distributeRandomly(Sector sector) {
    int numSensors = sector.getSensorCount();
    int width = sector.getWidth() - 1;
    int height = sector.getHeight() - 1;
    Position[] position = new Position[numSensors];
    Enumeration enum = sector.getSensors();
    for (int i = 0; enum.hasMoreElements(); i++) {
      Sensor sensor = (Sensor) enum.nextElement();
      position[i] = new Position(
        getRandomIntegerInRange(width),
        getRandomIntegerInRange(height));
      if (i > 0) {
        boolean duplicate = false;
        do {
          for (int j = 0; j < i; j++) {
            if (position[i].equals(position[j])) {
              duplicate = true;
            }
          }
          if (duplicate) {
            position[i].setX(getRandomIntegerInRange(width));
            position[i].setY(getRandomIntegerInRange(height));
          }
        } while (duplicate);
      }
      sensor.setPosition(position[i]);
    }
  }

  static int getRandomIntegerInRange(int value) {
    return (int) (Math.random() * value);
  }
}
