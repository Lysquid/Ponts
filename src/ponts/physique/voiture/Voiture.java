package ponts.physique.voiture;


import java.awt.Graphics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ponts.ihm.Box2D;

public class Voiture {
    
    Roue roueAvant;
    Roue roueArriere;
    Carrosserie carrosserie;

    public Voiture(World world, Vec2 pos ) { //carrosserie + roue en parametre ?
        carrosserie = new Carrosserie(world, pos.add(new Vec2(-1f,-1f)));
        roueAvant = new Roue(world, pos.add(new Vec2(-1f,0.0f)));
        roueArriere = new Roue(world, pos);

    }

    public void dessiner(Graphics g, Box2D box2d) {
        roueArriere.dessiner(g, box2d);
        roueAvant.dessiner(g, box2d);
        carrosserie.dessiner(g,box2d);
    }
}
