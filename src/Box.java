import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class Box {

    Body body;

    float x, y;
    float w, h;
    float angle;

    public Box(World world, float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        // Step 1 : Body def
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(x, y);

        // Step 1 : Create body
        body = world.createBody(bd);

        // Step 3 : Create Shape
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(w / 2, h / 2);

        // Step 4 : Create Fixture
        // FixtureDef fd = new FixtureDef();
        // fd.shape = ps;
        // fd.density = 1;
        // fd.friction = 0.3f;
        // fd.restitution = 0.5f;

        // Step 5 : Attach Shape to Body with Fixture
        // body.createFixture(fd);
        body.createFixture(ps, 1.0f);
        // body.setLinearVelocity(new Vec2(1000f, 100000f));

    }

    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        Vec2 v = body.getTransform().p;
        x = v.x;
        y = v.y;
        angle = body.getAngle();

        int[] xCoords = new int[4];
        int[] yCoords = new int[4];

        int[][] mult = { { 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };
        for (int i = 0; i < 4; i++) {
            float x2 = (x + w / 2 * mult[i][0]);
            float y2 = (y + h / 2 * mult[i][1]);
            xCoords[i] = (int) ((x2 - x) * Math.cos(angle) - (y2 - y) * Math.sin(angle) + x);
            yCoords[i] = (int) ((x2 - x) * Math.sin(angle) + (y2 - y) * Math.cos(angle) + y);
        }

        // g.drawRect((int) v.x, (int) v.y, (int) w, (int) h);
        g.drawPolygon(xCoords, yCoords, 4);
    }

}
