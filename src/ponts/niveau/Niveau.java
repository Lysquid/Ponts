package ponts.niveau;

import java.util.LinkedList;

import org.jbox2d.common.Vec2;

import ponts.ihm.Box2D;
import ponts.physique.liaisons.Liaison;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public class Niveau implements Serializable {

    LinkedList<Vec2> posCoins;
    LinkedList<Vec2> posLiaisons;

    public Niveau() {
        posCoins = new LinkedList<Vec2>();
        posLiaisons = new LinkedList<Vec2>();

    }

    public void dessiner(Graphics2D g, Box2D box2d) {
        g.setColor(Color.BLACK);
        Vec2 prevVec = null;
        for (Vec2 vec : posCoins) {
            if (prevVec != null) {

                int x1 = box2d.worldToPixelX(prevVec.x);
                int y1 = box2d.worldToPixelY(prevVec.y);
                int x2 = box2d.worldToPixelX(vec.x);
                int y2 = box2d.worldToPixelY(vec.y);
                g.drawLine(x1, y1, x2, y2);
            }
            prevVec = vec;

        }

        int r = box2d.worldToPixel(Liaison.RAYON);
        for (Vec2 posLiaison : posLiaisons) {
            int x = box2d.worldToPixelX(posLiaison.x);
            int y = box2d.worldToPixelY(posLiaison.y);
            g.fillOval(x - r, y - r, r * 2, r * 2);
        }
    }

    public void ajouterPoint(Vec2 posSouris) {
        posCoins.add(posSouris);
    }

    public void ajouterLiaison(Vec2 posSouris) {
        ajouterPoint(posSouris);
        posLiaisons.add(posSouris);
    }

    public void ajouterExtremitees(Box2D box2d) {
        Vec2 bordGauche = new Vec2(0, posCoins.getFirst().y);
        posCoins.add(bordGauche);
        Vec2 bordDroit = new Vec2(box2d.getLargeur(), posCoins.getLast().y);
        posCoins.add(bordDroit);
    }

}
