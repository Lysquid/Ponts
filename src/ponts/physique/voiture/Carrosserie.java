package ponts.physique.voiture;

import java.util.Locale.Category;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.PolygonAndCircleContact;

import ponts.physique.ObjetPhysique;
import ponts.physique.barres.Barre;
import ponts.physique.environnement.Bord;

public class Carrosserie extends ObjetPhysique {
    public static final int CATEGORY = 0b1000;
    public static final int MASK = Bord.CATEGORY | Barre.CATEGORY;

    FixtureDef fixtureDef;
    Fixture fixture;
    PolygonShape shape;

    public Carrosserie(World world, Vec2 pos ) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(pos);

        body = world.createBody(bodyDef);
        shape = new PolygonShape();

        fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;

        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

    }

}
