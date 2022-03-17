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

        Liaison liaisonProche = testLiaisonProche(posSouris);

        if (barreEnCreation != null) {
            if (liaisonProche == null || liaisonProche == barreEnCreation.liaisonsLiees.get(0)) {
                majPreview(posSouris);
            } else {
                majPreview(liaisonProche.getPos());
            }
        }

        if (clicSouris) {

            switch (boutonSouris) {

                case "gauche":

                    if (barreEnCreation != null) {
                        if (barreEnCreation.tailleMinimum()) {
                            if (liaisonProche != null) {
                                if (!barreExisteDeja(barreEnCreation, liaisonProche)) {
                                    barreEnCreation.accrocher(world, liaisonProche);
                                }
                            }
                            liaisonProche = liaisonEnCreation;
                            lacherBarre(world);
                        }
                    }
                    if (barreEnCreation == null && liaisonProche != null) {
                        creerBarre(world, posSouris, liaisonProche, materiau);
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

    private boolean barreExisteDeja(Barre barreATester, Liaison liaison2) {
        Liaison liaison1 = barreATester.liaisonsLiees.get(0);
        for (Barre barre : liaison1.barresLiees) {
            if (barre.liaisonsLiees.contains(liaison2)) {
                return true;
            }
        }
        return false;
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
        barreEnCreation.activerPhysique(world);

        for (Liaison liaison : barreEnCreation.liaisonsLiees) {
            liaison.activerPhysique();
            barreEnCreation.lier(world, liaison);
        }

        liaisonEnCreation = null;
        barreEnCreation = null;

    }

    private void majPreview(Vec2 posSouris) {
        liaisonEnCreation.setPos(posSouris);
        barreEnCreation.ajusterPos();
    }

    public Liaison testLiaisonProche(Vec2 posClic) {
        Liaison liaisonPlusProche = null;
        float distanceMin = Float.POSITIVE_INFINITY;
        for (Liaison liaison : liaisons) {
            if (liaison.testLiaisonCliquee(posClic) && liaison != liaisonEnCreation) {
                float distance = liaison.distancePoint(posClic);
                if (distance < distanceMin) {
                    distanceMin = distance;
                    liaisonPlusProche = liaison;
                }
            }
        }
        return liaisonPlusProche;
    }

    public void creerBarre(World world, Vec2 posClic, Liaison liaisonCliquee, Materiau materiau) {

        liaisonEnCreation = new LiaisonMobile(world, posClic);
        liaisons.add(liaisonEnCreation);

        Liaison liaison1 = liaisonCliquee;
        Liaison liaison2 = liaisonEnCreation;

        switch (materiau) {
            case BOIS:
                barreEnCreation = new BarreBois(world, liaison1, liaison2);
                break;
            case GOUDRON:
                barreEnCreation = new BarreGoudron(world, liaison1, liaison2);
                break;
        }

        barres.add(barreEnCreation);

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

    public void testCasse(World world, float dt) {
        for (Barre barre : barres) {
            if (barre != barreEnCreation) {
                LinkedList<Liaison> liaisonsCrees = barre.testCasse(world, dt);
                liaisons.addAll(liaisonsCrees);
            }
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
