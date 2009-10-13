package dsn;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import java.util.Enumeration;
import java.util.Hashtable;

import yaak.concurrent.DefaultExecutor;
import yaak.util.Util;

/**
* <p>Implements the application entry point.</p>
*/

public class VisualSimulation {
  private static final String DEFAULT_TITLE = "DSN Simulation";
  private Dimension mainWindowSize =
    new Dimension(Configuration.getAppWindowWidth(),
      Configuration.getAppWindowHeight());
  private Dimension simSize = new Dimension(0, 0);
  private Dimension sensorSize =
    new Dimension(Configuration.getVisualSensorWidth(),
      Configuration.getVisualSensorHeight());
  private Dimension targetSize =
    new Dimension(Configuration.getVisualTargetWidth(),
      Configuration.getVisualTargetHeight());
  private Color gridColor = Configuration.getGridColor();
  private Color sensorColor = Configuration.getSensorColor();
  private Color alertedSensorColor = Configuration.getAlertedSensorColor();
  private Color targetColor = Configuration.getTargetColor();
  private Border sensorBorder = BorderFactory.createEmptyBorder();
  private Border targetBorder = BorderFactory.createEmptyBorder();
  private Cursor stdCursor, waitCursor;
  private NewSimulationAction newSimulationAction =
    new NewSimulationAction("New Simulation");
  private StartSimulationAction startSimulationAction =
    new StartSimulationAction("Start Simulation");
  private StopSimulationAction stopSimulationAction =
    new StopSimulationAction("Stop Simulation");
  private ExitAction exitAction = new ExitAction("Exit");
  private JComboBox targetBehaviorCB;
  private JFrame mainWindow;
  private JFrame simulationWindow;
  private VisualArea visualArea;
  private JButton target = new JButton("");
  private SensorSim sensorSim;
  private DistributedSensorExecutive exec;
  private String currentTargetBehaviorClassname =
    Configuration.getTargetBehaviorClassname();
  private String[][] targetData = new String[][] {
    new String[] {"Billiard Ball", "dsn.BilliardBall"},
    new String[] {"Cockroach", "dsn.Cockroach"},
    new String[] {
      "Southwest To Northeast Trek", "dsn.SouthwestToNortheastTrek"}
  };
  private TargetTable targetTable = new TargetTable(targetData);

  private VisualSimulation() {
    mainWindow = initGUI();
  }

  private JFrame initGUI() {
    JFrame frame = new JFrame(DEFAULT_TITLE);
//    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    Container c = frame.getContentPane();
    frame.setJMenuBar(buildMainWindowMenu());
    c.add(buildMainWindowToolbar(), BorderLayout.NORTH);
    stdCursor = frame.getCursor();
    waitCursor = new Cursor(Cursor.WAIT_CURSOR);
    frame.pack();
    frame.setSize(mainWindowSize);
    frame.setVisible(true);
    startSimulationAction.setEnabled(false);
    stopSimulationAction.setEnabled(false);
    target.setSize(targetSize);
    target.setBorder(targetBorder);
    frame.addWindowListener(new WindowHandler());
    return frame;
  }

  private JMenuBar buildMainWindowMenu() {
    JMenuBar menubar = new JMenuBar();
    JMenu file = new JMenu("File");
    file.add(exitAction);
    menubar.add(file);
    JMenu simulation = new JMenu("Simulation");
    simulation.add(newSimulationAction);
    simulation.add(stopSimulationAction);
    menubar.add(simulation);
/*
    JMenu behavior = new JMenu("Behavior");
    JMenu targetBehavior = new JMenu("Target");
    behavior.add(targetBehavior);
    for (int i = 0; i < targetData.length; i++) {
      TargetBehaviorAction targetBehaviorAction =
        new TargetBehaviorAction(targetData[i][0]);
      targetBehavior.add(targetBehaviorAction);
    }
    menubar.add(behavior);
*/
    return menubar;
  }

  private JToolBar buildMainWindowToolbar() {
    JToolBar toolbar = new JToolBar();
    toolbar.add(newSimulationAction);
    toolbar.add(new JSeparator());
    targetBehaviorCB = new JComboBox();
    toolbar.add(targetBehaviorCB);
    int startupIndex = 0;
    for (int i = 0; i < targetData.length; i++) {
      targetBehaviorCB.addItem(targetData[i][0]);
      if (currentTargetBehaviorClassname.equals(targetData[i][1])) {
        startupIndex = i; // default is in list
      }
    }
    targetBehaviorCB.setSelectedIndex(startupIndex);
    toolbar.add(new JSeparator());
    targetBehaviorCB.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          String behavior = (String) e.getItem();
//          System.out.println("New target behavior: " + behavior + ".");
          currentTargetBehaviorClassname =
            new String(targetTable.getTargetBehaviorClassname(behavior));
//          System.out.println("Current target behavior classname: " +
//            currentTargetBehaviorClassname + ".");
        }
      }
    });
    return toolbar;
  }

  private JToolBar buildSimulationWindowToolbar() {
    JToolBar toolbar = new JToolBar();
    toolbar.add(startSimulationAction);
    toolbar.add(stopSimulationAction);
    return toolbar;
  }

  private JPopupMenu buildSimulationWindowPopup() {
    JPopupMenu popup = new JPopupMenu();
    popup.add(startSimulationAction);
    popup.add(stopSimulationAction);
    return popup;
  }

  private void enableActions() {
    newSimulationAction.setEnabled(true);
    startSimulationAction.setEnabled(true);
    stopSimulationAction.setEnabled(true);
    targetBehaviorCB.setEnabled(true);
  }

  private void disableActions() {
    newSimulationAction.setEnabled(false);
    startSimulationAction.setEnabled(false);
    stopSimulationAction.setEnabled(false);
    targetBehaviorCB.setEnabled(false);
  }

  private void setStdCursor(JFrame frame) {
    frame.setCursor(stdCursor);
  }

  private void setWaitCursor(JFrame frame) {
    frame.setCursor(waitCursor);
  }

  private void newSimulation() {
    sensorSim = new SensorSim();
    String simCurrentTargetBehavior = sensorSim.getTargetBehavior();
    if (!currentTargetBehaviorClassname.equals(simCurrentTargetBehavior)) {
      sensorSim.setTargetBehavior(currentTargetBehaviorClassname);
    }
    simSize.width = sensorSim.getWidth(); // for now, 1:1
    simSize.height = sensorSim.getHeight(); // for now, 1:1
    sensorSim.getTrackingManager().addTargetListener(new TargetListener() {
      public void moved(final MoveEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            Position p = translateGridCoordToSwingCoord(e.getPosition());
            adjustTargetLocation(p.getX(), p.getY());
          }
        });
      }
    });
    simulationWindow = new JFrame("Simulation");
//    simulationWindow.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    simulationWindow.addWindowListener(new WindowHandler());
    Container c = simulationWindow.getContentPane();
//    c.setLayout(null);
    JToolBar toolbar = buildSimulationWindowToolbar();
    c.add(toolbar, BorderLayout.NORTH);
    visualArea = new VisualArea(gridColor);
    JPopupMenu popup = buildSimulationWindowPopup();
    visualArea.add(popup);
    visualArea.addMouseListener(new PopupMouseHandler(popup));
    c.add(visualArea);
/*****
    c.add(new JScrollPane(visualArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
      JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
******/
//    visualArea.setSize(simSize.width, simSize.height);
    adjustTargetLocation(0, simSize.height + targetSize.height); // hide it
    visualArea.add(target);
    target.setBackground(targetColor);
    mapEntities();
    simulationWindow.setSize(visualArea.getMinimumSize());
/****!!!!!!!!!!!!****/
    //
    // Welcome to Kludge City, sponsored by Java/Swing, the world's worst
    // graphical toolkit:
    //
    simulationWindow.pack();
    Insets insets = simulationWindow.getInsets();
    visualArea.setSize(simSize.width, simSize.height);
    simulationWindow.setSize(
      visualArea.getSize().width + insets.left + insets.right,
      visualArea.getSize().height + insets.top + insets.bottom +
      toolbar.getSize().height);
/****!!!!!!!!!!!!*****/
    newSimulationAction.setEnabled(false);
    targetBehaviorCB.setEnabled(false);
    startSimulationAction.setEnabled(true);
    simulationWindow.setVisible(true);
/*
    System.out.println("===== visual width = " + visualArea.getSize().width);
    System.out.println("===== visual height = " + visualArea.getSize().height);
    System.out.println("===== sim win width = " + simulationWindow.getSize().width);
    System.out.println("===== simu win height = " + simulationWindow.getSize().height);
    System.out.println("===== sim grid width = " + simSize.width);
    System.out.println("===== sim grid height = " + simSize.height);
*/
  }

  private void startSimulation() {
    startSimulationAction.setEnabled(false);
    stopSimulationAction.setEnabled(true);
    new DefaultExecutor().execute(new TargetHandler(sensorSim));
//    SwingUtilities.invokeLater(new TargetHandler(sensorSim));
  }

  private void adjustTargetLocation(int x, int y) {
    target.setLocation(x - (targetSize.width / 2),
      y - (targetSize.height / 2));
  }

  private void mapEntities() {
    Sector[] sectors = sensorSim.getSectors();
    for (int i = 0; i < sectors.length; i++) {
      Enumeration enum = sectors[i].getSensors();
      for (int j = 0; enum.hasMoreElements(); j++) {
        Sensor sensor = (Sensor) enum.nextElement();
        SensorButton sb = new SensorButton("");
        sb.setBorder(sensorBorder);
        sb.setSize(sensorSize);
        sb.setBackground(sensorColor);
        visualArea.add(sb);
        sb.reposition(sensor.getRelativePosition(), sensor);
        sensor.addSensorListener(sb);
      }
    }
  }

  private Position translateGridCoordToSwingCoord(Position pos) {
    return new Position(pos.getX(), simSize.height - pos.getY());
  }

  private void closeApplication() {
    System.exit(0);
  }

  private void stopSimulation() {
    setWaitCursor(mainWindow);
    setWaitCursor(simulationWindow);
    if (exec != null) {
      exec.stopOperations();
    }
    else {
      sensorSim.deactivate();
    }
    closeSimulation();
  }

  private void closeSimulation() {
    setStdCursor(mainWindow);
    if (simulationWindow != null) {
      simulationWindow.setVisible(false);
      simulationWindow.dispose();
      simulationWindow = null;
    }
    newSimulationAction.setEnabled(true);
    targetBehaviorCB.setEnabled(true);
    stopSimulationAction.setEnabled(false);
  }

  private class NewSimulationAction extends AbstractAction {
    private NewSimulationAction(String text) {
      super(text);
    }
    public void actionPerformed(ActionEvent e) {
      newSimulation();
    }
  }

  private class StartSimulationAction extends AbstractAction {
    private StartSimulationAction(String text) {
      super(text);
    }
    public void actionPerformed(ActionEvent e) {
      startSimulation();
    }
  }

  private class StopSimulationAction extends AbstractAction {
    private StopSimulationAction(String text) {
      super(text);
    }
    public void actionPerformed(ActionEvent e) {
      stopSimulation();
    }
  }

  private class ExitAction extends AbstractAction {
    private ExitAction(String text) {
      super(text);
    }
    public void actionPerformed(ActionEvent e) {
      closeApplication();
    }
  }

/*
  private class TargetBehaviorAction extends AbstractAction {
    private TargetBehaviorAction(String text) {
      super(text);
    }
    public void actionPerformed(ActionEvent e) {
      String action = e.getActionCommand();
      sensorSim.setTargetBehavior(
        targetTable.getTargetBehaviorClassname(action));
    }
  }
*/

  private class ClearAction extends AbstractAction {
    private ClearAction(String text) {
      super(text);
    }
    public void actionPerformed(ActionEvent e) {
    }
  }

  private class PopupMouseHandler extends MouseAdapter {
    private JPopupMenu popup;
    private PopupMouseHandler(JPopupMenu popup) {
      this.popup = popup;
    }
    public void mousePressed(MouseEvent e) {
      if (e.isPopupTrigger()) {
        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
    public void mouseReleased(MouseEvent e) {
      if (e.isPopupTrigger()) {
        popup.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }

  private class WindowHandler extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
      if (e.getID() != WindowEvent.WINDOW_CLOSING) {
        return;
      }
      if (e.getWindow() == mainWindow) {
        closeApplication();
      }
      else if (e.getWindow() == simulationWindow) {
        stopSimulation();
      }
    }
  }

  private class VisualArea extends JPanel {
    private Sector[] sectors;
    private VisualArea(Color background) {
      super(null);
      sectors = sensorSim.getSectors();
      setBackground(background);
      setSize(getPreferredSize());
    }
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
//      Graphics2D g2 = (Graphics2D) g;
      for (int i = 0; i < sectors.length; i++) {
        Position p =
          translateGridCoordToSwingCoord(sectors[i].getReferencePosition());
        int x = p.getX();
        int y = p.getY();
        if (sensorSim.getColumn(i) != 0) {
          g.drawLine(x, y, x, y - sectors[i].getHeight());
        }
        if (sensorSim.getRow(i) != 0) {
          g.drawLine(x, y, x + sectors[i].getWidth(), y);
        }
      }
/*************
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
      Shape shape1 = new Ellipse2D.Double(40.0, 60.0, 70.0, 70.);
      Shape shape2 = new Rectangle2D.Double(65.0, 35.0, 70.0, 70.0);
      g2.draw(shape1);
      g2.draw(shape2);
*************/
    }
    public Dimension getMinimumSize() {
      return new Dimension(simSize.width, simSize.height);
    }
    public Dimension getMaximumSize() {
      return getMinimumSize();
    }
    public Dimension getPreferredize() {
      return getMaximumSize();
    }
  }

  private class TargetHandler implements Runnable {
    private TargetHandler(SensorSim sensorSim) {
      exec = new DistributedSensorExecutive();
      exec.setWorld(sensorSim);
    }
    public void run() {
      target.setVisible(true);
      exec.startOperations();
    }
  }

  private class SensorButton extends JButton implements SensorListener {
    private SensorButton(String label) {
      super(label);
    }
    public void sensed(SensingEvent e) {
      Sensor sensor = (Sensor) e.getSource();
      setBackground(alertedSensorColor);
    }
    public void notSensed(SensingEvent e) {
      Sensor sensor = (Sensor) e.getSource();
      setBackground(sensorColor);
    }
    public void moved(MoveEvent e) {
      Sensor sensor = (Sensor) e.getSource();
      Position p = sensor.getRelativePosition();
        reposition(p, sensor);
    }
    public void reposition(Position p, CoordinatedEntity sensor) {
      setLocation(
        translateGridCoordToSwingCoord(p).getX() -
          (sensor.getEntityDimension().width / 2),
        translateGridCoordToSwingCoord(p).getY() -
          (sensor.getEntityDimension().height / 2));
    }
  }

  private class TargetTable {
    Hashtable tt = new Hashtable();

    TargetTable(String[][] data) {
      if (data == null) {
        return;
      }
      for (int i = 0; i < data.length; i++) {
//        System.out.println(data[i][0] + " " + data[i][1]);
        tt.put(data[i][0], data[i][1]);
      }
    }
    String getTargetBehaviorClassname(String key) {
      return (String) tt.get(key);
    }
  }

  /**
  * <p>Not for public consumption.</p>
  * <p>No arguments are processed.</p>
  */

  public static void main(String[] args) {
    new VisualSimulation();
  }
}

