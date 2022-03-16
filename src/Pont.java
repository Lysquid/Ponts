import java.awt.Graphics;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Pont {

    LinkedList<Barre> barres;
    LinkedList<Liaison> liaisons;
    Liaison liaisonCliqueeAvant;

    public Pont(World world, Box2D box2d) {

        barres = new LinkedList<Barre>();
        liaisons = new LinkedList<Liaison>();

        liaisons.add(new LiaisonFixe(world, new Vec2(box2d.largeur * 0.5f, box2d.hauteur * 0.05f)));
        liaisons.add(new LiaisonFixe(world, new Vec2(box2d.largeur * 0.15f, box2d.hauteur * 0.4f)));
        liaisons.add(new LiaisonFixe(world, new Vec2(box2d.largeur * 0.86f, box2d.hauteur * 0.43f)));

    }

    public void dessiner(Graphics g, Box2D box2d) {
        for (Liaison liaison : liaisons) {
            liaison.dessiner(g, box2d);
            for (Barre barre : liaison.barresLiees) {
                barre.dessiner(g, box2d);
            }
        }
    }

    public Liaison ajouterBarre(World world, Liaison liaison1, Liaison liaison2, Materiau materiau) {

        Vec2 centre = liaison1.getPos().add(liaison2.getPos()).mul(0.5f);
        Vec2 difference = liaison1.getPos().sub(liaison2.getPos());
        float angle = (float) Math.atan(difference.y / difference.x);
        Barre barre = null;

        boolean dejaLiees = false;
        for (Barre bar : liaison1.barresLiees) {
            if (bar.liaisonsLiees.contains(liaison2)) {
                dejaLiees = true;
                break;
            }
        }

        if (!dejaLiees) {
            switch (materiau) {
                case BOIS:
                    barre = new BarreBois(world, centre, angle, difference.length());
                    break;
                case GOUDRON:
                    barre = new BarreGoudron(world, centre, angle, difference.length());
                    break;
            }

            barre.lier(world, liaison1);
            barre.lier(world, liaison2);

            barres.add(barre);
        }

        return liaison2;

    }

    public Liaison ajouterBarre(World world, Liaison liaison, Vec2 pos, Materiau materiau) {
        LiaisonMobile nouvelleLiaison = new LiaisonMobile(world, pos);
        liaisons.add(nouvelleLiaison);
        return ajouterBarre(world, liaison, nouvelleLiaison, materiau);
    }

    public void testCasse(World world, float dt) {
        for (Barre barre : barres) {
            LinkedList<Liaison> liaisonsCrees = barre.testCasse(world, dt);
            liaisons.addAll(liaisonsCrees);
        }

    }

    public void gererClique(World world, Vec2 posClic, String boutonSouris, Materiau materiau) {
        switch (boutonSouris) {
            case "gauche":
                testLiaisonCliquee(world, posClic, materiau);
                break;
            case "droite":
                testBarreCliquee(world, posClic);
            default:
                break;
        }

    }

    public void testLiaisonCliquee(World world, Vec2 posClic, Materiau materiau) {
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
            if (difference.length() >= Barre.LONGUEUR_MIN) {

                Liaison derniereLiaison = null;
                if (difference.length() <= Barre.LONGUEUR_MAX) {
                    if (liaisonPlusProche == null) {
                        derniereLiaison = ajouterBarre(world, liaisonCliqueeAvant, posClic, materiau);
                    } else {
                        derniereLiaison = ajouterBarre(world, liaisonCliqueeAvant, liaisonPlusProche, materiau);
                    }
                } else {
                    Vec2 pos = difference.mul(Barre.LONGUEUR_MAX / difference.length())
                            .add(liaisonCliqueeAvant.getPos());
                    derniereLiaison = ajouterBarre(world, liaisonCliqueeAvant, pos, materiau);
                }
                liaisonCliqueeAvant.cliquee = false;
                liaisonCliqueeAvant = null;

                liaisonCliqueeAvant = derniereLiaison;
                liaisonCliqueeAvant.cliquee = true;
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
            if (barreASupprimer.liaisonsLiees.contains(liaisonCliqueeAvant)) {
                liaisonCliqueeAvant.cliquee = false;
                liaisonCliqueeAvant = null;
            }
        } else {
            if (liaisonCliqueeAvant != null) {
                liaisonCliqueeAvant.cliquee = false;
                liaisonCliqueeAvant = null;
            }
        }
    }

}
