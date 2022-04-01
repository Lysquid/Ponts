package ponts.physique;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ponts.ihm.Box2D;
import ponts.physique.barres.Barre;
import ponts.physique.barres.BarreBois;
import ponts.physique.barres.BarreGoudron;
import ponts.physique.barres.Materiau;
import ponts.physique.liaisons.Liaison;
import ponts.physique.liaisons.LiaisonFixe;
import ponts.physique.liaisons.LiaisonMobile;

public class Pont implements Serializable {

    LinkedList<Barre> barres;
    LinkedList<Liaison> liaisons;
    Barre barreEnCreation;
    Liaison liaisonEnCreation;
    Liaison liaisonProche;

    public Pont(World world, Box2D box2d) {

        barres = new LinkedList<Barre>();
        liaisons = new LinkedList<Liaison>();

        liaisons.add(new LiaisonFixe(world, new Vec2(box2d.getLargeur() * 0.5f, box2d.getHauteur() * 0.05f)));
        liaisons.add(new LiaisonFixe(world, new Vec2(box2d.getLargeur() * 0.15f, box2d.getHauteur() * 0.4f)));
        liaisons.add(new LiaisonFixe(world, new Vec2(box2d.getLargeur() * 0.86f, box2d.getHauteur() * 0.43f)));

    }

    public void dessiner(Graphics g, Box2D box2d, Vec2 posSouris) {
        LinkedList<Barre> barresDesinees = new LinkedList<Barre>();
        for (Liaison liaison : liaisons) {
            for (Barre barre : liaison.getBarresLiees()) {
                if (!barresDesinees.contains(barre) && barre != barreEnCreation) {
                    barre.dessiner(g, box2d);
                    barresDesinees.add(barre);
                }
            }
            if (liaison != liaisonEnCreation) {
                liaison.dessiner(g, box2d, false);
            }
        }
        // On dessine les objets en creation a la fin pour qu'il apparaissent par dessus
        // les autres
        if (barreEnCreation != null) {
            barreEnCreation.dessiner(g, box2d);
            liaisonEnCreation.dessiner(g, box2d, false);
        }
        // On redessine la liaison proche pour quelle soit d'une autre couleur
        // et qu'elle se supperpose sur la liaison en création
        Liaison liaisonSurvolee = recupLiaisonProche(posSouris);
        if (liaisonSurvolee != null) {
            liaisonSurvolee.dessiner(g, box2d, true);
        }
    }

    public void gererInput(World world, Vec2 posSouris, int boutonSouris, boolean clicSouris, Materiau materiau) {

        Vec2 posSourisMax = posSourisMax(barreEnCreation, posSouris);
        liaisonProche = recupLiaisonProche(posSourisMax);
        boolean barreValide = barreValide(barreEnCreation, liaisonProche);

        if (barreEnCreation != null) {

            if (!barreEnCreation.inferieurLongeurMax(posSouris)) {
                majPreview(posSourisMax);
                liaisonProche = null;
            } else if (liaisonProche != null && barreValide) {
                majPreview(liaisonProche.getPos());
            } else {
                majPreview(posSouris);
            }

        }

        if (clicSouris) {

            switch (boutonSouris) {

                case 1: // clic gauche

                    // Test si la barre vérifie des conditions
                    if (barreValide) {
                        if (liaisonProche != null) {
                            barreEnCreation.accrocher(world, liaisonProche);
                            liaisons.remove(liaisonEnCreation);
                        } else {
                            liaisonProche = liaisonEnCreation;
                        }
                        lacherBarre(world);
                    }

                    // Cas ou on doit creer une nouvelle barre
                    if (barreEnCreation == null && liaisonProche != null) {
                        creerBarre(world, posSouris, liaisonProche, materiau);
                        posSourisMax = posSourisMax(barreEnCreation, posSouris);
                        majPreview(posSourisMax);
                    }

                    break;

                case 3: // clic droit
                    if (barreEnCreation != null) {
                        arreterCreation(world);
                    } else {
                        supprimerBarresCliquees(world, posSouris);
                    }

                    break;

            }
        }

    }

    private Vec2 posSourisMax(Barre barre, Vec2 posSouris) {
        if (barre == null) {
            return posSouris;
        } else if (barre.inferieurLongeurMax(posSouris)) {
            return posSouris;
        } else {
            return barreEnCreation.posLiaisonMax(posSouris);
        }
    }

    private boolean barreValide(Barre barre, Liaison liaisonProche) {
        // barre n'existe pas
        if (barre == null)
            return false;
        // taille minimum
        if (!barre.tailleMinimum())
            return false;
        // si elle est proche d'une liaison existante
        if (liaisonProche != null) {
            // la barre reliant les deux liaisons n'existe pas deja
            if (barreExisteDeja(barre, liaisonProche))
                return false;
            // la barre ne boucle pas sur elle même
            if (barre.getLiaisonsLiees().contains(liaisonProche))
                return false;
        }
        return true;
    }

    private boolean barreExisteDeja(Barre barreATester, Liaison liaison2) {
        if (liaison2 == null) {
            return false;
        }
        Liaison liaison1 = barreATester.getLiaisonsLiees().get(0);
        for (Barre barre : liaison1.getBarresLiees()) {
            if (barre.getLiaisonsLiees().contains(liaison2)) {
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
        barreEnCreation.activerPhysique();

        for (Liaison liaison : barreEnCreation.getLiaisonsLiees()) {
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

    public Liaison recupLiaisonProche(Vec2 posClic) {
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
