import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Liaison extends ObjetPhysique {

    final static int CATEGORY = 0b0100;
    final static int MASK = Bord.CATEGORY | Liaison.CATEGORY | Barre.CATEGORY;

    final float RAYON = 0.5f;
    final Color COULEUR_REMPLISSAGE = Color.decode("#e3f069");
    final Color COULEUR_CONTOUR = Color.BLACK;
    final Color COULEUR_CLIQUEE = Color.decode("#e86933");

    LinkedList<Barre> barresLiees;
    boolean cliquee;

    public Liaison(World world, Vec2 pos) {

        barresLiees = new LinkedList<Barre>();

        cliquee = false;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(pos);

        body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(this.RAYON);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

        body.createFixture(fixtureDef);

    }

    public void dessiner(Graphics g, Box2D box2d) {

        int x = box2d.worldToPixelX(getX());
        int y = box2d.worldToPixelY(getY());
        int r = box2d.worldToPixel(this.RAYON);

        if (cliquee) {
            g.setColor(COULEUR_CLIQUEE);
        } else {
            g.setColor(COULEUR_REMPLISSAGE);
        }
        g.fillOval(x - r, y - r, r * 2, r * 2);
        g.setColor(COULEUR_CONTOUR);
        g.drawOval(x - r, y - r, r * 2, r * 2);
    }

    public float distancePoint(Vec2 pos) {
        Vec2 centre = getPos();
        return (centre.sub(pos).length());
    }

    public boolean estDanslaZone(Vec2 pos) {
        return (distancePoint(pos) <= RAYON);
    }

}
