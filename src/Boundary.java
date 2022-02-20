import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class Boundary {

    Body body;

    float x, y;
    float w, h;
    float angle;

    public Boundary(World world, int frameX, int frameY) {
        this.x = (float) frameX / 2;
        this.y = (float) frameY;
        this.w = (float) frameX;
        this.h = 2f;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(x, y);

        body = world.createBody(bd);

        PolygonShape ps = new PolygonShape();
        ps.setAsBox(w / 2, h / 2);

        body.createFixture(ps, 0);

    }
}