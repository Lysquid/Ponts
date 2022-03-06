import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Liaison extends ObjetPhysique {

    final static int CATEGORY = 0b0100;
    final static int MASK = Bord.CATEGORY | CATEGORY | Barre.CATEGORY;

    final float RAYON = 0.5f;
    final Color couleur = new Color(224, 247, 74);

    public LinkedList<Barre> barresLiees;

    public Liaison(World world, float x, float y) {

        barresLiees = new LinkedList<Barre>();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);

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

        g.setColor(couleur);

        float x = getX();
        float y = getY();

        int Xcentre = box2d.worldToPixelX(x);
        int Ycentre = box2d.worldToPixelY(y);
        int r = box2d.worldToPixel(this.RAYON);

        g.fillOval(Xcentre - r, Ycentre - r, r * 2, r * 2);
        g.setColor(Color.BLACK);
        g.drawOval(Xcentre - r, Ycentre - r, r * 2, r * 2);
    }

}
