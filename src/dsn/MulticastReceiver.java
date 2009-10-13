package dsn;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.io.IOException;

/**
* <p><code>MulticastReceiver</code> implements a simple "Internet
* receiver," based on network broadcasting, not RF.</p> 
* @author Jerry Smith
* @version $Id: MulticastReceiver.java 16 2006-02-13 19:29:48Z jsmith $
*/

public class MulticastReceiver implements Runnable {
  /**
  * <p>The default signal that, upon receipt, stops the receiver.
  * The termination string should be fairly unique to prevent accidental
  * stoppages.</p>
  */
  
  public static final String DEFAULT_STOP_SIGNAL = "!!!whoa!!!";
  private static final int WAIT_INTERVAL = 100; // milliseconds
  private MulticastListener listener; // only one
  private MulticastSocket ms;
  private InetAddress group;
  private String id;
  private boolean terminated = false;
  private String stopSignal = DEFAULT_STOP_SIGNAL;

  /**
  * <p>Creates a receiver.</p>
  */

  public MulticastReceiver() {
    this("Receiver" + ":" + Configuration.getMulticastNet() + ":" +
      Configuration.getMulticastPort());
  }

  /**
  * <p>Creates a receiver, allowing an application-specific ID.</p>
  * @param id The ID.
  */

  public MulticastReceiver(String id) {
    try {
      ms = new MulticastSocket(Configuration.getMulticastPort());
      group = InetAddress.getByName(Configuration.getMulticastNet());
      ms.joinGroup(group);
    }
    catch (IOException e) {
      DSNSystem.getLogger().warning(
        "exception setting up multicast receiver.");
      e.printStackTrace();
    }
  }

  /**
  * <p>Retrieves the application-assigned ID for this receiver.</p>
  * @return The receiver ID.
  */

  public String getID() {
    return id;
  }

  /**
  * <p>Retrieves the special signal which, upon receipt, stops
  * a running <code>MulticastReceiver</code> instance.  The termination
  * string should be fairly unique to prevent accidental stoppages.</p>
  * @return The stop signal.
  */

  public String getStopSignal() {
    return stopSignal;
  }

  /**
  * <p>Sets the special signal which, upon receipt, stops
  * a running <code>MulticastReceiver</code> instance.  The termination
  * string should be fairly unique to prevent accidental stoppages.</p>
  * @param stopSignal The stop signal.
  */

  public void setStopSignal(String stopSignal) {
    this.stopSignal = stopSignal;
  }

  /**
  * <p>Starts the receiver (in the current thread).</p>
  */

  public void start() {
    run();
  }

  /**
  * <p>Starts the receiver (in the current thread).</p>
  */

  public void run() {
    byte[] buffer = new byte[8192];
    try {
      while (!terminated) {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        ms.receive(dp);
        String leanData = new String(dp.getData()).trim();
        if (leanData.equals(stopSignal)) {
          terminated = true;
          break;
        }
        notifyTransmission(leanData);
      }
    }
    catch (IOException e) {
      DSNSystem.getLogger().warning("exception setting up receiver.");
      e.printStackTrace();
    }
    close();
  }

  /**
  * <p>Stops (shuts down) the receiver.  Currently, only works prior to
  * the start of the receiver (which blocks).</p>
  */

  public void stop() {
    terminated = true;
    close();
  }

  private void close() {
    if (ms != null) {
      try {
        ms.leaveGroup(group);
        ms.close();
      }
      catch (IOException e) {
//        System.out.println("IOException closing multicast socket!");
      } 
    }
  }
  
  /**
  * <p>Notifies the registered listener of a transmission.</p>
  * @param data The transmitted data.
  */

  protected void notifyTransmission(String data) {
    MulticastEvent me = new MulticastEvent(this, data);
    if (listener != null) {
      listener.received(me);
    }
  }

  /**
  * <p>Sets the (one and only) multicast listener.</p>
  * @param listener The multicast listener.
  */

  public synchronized void setMulticastListener(MulticastListener listener) {
    this.listener = listener;
  }

  /**
  * <p>Removes the multicast listener.</p>
  */

  public synchronized void removeMulticastListener() {
    listener = null;
  }
}
