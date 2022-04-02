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
        Vec2 pos = niveau.getPosCoins().get(1);
        pos.addLocal(0, Roue.RAYON);
        carrosserie = new Carrosserie(world, pos.add(new Vec2(2.5f, 2f)));
        roueAvant = new Roue(world, pos.add(new Vec2(5f, 0.0f)));
        roueArriere = new Roue(world, pos);
        roueArriere.lierVoiture(world, carrosserie);
        roueAvant.lierVoiture(world, carrosserie);

    }

    public void dessiner(Graphics g, Box2D box2d) {
        roueArriere.dessiner(g, box2d);
        roueAvant.dessiner(g, box2d);
        carrosserie.dessiner(g, box2d);
    }
}
