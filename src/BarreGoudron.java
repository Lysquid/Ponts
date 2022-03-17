import java.awt.Color;

import org.jbox2d.dynamics.World;

public class BarreGoudron extends Barre {

    public BarreGoudron(World world, Liaison liaison1, Liaison liaison2) {
        super(world, liaison1, liaison2);

        FORCE_MAX = 4000f;
        COULEUR_REMPLISSAGE = Color.decode("#333333");
        this.largeur = 1;

    }

}
