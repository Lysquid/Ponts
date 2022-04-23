package ponts.physique.barres;

import java.awt.Color;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import ponts.physique.liaisons.Liaison;

public class BarreBois extends Barre {

    public static final int CATEGORY = 0b00100000;
    public static final int MASK = Barre.MASK;

    public BarreBois(World world, Liaison liaison1, Liaison liaison2) {
        super(world, liaison1, liaison2);

        couleurRemplissage = Color.decode("#ba754a");
        prixMateriau *= 1;
        forceMax *= 0.2f;
    }

    public FixtureDef creerFixtureDef(Shape shape) {
        return creerFixtureDef(FRICTION, ELASTICITE, DENSITE, CATEGORY, MASK);
    }

}
