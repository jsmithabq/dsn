package dsn;

import yaak.agent.AgentID;

/**
* <p><code>SectorManager</code> coordinates all sector-level operations.</p>
* @author Jerry Smith
* @version $Id: SectorManager.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class SectorManager extends SensorAgent implements MonitorListener {
  private Sector sector;

  /**
  * <p>Not for public consumption.</p>
  */

  public SectorManager() {
    super();
  }

  /**
  * <p>Sets the affiliated sector for future reference.</p>
  * @param sector The managed sector.
  */

  public void setSector(Sector sector) {
    this.sector = sector;
  }

  /**
  * <p>Monitors a movement.</p>
  * @param e The move event.
  */

  public void moved(MoveEvent e) {
    AgentID id = e.getAgentID();
    DSNSystem.getLogger().fine(id + "'s new location: " +
      "x = " + e.getPosition().getX() + ", y = " +
      e.getPosition().getY() + ".");
  }

  /**
  * <p>Monitors a sensing operation.</p>
  * @param e The sensing event.
  */

  public void sensed(SensingEvent e) {
  }

  /**
  * <p>Monitors a sensing operation.</p>
  * @param e The sensing event.
  */

  public void notSensed(SensingEvent e) {
  }
}
