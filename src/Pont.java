import java.awt.Graphics;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Pont {

    LinkedList<Barre> barres;
    LinkedList<Liaison> liaisons;

    Liaison liaisonCliquee;

    final float LARGEUR_BARRE = 6;
    final float HAUTEUR_BARRE = 1;

    public Pont(World world, Vec2 pos) {

        barres = new LinkedList<Barre>();
        liaisons = new LinkedList<Liaison>();

        Liaison liaison = new Liaison(world, pos);
        liaisons.add(liaison);

        Vec2 posBarre = pos.add(new Vec2(LARGEUR_BARRE, 0));
        ajouterBarre(world, liaison, posBarre);

    }

    public void dessiner(Graphics g, Box2D box2d) {
        for (Barre barre : barres) {
            barre.dessiner(g, box2d);
        }
        for (Liaison liaison : liaisons) {
            liaison.dessiner(g, box2d);
        }
    }

    public Liaison ajouterBarre(World world, Liaison liaison, Vec2 posClic) {

        Vec2 centre = posClic.add(liaison.getPos()).mul(0.5f);
        Vec2 difference = posClic.sub(liaison.getPos());
        float angle = (float) Math.atan(difference.y / difference.x);
        Barre barre = new Barre(world, centre, angle, difference.length() - HAUTEUR_BARRE, HAUTEUR_BARRE);

        barre.lier(world, liaison);
        Liaison nouvelleLiaison = new Liaison(world, posClic);
        barre.lier(world, nouvelleLiaison);

        barres.add(barre);
        liaisons.add(nouvelleLiaison);
        return nouvelleLiaison;

    }

    public void testCasse(World world, float dt) {
        for (Barre barre : barres) {
            LinkedList<Liaison> liaisonsCrees = barre.testCasse(world, dt);
            liaisons.addAll(liaisonsCrees);
        }

    }

    public void liaisonCliquee(World world, Vec2 pos) {

        Liaison liaisonPlusProche = null;
        float distanceMin = Float.POSITIVE_INFINITY;
        for (Liaison liaison : liaisons) {
            if (liaison.estDanslaZone(pos)) {
                float distance = liaison.distancePoint(pos);
                if (distance < distanceMin) {
                    distanceMin = distance;
                    liaisonPlusProche = liaison;
                }
            }
        }

        if (liaisonCliquee == null) {
            if (liaisonPlusProche != null) {
                liaisonCliquee = liaisonPlusProche;
                liaisonCliquee.cliquee = true;
            }
        } else {
            ajouterBarre(world, liaisonCliquee, pos);
            liaisonCliquee.cliquee = false;
            liaisonCliquee = null;
        }

    }

}
