import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class Bord extends ObjetPhysique {

    static final int CATEGORY = 0b0001;
    static final int MASK = 0b1111;

    float largeur;
    float hauteur;
    float angle;

    public Bord(World world, float frameX, float frameY) {

        float x = frameX / 2;
        float y = 0;
        largeur = frameX * 10;
        hauteur = 0.1f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largeur / 2, hauteur / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

        body.createFixture(fixtureDef);

    }
}