package ponts.niveau;

import java.util.LinkedList;

import org.jbox2d.common.Vec2;

import ponts.ihm.Box2D;
import ponts.physique.liaisons.Liaison;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public class Niveau implements Serializable {

    private static final long serialVersionUID = 5014471600563766405L;

    LinkedList<Vec2> posCoins;
    LinkedList<Vec2> posLiaisons;
    int budget;

    public Niveau() {
        posCoins = new LinkedList<Vec2>();
        posLiaisons = new LinkedList<Vec2>();
    }

    public void dessiner(Graphics2D g, Box2D box2d) {
        g.setColor(Color.BLACK);
        if (!posCoins.isEmpty()) {
            Vec2 prevPos = posCoins.getFirst();
            for (Vec2 pos : posCoins) {
                int x1 = box2d.worldToPixelX(prevPos.x);
                int y1 = box2d.worldToPixelY(prevPos.y);
                int x2 = box2d.worldToPixelX(pos.x);
                int y2 = box2d.worldToPixelY(pos.y);
                g.drawLine(x1, y1, x2, y2);
                prevPos = pos;
            }
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
        posCoins.addFirst(bordGauche);
        Vec2 bordDroit = new Vec2(box2d.getLargeur(), posCoins.getLast().y);
        posCoins.add(bordDroit);
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getBudget() {
        return budget;
    }

    public void undo() {
        if (!posCoins.isEmpty()) {
            Vec2 dernierePos = posCoins.getLast();
            posCoins.removeLast();
            if (!posLiaisons.isEmpty()) {
                if (posLiaisons.getLast() == dernierePos) {
                    posLiaisons.removeLast();
                }
            }
        }
    }

}
