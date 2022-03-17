import java.awt.Graphics;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Pont {

    LinkedList<Barre> barres;
    LinkedList<Liaison> liaisons;
    Barre barreEnCreation;
    Liaison liaisonEnCreation;

    public Pont(World world, Box2D box2d) {

        barres = new LinkedList<Barre>();
        liaisons = new LinkedList<Liaison>();

        liaisons.add(new LiaisonFixe(world, new Vec2(box2d.largeur * 0.5f, box2d.hauteur * 0.05f)));
        liaisons.add(new LiaisonFixe(world, new Vec2(box2d.largeur * 0.15f, box2d.hauteur * 0.4f)));
        liaisons.add(new LiaisonFixe(world, new Vec2(box2d.largeur * 0.86f, box2d.hauteur * 0.43f)));

    }

    public void dessiner(Graphics g, Box2D box2d) {
        LinkedList<Barre> barresDesinees = new LinkedList<Barre>();
        for (Liaison liaison : liaisons) {
            for (Barre barre : liaison.barresLiees) {
                if (!barresDesinees.contains(barre)) {
                    barre.dessiner(g, box2d);
                    barresDesinees.add(barre);
                }
            }
            liaison.dessiner(g, box2d);
        }
    }

    public void gererInput(World world, Vec2 posSouris, String boutonSouris, boolean clicSouris, Materiau materiau) {

        if (barreEnCreation != null) {
            majPreview(posSouris);
        }

        if (clicSouris) {

            switch (boutonSouris) {

                case "gauche":
                    Liaison liaisonCliquee = testLiaisonCliquee(posSouris);

                    if (barreEnCreation != null) {
                        liaisonCliquee = liaisonEnCreation;
                        lacherBarre(world);
                    }
                    if (barreEnCreation == null) {
                        ajouterBarre(world, posSouris, liaisonCliquee, materiau);
                    }

                    break;

                case "droite":
                    if (barreEnCreation != null) {
                        arreterCreation(world);
                    } else {
                        supprimerBarresCliquees(world, posSouris);
                    }

                    break;

                default:

                    break;
            }
        }

    }

    public void arreterCreation(World world) {
        supprimerBarre(world, barreEnCreation);
        barreEnCreation = null;
        liaisonEnCreation = null;
    }

    private void supprimerBarre(World world, Barre barre) {
        LinkedList<LiaisonMobile> liaisonASupprimer = barre.supprimer(world);
        barres.remove(barreEnCreation);
        liaisons.removeAll(liaisonASupprimer);
    }

    private void lacherBarre(World world) {
        barreEnCreation.initiliserPhysique(world);

        for (Liaison liaison : barreEnCreation.liaisonsLiees) {
            liaison.initialiserPhysique();
            barreEnCreation.lier(world, liaison);
        }

        liaisonEnCreation = null;
        barreEnCreation = null;

    }

    private void majPreview(Vec2 posSouris) {
        liaisonEnCreation.setPos(posSouris);
        barreEnCreation.ajusterPos();
    }

    public Liaison testLiaisonCliquee(Vec2 posClic) {
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
        return liaisonPlusProche;
    }

    public void ajouterBarre(World world, Vec2 posClic, Liaison liaisonCliquee, Materiau materiau) {

        liaisonEnCreation = new LiaisonMobile(world, posClic);
        liaisons.add(liaisonEnCreation);
        barreEnCreation = creerBarre(world, liaisonCliquee, liaisonEnCreation, materiau);

        // if (liaisonCliqueeAvant == null) {
        // // Selection d'une liaison d'origine
        // if (liaisonCliquee != null) { // seulement si on a cliquée sur une liaison
        // liaisonCliqueeAvant = liaisonCliquee;
        // liaisonCliqueeAvant.cliquee = true;
        // }
        // } else {
        // // Ajout d'une barre

        // Vec2 difference = posClic.sub(liaisonCliqueeAvant.getPos());
        // if (difference.length() >= Barre.LONGUEUR_MIN) {
        // // Si la longueur est bien superieure

        // Liaison derniereLiaison = null;
        // if (difference.length() <= Barre.LONGUEUR_MAX) {
        // if (liaisonCliquee == null) {
        // derniereLiaison = creerBarre(world, liaisonCliqueeAvant, posClic, materiau);
        // } else {
        // derniereLiaison = creerBarre(world, liaisonCliqueeAvant, liaisonCliquee,
        // materiau);
        // }
        // } else {
        // Vec2 pos = difference.mul(Barre.LONGUEUR_MAX / difference.length())
        // .add(liaisonCliqueeAvant.getPos());
        // derniereLiaison = creerBarre(world, liaisonCliqueeAvant, pos, materiau);
        // }
        // liaisonCliqueeAvant.cliquee = false;
        // liaisonCliqueeAvant = null;

        // liaisonCliqueeAvant = derniereLiaison;
        // liaisonCliqueeAvant.cliquee = true;
        // }
        // }
    }

    public Barre creerBarre(World world, Liaison liaison1, Liaison liaison2, Materiau materiau) {

        // boolean dejaLiees = false;
        // for (Barre bar : liaison1.barresLiees) {
        // if (bar.liaisonsLiees.contains(liaison2)) {
        // dejaLiees = true;
        // break;
        // }
        // }

        Barre barre = null;
        // if (!dejaLiees) {
        switch (materiau) {
            case BOIS:
                barre = new BarreBois(world, liaison1, liaison2);
                break;
            case GOUDRON:
                barre = new BarreGoudron(world, liaison1, liaison2);
                break;
        }

        barres.add(barre);
        // }

        return barre;

    }

    // public Liaison creerBarre(World world, Liaison liaison, Vec2 pos, Materiau
    // materiau) {
    // LiaisonMobile nouvelleLiaison = new LiaisonMobile(world, pos);
    // liaisons.add(nouvelleLiaison);
    // return creerBarre(world, liaison, nouvelleLiaison, materiau);
    // }

    public void testCasse(World world, float dt) {
        for (Barre barre : barres) {
            // LinkedList<Liaison> liaisonsCrees = barre.testCasse(world, dt);
            // liaisons.addAll(liaisonsCrees);
        }

    }

    public void supprimerBarresCliquees(World world, Vec2 posClic) {
        for (Barre barre : barres) {
            if (barre.testBarreCliquee(posClic)) {
                supprimerBarre(world, barre);
            }
        }
    }

}
