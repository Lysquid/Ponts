import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Liaison extends ObjetPhysique {

    final static int CATEGORY = 0b0100;
    final static int MASK = Bord.CATEGORY | CATEGORY;

    float rayon;

    public Liaison(World world, float x, float y, float rayon) {

        this.rayon = rayon;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(this.rayon);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

        body.createFixture(fixtureDef);
    }

    public void draw(Graphics g, Box2D box2d) {

        g.setColor(Color.CYAN);

        float x = getX();
        float y = getY();

        int Xcentre = box2d.worldToPixelX(x);
        int Ycentre = box2d.worldToPixelY(y);
        int r = box2d.worldToPixel(this.rayon);

        g.drawOval(Xcentre - r, Ycentre - r, r * 2, r * 2);

    }

}
