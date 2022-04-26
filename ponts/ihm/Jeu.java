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
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;

import org.jbox2d.common.Vec2;

import ponts.niveau.Niveau;
import ponts.physique.Partie;
import ponts.physique.barres.Materiau;

/**
 * JPanel ou est dessiné le jeu
 */
public class Jeu extends JPanel implements ActionListener, MouseInputListener {

    private Fenetre fenetre;
    private Box2D box2d;
    private Partie partie;

    private Map<String, Integer> meilleursPrix;

    private int boutonSouris;
    private boolean clicSouris = false;
    private Vec2 posSouris = new Vec2();

    private static final int TICK_PHYSIQUE = 16;
    private long tempsPhysique;
    private Timer timerGraphique;
    private Timer timerPhysique;

    private JButton boutonPrecedent;
    private JComboBox<String> comboBoxNiveaux;
    private JButton boutonSuivant;
    private JButton boutonMateriauGoudron;
    private JButton boutonMateriauAcier;
    private JButton boutonMateriauBois;
    private JButton boutonDemarrer;
    private JButton boutonRecommencer;
    private JButton boutonEditeur;
    private JLabel textePrix;
    private JLabel texteBudget;
    private JLabel texteMeilleur;

    /**
     * Constructeur de Jeu
     * 
     * @param fenetre
     * @param box2d
     * @param refreshRate
     */
    public Jeu(Fenetre fenetre, Box2D box2d, int refreshRate) {

        this.fenetre = fenetre;
        this.box2d = box2d;
        meilleursPrix = new HashMap<String, Integer>();
        ihm();

        majListeNiveaux();

        timerPhysique = new Timer(TICK_PHYSIQUE, this);
        timerPhysique.start();

        int fps = (int) (1.0 / refreshRate * 1000.0);
        timerGraphique = new Timer(fps, this);
        timerGraphique.start();

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Methode créant tous les composants de l'IHM
     * Elle utilise avtangeusement les layout managers pour ne pas avoir positionner
     * manuellement tous les boutons
     * cela permet aussi d'adapter l'affichage à l'écran
     */
    private void ihm() {

        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        JPanel ligneHaut = new Ligne(box2d.getLargeurPixels() / 20, box2d.getHauteurPixels() / 100);
        this.add(ligneHaut, BorderLayout.PAGE_START);

        JPanel colonneNiveau = new Colonne();
        ligneHaut.add(colonneNiveau);
        JLabel texteNiveau = new JLabel("Niveau");
        colonneNiveau.add(texteNiveau);
        JPanel ligneNiveau = new Ligne();
        colonneNiveau.add(ligneNiveau);
        boutonPrecedent = new JButton("Précédent");
        boutonPrecedent.addActionListener(this);
        ligneNiveau.add(boutonPrecedent);
        comboBoxNiveaux = new JComboBox<String>();
        comboBoxNiveaux.addActionListener(this);
        ligneNiveau.add(comboBoxNiveaux);
        boutonSuivant = new JButton("Suivant");
        boutonSuivant.addActionListener(this);
        ligneNiveau.add(boutonSuivant);

        JPanel colonneMateriau = new Colonne();
        ligneHaut.add(colonneMateriau);
        JLabel textMateriau = new JLabel("Materiaux");
        colonneMateriau.add(textMateriau);
        JPanel ligneMateriau = new Ligne();
        colonneMateriau.add(ligneMateriau);
        boutonMateriauGoudron = new JButton("Goudron");
        boutonMateriauGoudron.setToolTipText("Le seul materiau sur lequel la voiture peut rouler");
        boutonMateriauGoudron.addActionListener(this);
        ligneMateriau.add(boutonMateriauGoudron);
        boutonMateriauBois = new JButton("Bois");
        boutonMateriauBois.setToolTipText("Materiau peu coûteux mais peu résistant");
        boutonMateriauBois.addActionListener(this);
        ligneMateriau.add(boutonMateriauBois);
        boutonMateriauAcier = new JButton("Acier");
        boutonMateriauAcier.setToolTipText("Materiau plus cher mais aussi plus solide");
        boutonMateriauAcier.addActionListener(this);
        ligneMateriau.add(boutonMateriauAcier);

        JPanel colonneSimulation = new Colonne();
        ligneHaut.add(colonneSimulation);
        JLabel texteControles = new JLabel("Simulation");
        colonneSimulation.add(texteControles);
        JPanel ligneControles = new Ligne();
        colonneSimulation.add(ligneControles);
        boutonDemarrer = new JButton();
        boutonDemarrer.addActionListener(this);
        ligneControles.add(boutonDemarrer);
        boutonRecommencer = new JButton("Recommencer");
        boutonRecommencer.addActionListener(this);
        ligneControles.add(boutonRecommencer);

        JPanel colonneEditeur = new Colonne();
        ligneHaut.add(colonneEditeur);
        JLabel texteEditeur = new JLabel("Editeur");
        colonneEditeur.add(texteEditeur);
        JPanel ligneEditeur = new Ligne();
        colonneEditeur.add(ligneEditeur);
        boutonEditeur = new JButton("Editer un niveau");
        boutonEditeur.addActionListener(this);
        ligneEditeur.add(boutonEditeur);

        JPanel bas = new Ligne(box2d.getLargeurPixels() / 20, box2d.getHauteurPixels() / 50);
        this.add(bas, BorderLayout.PAGE_END);

        JPanel colonnePrix = new Colonne();
        bas.add(colonnePrix);
        JLabel prix = new JLabel("Prix");
        colonnePrix.add(prix);
        textePrix = new JLabel();
        colonnePrix.add(textePrix);

        JPanel colonneBudget = new Colonne();
        bas.add(colonneBudget);
        JLabel budget = new JLabel("Budget");
        colonneBudget.add(budget);
        texteBudget = new JLabel();
        colonneBudget.add(texteBudget);

        JPanel colonneMeilleur = new Colonne();
        bas.add(colonneMeilleur);
        JLabel meilleur = new JLabel("Meilleur");
        colonneMeilleur.add(meilleur);
        texteMeilleur = new JLabel();
        colonneMeilleur.add(texteMeilleur);
    }

    /**
     * Méthode pour dessiner le jeu, héritée de JPanel
     */
    @Override
    public void paintComponent(Graphics g0) {

        // Activer l'anti-aliasing
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Toolkit.getDefaultToolkit().sync(); // Fonction pour améliorer l'affichage sur Mac/Linux

        g.setColor(Color.decode("#55a3d4"));
        g.fillRect(0, 0, getWidth(), getHeight());

        if (partie != null) {
            majTextLabels();
            partie.dessiner(g, box2d, posSouris);
        }
    }

    /**
     * Traite les évenements des timers et des composants de l'IHM
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (partie != null) {
            if (source == timerPhysique) {

                long nouveauTempsPhysique = System.currentTimeMillis();
                float dt = (nouveauTempsPhysique - tempsPhysique) / 1000f;
                tempsPhysique = nouveauTempsPhysique;

                partie.tickPhysique(posSouris, boutonSouris, clicSouris, dt);
                clicSouris = false;

            }

            if (source == timerGraphique) {
                repaint();
            }

            if (source == boutonPrecedent) {
                int numero = comboBoxNiveaux.getSelectedIndex();
                if (numero > 0) {
                    numero--;
                    comboBoxNiveaux.setSelectedIndex(numero);
                }
            }

            if (source == boutonSuivant) {
                int numero = comboBoxNiveaux.getSelectedIndex();
                if (numero < comboBoxNiveaux.getItemCount() - 1) {
                    numero++;
                    comboBoxNiveaux.setSelectedIndex(numero);
                }
            }

            if (source == comboBoxNiveaux) {
                nouvellePartie();
            }

            Materiau materiau = null;
            if (source == boutonMateriauBois) {
                materiau = Materiau.BOIS;
            }
            if (source == boutonMateriauGoudron) {
                materiau = Materiau.GOUDRON;
            }
            if (source == boutonMateriauAcier) {
                materiau = Materiau.ACIER;
            }
            if (materiau != null) {
                partie.changementMateriau(materiau);
            }

            if (source == boutonDemarrer) {
                partie.toggleSimulationPhysique();
                majSimulation();
            }

            if (source == boutonRecommencer) {
                nouvellePartie();
            }
        }

        if (source == boutonEditeur) {
            if (partie != null) {
                partie.setSimulationPhsyique(false);
                majSimulation();
            }
            fenetre.lancerEditeur();
        }

    }

    /**
     * Met à jour les labels concernant le prix en bas de l'écran
     */
    private void majTextLabels() {
        textePrix.setText(Integer.toString(partie.getPrix()) + " $");
        texteBudget.setText(Integer.toString(partie.getBuget()) + " $");
        int meilleurPrix = recupererMeilleurPrix();
        if (meilleurPrix == -1) {
            texteMeilleur.setText("Ø");
        } else {
            texteMeilleur.setText(Integer.toString(meilleurPrix) + " $");
        }
    }

    /**
     * Met à jour la liste des niveaux
     */
    public void majListeNiveaux() {
        String niveauSelectionne = recupererNomNiveau();
        String[] nomsNiveaux = new File(Niveau.CHEMIN_NIVEAUX.toString()).list();
        Arrays.sort(nomsNiveaux);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nomsNiveaux);
        comboBoxNiveaux.setModel(model);
        if (niveauSelectionne != null && Arrays.asList(nomsNiveaux).contains(niveauSelectionne)) {
            comboBoxNiveaux.setSelectedItem(niveauSelectionne);
        } else {
            nouvellePartie();
        }
    }

    /**
     * Met à jour le bouton controlant la simulation en fonction de l'état du jeu
     */
    private void majSimulation() {
        reinitialiserTemps();
        if (partie.getSimulationPhysique()) {
            boutonDemarrer.setText("Pause");
        } else {
            boutonDemarrer.setText("Reprendre");
        }
    }

    /**
     * Reccupére un niveau
     * 
     * @return niveau
     */
    private Niveau recupererNiveau() {
        boutonDemarrer.setText("Démarrer");
        String nomNiveau = recupererNomNiveau();
        return Niveau.charger(fenetre, nomNiveau);

    }

    /**
     * Reccupère le nom du niveau sélectionné dans la liste
     * 
     * @return
     */
    private String recupererNomNiveau() {
        String nomNiveau = (String) comboBoxNiveaux.getSelectedItem();
        return nomNiveau;
    }

    /**
     * Gestion de la fin de partie
     * 
     * @param niveauReussi
     * @param prix
     */
    public void finPartie(boolean niveauReussi, int prix) {
        majMeilleurPrix(prix);
        messageFinPartie(niveauReussi);
        reinitialiserTemps();
    }

    /**
     * Affiche le tutoriel
     */
    public void messageDebutJeu() {
        String texte = "Bienvenue sur notre jeu de ponts !";
        texte += "\n\n" + "Tu peux construire ton pont en cliquant sur des liaisons (les cercles).";
        texte += "\n" + "Choisis bien le materiau en fonction de ses propriétés et son prix.";
        texte += "\n" + "Quand tu es prêt, lance la simulation en cliquant sur le bouton ";
        texte += boutonDemarrer.getText() + ".";
        texte += "\n\n" + "Le niveau sera réussi si la voiture arrive de l'autre côté";
        texte += "\n" + "et si le prix du pont est inférieur au budget.";
        texte += "\n\n" + "Bonne chance !";
        JOptionPane.showMessageDialog(fenetre, texte, "Tutoriel", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Affiche le message de fin de partie
     * 
     * @param niveauReussi
     */
    private void messageFinPartie(boolean niveauReussi) {
        String texte = "";
        String titre = "";
        if (niveauReussi) {
            titre = "Niveau terminé";
            texte += "Bravo, tu as réussi le niveau " + recupererNomNiveau() + " !";
            texte += "\n\n" + "Prix : " + Integer.toString(partie.getPrix()) + " $";
            texte += "\n" + "Meilleur : " + Integer.toString(recupererMeilleurPrix()) + " $";
            texte += "\n\n" + "Tu peux passer au niveau suivant";
            texte += "\n" + "ou essayer de faire un pont moins cher.";
        } else {
            titre = "Niveau échoué";
            texte += "Dommage, tu n'as pas réussi le niveau " + recupererNomNiveau() + ".";
            texte += "\n\n" + "Ton pont a coûté trop cher :";
            texte += "\n" + "Prix : " + Integer.toString(partie.getPrix()) + " $";
            texte += "\n" + "Budget : " + Integer.toString(partie.getBuget()) + " $";
            texte += "\n\n" + "Tu peux réessayer en cliquant sur " + boutonRecommencer.getText() + ".";
        }
        JOptionPane.showMessageDialog(fenetre, texte, titre, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Met à jour l'affichage du meilleur prix réalisé
     */
    private void majMeilleurPrix(int prix) {
        int meilleurPrix = recupererMeilleurPrix();
        if (prix <= meilleurPrix || meilleurPrix == -1) {
            meilleursPrix.put(recupererNomNiveau(), prix);
        }
    }

    /**
     * Reccupère le meilleur prix associé au niveau
     * 
     * @return meilleur prix
     */
    private int recupererMeilleurPrix() {
        if (meilleursPrix.containsKey(recupererNomNiveau())) {
            return meilleursPrix.get(recupererNomNiveau());
        } else {
            return -1;
        }
    }

    /**
     * Reinitilise le temps écoulé depuis la dernière mise à jour de la physique,
     * typiquement après avoir mis le jeu en pause
     */
    private void reinitialiserTemps() {
        tempsPhysique = System.currentTimeMillis();
    }

    /**
     * Creer une nouvelle partie
     */
    private void nouvellePartie() {
        Niveau niveau = recupererNiveau();
        if (niveau != null) {
            partie = new Partie(this, box2d, niveau);
        } else {
            partie = null;
        }
    }

    /**
     * Verifie si le niveau a bien été chargé et affiche un message d'erreur dans le
     * cas contraire
     */
    public void verifierExistanceNiveaux() {
        if (partie == null) {
            String texte = "Aucun niveau détecté.";
            texte += "\n" + "Commence par en créer un en cliquant sur le bouton '" + boutonEditeur.getText() + "'";
            JOptionPane.showMessageDialog(fenetre, texte, "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Met à jour la position de la souris dans un vecteur
     * 
     * @param e
     */
    private void majPosSouris(MouseEvent e) {
        posSouris = box2d.pixelToWorld(e.getX(), e.getY());
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
        majPosSouris(e);
        boutonSouris = e.getButton();
        clicSouris = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
