package lightsensorrobot;

public class LightSeeking extends Behavior {
    private double minLum;

    public LightSeeking(Sensors sensors) {
        super(sensors);
        minLum = 0;
    }

    @Override
    public Velocities act() {
        double rightLum = Tools.luxToLuminance(getSensors().getLightR().getLux());
        double leftLum = Tools.luxToLuminance(getSensors().getLightL().getLux());
        double currentLum = (rightLum + leftLum) / 2;

        if (minLum == 0){
            minLum = currentLum;
        }

        double rotationalVelocity = (leftLum - rightLum) * Math.PI * 5;
        double translationalVelocity = 0;

        if (approximatelyEqual(leftLum, rightLum, .2F)) {
            translationalVelocity = TRANSLATIONAL_VELOCITY;

            if (minLum > currentLum) {
                minLum = currentLum;
                rotationalVelocity = Math.PI / 7;
                
                if (rightLum > leftLum){
                    rotationalVelocity = -rotationalVelocity;
                }
            }
        } else
            rotationalVelocity *= 5;

        return new Velocities(translationalVelocity, rotationalVelocity);
    }

    @Override
    public boolean isActive() {
        return true;
    }
    
     public static boolean approximatelyEqual(double value1, double value2, float tolerance) {
        double diff = Math.abs(value1 - value2);
        double tol1 = tolerance / 100 * value1;
        double tol2 = tolerance / 100 * value2;
        
        return diff < tol1 || diff < tol2;
    }
     
}