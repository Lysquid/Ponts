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

        Vec2 gauche = new Vec2(-largeur, 0);
        Vec2 droite = new Vec2(largeur, 0);
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