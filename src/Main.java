import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class Main extends JFrame {

    static final int LARGEUR = 600;
    static final int HAUTEUR = 600;
    int refreshRate;

    public Main() {
        setTitle("Project");
        setSize(LARGEUR, HAUTEUR);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Jeu jeu = new Jeu(LARGEUR, HAUTEUR);
        add(jeu);

        setVisible(true);

        refreshRate = getRefreshRate();
        jeu.init(refreshRate);

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
        new Main();
    }

}
