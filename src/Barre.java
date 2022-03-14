import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class Barre extends ObjetPhysique {

    final static int CATEGORY = 0b0010;
    final static int MASK = Bord.CATEGORY | Barre.CATEGORY | Liaison.CATEGORY;

    float COUPLE_RESISTANCE;
    float FORCE_MAX;

    float LARGEUR_BARRE;

    Color COULEUR_REMPLISSAGE;
    Color COULEUR_CONTOUR = Color.BLACK;
    
    static float LONGUEUR_MAX = 8;
    static float LONGUEUR_MIN = 1;

    PolygonShape shape;
    ArrayList<Liaison> liaisonsLiees;
    ArrayList<RevoluteJoint> joints;

    float longueur, largeur;

    public Barre(World world, Vec2 pos, float angle, float longueur) {

        liaisonsLiees = new ArrayList<Liaison>(2);
        joints = new ArrayList<RevoluteJoint>(2);

        // Etape 1 : Définir le "body"
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(pos);
        bodyDef.angle = angle;

        // Etape 2 : Créer un "body"
        body = world.createBody(bodyDef);

    }

    public void lier(World world, Liaison liaison) {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.initialize(body, liaison.body, liaison.getPos());
        jointDef.enableMotor = true;
        jointDef.maxMotorTorque = COUPLE_RESISTANCE;
        RevoluteJoint joint = (RevoluteJoint) world.createJoint(jointDef);

        joints.add(joint);
        liaisonsLiees.add(liaison);
        liaison.barresLiees.add(this);

    }

    public LinkedList<Liaison> testCasse(World world, float dt) {

        LinkedList<Liaison> nouvellesLiaisons = new LinkedList<Liaison>();

        for (int i = 0; i < liaisonsLiees.size(); i++) {
            RevoluteJoint joint = joints.get(i);
            Liaison liaison = liaisonsLiees.get(i);

            if (liaison.barresLiees.size() > 1) {

                Vec2 force = new Vec2();
                joint.getReactionForce(1 / dt, force);
                float norme = force.length();

                if (norme > FORCE_MAX) {
                    world.destroyJoint(joint);
                    liaison.barresLiees.remove(this);
                    joints.remove(joint);
                    liaisonsLiees.remove(liaison);

                    LiaisonMobile nouvelleLiaison = new LiaisonMobile(world, liaison.getPos());
                    lier(world, nouvelleLiaison);
                    nouvellesLiaisons.add(nouvelleLiaison);
                }
            }
        }
        return nouvellesLiaisons;
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
            float y2 = (y + largeur / 2 * positionCoins[i][1]);

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

    public boolean testBarreCliquee(Vec2 posClic) {
        Transform transform = new Transform(getPos(), new Rot(getAngle()));
        return shape.testPoint(transform, posClic);
    }

    public LinkedList<LiaisonMobile> supprimer(World world) {
        LinkedList<LiaisonMobile> liaisonsASupprimer = new LinkedList<LiaisonMobile>();
        for (Liaison liaison : liaisonsLiees) {
            liaison.barresLiees.remove(this);
            if (liaison instanceof LiaisonMobile && liaison.barresLiees.size() < 1) {
                LiaisonMobile liaisonASupprimer = (LiaisonMobile) liaison;
                liaisonsASupprimer.add(liaisonASupprimer);
                liaisonASupprimer.supprimer(world);
            }
        }
        world.destroyBody(this.body);
        return liaisonsASupprimer;
    }

}
