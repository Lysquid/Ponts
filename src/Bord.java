import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class Bord extends ObjetPhysique {

    float w, h;
    float angle;

    public Bord(World world, float frameX, float frameY) {

        float x = frameX / 2;
        float y = 0;
        this.w = (float) frameX;
        this.h = 0.1f;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(x, y);

        body = world.createBody(bd);

        PolygonShape ps = new PolygonShape();
        ps.setAsBox(w / 2, h / 2);

        body.createFixture(ps, 0);

    }
}