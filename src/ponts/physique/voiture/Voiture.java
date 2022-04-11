package ponts.physique.voiture;

import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ponts.ihm.Box2D;
import ponts.niveau.Niveau;
import ponts.physique.barres.Barre;
import ponts.physique.environnement.Bord;

public class Voiture {

    public static final int CATEGORY = 0b1000;
    public static final int MASK = Bord.CATEGORY | Barre.CATEGORY;

    Roue roueAvant;
    Roue roueArriere;
    Carrosserie carrosserie;

    public Voiture(World world, Niveau niveau) {
        Vec2 posRoueArriere = niveau.getPosCoins().get(3).add(new Vec2(Roue.RAYON, Roue.RAYON));
        Vec2 posRoueAvant = posRoueArriere.add(new Vec2(5f, 0f));
        Vec2 posCarrosserie = new Vec2(0.5f * (posRoueArriere.x + posRoueAvant.x), posRoueArriere.y + 2f);

        carrosserie = new Carrosserie(world, posCarrosserie);
        roueArriere = new Roue(world, posRoueArriere);
        roueAvant = new Roue(world, posRoueAvant);

        roueArriere.lierVoiture(world, carrosserie);
        roueAvant.lierVoiture(world, carrosserie);

    }

    public void dessiner(Graphics g, Box2D box2d) {
        roueArriere.dessiner(g, box2d);
        roueAvant.dessiner(g, box2d);
        carrosserie.dessiner(g, box2d);
    }
}
