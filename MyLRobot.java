package lightsensorrobot;

import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;

class MyRobot extends Agent {
    private final Sensors sensors;
    private Behavior[] behaviors;
    private boolean[][] subsumes;
    private int Index;
    private boolean checkCompleted;
    private int checkTurnCount;
    private double maxLum;
    
    MyRobot(Vector3d position, String name) {
        super(position, name);

        RangeSensorBelt sonars = RobotFactory.addSonarBeltSensor(this, 8);

        RangeSensorBelt bumpers = RobotFactory.addBumperBeltSensor(this, 8);
               
        LightSensor lightSensorRight = RobotFactory.addLightSensorRight(this);
        LightSensor lightSensorLeft = RobotFactory.addLightSensorLeft(this);
        LightSensor lightSensorCenter = RobotFactory.addLightSensor(this);

        sensors = new Sensors(sonars, bumpers, lightSensorRight, lightSensorLeft, lightSensorCenter);
    }

    @Override
    protected void initBehavior() {
        behaviors = new Behavior[]{
                new ReachGoal(sensors, this),
                new Avoidance(sensors),
                new LightSeeking(sensors)
        };

        subsumes = new boolean[][]{            
                {false, true, true},
                {false, false, true},
                {false, false, false}};

        Tools.setRobotRadius(this.getRadius());

        Index = 0;
        checkCompleted = false;
        checkTurnCount = 10;
        maxLum = 0;
    }

    private void searchForLuminance() {
        this.setTranslationalVelocity(0);
        this.setRotationalVelocity(Math.PI / 2);

        double currentLuminance = Tools.luxToLuminance(sensors.getLightR().getLux(), sensors.getLightL().getLux());

        if (maxLum < currentLuminance){
            maxLum = currentLuminance;
        }

        checkTurnCount--;
    }

    private void turnTowardsLight() {
        double currentLuminance = Tools.luxToLuminance(sensors.getLightR().getLux(), sensors.getLightL().getLux());

        if (approximatelyEqual(currentLuminance, maxLum, .005F)){
            checkCompleted = true;
        } else{
            this.setRotationalVelocity(Math.PI / 2);
        }
    }

    private void performInitialCheck() {
        if (checkTurnCount > 0){
            searchForLuminance();
        } else if (!checkCompleted && maxLum > 0) {
            turnTowardsLight();

            if (sensors.getLightR().getAverageLuminance() < sensors.getLightL().getAverageLuminance()){
                Avoidance.setClockwise(false);
            } else{
                Avoidance.setClockwise(true);
            }
        }
    }

    private void chooseBehavior() {
        boolean[] isActive = new boolean[behaviors.length];

        for (int i = 0; i < isActive.length; i++){
            isActive[i] = behaviors[i].isActive();
        }

        boolean ranABehavior = false;

        while (!ranABehavior) {
            boolean runCurrentBehavior = isActive[Index];

            if (runCurrentBehavior) {
                for (int i = 0; i < subsumes.length; i++){
                    if (isActive[i] && subsumes[i][Index]) {
                        runCurrentBehavior = false;
                        break;
                    }
                }
            }

            if (runCurrentBehavior) {
                if (Index < behaviors.length) {
                    Velocities newVelocities = behaviors[Index].act();
                    this.setTranslationalVelocity(newVelocities.getTranslationalVelocity());
                    this.setRotationalVelocity(newVelocities.getRotationalVelocity());
                }

                ranABehavior = true;
            }

            if (behaviors.length > 0){
                Index = (Index + 1) % behaviors.length;
            }
        }
    }

    public void performBehavior() {
        performInitialCheck();

        if (checkCompleted){
            chooseBehavior();
        }
    }
    
    public static boolean approximatelyEqual(double value1, double value2, float tolerance) {
        double diff = Math.abs(value1 - value2);
        double tol1 = tolerance / 100 * value1;
        double tol2 = tolerance / 100 * value2;
        
        return diff < tol1 || diff < tol2;
    }
    
}