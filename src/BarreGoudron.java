import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Rot;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class BarreGoudron extends Barre {

    public BarreGoudron(World world, Vec2 pos, float angle, float longueur) {
        super(world, pos, angle, longueur);

        LARGEUR_BARRE = 1;

        COUPLE_RESISTANCE = 1000000f;
        FORCE_MAX = 3000f;

        COULEUR_REMPLISSAGE = Color.decode("#333333");

        longueur -= LARGEUR_BARRE;
        this.longueur = longueur;
        this.largeur = LARGEUR_BARRE;

        // Etape 3 : Définir la "shape"
        shape = new PolygonShape();
        shape.setAsBox(longueur / 2, largeur / 2);

        // Etape 4 : Définir la "fixture"
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        // fixtureDef.restitution = 0.5f;
        // fixtureDef.friction = 0.3f;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

        // Etape 5 : Attacher la shape au body avec la fixture
        body.createFixture(fixtureDef);
    }

}
