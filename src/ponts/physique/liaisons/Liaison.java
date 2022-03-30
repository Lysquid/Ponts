package ponts.physique.liaisons;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import ponts.ihm.Box2D;
import ponts.physique.ObjetPhysique;
import ponts.physique.barres.Barre;
import ponts.physique.environnement.Bord;

public abstract class Liaison extends ObjetPhysique {

    public static final int CATEGORY = 0b0100;
    public static final int MASK = Bord.CATEGORY;

    static final float RAYON = 0.5f;
    static final float RAYON_CLICK = 1f;

    Color couleurRemplissage;
    Color couleurContour;
    Color couleurSurvolee;

    LinkedList<Barre> barresLiees;
    boolean cliquee;
    transient CircleShape shape;

    boolean apercu;

    protected Liaison(World world, Vec2 pos, BodyType bodyType) {

        couleurContour = Color.BLACK;
        couleurSurvolee = Color.decode("#e86933");

        barresLiees = new LinkedList<Barre>();

        cliquee = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(pos);

        body = world.createBody(bodyDef);
        shape = new CircleShape();
        shape.setRadius(RAYON);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

        body.createFixture(fixtureDef);
    }

    public abstract void activerPhysique();

    public void dessiner(Graphics g, Box2D box2d, boolean estSurvolee) {

        int x = box2d.worldToPixelX(getX());
        int y = box2d.worldToPixelY(getY());
        int r = box2d.worldToPixel(RAYON);

        int alpha = apercu ? 100 : 255;
        couleurRemplissage = ObjetPhysique.setColorAlpha(couleurRemplissage, alpha);
        couleurContour = ObjetPhysique.setColorAlpha(couleurContour, alpha);
        couleurSurvolee = ObjetPhysique.setColorAlpha(couleurSurvolee, alpha);

        g.setColor(estSurvolee ? couleurSurvolee : couleurRemplissage);
        g.fillOval(x - r, y - r, r * 2, r * 2);
        g.setColor(couleurContour);
        g.drawOval(x - r, y - r, r * 2, r * 2);
    }

    public float distancePoint(Vec2 pos) {
        Vec2 centre = getPos();
        return (centre.sub(pos).length());
    }

    public boolean testLiaisonCliquee(Vec2 pos) {
        return (distancePoint(pos) <= RAYON_CLICK);
    }

    public LinkedList<Barre> getBarresLiees() {
        return barresLiees;
    }

}
