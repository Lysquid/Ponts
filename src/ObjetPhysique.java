import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public abstract class ObjetPhysique {

    Body body;

    public ObjetPhysique() {
    }

    public Vec2 getPos() {
        return body.getTransform().p;
    }

    public float getX() {
        return body.getTransform().p.x;
    }

    public float getY() {
        return body.getTransform().p.y;
    }

    public float getAngle() {
        return body.getAngle();
    }

}
