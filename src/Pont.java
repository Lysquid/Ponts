import java.awt.Graphics;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Pont {

    LinkedList<Barre> barres;

    Liaison liaisonCliquee;

    final float BAR_W = 6;
    final float BAR_H = 1;

    public Pont(World world, float x, float y) {

        liaisonCliquee = null;

        barres = new LinkedList<Barre>();

        Liaison liaison = new Liaison(world, x, y);

        ajouterBarre(world, liaison, x + BAR_W, y);

    }

    public void dessiner(Graphics g, Box2D box2d) {
        for (Barre barre : barres) {
            barre.dessiner(g, box2d);
            for (Liaison liaison : barre.liaisons) {
                liaison.dessiner(g, box2d);
            }
        }
    }

    public void ajouterBarre(World world, Liaison liaison, float x, float y) {

        Vec2 positionClique = new Vec2(x, y);
        Vec2 centre = positionClique.add(liaison.getPos()).mul(0.5f);

        Vec2 difference = positionClique.sub(liaison.getPos());
        float angle = (float) Math.atan(difference.y / difference.x);

        Barre barre = new Barre(world, centre.x, centre.y, angle, difference.length() - BAR_H, BAR_H);

        barre.lier(world, liaison);

        Liaison liaison2 = new Liaison(world, positionClique.x, positionClique.y);
        barre.lier(world, liaison2);

        barres.add(barre);

    }

    public void testCasse(World world, float dt) {
        for (Barre barre : barres) {
            barre.testCasse(world, dt);

        }

    }

    public LinkedList<Liaison> getLiaisons() {
        LinkedList<Liaison> ListeLiaisons = new LinkedList<Liaison>();
        for (Barre barre : barres) {
            for (Liaison liaison : barre.liaisons) {
                if (!ListeLiaisons.contains(liaison)) {
                    ListeLiaisons.add(liaison);
                }
            }
        }
        return ListeLiaisons;

    }

    public void liaisonCliquee(World world, float x, float y) {

        Liaison liaisonTemp = null;
        float longueurTemp = Float.POSITIVE_INFINITY;
        for (Liaison liaison : getLiaisons()) {
            if (liaison.estDanslaZone(x, y)) {
                if (liaison.donneMoiLaDistance(x, y) <= longueurTemp) {
                    longueurTemp = liaison.donneMoiLaDistance(x, y);
                    liaisonTemp = liaison;
                }
            }
        }

        if (liaisonCliquee == null) {
            liaisonCliquee = liaisonTemp;
            liaisonCliquee.cliquee = true;
        } else {
            liaisonCliquee.cliquee = false;
            ajouterBarre(world, liaisonCliquee, x, y);
            liaisonCliquee = null;
        }

    }

}
