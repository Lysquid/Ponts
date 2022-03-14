import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Jeu extends JPanel implements ActionListener, MouseInputListener {

    Box2D box2d;
    World world;
    Timer timerGraphique;
    Timer timerPhysique;
    final int TICK_PHYSIQUE = 16;
    long tempsPhysique;

    LinkedList<Barre> boites;
    Pont pont;

    boolean sourisAppyuee;
    int boutonSouris;
    Vec2 posSouris;
    boolean initilise = false;

    long tempsPrecedent = 0;
    final int DELAI_APPARITION = 100;

    public Jeu() {
        boites = new LinkedList<Barre>();
    }

    public void init(int refreshRate) {

        box2d = new Box2D(getWidth(), getHeight());

        Vec2 gravity = new Vec2(0.0f, -9.81f);
        world = new World(gravity);

        new Bord(world, box2d.largeur, box2d.hauteur);
        Vec2 posPont = new Vec2(box2d.largeur / 2, box2d.hauteur * 0.01f); //qu'est-ce qu'on fait? oups javais oublié que ca lancait chez toi
        pont = new Pont(world, posPont);

        addMouseListener(this);
        addMouseMotionListener(this);

        timerPhysique = new Timer(TICK_PHYSIQUE, this);
        timerPhysique.start();
        tempsPhysique = System.currentTimeMillis();

        int fps = (int) (1.0 / refreshRate * 1000.0);
        timerGraphique = new Timer(fps, this);
        timerGraphique.start();

        initilise = true;

    }

    public void paint(Graphics g0) {

        // Activer l'anti-alias
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fonction pour améliorer l'affichage sur Mac/Linux
        Toolkit.getDefaultToolkit().sync();

        g.setColor(Color.decode("#55a3d4"));
        g.fillRect(0, 0, getWidth(), getHeight());

        for (Barre box : boites) {
            box.dessiner(g, box2d);
        }

        if (pont != null) {
            pont.dessiner(g, box2d);
        }

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == timerPhysique) {
            if (sourisAppyuee) {
                long temps = System.currentTimeMillis();
                // System.out.println(time - prevTime);
                switch (boutonSouris) {
                    case 1:
                        pont.gererClique(world, posSouris);
                        sourisAppyuee = false;
                        break;
                    case 3:
                        if (temps - tempsPrecedent > DELAI_APPARITION) {
                            Barre newBox = new Barre(world, posSouris, 0, 4, 3);
                            boites.add(newBox);
                            tempsPrecedent = temps;
                        }
                        break;

                }
            }

            long nouveauTempsPhysique = System.currentTimeMillis();
            float dt = (nouveauTempsPhysique - tempsPhysique) / 1000f;
            tempsPhysique = nouveauTempsPhysique;

            pont.testCasse(world, dt);

            world.step(dt, 10, 8);
        }

        if (e.getSource() == timerGraphique)

        {
            repaint();
        }

    }

    public void majPosSouris(MouseEvent e) {
        float x = box2d.pixelToWorldX(e.getX());
        float y = box2d.pixelToWorldY(e.getY());
        posSouris = new Vec2(x, y);
        if (SwingUtilities.isLeftMouseButton(e)) {
            boutonSouris = 1;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            boutonSouris = 3;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        sourisAppyuee = true;
        majPosSouris(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        sourisAppyuee = false;
        majPosSouris(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        majPosSouris(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        majPosSouris(e);
    }
}
