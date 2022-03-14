import java.awt.Color;
import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class LiaisonMobile extends Liaison {

    Color COULEUR_REMPLISSAGE = Color.decode("#e3f069");

    public LiaisonMobile(World world, Vec2 pos) {
        super(world, pos, true);
    }

    public void supprimer(World world) {
        world.destroyBody(body);
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
