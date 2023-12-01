package lightsensorrobot;

import simbad.sim.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

class Environment extends EnvironmentDescription {
    Environment() {
        this.add(new Box(new Vector3d(3, 0, 5), new Vector3f(3, 1, 7), this));
        this.add(new Box(new Vector3d(-6, 0, -6), new Vector3f(2, 1, 2), this));
        this.add(new Box(new Vector3d(-4, 0, 0.5), new Vector3f(2, 1, 7), this));
       
        this.light2IsOn = true;
        this.light1Color = new Color3f(1, 0, 0);
        this.light2Position = new Vector3d(6, 2, 6);

    }
}