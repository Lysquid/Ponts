package ponts.physique;

import java.awt.Graphics2D;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ponts.ihm.Box2D;
import ponts.ihm.Jeu;
import ponts.niveau.Niveau;
import ponts.physique.barres.Materiau;
import ponts.physique.environnement.Bord;
import ponts.physique.voiture.Voiture;

/**
 * Classe gérant une partie
 */
public class Partie {

    private Jeu jeu;
    private World world;
    private Pont pont;
    private Bord bord;
    private Voiture voiture;
    private int budget;

    private boolean simulationPhysique = false;
    private boolean creationPont = true;
    private boolean terminee = false;
    private Materiau materiau = Materiau.GOUDRON;

    /**
     * Constructeur d'une partie
     * 
     * @param jeu
     * @param box2d
     * @param niveau
     */
    public Partie(Jeu jeu, Box2D box2d, Niveau niveau) {
        this.jeu = jeu;

        niveau.centrer(box2d);
        niveau.ajouterExtremitees(box2d);

        Vec2 gravity = new Vec2(0.0f, -9.81f);
        world = new World(gravity);

        bord = new Bord(world, niveau);
        pont = new Pont(world, niveau);
        voiture = new Voiture(world, niveau);
        budget = niveau.getBudget();

    }

    public boolean getSimulationPhysique() {
        return simulationPhysique;
    }

    public void setSimulationPhsyique(boolean simulationPhysique) {
        this.simulationPhysique = simulationPhysique;
        pont.arreterCreation(world);
        if (creationPont) {
            creationPont = false;
        }
    }

    public void toggleSimulationPhysique() {
        setSimulationPhsyique(!getSimulationPhysique());
    }

    /**
     * Dessine une partie
     * 
     * @param g
     * @param box2d
     * @param posSouris
     */
    public void dessiner(Graphics2D g, Box2D box2d, Vec2 posSouris) {
        bord.dessiner(g, box2d);
        pont.dessiner(g, box2d, posSouris, creationPont);
        voiture.dessiner(g, box2d);
    }

    /**
     * Effectue les calculs de la physique qui s'est écoulée pendant le temps dt
     * 
     * @param posSouris
     * @param boutonSouris
     * @param clicSouris
     * @param dt
     */
    public void tickPhysique(Vec2 posSouris, int boutonSouris, boolean clicSouris, float dt) {

        if (simulationPhysique) {
            world.step(dt, 10, 8);
            pont.testCasse(world, dt);
            voiture.arreter();
            if (!terminee && voiture.testArrivee()) {
                finPartie();
            }

        } else if (creationPont) {
            pont.gererInput(world, posSouris, boutonSouris, clicSouris, materiau, bord);
        }

    }

    /**
     * Méthode appelée en fin de partie
     */
    private void finPartie() {
        boolean niveauReussi = getPrix() <= getBuget();
        jeu.finPartie(niveauReussi, getPrix());
        terminee = true;
    }

    /**
     * Méthode pour changer de matériau sélectionné
     * 
     * @param materiau
     */
    public void changementMateriau(Materiau materiau) {
        this.materiau = materiau;
        pont.arreterCreation(world);

    }

    public int getPrix() {
        return pont.prix();
    }

    public int getBuget() {
        return budget;
    }

}
