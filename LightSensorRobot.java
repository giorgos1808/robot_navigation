package lightsensorrobot;

import simbad.sim.*;
import javax.vecmath.Vector3d;
import simbad.gui.Simbad;

public class LightSensorRobot{

    public static void main(String[] args) {
        MyRobot robot = new MyRobot(new Vector3d(-8, 0, -6), "MyRobot"); 
   
        Environment environment = new Environment();
        environment.add(robot);

        new Simbad(environment, false);
    }
    
}