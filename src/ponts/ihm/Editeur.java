package ponts.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;

import org.jbox2d.common.Vec2;

import ponts.niveau.Niveau;

public class Editeur extends JPanel implements ActionListener, MouseInputListener {

    Fenetre fenetre;
    Box2D box2d;
    Niveau niveau;

    JLabel texteNomNiveau;
    JLabel texteBudget;
    JTextField champNomNiveau;
    JTextField champBudget;
    JButton boutonSauvegarder;
    JButton boutonCharger;
    JButton boutonAnnuler;
    JButton boutonEffacer;
    JButton boutonSupprimer;
    JButton boutonJeu;

    public Editeur(Fenetre fenetre, Box2D box2d, int refreshRate) {

        this.fenetre = fenetre;
        this.box2d = box2d;
        ihm();

        niveau = new Niveau();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void ihm() {

        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        JPanel ligneHaut = new JPanel();
        ligneHaut.setLayout(new FlowLayout(FlowLayout.CENTER, fenetre.getWidth() / 20, fenetre.getHeight() / 100));
        ligneHaut.setOpaque(false);
        this.add(ligneHaut, BorderLayout.PAGE_START);

        JPanel colonneFichier = new JPanel();
        colonneFichier.setLayout(new BoxLayout(colonneFichier, BoxLayout.Y_AXIS));
        colonneFichier.setOpaque(false);
        ligneHaut.add(colonneFichier);
        JLabel texteFichier = new JLabel("Fichier");
        texteFichier.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneFichier.add(texteFichier);
        JPanel ligneFichier = new JPanel();
        ligneFichier.setOpaque(false);
        ligneFichier.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneFichier.add(ligneFichier);
        boutonSauvegarder = new JButton("Sauvegarder");
        boutonSauvegarder.addActionListener(this);
        ligneFichier.add(boutonSauvegarder);
        boutonCharger = new JButton("Charger");
        boutonCharger.addActionListener(this);
        ligneFichier.add(boutonCharger);
        boutonSupprimer = new JButton("Supprimer");
        boutonSupprimer.addActionListener(this);
        ligneFichier.add(boutonSupprimer);

        JPanel colonneNom = new JPanel();
        colonneNom.setLayout(new BoxLayout(colonneNom, BoxLayout.Y_AXIS));
        colonneNom.setOpaque(false);
        ligneHaut.add(colonneNom);
        JLabel texteNom = new JLabel("Nom");
        texteNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneNom.add(texteNom);
        JPanel ligneNom = new JPanel();
        ligneNom.setOpaque(false);
        ligneNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneNom.add(ligneNom);
        champNomNiveau = new JTextField(6);
        ligneNom.add(champNomNiveau);

        JPanel colonneBudget = new JPanel();
        colonneBudget.setLayout(new BoxLayout(colonneBudget, BoxLayout.Y_AXIS));
        colonneBudget.setOpaque(false);
        ligneHaut.add(colonneBudget);
        texteBudget = new JLabel("Budget");
        colonneBudget.add(texteBudget);
        JPanel ligneBudget = new JPanel();
        ligneBudget.setOpaque(false);
        ligneBudget.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneBudget.add(ligneBudget);
        champBudget = new JTextField("0", 5);
        ligneBudget.add(champBudget);

        JPanel colonneCreation = new JPanel();
        colonneCreation.setLayout(new BoxLayout(colonneCreation, BoxLayout.Y_AXIS));
        colonneCreation.setOpaque(false);
        ligneHaut.add(colonneCreation);
        JLabel texteCreation = new JLabel("Creation");
        texteCreation.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneCreation.add(texteCreation);
        JPanel ligneCreation = new JPanel();
        ligneCreation.setOpaque(false);
        ligneCreation.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneCreation.add(ligneCreation);
        boutonAnnuler = new JButton("Annuler");
        boutonAnnuler.addActionListener(this);
        ligneCreation.add(boutonAnnuler);
        boutonEffacer = new JButton("Effacer");
        boutonEffacer.addActionListener(this);
        ligneCreation.add(boutonEffacer);

        JPanel colonneJeu = new JPanel();
        colonneJeu.setLayout(new BoxLayout(colonneJeu, BoxLayout.Y_AXIS));
        colonneJeu.setOpaque(false);
        ligneHaut.add(colonneJeu);
        JLabel texteJeu = new JLabel("Jeu");
        texteJeu.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneJeu.add(texteJeu);
        JPanel ligneJeu = new JPanel();
        ligneJeu.setOpaque(false);
        ligneJeu.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneJeu.add(ligneJeu);
        boutonJeu = new JButton("Retour au jeu");
        boutonJeu.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonJeu.addActionListener(this);
        ligneJeu.add(boutonJeu);

        JPanel bas = new JPanel();
        bas.setLayout(new FlowLayout(FlowLayout.CENTER, fenetre.getWidth() / 20, fenetre.getHeight() / 50));
        bas.setOpaque(false);
        this.add(bas, BorderLayout.PAGE_END);

        JPanel colonnePoint = new JPanel();
        colonnePoint.setLayout(new BoxLayout(colonnePoint, BoxLayout.Y_AXIS));
        colonnePoint.setOpaque(false);
        bas.add(colonnePoint);
        JLabel pointCommande = new JLabel("Clic gauche :");
        pointCommande.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonnePoint.add(pointCommande);
        JLabel point = new JLabel("ajouter un point");
        point.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonnePoint.add(point);

        JPanel colonneLiaison = new JPanel();
        colonneLiaison.setLayout(new BoxLayout(colonneLiaison, BoxLayout.Y_AXIS));
        colonneLiaison.setOpaque(false);
        bas.add(colonneLiaison);
        JLabel liaisonCommande = new JLabel("Clic droit :");
        liaisonCommande.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneLiaison.add(liaisonCommande);
        JLabel liaison = new JLabel("ajouter une liaison");
        liaison.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneLiaison.add(liaison);
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
        Object source = e.getSource();

        if (source == boutonSauvegarder) {
            niveau.sauvegarder(fenetre, nomNiveau(), champBudget.getText());
        }
        if (source == boutonCharger) {
            Niveau niveauChargee = Niveau.charger(fenetre, nomNiveau());
            if (niveauChargee != null) {
                niveau = niveauChargee;
            }
            champBudget.setText(Integer.toString(niveau.getBudget()));
        }
        if (source == boutonSupprimer) {
            Niveau.supprimer(fenetre, nomNiveau());
        }
        if (source == boutonAnnuler) {
            niveau.undo();
        }
        if (source == boutonEffacer) {
            niveau = new Niveau();
        }
        if (source == boutonJeu) {
            fenetre.lancerJeu();
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
