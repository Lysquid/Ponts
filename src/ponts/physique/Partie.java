package ponts.physique;

import java.awt.Graphics2D;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ponts.ihm.Box2D;
import ponts.niveau.Niveau;
import ponts.physique.barres.Materiau;
import ponts.physique.environnement.Bord;

public class Partie {

    Pont pont;
    World world;
    private boolean simulationPhysique = false;
    Materiau materiau = Materiau.BOIS;

    public Partie(Box2D box2d, Niveau niveau) {
        Vec2 gravity = new Vec2(0.0f, -9.81f);
        world = new World(gravity);

        new Bord(world, box2d.getLargeur(), box2d.getHauteur());
        pont = new Pont(world, box2d);

    }

    public boolean isSimulationPhysique() {
        return simulationPhysique;
    }

    public void setSimulationPhysique(boolean estActive) {
        simulationPhysique = estActive;
    }

    public void dessiner(Graphics2D g, Box2D box2d, Vec2 posSouris) {
        pont.dessiner(g, box2d, posSouris);
    }

    public void tickPhysique(Vec2 posSouris, int boutonSouris, boolean clicSouris, float dt) {

        pont.gererInput(world, posSouris, boutonSouris, clicSouris, materiau);

        pont.testCasse(world, dt);

        if (isSimulationPhysique()) {
            world.step(dt, 10, 8);

        }

    }

    public void changementMateriau(Materiau materiau) {
        this.materiau = materiau;
        pont.arreterCreation(world);

    }

    public void toggleSimulationPhysique() {
        simulationPhysique = !simulationPhysique;
    }

}
