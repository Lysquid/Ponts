import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class GBox extends ImageIcon {
    Body body;

    float x, y;
    float w, h;
    float angle;

    public GBox(World world, float x, float y, float w, float h) {
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

        // Step 4 : Create fixture
        body.createFixture(ps, 1);

    }

}
