package ponts.physique.barres;

import java.awt.Color;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import ponts.physique.liaisons.Liaison;
import ponts.physique.voiture.Voiture;

public class BarreGoudron extends Barre {

    public static final int CATEGORY = 0b00010000;
    public static final int MASK = Barre.MASK | Voiture.CATEGORY;

    public BarreGoudron(World world, Liaison liaison1, Liaison liaison2) {
        super(world, liaison1, liaison2);

        couleurRemplissage = Color.decode("#333333");
        prixMateriau *= 3;
        forceMax *= 1;
    }

    public FixtureDef creerFixtureDef(Shape shape) {
        return creerFixtureDef(FRICTION, ELASTICITE, DENSITE, CATEGORY, MASK);
    }

}