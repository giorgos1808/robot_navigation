package lightsensorrobot;

public abstract class Behavior {
    static final double TRANSLATIONAL_VELOCITY = 0.4;
    private final Sensors sensors;

    Behavior(Sensors sensors) {
        this.sensors = sensors;
    }

    public abstract Velocities act();

    public abstract boolean isActive();

    Sensors getSensors() {
        return sensors;
    }
}