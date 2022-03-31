package ponts;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

import ponts.ihm.Editeur;
import ponts.ihm.Jeu;

public class Main extends JFrame {

    static final int LARGEUR = 600;
    static final int HAUTEUR = 600;

    int refreshRate;

    public Main() {
        setTitle("Project");
        setSize(LARGEUR, HAUTEUR);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        refreshRate = getRefreshRate();

        if (false) {
            Jeu jeu = new Jeu(LARGEUR, HAUTEUR);
            add(jeu);
            setVisible(true);
            jeu.init(refreshRate);
        } else {
            Editeur editeur = new Editeur(LARGEUR, HAUTEUR);
            add(editeur);
            setVisible(true);
        }

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

    public static void main(String[] args) {
        setLookAndFeel();
        new Main();
    }

    public static void setLookAndFeel() {
        FlatLightLaf.setup();

        int arrondi = 30;
        UIManager.put("Button.arc", arrondi);
        UIManager.put("Component.arc", arrondi);
        UIManager.put("TextComponent.arc", arrondi);

        int marge = 10;
        Insets insets = new Insets(marge, marge, marge, marge);
        UIManager.put("Button.margin", insets);
        UIManager.put("TextField.margin", insets);

    }

}
