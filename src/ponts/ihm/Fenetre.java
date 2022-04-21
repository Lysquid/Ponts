package ponts.ihm;

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

public class Fenetre extends JFrame {

    public Fenetre() {

        setTitle("Ponts");
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        int refreshRate = getRefreshRate();

        setLayout(new BorderLayout());

        if (true) {
            new Jeu(this, refreshRate);
        } else {
            new Editeur(this, refreshRate);
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

    public static void setLookAndFeel() {
        FlatLightLaf.setup();

        int arrondi = 30;
        UIManager.put("Button.arc", arrondi);
        UIManager.put("Component.arc", arrondi);
        UIManager.put("TextComponent.arc", arrondi);
        UIManager.put("ComboBox.arc", arrondi);

        int marge = 10;
        Insets insets = new Insets(marge, marge, marge, marge);
        UIManager.put("Button.margin", insets);
        UIManager.put("TextField.margin", insets);
        UIManager.put("Component.margin", insets);
        UIManager.put("TextComponent.margin", insets);
        UIManager.put("ComboBox.margin", insets);

    }

}
