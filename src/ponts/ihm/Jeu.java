package ponts.ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ponts.physique.Partie;
import ponts.physique.Pont;
import ponts.physique.barres.Materiau;
import ponts.physique.environnement.Bord;

public class Jeu extends JPanel implements ActionListener, MouseInputListener {

    Box2D box2d;
    World world;
    Timer timerGraphique;
    Timer timerPhysique;
    static final int TICK_PHYSIQUE = 16;
    long tempsPhysique;
    JButton boutonLancer;
    JButton boutonMateriauBois;
    JButton boutonMateriauGoudron;

    Pont pont;
    Partie partie;

    String boutonSouris;
    boolean clicSouris = false;
    Vec2 posSouris;
    boolean initilise = false;

    long tempsPrecedent = 0;
    static final int DELAI_APPARITION = 100;
    boolean simulationPhysique;
    Materiau materiau = Materiau.BOIS;

    public Jeu(int largeur, int hauteur) {

        setSize(largeur, hauteur);
        boutonLancer = new JButton("Lancer");
        boutonLancer.setBounds(10, 20, 100, 50);
        boutonLancer.setBackground(Color.WHITE);
        boutonLancer.addActionListener(this);
        setLayout(null);
        add(boutonLancer);

        boutonMateriauBois = new JButton("Bois");
        boutonMateriauBois.setBounds(150, 20, 100, 50);
        boutonMateriauBois.setBackground(Color.WHITE);
        boutonMateriauBois.addActionListener(this);
        setLayout(null);
        add(boutonMateriauBois);

        boutonMateriauGoudron = new JButton("Goudron");
        boutonMateriauGoudron.setBounds(290, 20, 100, 50);
        boutonMateriauGoudron.setBackground(Color.WHITE);
        boutonMateriauGoudron.addActionListener(this);
        setLayout(null);
        add(boutonMateriauGoudron);

        simulationPhysique = false;
        posSouris = new Vec2();
    }

    public void init(int refreshRate) {

        box2d = new Box2D(getWidth(), getHeight());

        Vec2 gravity = new Vec2(0.0f, -9.81f);
        world = new World(gravity);

        new Bord(world, box2d.largeur, box2d.hauteur);
        pont = new Pont(world, box2d);
        partie = new Partie(pont);

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

    @Override
    public void paintComponent(Graphics g0) {

        // Activer l'anti-alias
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fonction pour améliorer l'affichage sur Mac/Linux
        Toolkit.getDefaultToolkit().sync();

        g.setColor(Color.decode("#55a3d4"));
        g.fillRect(0, 0, getWidth(), getHeight());

        if (partie != null) {
            partie.dessiner(g, box2d, posSouris);
        }

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == timerPhysique) {
            pont.gererInput(world, posSouris, boutonSouris, clicSouris, materiau);
            clicSouris = false;

            long nouveauTempsPhysique = System.currentTimeMillis();
            float dt = (nouveauTempsPhysique - tempsPhysique) / 1000f;
            tempsPhysique = nouveauTempsPhysique;

            pont.testCasse(world, dt);

            if (simulationPhysique) {
                world.step(dt, 10, 8);

            }
        }

        if (e.getSource() == timerGraphique) {
            repaint();
        }

        if (e.getSource() == boutonLancer) {
            if (simulationPhysique) {
                boutonLancer.setText("Arreter");
                deserialiserPartie();
                simulationPhysique = false;
            } else {
                boutonLancer.setText("Lancer");
                serializerPartie();
                simulationPhysique = true;
            }
        }

        if (e.getSource() == boutonMateriauBois) {
            materiau = Materiau.BOIS;
            pont.arreterCreation(world);
        }
        if (e.getSource() == boutonMateriauGoudron) {
            materiau = Materiau.GOUDRON;
            pont.arreterCreation(world);
        }

    }

    public void serializerPartie() {
        try {
            FileOutputStream fileOut = new FileOutputStream("partie.txt");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(partie);
            objectOut.close();
            fileOut.close();
            System.out.println("serialized");
        } catch (FileNotFoundException i) {
            i.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void deserialiserPartie() {
        try {
            FileInputStream fileIn = new FileInputStream("partie.txt");
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            partie = (Partie) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            System.out.println("deserialized");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException i) {
            i.printStackTrace();
        }
    } 

    public void majInfosSouris(MouseEvent e) {
        posSouris = box2d.pixelToWorld(e.getX(), e.getY());
        if (SwingUtilities.isLeftMouseButton(e)) {
            boutonSouris = "gauche";
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            boutonSouris = "droite";
        }
        if (SwingUtilities.isMiddleMouseButton(e)) {
            boutonSouris = "molette";
            serializerPartie();
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
        majInfosSouris(e);
        clicSouris = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        majInfosSouris(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        majInfosSouris(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        majInfosSouris(e);
    }
}
