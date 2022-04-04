package ponts.niveau;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.jbox2d.common.Vec2;

import ponts.ihm.Box2D;
import ponts.physique.liaisons.Liaison;

public class Niveau implements Serializable {

    public static final Path CHEMIN = Paths.get("res", "niveaux");
    static final long serialVersionUID = 5014471600563766405L;

    LinkedList<Vec2> posCoins;
    LinkedList<Vec2> posLiaisons;
    int budget = 0;

    public Niveau() {
        posCoins = new LinkedList<Vec2>();
        posLiaisons = new LinkedList<Vec2>();
    }

    public LinkedList<Vec2> getPosLiaisons() {
        return posLiaisons;
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

    public boolean ajouterExtremitees(Box2D box2d) {
        if (posCoins.isEmpty()) {
            return false;
        }
        Vec2 bordGauche = new Vec2(0, posCoins.getFirst().y);
        posCoins.addFirst(bordGauche);
        Vec2 bordDroit = new Vec2(box2d.getLargeur(), posCoins.getLast().y);
        posCoins.add(bordDroit);

        Vec2 coinGauche = new Vec2(0, 0);
        posCoins.addFirst(coinGauche);
        Vec2 coinDroit = new Vec2(box2d.getLargeur(), 0);
        posCoins.add(coinDroit);
        return true;
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

    public void sauvegarder(String nomNiveau) {
        String chemin = CHEMIN.resolve(nomNiveau).toString();

        try {

            FileOutputStream fileOut = new FileOutputStream(chemin);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            fileOut.close();
        } catch (FileNotFoundException i) {
            System.out.println("Nom de fichier invalide");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static Niveau charger(String nomNiveau) {
        String chemin = CHEMIN.resolve(nomNiveau).toString();
        Niveau niveau = null;
        try {
            FileInputStream fileIn = new FileInputStream(chemin);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            niveau = (Niveau) objectIn.readObject();
            objectIn.close();
            fileIn.close();

        } catch (FileNotFoundException i) {
            System.out.println("Fichier introuvable");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException i) {
            i.printStackTrace();
        }

        return niveau;
    }

    public LinkedList<Vec2> getPosCoins() {
        return posCoins;
    }

}
