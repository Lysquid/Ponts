import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import org.jbox2d.dynamics.FixtureDef;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

enum Materiau {
    BOIS,
    GOUDRON
}

public class Barre extends ObjetPhysique {

    static int CATEGORY = 0b0010;
    static int MASK = Bord.CATEGORY;

    float FORCE_MAX;

    Color COULEUR_REMPLISSAGE;
    Color COULEUR_CONTOUR = Color.BLACK;
    boolean apercu;

    static float LONGUEUR_MAX = 8;
    static float LONGUEUR_MIN = 3;

    PolygonShape shape;
    ArrayList<Liaison> liaisonsLiees;
    ArrayList<RevoluteJoint> joints;
    FixtureDef fixtureDef;
    Fixture fixture;

    float longueur, largeur;

    public Barre(World world, Liaison liaison1, Liaison liaison2) {

        apercu = true;
        liaisonsLiees = new ArrayList<Liaison>(2);
        joints = new ArrayList<RevoluteJoint>(2);
        ajouterLiaison(liaison1);
        ajouterLiaison(liaison2);

        // Etape 1 : Définir le "body"
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.KINEMATIC;

        // Etape 2 : Créer un "body"
        body = world.createBody(bodyDef);

        // Etape 3 : Définir la "shape"
        shape = new PolygonShape();

        // Etape 2 : Créer un "body"
        body = world.createBody(bodyDef);

        fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        // fixtureDef.restitution = 0.5f;
        // fixtureDef.friction = 0.3f;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

        ajusterPos();
    }

    public void ajouterLiaison(Liaison liaison) {
        liaisonsLiees.add(liaison);
        liaison.barresLiees.add(this);
    }

    public void lier(World world, Liaison liaison) {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.initialize(body, liaison.body, liaison.getPos());
        RevoluteJoint joint = (RevoluteJoint) world.createJoint(jointDef);

        joints.add(joint);

    }

    public LinkedList<Liaison> testCasse(World world, float dt) {

        LinkedList<Liaison> nouvellesLiaisons = new LinkedList<Liaison>();

        for (int i = 0; i < joints.size(); i++) {
            RevoluteJoint joint = joints.get(i);
            Liaison liaison = liaisonsLiees.get(i);

            if (liaison.barresLiees.size() > 1) {

                Vec2 force = new Vec2();
                joint.getReactionForce(1 / dt, force);
                float norme = force.length();

                if (norme > FORCE_MAX) {
                    supprimerLiaison(world, i);

                    LiaisonMobile nouvelleLiaison = new LiaisonMobile(world, liaison.getPos());
                    liaisonsLiees.add(nouvelleLiaison);
                    nouvelleLiaison.activerPhysique();
                    lier(world, nouvelleLiaison);
                    nouvellesLiaisons.add(nouvelleLiaison);
                }
            }
        }
        return nouvellesLiaisons;
    }

    public void supprimerLiaison(World world, int index) {
        if (joints.size() > 0) {
            RevoluteJoint joint = joints.get(index);
            world.destroyJoint(joint);
            joints.remove(index);
        }

        Liaison liaison = liaisonsLiees.get(index);
        liaison.barresLiees.remove(this);
        liaisonsLiees.remove(index);
    }

    public void dessiner(Graphics g, Box2D box2d) {

        int[] xCoins = new int[4];
        int[] yCoins = new int[4];

        for (int i = 0; i < 4; i++) {
            Vec2 pos = shape.getVertex(i);

            float cos = (float) Math.cos(getAngle());
            float sin = (float) Math.sin(getAngle());
            float x = pos.x * cos - pos.y * sin + getX();
            float y = pos.x * sin + pos.y * cos + getY();

            xCoins[i] = box2d.worldToPixelX(x);
            yCoins[i] = box2d.worldToPixelY(y);
        }

        int alpha = apercu ? 100 : 255;
        COULEUR_REMPLISSAGE = ObjetPhysique.setColorAlpha(COULEUR_REMPLISSAGE, alpha);
        COULEUR_CONTOUR = ObjetPhysique.setColorAlpha(COULEUR_CONTOUR, alpha);

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

    public void ajusterPos() {

        Liaison liaison1 = liaisonsLiees.get(0);
        Liaison liaison2 = liaisonsLiees.get(1);
        Vec2 centre = liaison1.getPos().add(liaison2.getPos()).mul(0.5f);
        Vec2 difference = liaison1.getPos().sub(liaison2.getPos());
        float angle = (float) Math.atan(difference.y / difference.x);
        longueur = difference.length();

        shape.setAsBox(longueur / 2, largeur / 2);
        setPos(centre, angle);

        if (fixture != null) {
            body.destroyFixture(fixture);
        }
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);

    }

    public void activerPhysique(World world) {
        body.setType(BodyType.DYNAMIC);
        apercu = false;
    }

    public void accrocher(World world, Liaison liaisonCliquee) {
        supprimerLiaison(world, 1);
        ajouterLiaison(liaisonCliquee);
    }

}
