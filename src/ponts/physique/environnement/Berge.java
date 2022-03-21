package ponts.physique.environnement;

import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import java.awt.Graphics2D;

public class Berge {

    public static final int CATEGORY = 0b0001;
    public static final int MASK = 0b1111;

    LinkedList<Vec2> listeVec;

    public Berge() {
        listeVec = new LinkedList<Vec2>();

    }

    public void dessiner(Graphics2D g) {
        Vec2 prevVec = null;
        for (Vec2 vec : listeVec) {
            if (prevVec != null) {
            }

        }
    }

    public void ajouterPoint(Vec2 posSouris) {
        listeVec.add(posSouris);
    }

}
