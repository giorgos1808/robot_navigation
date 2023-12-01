package lightsensorrobot;

import simbad.sim.Agent;

public class ReachGoal extends Behavior {
    private static final double LUMINANCE_STOP_POINT = 0.727;
    private final Agent robot;

    public ReachGoal(Sensors sensors, Agent robot) {
        super(sensors);
        this.robot = robot;
    }

    public Velocities act() {
        return new Velocities(0, 0);
    }

    public boolean isActive() { 
        return Tools.luxToLuminance(getSensors().getLightC().getLux() )  >= LUMINANCE_STOP_POINT;
    }
}