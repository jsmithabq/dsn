package dsn;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.io.IOException;

/**
* <p><code>MulticastTransmitter</code> implements a simple "Internet
* transmitter," based on network broadcasting, not RF.</p> 
* @author Jerry Smith
* @version $Id: MulticastTransmitter.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class MulticastTransmitter {
  private MulticastSocket ms;
  private InetAddress group;
  private int port = Configuration.getMulticastPort();
  private int configTTL = Configuration.getMulticastTTL();

  /**
  * <p>Creates a transmitter.</p>
  */

  public MulticastTransmitter() {
    try {
      ms = new MulticastSocket();
      group = InetAddress.getByName(Configuration.getMulticastNet());
    }
    catch (IOException e) {
      DSNSystem.getLogger().warning(
        "exception setting up multicast transmitter.");
      e.printStackTrace();
    }
  }

  /**
  * <p>Emits the specified data.</p>
  * @param data The data to emit.
  */

  public void emit(String data) {
//    byte[] bytes = (data + "\r\n").getBytes();
    byte[] bytes = data.getBytes();
    DatagramPacket dp = new DatagramPacket(bytes, bytes.length, group, port);
    try {
      int ttl = ms.getTimeToLive();
      //
      // Note that a TTL > 1 imples "cross channeling from other hosts.
      //
      ms.setTimeToLive(configTTL);
      ms.send(dp);
      ms.setTimeToLive(ttl);
    }
    catch (Exception e) {
      DSNSystem.getLogger().warning("exception transmitting data.");
      e.printStackTrace();
    }
  }

  /**
  * <p>Stops (shuts down) the transmitter.</p>
  */

  synchronized public void stop() {
    close();
  }

  private void close() {
    if (ms != null) {
      ms.close();
    }
  }
}
