import java.awt.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public abstract class ObjetPhysique {

    Body body;

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

    public void setPos(Vec2 pos, float angle) {
        body.setTransform(pos.clone(), angle);
    }

    public void setPos(Vec2 pos) {
        setPos(pos, getAngle());
    }

    public static Color setColorAlpha(Color color, int alpha) {
        if (color.getAlpha() == alpha) {
            return color;
        } else {
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        }
    }

}
