package ponts.physique;

import java.awt.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public abstract class ObjetPhysique {

    protected Body body;

    public static final float FRICTION = 0.8f; // Coefficient de frottement
    public static final float ELASTICITE = 0.3f; // Determine si l'objet rebondi
    public static final float DENSITE = 1f; // Densitée de l'objet

    public abstract void creerObjetPhysique(World world);

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

    public Body getBody() {
        return body;
    }

    public FixtureDef creerFixtureDef(float friction, float elasticite, float densite, int category,
            int mask) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = friction;
        fixtureDef.restitution = elasticite;
        fixtureDef.density = densite;
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = mask;
        return fixtureDef;
    }

}
