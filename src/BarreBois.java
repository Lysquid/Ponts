import java.awt.Color;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class BarreBois extends Barre {

    public BarreBois(World world) {
        super(world);

        LARGEUR_BARRE = 1;

        COUPLE_RESISTANCE = 1000000f * 0;
        FORCE_MAX = 4000f;

        COULEUR_REMPLISSAGE = Color.decode("#ba754a");

        // longueur -= LARGEUR_BARRE;
        this.longueur = longueur;
        this.largeur = LARGEUR_BARRE;

    }

}
