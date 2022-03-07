import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class Barre extends ObjetPhysique {

    final static int CATEGORY = 0b0010;
    final static int MASK = Bord.CATEGORY | Barre.CATEGORY | Liaison.CATEGORY;

    final float COUPLE_RESISTANCE = 1000000f;
    final float FORCE_MAX = 1000f;
    final Color COULEUR_REMPLISSAGE = Color.DARK_GRAY;
    final Color COULEUR_CONTOUR = Color.BLACK;

    ArrayList<Liaison> liaisons;
    ArrayList<RevoluteJoint> joints;

    float longueur, hauteur;
    float angle;

    public Barre(World world, float x, float y, float angle, float longueur, float hauteur) {
        this.longueur = longueur;
        this.hauteur = hauteur;

        liaisons = new ArrayList<Liaison>(2);
        joints = new ArrayList<RevoluteJoint>(2);

        // Etape 1 : Définir le "body"
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);
        bodyDef.angle = angle;

        // Etape 2 : Créer un "body"
        body = world.createBody(bodyDef);

        // Etape 3 : Définir la "shape"
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(longueur / 2, hauteur / 2);

        // Etape 4 : Définir la "fixture"
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        // fixtureDef.restitution = 0.5f;
        // fixtureDef.friction = 0.3f;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

        // Etape 5 : Attacher la shape au body avec la fixture
        body.createFixture(fixtureDef);

    }

    public void lier(World world, Liaison liaison) {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.initialize(body, liaison.body, liaison.getPos());
        jointDef.enableMotor = true;
        jointDef.maxMotorTorque = COUPLE_RESISTANCE;
        RevoluteJoint joint = (RevoluteJoint) world.createJoint(jointDef);

        joints.add(joint);
        liaisons.add(liaison);
        liaison.barresLiees.add(this);
    }

    public void testCasse(World world, float dt) {
        for (int i = 0; i < liaisons.size(); i++) {
            RevoluteJoint joint = joints.get(i);
            Liaison liaison = liaisons.get(i);

            if (liaison.barresLiees.size() > 1) {

                Vec2 force = new Vec2();
                joint.getReactionForce(1 / dt, force);
                float norme = force.length();

                if (norme > FORCE_MAX) {
                    world.destroyJoint(joint);
                    liaison.barresLiees.remove(this);
                    joints.remove(joint);
                    liaisons.remove(liaison);

                    Liaison nouvelleLiaison = new Liaison(world, liaison.getX(), liaison.getY());
                    lier(world, nouvelleLiaison);
                }
            }

        }
    }

    public void dessiner(Graphics g, Box2D box2d) {

        float x = getX();
        float y = getY();
        float angle = getAngle();

        int[] xCoins = new int[4];
        int[] yCoins = new int[4];
        int[][] positionCoins = { { 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };
        for (int i = 0; i < 4; i++) {
            float x2 = (x + longueur / 2 * positionCoins[i][0]);
            float y2 = (y + hauteur / 2 * positionCoins[i][1]);

            double x3 = ((x2 - x) * Math.cos(angle) - (y2 - y) * Math.sin(angle) + x);
            double y3 = ((x2 - x) * Math.sin(angle) + (y2 - y) * Math.cos(angle) + y);

            xCoins[i] = box2d.worldToPixelX((float) x3);
            yCoins[i] = box2d.worldToPixelY((float) y3);
        }

        g.setColor(COULEUR_REMPLISSAGE);
        g.fillPolygon(xCoins, yCoins, 4);
        g.setColor(COULEUR_CONTOUR);
        g.drawPolygon(xCoins, yCoins, 4);

    }

}
