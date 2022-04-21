package ponts.physique.voiture;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import ponts.ihm.Box2D;
import ponts.physique.ObjetPhysique;

public class Roue extends ObjetPhysique {

    public static final int CATEGORY = Voiture.CATEGORY;
    public static final int MASK = Voiture.MASK;

    static final float RAYON = 1f;
    static final float MOTOR_SPEED = 10f; // Vitesse du moteur
    static final float MOTOR_TORQUE = 1000f; // Puissance du moteur

    Color couleurContour = Color.BLACK;
    Color couleurRemplissage = Color.GRAY;

    RevoluteJoint joint;
    CircleShape shape;

    public Roue(World world, Vec2 pos) {
        creerObjetPhysique(world);
        setPos(pos);
    }

    public void creerObjetPhysique(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        body = world.createBody(bodyDef);

        shape = new CircleShape();
        shape.setRadius(RAYON);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;
        body.createFixture(fixtureDef);
    }

    public void lierVoiture(World world, Carrosserie carosserie) {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.initialize(body, carosserie.getBody(), getPos());
        jointDef.motorSpeed = MOTOR_SPEED;
        jointDef.maxMotorTorque = MOTOR_TORQUE;
        jointDef.enableMotor = true;

        joint = (RevoluteJoint) world.createJoint(jointDef);
    }

    public void dessiner(Graphics g, Box2D box2d) {

        int x = box2d.worldToPixelX(getX());
        int y = box2d.worldToPixelY(getY());
        int r = box2d.worldToPixel(RAYON);

        g.setColor(couleurRemplissage);
        g.fillOval(x - r, y - r, r * 2, r * 2);
        g.setColor(couleurContour);
        g.drawOval(x - r, y - r, r * 2, r * 2);
        int x2 = (int) (x + r * Math.cos(-getAngle()));
        int y2 = (int) (y + r * Math.sin(-getAngle()));
        g.drawLine(x, y, x2, y2);
    }

}
