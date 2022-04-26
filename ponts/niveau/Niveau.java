package ponts.niveau;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
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

import javax.swing.JOptionPane;

import org.jbox2d.common.Vec2;

import ponts.ihm.Box2D;
import ponts.ihm.Fenetre;
import ponts.physique.liaisons.Liaison;

/**
 * Classe d'un niveau, stock la positions des points et des liaisons
 * Elle implémentes l'interface Serializable pour pouvoir être sauvgardée dans
 * un fichier
 */
public class Niveau implements Serializable {

    public static final Path CHEMIN = Paths.get("res", "niveaux");
    private static final long serialVersionUID = 5014471600563766405L; // à fixer, sinon il change à chaque modification
                                                                       // de la classe et on ne peut plus lire les
                                                                       // niveaux créés avec une version précédente
    private LinkedList<Vec2> posCoins;
    private LinkedList<Vec2> posLiaisons;
    int budget = 0;

    /**
     * Constructeur d'un niveau
     */
    public Niveau() {
        posCoins = new LinkedList<Vec2>();
        posLiaisons = new LinkedList<Vec2>();
    }

    public LinkedList<Vec2> getPosLiaisons() {
        return posLiaisons;
    }

    /**
     * Méthode pour desinner un niveau
     * 
     * @param g
     * @param box2d
     */
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

    /**
     * Ajoute un point à la liste de points
     * 
     * @param posSouris
     */
    public void ajouterPoint(Vec2 posSouris) {
        posCoins.add(posSouris);
    }

    /**
     * Ajoute une liaison à la liste des liaisons
     * 
     * @param posSouris
     */
    public void ajouterLiaison(Vec2 posSouris) {
        ajouterPoint(posSouris);
        posLiaisons.add(posSouris);
    }

    /**
     * Vérifie que le niveau est valide
     * 
     * @return boolean
     */
    private boolean valide() {
        return !posCoins.isEmpty();
    }

    /**
     * Ajoute des points aux extréimtés de la fenêtre pour faire un polygone
     * 
     * @param box2d
     */
    public void ajouterExtremitees(Box2D box2d) {
        float largeur = box2d.getLargeur();
        float hauteur = box2d.getHauteur();

        Vec2 bordGauche = new Vec2(-largeur, posCoins.getFirst().y);
        posCoins.addFirst(bordGauche);
        Vec2 bordDroit = new Vec2(2 * largeur, posCoins.getLast().y);
        posCoins.add(bordDroit);

        Vec2 coinGauche = new Vec2(-largeur, -hauteur);
        posCoins.addFirst(coinGauche);
        Vec2 coinDroit = new Vec2(2 * largeur, -hauteur);
        posCoins.add(coinDroit);
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getBudget() {
        return budget;
    }

    /**
     * Annule la création du point
     */
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

    /**
     * Sauvegarde le niveau dans un fichier, ou affiche une erreur s'il ce n'est pas
     * possible
     * 
     * @param fenetre
     * @param nomNiveau
     * @param texteBudget
     */
    public void sauvegarder(Fenetre fenetre, String nomNiveau, String texteBudget) {
        String chemin = cheminNiveau(nomNiveau);
        String titre = "Sauvegarde niveau";

        if (!valide()) {
            JOptionPane.showMessageDialog(fenetre, "Le niveau est invalide", titre,
                    JOptionPane.ERROR_MESSAGE);
        }
        try {
            budget = Integer.parseInt(texteBudget);
            FileOutputStream fileOut = new FileOutputStream(chemin);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            fileOut.close();
            JOptionPane.showMessageDialog(fenetre, "Niveau sauvegardé", titre,
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException i) {
            JOptionPane.showMessageDialog(fenetre, "Le budget est invalide", titre,
                    JOptionPane.ERROR_MESSAGE);
        } catch (FileNotFoundException i) {
            JOptionPane.showMessageDialog(fenetre, "Le nom de niveau est invalide", titre,
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * Charge un niveau à partir de son nom
     * 
     * @param fenetre
     * @param nomNiveau
     * @return niveau
     */
    public static Niveau charger(Fenetre fenetre, String nomNiveau) {
        if (nomNiveau == null) {
            return null;
        }
        String chemin = cheminNiveau(nomNiveau);
        String titre = "Charge niveau";
        Niveau niveau = null;
        try {
            FileInputStream fileIn = new FileInputStream(chemin);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            niveau = (Niveau) objectIn.readObject();
            objectIn.close();
            fileIn.close();

        } catch (FileNotFoundException i) {
            JOptionPane.showMessageDialog(fenetre, "Niveau introuvable", titre, JOptionPane.ERROR_MESSAGE);
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

    /**
     * Renvoie le chemin vers le fichier d'un niveau en fonction de son nom
     * 
     * @param nomNiveau
     * @return chemin
     */
    private static String cheminNiveau(String nomNiveau) {
        return CHEMIN.resolve(nomNiveau).toString();
    }

    /**
     * Supprime le fichier d'un niveau
     * 
     * @param fenetre
     * @param nomNiveau
     */
    public static void supprimer(Fenetre fenetre, String nomNiveau) {
        File file = new File(cheminNiveau(nomNiveau));
        String titre = "Suppression niveau";
        if (file.delete()) {
            JOptionPane.showMessageDialog(fenetre, "Niveau supprimé", titre,
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(fenetre, "Niveau introuvable", titre,
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Centre le niveau par rapport à la fenêtre pour utiliser au mieux l'espace
     * 
     * @param box2d
     */
    public void centrer(Box2D box2d) {
        Vec2 bordGauche = posCoins.getFirst();
        Vec2 bordDroit = posCoins.getLast();

        float deltaX = box2d.getLargeur() / 2 - (bordGauche.x + bordDroit.x) / 2;
        float deltaY = 1.1f * box2d.getHauteur() / 2 - (bordDroit.y + bordGauche.y) / 2;
        for (Vec2 posCoin : posCoins) {
            posCoin.addLocal(deltaX, deltaY);
        }
    }

    /**
     * Calcule la position du départ de la voiture
     * 
     * @return x
     */
    public float calculerDepart() {
        return posCoins.get(2).x;
    }

    /**
     * Calcul la position de l'arrivée de la voiture
     * 
     * @return x
     */
    public float calculerArrivee() {
        return posCoins.get(posCoins.size() - 3).x;
    }

}
