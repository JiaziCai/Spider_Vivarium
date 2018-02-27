# Spider_Vivarium
A 3D model of a spider that can change its leg, head, and other components in x,y,z directions. It also has 4 poses in key interaction. OpenGL project for 3D Animation in Java.
Adapted from BU CS480 platform of Lab hand model. 
 * @author Tai-Peng Tian <tiantp@gmail.com>
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since Spring 2008


Construct Body Code:
  
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
    
Construct Elements Code:
    
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

Rotation Settings:
    
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

The rest is a little bit tedious, so skip it to the test:


  
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
   
