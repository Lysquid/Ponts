import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class Liaison extends ObjetPhysique {

    float rayon;

    public Liaison(World world, float x, float y, float rayon) {

        this.rayon = rayon;

        // Créer une "body definition"
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(x, y);

        // Créer le body
        body = world.createBody(bd);
        CircleShape shape = new CircleShape();
        shape.setRadius(this.rayon);

        body.createFixture(shape, 1);
    }

    public void draw(Graphics g, Box2D box2d) {

        g.setColor(Color.CYAN);

        float x = getX();
        float y = getY();

        int Xcentre = box2d.worldToPixelX(x);
        int Ycentre = box2d.worldToPixelY(y);
        int r = box2d.worldToPixel(this.rayon);

        g.drawOval(Xcentre, Ycentre, r*2, r*2);

    }

}
