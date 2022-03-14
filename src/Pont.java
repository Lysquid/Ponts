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

        Liaison liaison = new LiaisonFixe(world, pos);
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

    public Liaison ajouterBarre(World world, Liaison liaison, Vec2 pos) {
        LiaisonMobile nouvelleLiaison = new LiaisonMobile(world, pos);
        liaisons.add(nouvelleLiaison);
        return ajouterBarre(world, liaison, nouvelleLiaison);
    }

    public void testCasse(World world, float dt) {
        for (Barre barre : barres) {
            LinkedList<Liaison> liaisonsCrees = barre.testCasse(world, dt);
            liaisons.addAll(liaisonsCrees);
        }

    }

    public void gererClique(World world, Vec2 posClic) {

        testLiaisonCliquee(world, posClic);
        testBarreCliquee(world, posClic);

    }

    public void testLiaisonCliquee(World world, Vec2 posClic) {
        Liaison liaisonPlusProche = null;
        float distanceMin = Float.POSITIVE_INFINITY;
        for (Liaison liaison : liaisons) {
            if (liaison.testLiaisonCliquee(posClic)) {
                float distance = liaison.distancePoint(posClic);
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

            Vec2 difference = posClic.sub(liaisonCliqueeAvant.getPos());
            if (difference.length() >= Barre.TAILLE_MIN) {
                if (difference.length() <= Barre.TAILLE_MAX) {
                    if (liaisonPlusProche == null) {
                        ajouterBarre(world, liaisonCliqueeAvant, posClic);
                    } else {
                        ajouterBarre(world, liaisonCliqueeAvant, liaisonPlusProche);
                    }
                } else {
                    Vec2 pos = difference.mul(Barre.TAILLE_MAX / difference.length()).add(liaisonCliqueeAvant.getPos());
                    ajouterBarre(world, liaisonCliqueeAvant, pos);
                }
                liaisonCliqueeAvant.cliquee = false;
                liaisonCliqueeAvant = null;
            }
        }
    }

    public void testBarreCliquee(World world, Vec2 posClic) {
        Barre barreASupprimer = null;
        for (Barre barre : barres) {
            if (barre.testBarreCliquee(posClic)) {
                LinkedList<LiaisonMobile> liaisonASupprimer = barre.supprimer(world);
                liaisons.removeAll(liaisonASupprimer);
                barreASupprimer = barre;

            }
        }
        if (barreASupprimer != null) {
            barres.remove(barreASupprimer);
        }
    }

}
