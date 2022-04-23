package ponts.physique.voiture;

import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ponts.ihm.Box2D;
import ponts.niveau.Niveau;
import ponts.physique.barres.BarreGoudron;
import ponts.physique.environnement.Bord;

public class Voiture {

    public static final int CATEGORY = 0b0010;
    public static final int MASK = Bord.CATEGORY | BarreGoudron.CATEGORY;

    Roue roueAvant;
    Roue roueArriere;
    Carrosserie carrosserie;

    boolean arretee = false;
    boolean arrivee = false;
    float xDepart;
    float xArrivee;

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

    public void dessiner(Graphics g, Box2D box2d) {
        carrosserie.dessiner(g, box2d);
        roueArriere.dessiner(g, box2d);
        roueAvant.dessiner(g, box2d);
    }

    public boolean testArrivee() {
        return carrosserie.getBody().getLinearVelocity().x <= 0.001f && arrivee;
    }

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
