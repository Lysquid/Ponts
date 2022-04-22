package ponts.ihm;

import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.formdev.flatlaf.FlatLightLaf;

public class Fenetre extends JFrame {

    Jeu jeu;
    Editeur editeur;

    public Fenetre() {

        setTitle("Ponts");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        int refreshRate = getRefreshRate();

        // Pour une raison obscure, sur Linux, le planel
        // se place bien seulement seulement après un certains délai
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        jeu = new Jeu(this, refreshRate);
        editeur = new Editeur(this, refreshRate);
        lancerJeu();
        jeu.messageDebutJeu();
    }

    public void lancerEditeur() {
        changerEcran(jeu, editeur);
    }

    public void lancerJeu() {
        changerEcran(editeur, jeu);
        jeu.listeNiveaux();
    }

    public void changerEcran(JPanel actuel, JPanel nouveau) {
        remove(actuel);
        add(nouveau);
        setVisible(true);
        nouveau.repaint();
    }

    public int getRefreshRate() {
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

    public static void setLookAndFeel() {
        FlatLightLaf.setup();

        int arrondi = 20;
        UIManager.put("Button.arc", arrondi);
        UIManager.put("Component.arc", arrondi);
        UIManager.put("TextComponent.arc", arrondi);

        int marge = 8;
        Insets insets = new Insets(marge, marge, marge, marge);
        UIManager.put("Button.margin", insets);
        UIManager.put("TextField.margin", insets);
        UIManager.put("TextComponent.margin", insets);
        UIManager.put("ComboBox.padding", insets);

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
