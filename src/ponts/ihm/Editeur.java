package ponts.ihm;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import org.jbox2d.common.Vec2;

import ponts.niveau.Niveau;

public class Editeur extends JPanel implements ActionListener, MouseInputListener {

    Box2D box2d;
    Timer timerGraphique;
    Niveau niveau;

    JLabel textNomFichier;
    JLabel textBudget;
    JTextField champNomFichier;
    JTextField champBudget;
    JButton boutonSauvgarder;
    JButton boutonCharger;

    public Editeur(int largeur, int hauteur, int refreshRate) {

        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        setSize(largeur, hauteur);
        box2d = new Box2D(getWidth(), getHeight());

        int fps = (int) (1.0 / refreshRate * 1000.0);
        timerGraphique = new Timer(fps, this);
        timerGraphique.start();

        addMouseListener(this);
        addMouseMotionListener(this);

        textBudget = new JLabel("Budget");
        add(textBudget);

        champBudget = new JTextField(6);
        add(champBudget);

        textNomFichier = new JLabel("Nom fichier");
        add(textNomFichier);

        champNomFichier = new JTextField(8);
        add(champNomFichier);

        boutonCharger = new JButton("Charger");
        add(boutonCharger);

        boutonSauvgarder = new JButton("Sauvgarder");
        add(boutonSauvgarder);

        niveau = new Niveau();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Toolkit.getDefaultToolkit().sync();

        g.setColor(Color.decode("#55a3d4"));
        g.fillRect(0, 0, getWidth(), getHeight());

        niveau.dessiner(g, box2d);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timerGraphique) {
            repaint();
        }
    }

    public String boutonSouris(MouseEvent e) {
        String boutonSouris = null;
        if (SwingUtilities.isLeftMouseButton(e)) {
            boutonSouris = "gauche";
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            boutonSouris = "droite";
        }
        if (SwingUtilities.isMiddleMouseButton(e)) {
            boutonSouris = "molette";
        }
        return boutonSouris;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Vec2 posSouris = box2d.pixelToWorld(e.getX(), e.getY());
        String boutonSouris = boutonSouris(e);
        switch (boutonSouris) {
            case "gauche":
                niveau.ajouterPoint(posSouris);
                break;
            case "droite":
                niveau.ajouterLiaison(posSouris);
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
