package ponts.ihm;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.formdev.flatlaf.FlatLightLaf;

/**
 * Classe de la fenêtre
 */
public class Fenetre extends JFrame {

    private Jeu jeu;
    private Editeur editeur;
    private final int LARGEUR_MIN = 960;
    private final int HAUTEUR_MIN = 540;

    /**
     * Constructeur d'une fenêtre
     */
    public Fenetre() {

        setTitle("Ponts");
        setMinimumSize(new Dimension(LARGEUR_MIN, HAUTEUR_MIN));
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int refreshRate = getRefreshRate();
        Box2D box2d = new Box2D(this);

        jeu = new Jeu(this, box2d, refreshRate);
        editeur = new Editeur(this, box2d, refreshRate);
        lancerJeu();
        setVisible(true);
        jeu.messageDebutJeu();
    }

    /**
     * Lancer le jeu
     */
    public void lancerJeu() {
        changerEcran(editeur, jeu);
        jeu.majListeNiveaux();
        jeu.verifierExistanceNiveaux();
    }

    /**
     * Lancer l'éditeur
     */
    public void lancerEditeur() {
        changerEcran(jeu, editeur);
    }

    /**
     * Changer d'écran en passant du jeu à l'éditeur ou l'inverse
     * 
     * @param actuel
     * @param nouveau
     */
    private void changerEcran(JPanel actuel, JPanel nouveau) {
        remove(actuel);
        add(nouveau);
        nouveau.repaint();
        setVisible(true);
    }

    /**
     * Reccupere le taux de rafraichissement de l'écran pour faire des repaint aux
     * bons moments et avoir le meilleur framerate
     */
    private int getRefreshRate() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] gs = ge.getScreenDevices();
            DisplayMode dm = gs[0].getDisplayMode();
            return dm.getRefreshRate();
        } catch (Exception e) {
            e.printStackTrace();
            return 60;
        }

    }

    /**
     * Modifications de l'apparence des composants Swing
     */
    public static void setLookAndFeel() {
        FlatLightLaf.setup(); // Initialise la librairie flatlaf

        // Coins arrondis
        int arrondi = 20;
        UIManager.put("Button.arc", arrondi);
        UIManager.put("Component.arc", arrondi);
        UIManager.put("TextComponent.arc", arrondi);

        // Marges
        int marge = 8;
        Insets insets = new Insets(marge, marge, marge, marge);
        UIManager.put("Button.margin", insets);
        UIManager.put("TextField.margin", insets);
        UIManager.put("TextComponent.margin", insets);
        UIManager.put("ComboBox.padding", insets);

        // Taille des polices d'écriture
        Font defaultFont = UIManager.getDefaults().getFont("Label.font");
        Font font = defaultFont.deriveFont(18f);
        Font fontBold = font.deriveFont(Font.BOLD);
        FontUIResource fontResource = new FontUIResource(font);
        FontUIResource fontResourceBold = new FontUIResource(fontBold);
        UIManager.put("Label.font", fontResourceBold);
        UIManager.put("Button.font", fontResource);
        UIManager.put("ComboBox.font", fontResource);
        UIManager.put("TextField.font", fontResource);

    }

}
