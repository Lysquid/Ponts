import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Barre extends ObjetPhysique {

    final static int CATEGORY = 0b0010;
    final static int MASK = Bord.CATEGORY | CATEGORY;

    float longueur, hauteur;
    float angle;

    public Barre(World world, float x, float y, float longueur, float hauteur) {
        this.longueur = longueur;
        this.hauteur = hauteur;

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

    public void draw(Graphics g, Box2D box2d) {

        g.setColor(Color.CYAN);

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

        g.drawPolygon(xCoords, yCoords, 4);

    }

}
