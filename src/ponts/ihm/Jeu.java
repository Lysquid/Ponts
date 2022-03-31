package ponts.ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import ponts.niveau.Niveau;
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

    JComboBox<String> comboBoxNiveaux;
    JButton boutonLancer;
    JButton boutonMateriauBois;
    JButton boutonMateriauGoudron;

    Pont pont;
    Partie partie;

    int boutonSouris;
    boolean clicSouris = false;
    Vec2 posSouris = new Vec2();
    boolean initilise = false;

    long tempsPrecedent = 0;
    static final int DELAI_APPARITION = 100;
    boolean simulationPhysique = false;
    Materiau materiau = Materiau.BOIS;

    public Jeu(int largeur, int hauteur) {

        setSize(largeur, hauteur);

        String[] nomsNiveaux = new File(Niveau.CHEMIN.toString()).list();
        comboBoxNiveaux = new JComboBox<String>(nomsNiveaux);
        comboBoxNiveaux.addActionListener(this);
        add(comboBoxNiveaux);

        boutonLancer = new JButton("Lancer");
        boutonLancer.addActionListener(this);
        add(boutonLancer);

        boutonMateriauBois = new JButton("Bois");
        boutonMateriauBois.addActionListener(this);
        add(boutonMateriauBois);

        boutonMateriauGoudron = new JButton("Goudron");
        boutonMateriauGoudron.addActionListener(this);
        add(boutonMateriauGoudron);

    }

    public void initialiser(int refreshRate) {

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
                simulationPhysique = false;
            } else {
                boutonLancer.setText("Lancer");
                simulationPhysique = true;
            }
        }

        if (e.getSource() == comboBoxNiveaux) {
            String nomNiveau = (String) comboBoxNiveaux.getSelectedItem();
            partie.chargerNiveau(nomNiveau);
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

    public void majInfosSouris(MouseEvent e) {
        posSouris = box2d.pixelToWorld(e.getX(), e.getY());
        boutonSouris = e.getButton();
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
