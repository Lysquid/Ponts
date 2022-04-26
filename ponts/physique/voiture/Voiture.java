package ponts.physique.voiture;

import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ponts.ihm.Box2D;
import ponts.niveau.Niveau;
import ponts.physique.barres.BarreGoudron;
import ponts.physique.environnement.Bord;

/**
 * Classe de la voiture
 */
public class Voiture {

    public static final int CATEGORY = 0b0010;
    public static final int MASK = Bord.CATEGORY | BarreGoudron.CATEGORY;

    private Roue roueAvant;
    private Roue roueArriere;
    private Carrosserie carrosserie;

    private boolean arretee = false;
    private boolean arrivee = false;
    private float xDepart;
    private float xArrivee;

    /**
     * Constructeur d'une voiture
     * 
     * @param world
     * @param niveau
     */
    public Voiture(World world, Niveau niveau) {

        xDepart = niveau.calculerDepart();
        xArrivee = niveau.calculerArrivee();

        float ecartRoues = 6f;
        Vec2 posRoueAvant = new Vec2(xDepart - ecartRoues, niveau.getPosCoins().get(1).y + Roue.RAYON);
        Vec2 posRoueArriere = posRoueAvant.sub(new Vec2(ecartRoues, 0f));
        Vec2 posCarrosserie = new Vec2(0.5f * (posRoueArriere.x + posRoueAvant.x), posRoueArriere.y + 1.4f);

        carrosserie = new Carrosserie(world, posCarrosserie);
        roueArriere = new Roue(world, posRoueArriere);
        roueAvant = new Roue(world, posRoueAvant);

        roueArriere.lierVoiture(world, carrosserie);
        roueAvant.lierVoiture(world, carrosserie);

    }

    /**
     * Dessine la voiture
     * 
     * @param g
     * @param box2d
     */
    public void dessiner(Graphics g, Box2D box2d) {
        carrosserie.dessiner(g, box2d);
        roueArriere.dessiner(g, box2d);
        roueAvant.dessiner(g, box2d);
    }

    /**
     * Teste si la voiture est arrivée à la fin du niveau
     * 
     * @return
     */
    public boolean testArrivee() {
        return carrosserie.getBody().getLinearVelocity().x <= 0.001f && arrivee;
    }

    /**
     * Arrête la voiture si elle arrive à la fin où si elle ne bouge plus
     */
    public void arreter() {
        if (arretee) {
            return;
        }
        float vitesse = carrosserie.getBody().getLinearVelocity().length();
        if (roueArriere.getX() > xArrivee) {
            roueArriere.arreter();
            roueAvant.arreter();
            arretee = true;
            arrivee = true;
        } else if (vitesse <= 0.001f && roueArriere.getX() > xDepart) {
            roueArriere.arreter();
            roueAvant.arreter();
            arretee = true;
        }
    }

}
