package ponts.physique.environnement;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import ponts.physique.ObjetPhysique;

public class Bord extends ObjetPhysique {

    public static final int CATEGORY = 0b0001;
    public static final int MASK = 0b1111;

    public Bord(World world, float frameX, float frameY) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set(frameX / 2, 0);

        body = world.createBody(bodyDef);

        Vec2 gauche = new Vec2(-frameX * 9, 0);
        Vec2 droite = new Vec2(frameX * 10, 0);
        EdgeShape shape = new EdgeShape();
        shape.set(gauche, droite);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

        body.createFixture(fixtureDef);

    }
}