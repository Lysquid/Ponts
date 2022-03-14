import java.awt.Graphics;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Pont {

    LinkedList<Barre> barres;
    LinkedList<Liaison> liaisons;

    Liaison liaisonCliqueeAvant;

    public Pont(World world, Vec2 pos) {

        barres = new LinkedList<Barre>();
        liaisons = new LinkedList<Liaison>();

        Liaison liaison = new Liaison(world, pos);
        liaisons.add(liaison);

        ajouterBarre(world, liaison, pos.add(new Vec2(6, 0)));

    }

    public void dessiner(Graphics g, Box2D box2d) {
        for (Barre barre : barres) {
            barre.dessiner(g, box2d);
        }
        for (Liaison liaison : liaisons) {
            liaison.dessiner(g, box2d);
        }
    }

    public Liaison ajouterBarre(World world, Liaison liaison1, Liaison liaison2) {

        Vec2 centre = liaison1.getPos().add(liaison2.getPos()).mul(0.5f);
        Vec2 difference = liaison1.getPos().sub(liaison2.getPos());
        float angle = (float) Math.atan(difference.y / difference.x);
        Barre barre = new Barre(world, centre, angle, difference.length());

        barre.lier(world, liaison1);
        barre.lier(world, liaison2);

        barres.add(barre);
        return liaison2;

    }

    public Liaison ajouterBarre(World world, Liaison liaison, Vec2 posClic) {
        Liaison nouvelleLiaison = new Liaison(world, posClic);
        liaisons.add(nouvelleLiaison);
        return ajouterBarre(world, liaison, nouvelleLiaison);
    }

    public void testCasse(World world, float dt) {
        for (Barre barre : barres) {
            LinkedList<Liaison> liaisonsCrees = barre.testCasse(world, dt);
            liaisons.addAll(liaisonsCrees);
        }

    }

    public void testSiLiaisonCliquee(World world, Vec2 pos) {

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

        if (liaisonCliqueeAvant == null) {
            if (liaisonPlusProche != null) {
                liaisonCliqueeAvant = liaisonPlusProche;
                liaisonCliqueeAvant.cliquee = true;
            }
        } else {
            if (liaisonPlusProche == null) {
                ajouterBarre(world, liaisonCliqueeAvant, pos);
            } else {
                ajouterBarre(world, liaisonCliqueeAvant, liaisonPlusProche);
            }
            liaisonCliqueeAvant.cliquee = false;
            liaisonCliqueeAvant = null;
        }

    }

}
