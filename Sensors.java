package lightsensorrobot;

import simbad.sim.LightSensor;
import simbad.sim.RangeSensorBelt;

public class Sensors {
    private final RangeSensorBelt sonars, bumpers;
    private final LightSensor lightSensorRight, lightSensorLeft, lightSensorCenter;

    public Sensors(RangeSensorBelt sonars, RangeSensorBelt bumpers, LightSensor lightSensorRight, LightSensor lightSensorLeft, LightSensor lightSensorCenter ) {
        this.sonars = sonars;
        this.bumpers = bumpers;
        this.lightSensorRight = lightSensorRight;
        this.lightSensorLeft = lightSensorLeft;
        this.lightSensorCenter = lightSensorCenter;
    }

    public RangeSensorBelt getSonars() {
        return sonars;
    }
    
    public RangeSensorBelt getBumpers() {
        return bumpers;
    }

    public LightSensor getLightR() {
        return lightSensorRight;
    }

    public LightSensor getLightL() {
        return lightSensorLeft;
    }

    public LightSensor getLightC() {
        return lightSensorCenter;
    }
    
}