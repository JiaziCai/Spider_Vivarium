/**
 * PA2.java - driver for the hand model simulation 
 * Jiazi Cai jiazi@bu.edu
 * Based on the code of this hand model simulation
 * History:
 * 
 * 19 February 2011
 * 
 * - added documentation
 * 
 * (Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>)
 * 
 * 16 January 2008
 * 
 * - translated from C code by Stan Sclaroff
 * 
 * (Tai-Peng Tian <tiantp@gmail.com>)
 * 
 */


import java.awt.event.KeyEvent; 
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;//for new version of gl
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;//for new version of gl
import com.jogamp.opengl.util.gl2.GLUT;//for new version of gl

/**
 * The main class which drives the hand model simulation.
 * 
 * @author Tai-Peng Tian <tiantp@gmail.com>
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since Spring 2008
 */
public class PA2 extends JFrame implements GLEventListener, KeyListener,
    MouseListener, MouseMotionListener {

  /**
   * A finger which has a palm joint, a middle joint, and a distal joint.
   * 
   * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
   * @since Spring 2011
   */
  private class Leg {
    /** The distal joint of this finger. */
    private final Component legBody;
    /** The list of all the joints in this finger. */
    private final List<Component> leg;
    /** The middle joint of this finger. */
    private final Component middleLeg;
    /** The palm joint of this finger. */
    private final Component distalLeg;

    /**
     * Instantiates this finger with the three specified joints.
     * 
     * @param leg_distal
     *          The palm joint of this finger.
     * @param leg_middle
     *          The middle joint of this finger.
     * @param leg_body
     *          The distal joint of this finger.
     */
    public Leg(final Component leg_distal, final Component leg_middle,
        final Component leg_body) {
      this.distalLeg = leg_distal;
      this.middleLeg = leg_middle;
      this.legBody = leg_body;

      this.leg = Collections.unmodifiableList(Arrays.asList(this.distalLeg,
          this.middleLeg, this.legBody));
    }

    /**
     * Gets the distal joint of this finger.
     * 
     * @return The distal joint of this finger.
     */
    Component legBody() {
      return this.legBody;
    }

    /**
     * Gets an unmodifiable view of the list of the joints of this finger.
     * 
     * @return An unmodifiable view of the list of the joints of this finger.
     */
    List<Component> leg() {
      return this.leg;
    }

    /**
     * Gets the middle joint of this finger.
     * 
     * @return The middle joint of this finger.
     */
    Component middleLeg() {
      return this.middleLeg;
    }

    /**
     * Gets the palm joint of this finger.
     * 
     * @return The palm joint of this finger.
     */
    Component distalLeg() {
      return this.distalLeg;
    }
  }

  /** The color for components which are selected for rotation. */
  public static final FloatColor ACTIVE_COLOR = FloatColor.RED;
  /** The radius of the components which comprise the abdomen. */
  public static final double ABD_RADIUS = 0.53;
  /** The radius of the components which comprise the throat. */
  public static final double THROAT_RADIUS = 0.13;
  /** The default width of the created window. */
  public static final int DEFAULT_WINDOW_HEIGHT = 512;
  /** The default height of the created window. */
  public static final int DEFAULT_WINDOW_WIDTH = 512;
  /** The height of the distal leg on each of the legs. */
  public static final double DISTAL_LEG_HEIGHT = 0.1;
  /** The radius of each legBody which comprises the leg. */
  public static final double LEG_RADIUS = 0.06;
  /** The height of the throat. */
  public static final double THROAT_HEIGHT = 0.1;
  /** The radius of the leg. */
  public static final double Head_RADIUS = 0.35;
  /** The color for components which are not selected for rotation. */
  public static final FloatColor INACTIVE_COLOR = FloatColor.ORANGE;
  /** The initial position of the top level component in the scene. */
  public static final Point3D INITIAL_POSITION = new Point3D(2, 1, 2);
  /** The height of the middle finger on each of the fingers. */
  public static final double MIDDLE_FINGER_HEIGHT = 0.55;
  /** The height of the palm joint on each of the fingers. */
  public static final double BODY_LEG_HEIGHT = 0.45;
  /** The angle by which to rotate the joint on user request to rotate. */
  public static final double ROTATION_ANGLE = 2.0;
  /** Randomly generated serial version UID. */
  private static final long serialVersionUID = -7060944143920496524L;
  /** The height of the upper arm. */
  public static final double ABD_HEIGHT = 1.0;

  /** 
   * Runs the hand simulation in a single JFrame.
   * 
   * @param args
   *          This parameter is ignored.
   */
  public static void main(final String[] args) {
    new PA2().animator.start();
  }

  /**
   * The animator which controls the framerate at which the canvas is animated.
   */
  final FPSAnimator animator;
  /** The canvas on which we draw the scene. */
  private final GLCanvas canvas;
  /** The capabilities of the canvas. */
  private final GLCapabilities capabilities = new GLCapabilities(null);
  /** The fingers on the hand to be modeled. */
  private final Leg[] legs;
  /** The forearm to be modeled. */
  private final Component throat;
  /** The OpenGL utility object. */
  private final GLU glu = new GLU();
  /** The OpenGL utility toolkit object. */
  private final GLUT glut = new GLUT();
  /** The head to be modeled. */
  private final Component head;
  /** The last x and y coordinates of the mouse press. */
  private int last_x = 0, last_y = 0;
  /** Whether the world is being rotated. */
  private boolean rotate_world = false;
  /** The axis around which to rotate the selected joints. */
  private Axis selectedAxis = Axis.X;
  /** The set of components which are currently selected for rotation. */
  private final Set<Component> selectedComponents = new HashSet<Component>(18);
  /**
   * The set of fingers which have been selected for rotation.
   * 
   * Selecting a joint will only affect the joints in this set of selected
   * fingers.
   **/
  private final Set<Leg> selectedFingers = new HashSet<Leg>(5);
  /** Whether the state of the model has been changed. */
  private boolean stateChanged = true;
  /**
   * The top level component in the scene which controls the positioning and
   * rotation of everything in the scene.
   */
  private final Component topLevelComponent;
  /** The abdomen to be modeled. */
  private final Component abdomen;
  /** The quaternion which controls the rotation of the world. */
  private Quaternion viewing_quaternion = new Quaternion();
  /** The set of all components. */
  private final List<Component> components;

  public static String LEG_BODY_ONE = "one body";
  public static String LEG_MIDDLE_ONE = "one middle";
  public static String LEG_DISTAL_ONE = "one distal";
  public static String LEG_BODY_TWO = "two body";
  public static String LEG_MIDDLE_TWO = "two middle";
  public static String LEG_DISTAL_TWO = "two distal";
  public static String LEG_BODY_THREE = "three body";
  public static String LEG_MIDDLE_THREE = "three middle";
  public static String LEG_DISTAL_THREE = "three distal";
  public static String LEG_BODY_FOUR = "four body";
  public static String LEG_MIDDLE_FOUR = "four middle";
  public static String LEG_DISTAL_FOUR = "four distal";
  public static String LEG_BODY_FIVE = "five body";
  public static String LEG_MIDDLE_FIVE = "five middle";
  public static String LEG_DISTAL_FIVE = "five distal";
  public static String LEG_BODY_SIX = "six body";
  public static String LEG_MIDDLE_SIX = "six distal";
  public static String LEG_DISTAL_SIX = "six distal";
  public static String LEG_BODY_SEVEN = "seven body";
  public static String LEG_MIDDLE_SEVEN = "seven distal";
  public static String LEG_DISTAL_SEVEN = "seven distal";
  public static String LEG_BODY_EIGHT = "eight body";
  public static String LEG_MIDDLE_EIGHT = "eight distal";
  public static String LEG_DISTAL_EIGHT = "eight distal";
  //public static String
  //onhodle for more
  public static String HEAD_NAME = "hand";
  public static String THROAT_NAME = "forearm";
  public static String ABDOMEN_NAME = "upper arm";
  public static String TOP_LEVEL_NAME = "top level";

  /**
   * Initializes the necessary OpenGL objects and adds a canvas to this JFrame.
   */
  public PA2() {
    this.capabilities.setDoubleBuffered(true);

    this.canvas = new GLCanvas(this.capabilities);
    this.canvas.addGLEventListener(this);
    this.canvas.addMouseListener(this);
    this.canvas.addMouseMotionListener(this);
    this.canvas.addKeyListener(this);
    // this is true by default, but we just add this line to be explicit
    this.canvas.setAutoSwapBufferMode(true);
    this.getContentPane().add(this.canvas);

    // refresh the scene at 60 frames per second
    this.animator = new FPSAnimator(this.canvas, 60);

    this.setTitle("CS480/CS680 : Hand Simulator");
    this.setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);

    // all the distal joints of the legs that related to the abdomen
    final Component distal1 = new Component(new Point3D(0, 0,
        MIDDLE_FINGER_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        DISTAL_LEG_HEIGHT, this.glut), LEG_DISTAL_FOUR);
    final Component distal2 = new Component(new Point3D(0, 0,
        MIDDLE_FINGER_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        DISTAL_LEG_HEIGHT, this.glut), LEG_DISTAL_TWO);
    final Component distal3 = new Component(new Point3D(0, 0,
        MIDDLE_FINGER_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        DISTAL_LEG_HEIGHT, this.glut), LEG_DISTAL_THREE);
    final Component distal4 = new Component(new Point3D(0, 0,
        MIDDLE_FINGER_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        DISTAL_LEG_HEIGHT, this.glut), LEG_DISTAL_ONE);
    final Component distal5 = new Component(new Point3D(0, 0,
        MIDDLE_FINGER_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        DISTAL_LEG_HEIGHT, this.glut), LEG_DISTAL_FIVE);
    final Component distal6 = new Component(new Point3D(0, 0,
        MIDDLE_FINGER_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        DISTAL_LEG_HEIGHT, this.glut), LEG_DISTAL_SIX);
    final Component distal7 = new Component(new Point3D(0, 0,
            MIDDLE_FINGER_HEIGHT), new RoundedCylinder(LEG_RADIUS,
            DISTAL_LEG_HEIGHT, this.glut), LEG_DISTAL_SIX);
    final Component distal8 = new Component(new Point3D(0, 0,
            MIDDLE_FINGER_HEIGHT), new RoundedCylinder(LEG_RADIUS,
            DISTAL_LEG_HEIGHT, this.glut), LEG_DISTAL_SIX);

    // all the middle joints
    final Component middle1 = new Component(new Point3D(0, 0,
        BODY_LEG_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        MIDDLE_FINGER_HEIGHT, this.glut), LEG_MIDDLE_FOUR);
    final Component middle2 = new Component(new Point3D(0, 0,
        BODY_LEG_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        MIDDLE_FINGER_HEIGHT, this.glut), LEG_MIDDLE_TWO);
    final Component middle3 = new Component(new Point3D(0, 0,
        BODY_LEG_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        MIDDLE_FINGER_HEIGHT, this.glut), LEG_MIDDLE_THREE);
    final Component middle4 = new Component(new Point3D(0, 0,
        BODY_LEG_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        MIDDLE_FINGER_HEIGHT, this.glut), LEG_MIDDLE_ONE);
    final Component middle5 = new Component(new Point3D(0, 0,
        BODY_LEG_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        MIDDLE_FINGER_HEIGHT, this.glut), LEG_MIDDLE_FIVE);
    final Component middle6 = new Component(new Point3D(0, 0,
    	BODY_LEG_HEIGHT), new RoundedCylinder(LEG_RADIUS,
    	MIDDLE_FINGER_HEIGHT, this.glut), LEG_MIDDLE_SIX);
    final Component middle7 = new Component(new Point3D(0, 0,
        	BODY_LEG_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        	MIDDLE_FINGER_HEIGHT, this.glut), LEG_MIDDLE_SIX);
    final Component middle8 = new Component(new Point3D(0, 0,
        	BODY_LEG_HEIGHT), new RoundedCylinder(LEG_RADIUS,
        	MIDDLE_FINGER_HEIGHT, this.glut), LEG_MIDDLE_SIX);
    // all the leg joints, displaced by various amounts from the abdomen
    final Component leg1 = new Component(new Point3D(-.25, 0, 0.75),
        new RoundedCylinder(LEG_RADIUS, BODY_LEG_HEIGHT, this.glut),
        LEG_BODY_FOUR);
    final Component leg2 = new Component(new Point3D(-.1, 0, 0.9),
        new RoundedCylinder(LEG_RADIUS, BODY_LEG_HEIGHT, this.glut),
        LEG_BODY_TWO);
    final Component leg3 = new Component(new Point3D(0.1, 0, 0.9),
        new RoundedCylinder(LEG_RADIUS, BODY_LEG_HEIGHT, this.glut),
        LEG_BODY_THREE);
    final Component leg4 = new Component(new Point3D(0.25, 0, 0.75),
        new RoundedCylinder(LEG_RADIUS, BODY_LEG_HEIGHT, this.glut),
        LEG_BODY_ONE);
    final Component leg5 = new Component(new Point3D(0.28, 0, 0.2),
        new RoundedCylinder(LEG_RADIUS, BODY_LEG_HEIGHT, this.glut),
        LEG_BODY_FIVE);
    final Component leg6 = new Component(new Point3D(-.4, 0, 0.5),
    	new RoundedCylinder(LEG_RADIUS, BODY_LEG_HEIGHT, this.glut),
    	LEG_BODY_SIX);
    final Component leg7 = new Component(new Point3D(-.28, 0, 0.2),
        	new RoundedCylinder(LEG_RADIUS, BODY_LEG_HEIGHT, this.glut),
        	LEG_BODY_SIX);
    final Component leg8 = new Component(new Point3D(0.4, 0, 0.5),
        	new RoundedCylinder(LEG_RADIUS, BODY_LEG_HEIGHT, this.glut),
        	LEG_BODY_SIX);
    // put together the legs for easier selection by keyboard input later on
    this.legs = new Leg[] { 
    	new Leg(leg1, middle1, distal1),
        new Leg(leg2, middle2, distal2),
        new Leg(leg3, middle3, distal3),
        new Leg(leg4, middle4, distal4),
        new Leg(leg5, middle5, distal5), 
        new Leg(leg6, middle6, distal6),
        new Leg(leg7, middle7, distal7),
        new Leg(leg8, middle8, distal8),
    };

    // the hand, which models the throat joint
    this.head = new Component(new Point3D(0, 0, THROAT_HEIGHT), new Palm(
        Head_RADIUS, this.glut), HEAD_NAME);

    // the forearm, which models the elbow joint
    this.throat = new Component(new Point3D(0, 0, ABD_HEIGHT),
        new RoundedCylinder(THROAT_RADIUS, THROAT_HEIGHT, this.glut),
        THROAT_NAME);

    // the upper arm which models the shoulder joint
    this.abdomen = new Component(new Point3D(1, 0, 1), new Palm(
        ABD_RADIUS, this.glut), ABDOMEN_NAME);

    // the top level component which provides an initial position and rotation
    // to the scene (but does not cause anything to be drawn)
    this.topLevelComponent = new Component(INITIAL_POSITION, TOP_LEVEL_NAME);

    this.topLevelComponent.addChild(this.abdomen);
    // the funny bone's connected to the...forearm
    this.abdomen.addChild(this.throat);
    // the forearm's connected to the...wrist bone
    this.throat.addChild(this.head);
    // the wrist bone's connected to the...fingers
    this.abdomen.addChildren(leg1, leg2, leg3, leg4, leg5, leg6, leg7, leg8);
    leg1.addChild(middle1);
    leg2.addChild(middle2);
    leg3.addChild(middle3);
    leg4.addChild(middle4);
    leg5.addChild(middle5);
    leg6.addChild(middle6);
    leg7.addChild(middle7);
    leg8.addChild(middle8);
    middle1.addChild(distal1);
    middle2.addChild(distal2);
    middle3.addChild(distal3);
    middle4.addChild(distal4);
    middle5.addChild(distal5);
    middle6.addChild(distal6);
    middle7.addChild(distal7);
    middle8.addChild(distal8);
    // turn the whole arm to be at an arm-like angle
    this.topLevelComponent.rotate(Axis.Y, 225);
    this.topLevelComponent.rotate(Axis.X, -90);

    // rotate the elbow to be half bent
    this.throat.rotate(Axis.Y, 5);
    
    // rotate the legs so that it is at a leg-like angle
    leg8.rotate(Axis.Y, 60);
    leg8.rotate(Axis.Z, 60);
    leg7.rotate(Axis.Y, -80);
    leg7.rotate(Axis.Z, 60);
    leg6.rotate(Axis.Y, -60);
    leg6.rotate(Axis.Z, 60);
    leg5.rotate(Axis.Y, 80);
    leg5.rotate(Axis.Z, 60);
    leg4.rotate(Axis.Y,55);
    leg4.rotate(Axis.Z, 80);
    leg3.rotate(Axis.Y,50);
    leg3.rotate(Axis.Z, 80);
    leg2.rotate(Axis.Y,-50);
    leg2.rotate(Axis.Z, 60);
    leg1.rotate(Axis.Y,-55);
    leg1.rotate(Axis.Z, 60); 
    middle1.rotate(Axis.Y, 70);
    middle1.rotate(Axis.Z, 185);
    middle2.rotate(Axis.Y, 70);
    middle2.rotate(Axis.Z, 185);
    middle3.rotate(Axis.Y, 70);
    middle3.rotate(Axis.Z, 185);
    middle4.rotate(Axis.Y, 70);
    middle4.rotate(Axis.Z, 185);
    middle5.rotate(Axis.Y, 70);
    middle5.rotate(Axis.Z, 185);
    middle6.rotate(Axis.Y, 70);
    middle6.rotate(Axis.Z, 185);
    middle7.rotate(Axis.Y, 70);
    middle7.rotate(Axis.Z, 185);
    middle8.rotate(Axis.Y, 70);
    middle8.rotate(Axis.X, -30);
    middle8.rotate(Axis.Z, 185);
    middle1.rotate(Axis.X, -30);
    middle2.rotate(Axis.X, -30);
    middle3.rotate(Axis.X, -30);
    middle4.rotate(Axis.X, -30);
    middle5.rotate(Axis.X, -30);
    middle6.rotate(Axis.X, -30);
    middle7.rotate(Axis.X, -30);
    distal1.rotate(Axis.Y, 45);
    distal2.rotate(Axis.Y, 45);
    distal3.rotate(Axis.Y, 45); //1
    distal4.rotate(Axis.Y, 45); //2
    distal5.rotate(Axis.Y, 45); //4
    distal6.rotate(Axis.Y, 45);
    distal7.rotate(Axis.Y, 45);
    distal8.rotate(Axis.Y, 45);  //3
    

    // set rotation limits for the head
    this.head.setXPositiveExtent(30);
    this.head.setXNegativeExtent(-30);
    this.head.setYPositiveExtent(15);
    this.head.setYNegativeExtent(-15);
    this.head.setZPositiveExtent(15);
    this.head.setZNegativeExtent(-15);

    // set rotation limits for the palm joints of the fingers
    for (final Component legJoint : Arrays.asList(leg1, leg2, leg3, leg4,leg5,leg6,leg7,leg8)) {
      legJoint.setXPositiveExtent(25);
      legJoint.setXNegativeExtent(-45);
      legJoint.setYPositiveExtent(80);
      legJoint.setYNegativeExtent(-80);
      legJoint.setZPositiveExtent(80);
      legJoint.setZNegativeExtent(0);
    }


    // set rotation limits for the middle joints of the finger
    for (final Component middleJoint : Arrays.asList(middle1, middle2,
        middle3, middle4, middle5, middle6, middle7, middle8)) {
      middleJoint.setXPositiveExtent(0);
      middleJoint.setXNegativeExtent(-30);
      middleJoint.setYPositiveExtent(290);
      middleJoint.setYNegativeExtent(0);
      middleJoint.setZPositiveExtent(185);
      middleJoint.setZNegativeExtent(0);
    }

    // set rotation limits for the distal joints of the finger
    for (final Component distalJoint : Arrays.asList(distal1, distal2,
        distal3, distal4, distal5, distal6, distal7, distal8)) {
      distalJoint.setXPositiveExtent(20);
      distalJoint.setXNegativeExtent(-20);
      distalJoint.setYPositiveExtent(45);
      distalJoint.setYNegativeExtent(-45);
      distalJoint.setZPositiveExtent(0);
      distalJoint.setZNegativeExtent(0);
    }

    // create the list of all the components for debugging purposes
    this.components = Arrays.asList(leg1, middle1, distal1, leg2, middle2,
        distal2, leg3, middle3, distal3, leg4, middle4, distal4, leg5,
        middle5, distal5, leg6, middle6, distal6, leg7, middle7, distal7, leg8, middle8, distal8, this.head, this.throat);
  }

  /**
   * Redisplays the scene containing the hand model.
   * 
   * @param drawable
   *          The OpenGL drawable object with which to create OpenGL models.
   */
  public void display(final GLAutoDrawable drawable) {
    final GL2 gl = (GL2)drawable.getGL();

    // clear the display
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    // from here on affect the model view
    gl.glMatrixMode(GL2.GL_MODELVIEW);

    // start with the identity matrix initially
    gl.glLoadIdentity();

    // rotate the world by the appropriate rotation quaternion
    gl.glMultMatrixf(this.viewing_quaternion.toMatrix(), 0);

    // update the position of the components which need to be updated
    // TODO only need to update the selected and JUST deselected components
    if (this.stateChanged) {
      this.topLevelComponent.update(gl);
      this.stateChanged = false;
    }

    // redraw the components
    this.topLevelComponent.draw(gl);
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param drawable
   *          This parameter is ignored.
   * @param modeChanged
   *          This parameter is ignored.
   * @param deviceChanged
   *          This parameter is ignored.
   */
  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged) {
    // intentionally unimplemented
  }

  /**
   * Initializes the scene and model.
   * 
   * @param drawable
   *          {@inheritDoc}
   */
  public void init(final GLAutoDrawable drawable) {
    final GL2 gl = (GL2)drawable.getGL();

    // perform any initialization needed by the hand model
    this.topLevelComponent.initialize(gl);

    // initially draw the scene
    this.topLevelComponent.update(gl);

    // set up for shaded display of the hand
    final float light0_position[] = { 1, 1, 1, 0 };
    final float light0_ambient_color[] = { 0.25f, 0.25f, 0.25f, 1 };
    final float light0_diffuse_color[] = { 1, 1, 1, 1 };

    gl.glPolygonMode(GL.GL_FRONT, GL2.GL_FILL);
    gl.glEnable(GL2.GL_COLOR_MATERIAL);
    gl.glColorMaterial(GL.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);

    gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glShadeModel(GL2.GL_SMOOTH);

    // set up the light source
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light0_position, 0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light0_ambient_color, 0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light0_diffuse_color, 0);

    // turn lighting and depth buffering on
    gl.glEnable(GL2.GL_LIGHTING);
    gl.glEnable(GL2.GL_LIGHT0);
    gl.glEnable(GL2.GL_DEPTH_TEST);
    gl.glEnable(GL2.GL_NORMALIZE);
  }

  /**
   * Interprets key presses according to the following scheme:
   * 
   * up-arrow, down-arrow: increase/decrease rotation angle
   * 
   * @param key
   *          The key press event object.
   */
  public void keyPressed(final KeyEvent key) {
    switch (key.getKeyCode()) {
    case KeyEvent.VK_KP_UP:
    case KeyEvent.VK_UP:
      for (final Component component : this.selectedComponents) {
        component.rotate(this.selectedAxis, ROTATION_ANGLE);
      }
      this.stateChanged = true;
      break;
    case KeyEvent.VK_KP_DOWN:
    case KeyEvent.VK_DOWN:
      for (final Component component : this.selectedComponents) {
        component.rotate(this.selectedAxis, -ROTATION_ANGLE);
      }
      this.stateChanged = true;
      break;
    default:
      break;
    }
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param key
   *          This parameter is ignored.
   */
  public void keyReleased(final KeyEvent key) {
    // intentionally unimplemented
  }

  private final TestCases testCases = new TestCases();

  private void setModelState(final Map<String, Angled> state) {
    this.abdomen.setAngles(state.get(ABDOMEN_NAME));
    this.throat.setAngles(state.get(THROAT_NAME));
    this.head.setAngles(state.get(HEAD_NAME));
    this.legs[0].distalLeg().setAngles(state.get(LEG_BODY_FOUR));
    this.legs[0].middleLeg().setAngles(state.get(LEG_MIDDLE_FOUR));
    this.legs[0].legBody().setAngles(state.get(LEG_DISTAL_FOUR));
    this.legs[1].distalLeg().setAngles(state.get(LEG_BODY_TWO));
    this.legs[1].middleLeg().setAngles(state.get(LEG_MIDDLE_TWO));
    this.legs[1].legBody().setAngles(state.get(LEG_DISTAL_TWO));
    this.legs[2].distalLeg().setAngles(state.get(LEG_BODY_THREE));
    this.legs[2].middleLeg().setAngles(state.get(LEG_MIDDLE_THREE));
    this.legs[2].legBody().setAngles(state.get(LEG_DISTAL_THREE));
    this.legs[3].distalLeg().setAngles(state.get(LEG_BODY_ONE));
    this.legs[3].middleLeg().setAngles(state.get(LEG_MIDDLE_ONE));
    this.legs[3].legBody().setAngles(state.get(LEG_DISTAL_ONE));
    this.legs[4].distalLeg().setAngles(state.get(LEG_BODY_FIVE));
    this.legs[4].middleLeg().setAngles(state.get(LEG_MIDDLE_FIVE));
    this.legs[4].legBody().setAngles(state.get(LEG_DISTAL_FIVE));
    this.legs[5].distalLeg().setAngles(state.get(LEG_BODY_SIX));
    this.legs[5].middleLeg().setAngles(state.get(LEG_MIDDLE_SIX));
    this.legs[5].legBody().setAngles(state.get(LEG_DISTAL_SIX));
    this.legs[6].distalLeg().setAngles(state.get(LEG_BODY_SEVEN));
    this.legs[6].middleLeg().setAngles(state.get(LEG_MIDDLE_SEVEN));
    this.legs[6].legBody().setAngles(state.get(LEG_DISTAL_SEVEN));
    this.legs[7].distalLeg().setAngles(state.get(LEG_BODY_EIGHT));
    this.legs[7].middleLeg().setAngles(state.get(LEG_MIDDLE_EIGHT));
    this.legs[7].legBody().setAngles(state.get(LEG_DISTAL_EIGHT));
    this.stateChanged = true;
  }

  /**
   * Interprets typed keys according to the following scheme:
   * 
   * 1 : toggle the first leg active in rotation
   * 
   * 2 : toggle the second leg active in rotation
   * 
   * 3 : toggle the third leg active in rotation
   * 
   * 4 : toggle the fourth leg active in rotation
   * 
   * 5 : toggle the fifth leg active in rotation
   * 
   * 6 : toggle the sixth leg active in rotation
   * 
   * 7 : toggle the seventh leg active in rotation
   * 
   * 8 : toggle the eighth leg active in rotation
   * 
   * H : toggle the head active for rotation
   * 
   * X : use the X axis rotation at the active joint(s)
   * 
   * Y : use the Y axis rotation at the active joint(s)
   * 
   * Z : use the Z axis rotation at the active joint(s)
   * 
   * O : change the test poses for demo
   * 
   * I : change the test poses for demo
   * 
   * P : select joint that connects finger to palm
   * 
   * M : select middle joint
   * 
   * D : select last (distal) joint
   * 
   * R : resets the view to the initial rotation
   * 
   * K : prints the angles of the five fingers for debugging purposes
   * 
   * 
   * Q, Esc : exits the program
   * 
   */
  public void keyTyped(final KeyEvent key) {
    switch (key.getKeyChar()) {
    case 'Q':
    case 'q':
    case KeyEvent.VK_ESCAPE:
      new Thread() {
        @Override
        public void run() {
          PA2.this.animator.stop();
        }
      }.start();
      System.exit(0);
      break;

    // print the angles of the components
    case 'K':
    case 'k':
      printJoints();
      break;

    // resets to the stop sign
    case 'C':
    case 'c':
     // middle1.rotate(Axis.X, 45);
      break;

    // set the state of the hand to the next test case
    case 'T':
    case 't':
      this.setModelState(this.testCases.next());
      break;

    // set the viewing quaternion to 0 rotation
    case 'R':
    case 'r':
      this.viewing_quaternion.reset();
      break;

    // Toggle which finger(s) are affected by the current rotation
    case '1':
      toggleSelection(this.legs[0]);
      break;
    case '2':
      toggleSelection(this.legs[1]);
      break;
    case '3':
      toggleSelection(this.legs[2]);
      break;
    case '4':
      toggleSelection(this.legs[3]);
      break;
    case '5':
      toggleSelection(this.legs[4]);
      break;
    case '6':
        toggleSelection(this.legs[5]);
        break;
    case '7':
        toggleSelection(this.legs[6]);
        break;
    case '8':
        toggleSelection(this.legs[7]);
        break;
    // toggle which joints are affected by the current rotation
    case 'D':
    case 'd':
      for (final Leg finger : this.selectedFingers) {
        toggleSelection(finger.legBody());
      }
      break;
    case 'M':
    case 'm':
      for (final Leg finger : this.selectedFingers) {
        toggleSelection(finger.middleLeg());
      }
      break;
    case 'P':
    case 'p':
      for (final Leg finger : this.selectedFingers) {
        toggleSelection(finger.distalLeg());
      }
      break;

    case 'H':
    case 'h':
      toggleSelection(this.head);
      break;

    // change the axis of rotation at current active joint
    case 'X':
    case 'x':
      this.selectedAxis = Axis.X;
      break;
    case 'Y':
    case 'y':
      this.selectedAxis = Axis.Y;
      break;
    case 'Z':
    case 'z':
      this.selectedAxis = Axis.Z;
      break;
    case 'O':
    case 'o':
    	toggleSelection(this.head);
    	if (ROTATION_ANGLE<30)
    		this.head.rotate(Axis.X, ROTATION_ANGLE);
    //	toggleSelection(this.head);
    //	this.head.rotate(Axis.X, 150);
    //	toggleSelection(this.head);
    	toggleSelection(this.legs[0]);
    	toggleSelection(this.legs[1]);
    	toggleSelection(this.legs[2]);
    	toggleSelection(this.legs[3]);
    	toggleSelection(this.legs[4]);
    	toggleSelection(this.legs[5]);
    	toggleSelection(this.legs[6]);
    	toggleSelection(this.legs[7]);
    	for (final Leg finger : this.selectedFingers) {
            toggleSelection(finger.distalLeg());
          }
    	for (final Leg finger : this.selectedFingers) {
            toggleSelection(finger.middleLeg());
          }
    	for (final Leg finger : this.selectedFingers) {
            toggleSelection(finger.legBody());
          }
    	for (final Component component : this.selectedComponents) {
            component.rotate(Axis.X, 30);
           
    	}
    	toggleSelection(this.head);
    	toggleSelection(this.legs[0]);
    	toggleSelection(this.legs[1]);
    	toggleSelection(this.legs[2]);
    	toggleSelection(this.legs[3]);
    	toggleSelection(this.legs[4]);
    	toggleSelection(this.legs[5]);
    	toggleSelection(this.legs[6]);
    	toggleSelection(this.legs[7]);
    	this.head.rotate(Axis.X, -150);

    	break;
    case 'I':
    case 'i':
    	toggleSelection(this.head);
    	this.head.rotate(Axis.X, 150);
    //	toggleSelection(this.head);
    	toggleSelection(this.legs[0]);
    	toggleSelection(this.legs[1]);
    	toggleSelection(this.legs[2]);
    	toggleSelection(this.legs[3]);
    	toggleSelection(this.legs[4]);
    	toggleSelection(this.legs[5]);
    	toggleSelection(this.legs[6]);
    	toggleSelection(this.legs[7]);
  //  	for (final Leg finger : this.selectedFingers) {
  //          toggleSelection(finger.distalLeg());
  //        }
    	for (final Leg finger : this.selectedFingers) {
            toggleSelection(finger.middleLeg());
          }

    	for (final Component component : this.selectedComponents) {
            component.rotate(Axis.X, 15);
           
    	}
    	toggleSelection(this.head);
    	toggleSelection(this.legs[0]);
    	toggleSelection(this.legs[1]);
    	toggleSelection(this.legs[2]);
    	toggleSelection(this.legs[3]);
    	toggleSelection(this.legs[4]);
    	toggleSelection(this.legs[5]);
    	toggleSelection(this.legs[6]);
    	toggleSelection(this.legs[7]);
    	
    	for (final Component component : this.selectedComponents) {
            component.rotate(Axis.Y, 30);
           
    	}
    	toggleSelection(this.head);
    	toggleSelection(this.head);
    	break;

   
    }
  }

  /**
   * Prints the joints on the System.out print stream.
   */
  private void printJoints() {
    this.printJoints(System.out);
  }

  /**
   * Prints the joints on the specified PrintStream.
   * 
   * @param printStream
   *          The stream on which to print each of the components.
   */
  private void printJoints(final PrintStream printStream) {
    for (final Component component : this.components) {
      printStream.println(component);
    }
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseClicked(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * Updates the rotation quaternion as the mouse is dragged.
   * 
   * @param mouse
   *          The mouse drag event object.
   */
  public void mouseDragged(final MouseEvent mouse) {
	if (this.rotate_world) {
		// get the current position of the mouse
		final int x = mouse.getX();
		final int y = mouse.getY();
	
		// get the change in position from the previous one
		final int dx = x - this.last_x;
		final int dy = y - this.last_y;
	
		// create a unit vector in the direction of the vector (dy, dx, 0)
		final double magnitude = Math.sqrt(dx * dx + dy * dy);
		final float[] axis = magnitude == 0 ? new float[]{1,0,0}: // avoid dividing by 0
			new float[] { (float) (dy / magnitude),(float) (dx / magnitude), 0 };
	
		// calculate appropriate quaternion
		final float viewing_delta = 3.1415927f / 180.0f;
		final float s = (float) Math.sin(0.5f * viewing_delta);
		final float c = (float) Math.cos(0.5f * viewing_delta);
		final Quaternion Q = new Quaternion(c, s * axis[0], s * axis[1], s
				* axis[2]);
		this.viewing_quaternion = Q.multiply(this.viewing_quaternion);
	
		// normalize to counteract acccumulating round-off error
		this.viewing_quaternion.normalize();
	
		// save x, y as last x, y
		this.last_x = x;
		this.last_y = y;
	}
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseEntered(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseExited(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * This method is intentionally unimplemented.
   * 
   * @param mouse
   *          This parameter is ignored.
   */
  public void mouseMoved(MouseEvent mouse) {
    // intentionally unimplemented
  }

  /**
   * Starts rotating the world if the left mouse button was released.
   * 
   * @param mouse
   *          The mouse press event object.
   */
  public void mousePressed(final MouseEvent mouse) {
    if (mouse.getButton() == MouseEvent.BUTTON1) {
      this.last_x = mouse.getX();
      this.last_y = mouse.getY();
      this.rotate_world = true;
    }
  }

  /**
   * Stops rotating the world if the left mouse button was released.
   * 
   * @param mouse
   *          The mouse release event object.
   */
  public void mouseReleased(final MouseEvent mouse) {
    if (mouse.getButton() == MouseEvent.BUTTON1) {
      this.rotate_world = false;
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @param drawable
   *          {@inheritDoc}
   * @param x
   *          {@inheritDoc}
   * @param y
   *          {@inheritDoc}
   * @param width
   *          {@inheritDoc}
   * @param height
   *          {@inheritDoc}
   */
  public void reshape(final GLAutoDrawable drawable, final int x, final int y,
      final int width, final int height) {
    final GL2 gl = (GL2)drawable.getGL();

    // prevent division by zero by ensuring window has height 1 at least
    final int newHeight = Math.max(1, height);

    // compute the aspect ratio
    final double ratio = (double) width / newHeight;

    // reset the projection coordinate system before modifying it
    gl.glMatrixMode(GL2.GL_PROJECTION);
    gl.glLoadIdentity();

    // set the viewport to be the entire window
    gl.glViewport(0, 0, width, newHeight);

    // set the clipping volume
    this.glu.gluPerspective(25, ratio, 0.1, 100);

    // camera positioned at (0,0,6), look at point (0,0,0), up vector (0,1,0)
    this.glu.gluLookAt(0, 0, 12, 0, 0, 0, 0, 1, 0);

    // switch back to model coordinate system
    gl.glMatrixMode(GL2.GL_MODELVIEW);
  }

  private void toggleSelection(final Component component) {
    if (this.selectedComponents.contains(component)) {
      this.selectedComponents.remove(component);
      component.setColor(INACTIVE_COLOR);
    } else {
      this.selectedComponents.add(component);
      component.setColor(ACTIVE_COLOR);
    }
    this.stateChanged = true;
  }

  private void toggleSelection(final Leg finger) {
    if (this.selectedFingers.contains(finger)) {
      this.selectedFingers.remove(finger);
      this.selectedComponents.removeAll(finger.leg());
      for (final Component joint : finger.leg()) {
        joint.setColor(INACTIVE_COLOR);
      }
    } else {
      this.selectedFingers.add(finger);
    }
    this.stateChanged = true;
  }

@Override
public void dispose(GLAutoDrawable drawable) {
	// TODO Auto-generated method stub
	
}
}
