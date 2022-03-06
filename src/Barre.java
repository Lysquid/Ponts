import java.awt.Color;
import java.awt.Graphics;

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
    final static int MASK = Bord.CATEGORY | CATEGORY | Liaison.CATEGORY;

    final float COUPLE_RESISTANCE = 1000000f;
    final float FORCE_MAX = 500f;

    Liaison[] liaisons;
    RevoluteJoint[] joints;

    float longueur, hauteur;
    float angle;

    Color couleur = Color.DARK_GRAY;

    public Barre(World world, float x, float y, float longueur, float hauteur) {
        this.longueur = longueur;
        this.hauteur = hauteur;

        liaisons = new Liaison[2];
        joints = new RevoluteJoint[2];

        // Etape 1 : Définir le "body"
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);

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
        RevoluteJointDef rjd = new RevoluteJointDef();
        rjd.initialize(body, liaison.body, liaison.getPos());
        rjd.enableMotor = true;
        rjd.maxMotorTorque = COUPLE_RESISTANCE;
        RevoluteJoint joint = (RevoluteJoint) world.createJoint(rjd);

        int i = 0;
        if (!(joints[0] == null)) {
            i = 1;
        }
        joints[i] = joint;
        liaisons[i] = liaison;

        liaison.barresLiees.add(this);
    }

    public void testCasse(World world, float dt) {
        for (int i = 0; i < 2; i++) {
            RevoluteJoint joint = joints[i];
            Liaison liaison = liaisons[i];
            if (joint != null && liaison.barresLiees.size() > 1) {
                Vec2 force = new Vec2();
                joint.getReactionForce(1 / dt, force);
                float norme = force.length();
                if (norme > FORCE_MAX) {
                    world.destroyJoint(joint);
                    joints[i] = null;
                    liaison.barresLiees.remove(this);

                    Liaison nouvelleLiaison = new Liaison(world, liaison.getX(), liaison.getY());
                    lier(world, nouvelleLiaison);
                }
            }

        }
    }

    public void dessiner(Graphics g, Box2D box2d) {

        g.setColor(couleur);

        float x = getX();
        float y = getY();
        float angle = getAngle();

        int[] xCoords = new int[4];
        int[] yCoords = new int[4];
        int[][] mult = { { 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };
        for (int i = 0; i < 4; i++) {
            float x2 = (x + longueur / 2 * mult[i][0]);
            float y2 = (y + hauteur / 2 * mult[i][1]);

            double x3 = ((x2 - x) * Math.cos(angle) - (y2 - y) * Math.sin(angle) + x);
            double y3 = ((x2 - x) * Math.sin(angle) + (y2 - y) * Math.cos(angle) + y);

            xCoords[i] = box2d.worldToPixelX((float) x3);
            yCoords[i] = box2d.worldToPixelY((float) y3);
        }

        g.fillPolygon(xCoords, yCoords, 4);
        g.setColor(Color.BLACK);
        g.drawPolygon(xCoords, yCoords, 4);

    }

}
