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

public class Partie {

    Jeu jeu;
    World world;
    Pont pont;
    Bord bord;
    Voiture voiture;
    int budget;

    private boolean simulationPhysique = false;
    private boolean creationPont = true;
    boolean terminee = false;
    Materiau materiau = Materiau.GOUDRON;

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

    public boolean isSimulationPhysique() {
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
        setSimulationPhsyique(!isSimulationPhysique());
    }

    public void dessiner(Graphics2D g, Box2D box2d, Vec2 posSouris) {
        bord.dessiner(g, box2d);
        pont.dessiner(g, box2d, posSouris, creationPont);
        voiture.dessiner(g, box2d);
    }

    public void tickPhysique(Vec2 posSouris, int boutonSouris, boolean clicSouris, float dt) {

        if (simulationPhysique) {
            world.step(dt, 10, 8);
            pont.testCasse(world, dt);
            voiture.arreter();
            if (!terminee && voiture.testArrivee()) {
                boolean niveauReussi = prix() <= budget();
                jeu.finPartie(niveauReussi);
                terminee = true;
            }

        } else if (creationPont) {
            pont.gererInput(world, posSouris, boutonSouris, clicSouris, materiau, bord);
        }

    }

    public void changementMateriau(Materiau materiau) {
        this.materiau = materiau;
        pont.arreterCreation(world);

    }

    public int prix() {
        return pont.prix();
    }

    public int budget() {
        return budget;
    }

}
