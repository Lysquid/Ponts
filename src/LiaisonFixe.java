import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class LiaisonFixe extends Liaison {

    Color COULEUR_REMPLISSAGE = Color.decode("#d33d3d");

    public LiaisonFixe(World world, Vec2 pos) {
        super(world, pos, false);
    }

    public void dessiner(Graphics g, Box2D box2d) {
        if (cliquee) {
            g.setColor(COULEUR_CLIQUEE);
        } else {
            g.setColor(COULEUR_REMPLISSAGE);
        }
        super.dessiner(g, box2d);
    }
}
