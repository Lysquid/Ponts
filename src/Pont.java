import java.awt.Graphics;
import java.util.LinkedList;

import org.jbox2d.dynamics.World;

public class Pont {

    LinkedList<Barre> barres;
    Liaison liaison;

    final float BAR_W = 6;
    final float BAR_H = 1;

    public Pont(World world, float x, float y) {

        barres = new LinkedList<Barre>();

        liaison = new Liaison(world, x, y);

        ajouterBarre(world, liaison);
        ajouterBarre(world, barres.getLast().liaisons.get(1));
        ajouterBarre(world, barres.getLast().liaisons.get(1));
        ajouterBarre(world, barres.getLast().liaisons.get(1));

    }

    public void dessiner(Graphics g, Box2D box2d) {
        for (Barre barre : barres) {
            barre.dessiner(g, box2d);
            for (Liaison liaison : barre.liaisons) {
                liaison.dessiner(g, box2d);
            }
        }
    }

    public void ajouterBarre(World world, Liaison liaison) {

        Barre barre = new Barre(world, liaison.getX() + BAR_W / 2 + BAR_H / 2, liaison.getY(), BAR_W, BAR_H);

        barre.lier(world, liaison);

        Liaison liaison2 = new Liaison(world, liaison.getX() + BAR_W + BAR_H, liaison.getY());
        barre.lier(world, liaison2);

        barres.add(barre);

    }

    public void testCasse(World world, float dt) {
        for (Barre barre : barres) {
            barre.testCasse(world, dt);

        }

    }

}
