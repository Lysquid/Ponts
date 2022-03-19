package ponts.physique.barres;

import java.awt.Color;

import org.jbox2d.dynamics.World;

import ponts.physique.liaisons.Liaison;

public class BarreBois extends Barre {

    public BarreBois(World world, Liaison liaison1, Liaison liaison2) {
        super(world, liaison1, liaison2);

        forceMax = 4000f;
        couleurRemplissage = Color.decode("#ba754a");
        this.largeur = 1;

    }

}
