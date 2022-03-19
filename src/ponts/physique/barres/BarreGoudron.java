package ponts.physique.barres;

import java.awt.Color;

import org.jbox2d.dynamics.World;

import ponts.physique.liaisons.Liaison;

public class BarreGoudron extends Barre {

    public BarreGoudron(World world, Liaison liaison1, Liaison liaison2) {
        super(world, liaison1, liaison2);

        forceMax = 4000f;
        couleurRemplissage = Color.decode("#333333");
        this.largeur = 1;

    }

}
