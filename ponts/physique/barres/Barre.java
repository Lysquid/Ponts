package ponts.physique.barres;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import ponts.ihm.Box2D;
import ponts.physique.ObjetPhysique;
import ponts.physique.environnement.Bord;
import ponts.physique.liaisons.Liaison;
import ponts.physique.liaisons.LiaisonFixe;
import ponts.physique.liaisons.LiaisonMobile;
import ponts.physique.voiture.Voiture;

public abstract class Barre extends ObjetPhysique {

    public static final int CATEGORY = 0b1000;
    public static final int MASK = Voiture.CATEGORY;

    static final float LONGUEUR_MAX = 8;
    static final float LONGUEUR_MIN = 0;

    Color couleurRemplissage;
    Color couleurContour = Color.BLACK;
    boolean apercu = true;

    ArrayList<Liaison> liaisonsLiees;
    public ArrayList<RevoluteJoint> joints;
    PolygonShape shape;
    FixtureDef fixtureDef;
    Fixture fixture;

    float longueur;
    float largeur = 1;

    float elasticite = Liaison.ELASTICITE;
    float forceMax = 500f;
    int prixMateriau = 1000;
    private int prix;

    protected Barre(World world, Liaison liaison1, Liaison liaison2) {

        liaisonsLiees = new ArrayList<Liaison>(2);
        joints = new ArrayList<RevoluteJoint>(2);
        ajouterLiaison(liaison1);
        ajouterLiaison(liaison2);

        creerObjetPhysique(world);
        ajusterPos();
    }

    public int getPrix() {
        return prix;
    }

    @Override
    public void creerObjetPhysique(World world) {

        // Etape 1 : Définir le "body"
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.KINEMATIC;

        // Etape 2 : Créer un "body"
        body = world.createBody(bodyDef);

        // Etape 3 : Créee la "shape"
        shape = new PolygonShape();

        // Etape 4 : Définir la "fixture"
        fixtureDef = creerFixtureDef(shape);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits |= Barre.CATEGORY;

    }

    public abstract FixtureDef creerFixtureDef(Shape shape);

    public void ajouterLiaison(Liaison liaison) {
        liaisonsLiees.add(liaison);
        liaison.getBarresLiees().add(this);
    }

    public void lier(World world, Liaison liaison) {
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.initialize(body, liaison.getBody(), liaison.getPos());
        jointDef.collideConnected = true;
        RevoluteJoint joint = (RevoluteJoint) world.createJoint(jointDef);

        joints.add(joint);

    }

    public LinkedList<Liaison> testCasse(World world, float dt) {

        LinkedList<Liaison> nouvellesLiaisons = new LinkedList<>();

        for (int i = 0; i < joints.size(); i++) {
            RevoluteJoint joint = joints.get(i);
            Liaison liaison = liaisonsLiees.get(i);

            if (liaison.getBarresLiees().size() > 1) {

                if (calculCasse(joint, liaison, dt)) {
                    supprimerLiaison(world, i);

                    LiaisonMobile nouvelleLiaison = new LiaisonMobile(world, liaison.getPos());
                    ajouterLiaison(nouvelleLiaison);
                    nouvelleLiaison.activerPhysique();
                    lier(world, nouvelleLiaison);
                    nouvellesLiaisons.add(nouvelleLiaison);
                }
            }
        }
        ajouterCollisionBord();
        return nouvellesLiaisons;
    }

    public boolean calculCasse(RevoluteJoint joint, Liaison liaison, float dt) {

        Vec2 force = new Vec2();
        joint.getReactionForce(1 / dt, force);

        Vec2 vectBarre = liaisonsLiees.get(0).getPos().sub(liaisonsLiees.get(1).getPos());
        Vec2 orthogonal = new Vec2(-vectBarre.y, vectBarre.x);
        orthogonal.normalize();
        float produitScalaire = Math.abs(Vec2.dot(orthogonal, force));

        float valeur = produitScalaire / (liaison.getBarresLiees().size() - 1);
        if (liaison instanceof LiaisonFixe) {
            valeur /= 20;
        }

        return valeur > forceMax;
    }

    public void supprimerLiaison(World world, int index) {
        // Condition nécessaire car si la physique n'a pas été activé,
        // les joints n'ont pas été crées
        if (!joints.isEmpty()) {
            RevoluteJoint joint = joints.get(index);
            world.destroyJoint(joint);
            joints.remove(index);
        }

        Liaison liaison = liaisonsLiees.get(index);
        liaison.getBarresLiees().remove(this);
        liaisonsLiees.remove(index);
    }

    public void ajouterCollisionBord() {
        // On ajoute des collisions avec les bords seulement si la barre n'est liée
        // a aucune liaisons fixe (sinon, problemes de collision)
        boolean fixe = false;
        for (Liaison liaison : liaisonsLiees) {
            if (liaison instanceof LiaisonFixe)
                fixe = true;
        }
        if (!fixe) {
            Filter filter = fixture.getFilterData();
            filter.maskBits |= Bord.CATEGORY;
            fixture.setFilterData(filter);
        }
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
        couleurRemplissage = ObjetPhysique.setColorAlpha(couleurRemplissage, alpha);
        couleurContour = ObjetPhysique.setColorAlpha(couleurContour, alpha);

        g.setColor(couleurRemplissage);
        g.fillPolygon(xCoins, yCoins, 4);
        g.setColor(couleurContour);
        g.drawPolygon(xCoins, yCoins, 4);

    }

    public boolean testBarreCliquee(Vec2 posClic) {
        Transform transform = new Transform(getPos(), new Rot(getAngle()));
        return shape.testPoint(transform, posClic);
    }

    public LinkedList<LiaisonMobile> supprimer(World world) {
        LinkedList<LiaisonMobile> liaisonsASupprimer = new LinkedList<>();
        for (Liaison liaison : liaisonsLiees) {
            liaison.getBarresLiees().remove(this);
            if (liaison instanceof LiaisonMobile && liaison.getBarresLiees().isEmpty()) {
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
            body.destroyFixture((Fixture) fixture);
        }
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);

    }

    public void activerPhysique() {
        body.setType(BodyType.DYNAMIC);
        apercu = false;
        prix = Math.round(longueur / LONGUEUR_MAX * prixMateriau);
        ajouterCollisionBord();
        ajusterPos();
    }

    public void accrocher(World world, Liaison liaisonCliquee) {
        supprimerLiaison(world, 1);
        ajouterLiaison(liaisonCliquee);
    }

    public boolean tailleMinimum() {
        return longueur > LONGUEUR_MIN;
    }

    public ArrayList<Liaison> getLiaisonsLiees() {
        return liaisonsLiees;
    }

    public boolean inferieurLongeurMax(Vec2 pos) {
        Liaison liaison1 = liaisonsLiees.get(0);
        float nouvelleLongeur = liaison1.getPos().sub(pos).length();
        return nouvelleLongeur <= LONGUEUR_MAX;
    }

    public Vec2 posLiaisonMax(Vec2 pos) {
        Liaison liaison1 = liaisonsLiees.get(0);
        Vec2 vecteur = pos.sub(liaison1.getPos());
        vecteur.normalize();
        return liaison1.getPos().add(vecteur.mul(LONGUEUR_MAX));
    }

}