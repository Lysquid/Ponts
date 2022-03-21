package ponts.physique.environnement;

import java.util.LinkedList;

import org.jbox2d.common.Vec2;

import ponts.ihm.Box2D;

import java.awt.Color;
import java.awt.Graphics2D;

public class Berge {

    public static final int CATEGORY = 0b0001;
    public static final int MASK = 0b1111;

    LinkedList<Vec2> listeVec;

    public Berge() {
        listeVec = new LinkedList<Vec2>();

    }

    public void dessiner(Graphics2D g, Box2D box2d) {
        g.setColor(Color.GREEN);
        Vec2 prevVec = null;
        for (Vec2 vec : listeVec) {
            if (prevVec != null) {

                int x1 = box2d.worldToPixelX(prevVec.x);
                int y1 = box2d.worldToPixelX(prevVec.y);
                int x2 = box2d.worldToPixelX(vec.x);
                int y2 = box2d.worldToPixelX(vec.y);
                g.drawLine(x1, y1, x2, y2);
            }

        }
    }

    public void ajouterPoint(Vec2 posSouris) {
        if (listeVec.isEmpty()) {
            Vec2 bordGauche = new Vec2(0, posSouris.y);
            listeVec.add(bordGauche);
        } else {
            listeVec.removeLast();
        }
        listeVec.add(posSouris);
        Vec2 bordDroit = new Vec2(0, posSouris.y);
        listeVec.add(bordDroit);
    }

}
