package lightsensorrobot;

import simbad.sim.RangeSensorBelt;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Avoidance extends Behavior {
    private static final double K1 = 5;
    private static final double K2 = 0.8;
    private static final double K3 = 1;
    private static final double dist_start = .4;
    private static final double dist_safe = .8;

    private static boolean clockwise;
    private int avoid;

    public Avoidance(Sensors sensors) {
        super(sensors);
        clockwise = true;
        avoid = 0;
    }

    public static void setClockwise(boolean clockwise) {
        Avoidance.clockwise = clockwise;
    }

    public static void reverseRotation() {
        Avoidance.clockwise = !clockwise;
    }

    private static double wrapToPi(double angle) {
        if (angle > Math.PI){
            return angle - Math.PI * 2;
        }

        if (angle <= -Math.PI){
            return angle + Math.PI * 2;
        }

        return angle;
    }

    public Velocities act() {
        RangeSensorBelt bumpers = getSensors().getBumpers();

        if (bumpers.getFrontQuadrantHits() > 0){
            return new Velocities(-TRANSLATIONAL_VELOCITY * 8, Math.PI);
        } else if (bumpers.getBackQuadrantHits() > 0){
            return new Velocities(TRANSLATIONAL_VELOCITY * 8, Math.PI);
        }
        
        RangeSensorBelt sonars = getSensors().getSonars();

        Point3d p = Tools.getSensedPoint(sonars, Tools.getMinSonarIndex(sonars));
        double distance = p.distance(new Point3d(0, 0, 0));
        Vector3d vector = clockwise ? new Vector3d(-p.z, 0, p.x) : new Vector3d(p.z, 0, -p.x);

        double phLin = Math.atan2(vector.z, vector.x);
        double phRot = Math.atan(K3 * (distance - dist_safe));

        if (clockwise || (!sonars.hasHit(3) && sonars.hasHit(5))){
            phRot = -phRot;
        }

        double phRef = wrapToPi(phLin + phRot);

        return new Velocities(K2 * Math.cos(phRef), K1 * phRef);
    }

    public boolean isActive() {
        if (getSensors().getBumpers().oneHasHit()){
            return true;
        }
        
        RangeSensorBelt sonars = getSensors().getSonars();

        int min = Tools.getMinSonarIndex(sonars);
        if (sonars.getMeasurement(min) <= dist_start && avoid == 0) {
            clockwise = !(min < 7);

            if (min == 0){
                if (sonars.getMeasurement(7) > sonars.getMeasurement(1)){
                    clockwise = false;
                }
            }

            avoid++;
            return true;
        } 

        avoid = 0;
        return false;
    }
}