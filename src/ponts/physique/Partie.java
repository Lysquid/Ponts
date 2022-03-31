package ponts.physique;

import java.awt.Graphics2D;

import org.jbox2d.common.Vec2;

import ponts.ihm.Box2D;

public class Partie {

    Pont pont;

    public Partie(Pont pont) {
        this.pont = pont;
    }

    public void dessiner(Graphics2D g, Box2D box2d, Vec2 posSouris) {
        pont.dessiner(g, box2d, posSouris);

    }

}
