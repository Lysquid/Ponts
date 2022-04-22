package ponts.ihm;

import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.formdev.flatlaf.FlatLightLaf;

public class Fenetre extends JFrame {

    Jeu jeu;
    Editeur editeur;
    int refreshRate;

    public Fenetre() {

        setTitle("Ponts");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        refreshRate = getRefreshRate();

        // Pour une raison obscure, sur Linux, le planel
        // se place bien seulement seulement après un certains délai
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (true) {
            jeu = new Jeu(this, refreshRate);
        } else {
            editeur = new Editeur(this, refreshRate);
        }

    }

    public void lancerJeu() {

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
        UIManager.put("ComboBox.arc", arrondi);
        UIManager.put("Panel.arc", arrondi);
        UIManager.put("Label.arc", arrondi);

        int marge = 8;
        Insets insets = new Insets(marge, marge, marge, marge);
        UIManager.put("Button.margin", insets);
        UIManager.put("ComboBox.padding", insets);
        UIManager.put("TextField.margin", insets);
        UIManager.put("Component.margin", insets);
        UIManager.put("TextComponent.margin", insets);
        UIManager.put("ComboBox.margin", insets);
        UIManager.put("Label.margin", insets);
        UIManager.put("Panel.margin", insets);
        UIManager.put("TextArea.margin", insets);
        UIManager.put("TextPane.margin", insets);

        Font defaultFont = UIManager.getDefaults().getFont("Label.font");
        Font font = defaultFont.deriveFont(18f);
        Font fontBold = font.deriveFont(Font.BOLD);
        FontUIResource fontResource = new FontUIResource(font);
        FontUIResource fontResourceBold = new FontUIResource(fontBold);
        UIManager.put("Label.font", fontResourceBold);
        UIManager.put("Button.font", fontResource);
        UIManager.put("ComboBox.font", fontResource);

    }

    public void lancerEditeur() {
        remove(jeu);
        if (editeur != null) {
            add(editeur);
            editeur.setVisible(true);
        } else {
            editeur = new Editeur(this, refreshRate);
        }
    }

}
