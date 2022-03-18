import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public abstract class Liaison extends ObjetPhysique {

    final static int CATEGORY = 0b0100;
    final static int MASK = Bord.CATEGORY;

    final float RAYON = 0.5f;
    final float RAYON_CLICK = 1f;
    Color COULEUR_REMPLISSAGE;
    Color COULEUR_CONTOUR;
    Color COULEUR_CLIQUEE;

    LinkedList<Barre> barresLiees;
    boolean cliquee;
    CircleShape shape;

    boolean apercu;

    public Liaison(World world, Vec2 pos, BodyType bodyType) {

        COULEUR_CONTOUR = Color.BLACK;
        COULEUR_CLIQUEE = Color.decode("#e86933");

        barresLiees = new LinkedList<Barre>();

        cliquee = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(pos);

        body = world.createBody(bodyDef);
        shape = new CircleShape();
        shape.setRadius(this.RAYON);

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
        int r = box2d.worldToPixel(this.RAYON);

        int alpha = apercu ? 100 : 255;
        COULEUR_REMPLISSAGE = ObjetPhysique.setColorAlpha(COULEUR_REMPLISSAGE, alpha);
        COULEUR_CONTOUR = ObjetPhysique.setColorAlpha(COULEUR_CONTOUR, alpha);
        COULEUR_CLIQUEE = ObjetPhysique.setColorAlpha(COULEUR_CLIQUEE, alpha);

        g.setColor(estSurvolee ? COULEUR_CLIQUEE : COULEUR_REMPLISSAGE);
        g.fillOval(x - r, y - r, r * 2, r * 2);
        g.setColor(COULEUR_CONTOUR);
        g.drawOval(x - r, y - r, r * 2, r * 2);
    }

    public float distancePoint(Vec2 pos) {
        Vec2 centre = getPos();
        return (centre.sub(pos).length());
    }

    public boolean testLiaisonCliquee(Vec2 pos) {
        return (distancePoint(pos) <= RAYON_CLICK);
    }

}
