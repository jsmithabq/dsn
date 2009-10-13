package dsn.test;

import dsn.MulticastEvent;
import dsn.MulticastListener;
import dsn.MulticastReceiver;
import dsn.MulticastTransmitter;

import yaak.concurrent.DefaultExecutor;
import yaak.util.Util;

import junit.framework.*;

/**
* <p><code>XTestMulticastTransmitAndReceive</code> tests simple
* transmitting and receiving of data via multicast.</p>
* @author Jerry Smith
* @version $Id: XTestMulticastTransmitAndReceive.java 4 2005-06-08 16:58:29Z jsmith $
*/

public class XTestMulticastTransmitAndReceive extends TestCase {
  MulticastReceiver receiver;
  MulticastTransmitter transmitter;

  public XTestMulticastTransmitAndReceive(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(XTestMulticastTransmitAndReceive.class);
  }

  protected void setUp() {
    receiver = new MulticastReceiver();
    receiver.setMulticastListener(new MulticastListener() {
      public void received(MulticastEvent e) {
        System.out.println("Received '" + e.getData() + "'.");
      }
    });
    transmitter = new MulticastTransmitter();
  }

  protected void tearDown() {
    transmitter.stop();
    receiver.stop();
  }

  public void testTransmitAndReceive() {
    new DefaultExecutor().execute(receiver);
    Util.sleepSeconds(1);
    transmitter.emit("Blurp 1");
    Util.sleepSeconds(1);
    transmitter.emit("Blurp 2");
    Util.sleepSeconds(1);
  }
}
