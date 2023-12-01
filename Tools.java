package lightsensorrobot;

import simbad.sim.RangeSensorBelt;
import javax.vecmath.Point3d;

public class Tools {
    private static float radius;

    public static void setRobotRadius(float radius) {
        Tools.radius = radius;
    }

    public static Point3d getSensedPoint(RangeSensorBelt sonars, int sonarIdx) {
        double v;
        if (sonars.hasHit(sonarIdx)) {
            v = radius + sonars.getMeasurement(sonarIdx);
        } else {
            v = radius + sonars.getMaxRange();
        }
        
        double x = v * Math.cos(sonars.getSensorAngle(sonarIdx));
        double z = v * Math.sin(sonars.getSensorAngle(sonarIdx));
        
        return new Point3d(x, 0, z);
    }

    public static double luxToLuminance(double lux) {
        return Math.pow(lux, 0.1);
    }

    public static double luxToLuminance(double lux1, double lux2) {
        return ( luxToLuminance(lux1) + luxToLuminance(lux2) ) / 2;
    }
    
    public static int getMinSonarIndex(RangeSensorBelt sonars) {
        int min = 0;
        for (int i = 1; i < sonars.getNumSensors(); i++) {
            if (sonars.getMeasurement(i) < sonars.getMeasurement(min)){
                min = i;
            }
        }
        
        return min;
    }
    
    public static double getMinSonarDist(RangeSensorBelt sonars) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < sonars.getNumSensors(); i++) {
            if (sonars.getMeasurement(i) < min) {
                min = sonars.getMeasurement(i);
            }
        }
        
        return min;
    }

}
