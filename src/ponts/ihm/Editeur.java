package ponts.ihm;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;

import org.jbox2d.common.Vec2;

import ponts.niveau.Niveau;

public class Editeur extends JPanel implements ActionListener, MouseInputListener {

    Box2D box2d;
    Niveau niveau;

    JLabel textNomNiveau;
    JLabel textBudget;
    JTextField champNomNiveau;
    JTextField champBudget;
    JButton boutonSauvegarder;
    JButton boutonCharger;
    JButton boutonUndo;
    JButton boutonEffacer;
    JButton boutonSupprimer;

    public Editeur(JFrame fenetre, int refreshRate) {

        ihm(fenetre);

        box2d = new Box2D(getWidth(), getHeight());
        niveau = new Niveau();
    }

    public void ihm(JFrame fenetre) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        addMouseListener(this);
        addMouseMotionListener(this);

        textNomNiveau = new JLabel("Nom niveau");
        add(textNomNiveau);

        champNomNiveau = new JTextField(5);
        add(champNomNiveau);

        boutonSauvegarder = new JButton("Sauvegarder");
        add(boutonSauvegarder);
        boutonSauvegarder.addActionListener(this);

        boutonCharger = new JButton("Charger");
        boutonCharger.addActionListener(this);
        add(boutonCharger);

        boutonSupprimer = new JButton("Supprimer");
        boutonSupprimer.addActionListener(this);
        add(boutonSupprimer);

        textBudget = new JLabel("Budget");
        add(textBudget);

        champBudget = new JTextField("0", 4);
        add(champBudget);

        boutonUndo = new JButton("Undo");
        boutonUndo.addActionListener(this);
        add(boutonUndo);

        boutonEffacer = new JButton("Effacer");
        boutonEffacer.addActionListener(this);
        add(boutonEffacer);

        // Pour une raison extremement obscure, sur Linux, le planel
        // se place bien seulement si on l'ajoute deux fois
        fenetre.add(this);
        fenetre.remove(this);
        fenetre.add(this);
        fenetre.setVisible(true);
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
        if (e.getSource() == boutonSauvegarder) {
            niveau.sauvegarder(nomNiveau(), champBudget.getText());
        }
        if (e.getSource() == boutonCharger) {
            niveau = Niveau.charger(nomNiveau());
            champBudget.setText(Integer.toString(niveau.getBudget()));
        }
        if (e.getSource() == boutonSupprimer) {
            Niveau.supprimer(nomNiveau());
        }
        if (e.getSource() == boutonUndo) {
            niveau.undo();
        }
        if (e.getSource() == boutonEffacer) {
            niveau = new Niveau();
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    public String nomNiveau() {
        return champNomNiveau.getText();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Vec2 posSouris = box2d.pixelToWorld(e.getX(), e.getY());
        switch (e.getButton()) {
            case 1: // clic gauche
                niveau.ajouterPoint(posSouris);
                break;
            case 2: // clic molette
                niveau.undo();
                break;
            case 3: // clic droit
                niveau.ajouterLiaison(posSouris);
                break;
        }
        repaint();
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
