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

/**
 * Classe d'une roue de la voiture
 */
public class Roue extends ObjetPhysique {

    public static final int CATEGORY = Voiture.CATEGORY;
    public static final int MASK = Voiture.MASK;

    public static final float RAYON = 1f;
    private static final float MOTOR_SPEED = 10f; // Vitesse du moteur
    private static final float MOTOR_TORQUE = 80f; // Puissance du moteur

    private Color couleurRemplissage = Color.decode("#555555");
    private Color couleurRayons = Color.decode("#888888");
    private Color couleurContour = Color.BLACK;

    private RevoluteJoint joint;
    private CircleShape shape;

    /**
     * Constructeur d'une roue
     * 
     * @param world
     * @param pos
     */
    public Roue(World world, Vec2 pos) {
        creerObjetPhysique(world);
        setPos(pos);
    }

    @Override
    protected void creerObjetPhysique(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        body = world.createBody(bodyDef);

        shape = new CircleShape();
        shape.setRadius(RAYON);

        FixtureDef fixtureDef = creerFixtureDef(FRICTION, ELASTICITE, DENSITE, CATEGORY, MASK);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }

    /**
     * Crée une liaison pivot motorisée avec la carrosserie
     * 
     * @param world
     * @param carosserie
     */
    public void lierVoiture(World world, Carrosserie carosserie) {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.initialize(body, carosserie.getBody(), getPos());
        jointDef.motorSpeed = MOTOR_SPEED;
        jointDef.maxMotorTorque = MOTOR_TORQUE;
        jointDef.enableMotor = true;

        joint = (RevoluteJoint) world.createJoint(jointDef);
    }

    /**
     * Dessine une roue avec ses rayons
     * 
     * @param g
     * @param box2d
     */
    public void dessiner(Graphics g, Box2D box2d) {

        int x = box2d.worldToPixelX(getX());
        int y = box2d.worldToPixelY(getY());
        int r = box2d.worldToPixel(RAYON);

        g.setColor(couleurRemplissage);
        g.fillOval(x - r, y - r, r * 2, r * 2);

        g.setColor(couleurRayons);
        int nombreRayons = 5;
        for (int i = 0; i < nombreRayons; i++) {
            double angle = getAngle() + 2 * Math.PI * i / (double) nombreRayons;
            int x2 = box2d.worldToPixelX(getX() + RAYON * (float) Math.cos(angle));
            int y2 = box2d.worldToPixelY(getY() + RAYON * (float) Math.sin(angle));
            g.drawLine(x, y, x2, y2);
        }

        g.setColor(couleurContour);
        g.drawOval(x - r, y - r, r * 2, r * 2);
    }

    /**
     * Désactive la motorisation de la roue
     */
    public void arreter() {
        joint.setMotorSpeed(0);
    }

}
