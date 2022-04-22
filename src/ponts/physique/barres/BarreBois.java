package ponts.physique.barres;

import java.awt.Color;

import org.jbox2d.dynamics.World;

import ponts.physique.liaisons.Liaison;

public class BarreBois extends Barre {

    public static final int CATEGORY = 0b00100000;
    public static final int MASK = Barre.MASK;

    public BarreBois(World world, Liaison liaison1, Liaison liaison2) {
        super(world, liaison1, liaison2);

        couleurRemplissage = Color.decode("#ba754a");
        prixMateriau *= 1;
        forceMax *= 1;
        elasticite *= 1;

    }

    @Override
    public void definirFiltre() {
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;
    }

}
