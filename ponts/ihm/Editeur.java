package ponts.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;

import org.jbox2d.common.Vec2;

import ponts.niveau.Niveau;

/**
 * Classe de l'éditeur de niveaux
 */
public class Editeur extends JPanel implements ActionListener, MouseInputListener {

    private Fenetre fenetre;
    private Box2D box2d;
    private Niveau niveau;

    private JButton boutonSauvegarder;
    private JButton boutonCharger;
    private JButton boutonSupprimer;
    private JTextField champNomNiveau;
    private JTextField champBudget;
    private JButton boutonAnnuler;
    private JButton boutonEffacer;
    private JButton boutonJeu;

    /**
     * Constructeur de l'éditeur
     * 
     * @param fenetre
     * @param box2d
     * @param refreshRate
     */
    public Editeur(Fenetre fenetre, Box2D box2d, int refreshRate) {

        this.fenetre = fenetre;
        this.box2d = box2d;
        ihm();

        niveau = new Niveau();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Méthode créeant tous les composants de l'IHM
     */
    private void ihm() {

        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        JPanel ligneHaut = new Ligne(box2d.getLargeurPixels() / 20, box2d.getHauteurPixels() / 100);
        this.add(ligneHaut, BorderLayout.PAGE_START);

        JPanel colonneFichier = new Colonne();
        ligneHaut.add(colonneFichier);
        JLabel texteFichier = new JLabel("Fichier");
        colonneFichier.add(texteFichier);
        JPanel ligneFichier = new Ligne();
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

        JPanel colonneNom = new Colonne();
        ligneHaut.add(colonneNom);
        JLabel texteNom = new JLabel("Nom");
        colonneNom.add(texteNom);
        JPanel ligneNom = new Ligne();
        colonneNom.add(ligneNom);
        champNomNiveau = new JTextField(6);
        ligneNom.add(champNomNiveau);

        JPanel colonneBudget = new Colonne();
        ligneHaut.add(colonneBudget);
        JLabel texteBudget = new JLabel("Budget");
        colonneBudget.add(texteBudget);
        JPanel ligneBudget = new Ligne();
        colonneBudget.add(ligneBudget);
        champBudget = new JTextField("0", 5);
        ligneBudget.add(champBudget);

        JPanel colonneCreation = new Colonne();
        ligneHaut.add(colonneCreation);
        JLabel texteCreation = new JLabel("Creation");
        colonneCreation.add(texteCreation);
        JPanel ligneCreation = new Ligne();
        colonneCreation.add(ligneCreation);
        boutonAnnuler = new JButton("Annuler");
        boutonAnnuler.addActionListener(this);
        ligneCreation.add(boutonAnnuler);
        boutonEffacer = new JButton("Effacer");
        boutonEffacer.addActionListener(this);
        ligneCreation.add(boutonEffacer);

        JPanel colonneJeu = new Colonne();
        ligneHaut.add(colonneJeu);
        JLabel texteJeu = new JLabel("Jeu");
        colonneJeu.add(texteJeu);
        JPanel ligneJeu = new Ligne();
        colonneJeu.add(ligneJeu);
        boutonJeu = new JButton("Retour au jeu");
        boutonJeu.addActionListener(this);
        ligneJeu.add(boutonJeu);

        JPanel bas = new Ligne(box2d.getLargeurPixels() / 20, box2d.getHauteurPixels() / 50);
        this.add(bas, BorderLayout.PAGE_END);

        JPanel colonnePoint = new Colonne();
        bas.add(colonnePoint);
        JLabel pointCommande = new JLabel("Clic gauche :");
        colonnePoint.add(pointCommande);
        JLabel point = new JLabel("ajouter un point");
        colonnePoint.add(point);

        JPanel colonneLiaison = new Colonne();
        bas.add(colonneLiaison);
        JLabel liaisonCommande = new JLabel("Clic droit :");
        colonneLiaison.add(liaisonCommande);
        JLabel liaison = new JLabel("ajouter une liaison");
        colonneLiaison.add(liaison);
    }

    /**
     * Méthode paint, pour mettre à jour l'affichage de l'éditeur
     */
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

    /**
     * Gère les évenements des boutons
     */
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

    /**
     * Renvoie le nom du niveau
     * 
     * @return nom du niveau
     */
    private String nomNiveau() {
        return champNomNiveau.getText();
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
