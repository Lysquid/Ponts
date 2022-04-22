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
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
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

public class Jeu extends JPanel implements ActionListener, MouseInputListener {

    Timer timerGraphique;
    Timer timerPhysique;
    static final int TICK_PHYSIQUE = 16;
    long tempsPhysique;

    JButton boutonPrecedent;
    JComboBox<String> comboBoxNiveaux;
    JButton boutonSuivant;
    JButton boutonMateriauGoudron;
    JButton boutonMateriauBois;
    JButton boutonMateriauAcier;
    JButton boutonDemarrer;
    JButton boutonRecommencer;
    JButton boutonEditeur;
    JLabel textePrix;
    JLabel texteBudget;
    JLabel texteMeilleur;

    Fenetre fenetre;
    Box2D box2d;
    Partie partie;

    int boutonSouris;
    boolean clicSouris = false;
    Vec2 posSouris = new Vec2();
    public Map<String, Integer> meilleursPrix;

    public Jeu(Fenetre fenetre, int refreshRate) {

        this.fenetre = fenetre;
        meilleursPrix = new HashMap<String, Integer>();
        ihm();

        box2d = new Box2D(fenetre.getWidth(), fenetre.getHeight());
        majListeNiveaux();

        timerPhysique = new Timer(TICK_PHYSIQUE, this);
        timerPhysique.start();

        int fps = (int) (1.0 / refreshRate * 1000.0);
        timerGraphique = new Timer(fps, this);
        timerGraphique.start();

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

        JPanel colonneNiveau = new JPanel();
        colonneNiveau.setLayout(new BoxLayout(colonneNiveau, BoxLayout.Y_AXIS));
        colonneNiveau.setOpaque(false);
        ligneHaut.add(colonneNiveau);
        JLabel texteNiveau = new JLabel("Niveau");
        texteNiveau.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneNiveau.add(texteNiveau);
        JPanel ligneNiveau = new JPanel();
        ligneNiveau.setOpaque(false);
        ligneNiveau.setAlignmentX(Component.CENTER_ALIGNMENT);
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

        JPanel colonneMateriau = new JPanel();
        colonneMateriau.setLayout(new BoxLayout(colonneMateriau, BoxLayout.Y_AXIS));
        colonneMateriau.setOpaque(false);
        ligneHaut.add(colonneMateriau);
        JLabel textMateriau = new JLabel("Materiaux");
        textMateriau.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneMateriau.add(textMateriau);
        JPanel ligneMateriau = new JPanel();
        ligneMateriau.setOpaque(false);
        colonneMateriau.add(ligneMateriau);
        boutonMateriauGoudron = new JButton("Goudron");
        boutonMateriauGoudron.addActionListener(this);
        ligneMateriau.add(boutonMateriauGoudron);
        boutonMateriauBois = new JButton("Bois");
        boutonMateriauBois.addActionListener(this);
        ligneMateriau.add(boutonMateriauBois);
        boutonMateriauAcier = new JButton("Acier");
        boutonMateriauAcier.addActionListener(this);
        ligneMateriau.add(boutonMateriauAcier);

        JPanel colonneSimulation = new JPanel();
        colonneSimulation.setLayout(new BoxLayout(colonneSimulation, BoxLayout.Y_AXIS));
        colonneSimulation.setOpaque(false);
        ligneHaut.add(colonneSimulation);
        JLabel texteControles = new JLabel("Simulation");
        texteControles.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneSimulation.add(texteControles);
        JPanel ligneControles = new JPanel();
        ligneControles.setOpaque(false);
        colonneSimulation.add(ligneControles);
        boutonDemarrer = new JButton();
        boutonDemarrer.addActionListener(this);
        ligneControles.add(boutonDemarrer);
        boutonRecommencer = new JButton("Recommencer");
        boutonRecommencer.addActionListener(this);
        ligneControles.add(boutonRecommencer);

        JPanel colonneEditeur = new JPanel();
        colonneEditeur.setLayout(new BoxLayout(colonneEditeur, BoxLayout.Y_AXIS));
        colonneEditeur.setOpaque(false);
        ligneHaut.add(colonneEditeur);
        JLabel texteEditeur = new JLabel("Editeur");
        texteEditeur.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneEditeur.add(texteEditeur);
        JPanel ligneEditeur = new JPanel();
        ligneEditeur.setOpaque(false);
        colonneEditeur.add(ligneEditeur);
        boutonEditeur = new JButton("Editer un niveau");
        boutonEditeur.addActionListener(this);
        ligneEditeur.add(boutonEditeur);

        JPanel bas = new JPanel();
        bas.setLayout(new FlowLayout(FlowLayout.CENTER, fenetre.getWidth() / 20, fenetre.getHeight() / 50));
        bas.setOpaque(false);
        this.add(bas, BorderLayout.PAGE_END);

        JPanel colonnePrix = new JPanel();
        colonnePrix.setLayout(new BoxLayout(colonnePrix, BoxLayout.Y_AXIS));
        colonnePrix.setOpaque(false);
        bas.add(colonnePrix);
        JLabel prix = new JLabel("Prix");
        prix.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonnePrix.add(prix);
        textePrix = new JLabel();
        textePrix.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonnePrix.add(textePrix);

        JPanel colonneBudget = new JPanel();
        colonneBudget.setLayout(new BoxLayout(colonneBudget, BoxLayout.Y_AXIS));
        colonneBudget.setOpaque(false);
        bas.add(colonneBudget);
        JLabel budget = new JLabel("Budget");
        budget.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneBudget.add(budget);
        texteBudget = new JLabel();
        texteBudget.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneBudget.add(texteBudget);

        JPanel colonneMeilleur = new JPanel();
        colonneMeilleur.setLayout(new BoxLayout(colonneMeilleur, BoxLayout.Y_AXIS));
        colonneMeilleur.setOpaque(false);
        bas.add(colonneMeilleur);
        JLabel meilleur = new JLabel("Meilleur");
        meilleur.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneMeilleur.add(meilleur);
        texteMeilleur = new JLabel();
        texteMeilleur.setAlignmentX(Component.CENTER_ALIGNMENT);
        colonneMeilleur.add(texteMeilleur);
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
            majTextLabels();
            partie.dessiner(g, box2d, posSouris);
        }
    }

    private void majTextLabels() {
        textePrix.setText(Integer.toString(partie.prix()) + " $");
        texteBudget.setText(Integer.toString(partie.budget()) + " $");
        int meilleurPrix = recupererMeilleurPrix();
        if (meilleurPrix == -1) {
            texteMeilleur.setText("Ø");
        } else {
            texteMeilleur.setText(Integer.toString(meilleurPrix) + " $");
        }
    }

    public void majListeNiveaux() {
        String niveauSelectionne = recupererNomNiveau();
        String[] nomsNiveaux = new File(Niveau.CHEMIN.toString()).list();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(nomsNiveaux);
        comboBoxNiveaux.setModel(model);
        if (niveauSelectionne != null && Arrays.asList(nomsNiveaux).contains(niveauSelectionne)) {
            comboBoxNiveaux.setSelectedItem(niveauSelectionne);
        } else {
            nouvellePartie();
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

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

        if (source == boutonEditeur) {
            partie.setSimulationPhsyique(false);
            majSimulation();
            fenetre.lancerEditeur();
        }

    }

    public void majSimulation() {
        reinitialiserTemps();
        if (partie.isSimulationPhysique()) {
            boutonDemarrer.setText("Pause");
        } else {
            boutonDemarrer.setText("Reprendre");
        }
    }

    private Niveau recupererNiveau() {
        boutonDemarrer.setText("Démarrer");
        String nomNiveau = recupererNomNiveau();
        return Niveau.charger(fenetre, nomNiveau);

    }

    public String recupererNomNiveau() {
        return (String) comboBoxNiveaux.getSelectedItem();
    }

    public void majPosSouris(MouseEvent e) {
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

    public void finPartie(boolean niveauReussi, int prix) {
        majMeilleurPrix(prix);
        messageFinPartie(niveauReussi);
        reinitialiserTemps();
    }

    public void messageDebutJeu() {
        String texte = "Bienvenue sur notre jeu de ponts !";
        texte += "\n\n" + "Tu peux construire ton pont en cliquant sur des liaisons (les cercles).";
        texte += "\n" + "Choisis bien le materiau en fonction de ses propriétés et son prix.";
        texte += "\n" + "Quand tu es prêt, lance la simulation en cliquant sur le bouton ";
        texte += boutonDemarrer.getText() + ".";
        texte += "\n\n" + "Le niveau sera réussi si tu la voiture arrive de l'autre côté";
        texte += "\n" + "et si le prix du pont est inférieur au budget.";
        texte += "\n\n" + "Bonne chance !";
        JOptionPane.showMessageDialog(fenetre, texte, "Tutoriel", JOptionPane.PLAIN_MESSAGE);
    }

    public void messageFinPartie(boolean niveauReussi) {
        String texte = "";
        String titre = "";
        if (niveauReussi) {
            titre = "Niveau terminé";
            texte += "Bravo, tu as réussi le niveau " + recupererNomNiveau() + " !";
            texte += "\n\n" + "Prix : " + Integer.toString(partie.prix()) + " $";
            texte += "\n" + "Meilleur : " + Integer.toString(recupererMeilleurPrix()) + " $";
            texte += "\n\n" + "Tu peux passer au niveau suivant";
            texte += "\n" + "ou essayer de faire un pont moins cher.";
        } else {
            titre = "Niveau échoué";
            texte += "Dommage, tu n'as pas réussi le niveau " + recupererNomNiveau() + ".";
            texte += "\n\n" + "Ton pont a coûté trop cher :";
            texte += "\n" + "Prix : " + Integer.toString(partie.prix()) + " $";
            texte += "\n" + "Budget : " + Integer.toString(partie.budget()) + " $";
            texte += "\n\n" + "Tu peux réessayer en cliquant sur " + boutonRecommencer.getText() + ".";
        }
        JOptionPane.showMessageDialog(fenetre, texte, titre, JOptionPane.PLAIN_MESSAGE);
    }

    public void majMeilleurPrix(int prix) {
        int meilleurPrix = recupererMeilleurPrix();
        if (prix <= meilleurPrix || meilleurPrix == -1) {
            meilleursPrix.put(recupererNomNiveau(), prix);
        }
    }

    public int recupererMeilleurPrix() {
        if (meilleursPrix.containsKey(recupererNomNiveau())) {
            return meilleursPrix.get(recupererNomNiveau());
        } else {
            return -1;
        }
    }

    public void reinitialiserTemps() {
        tempsPhysique = System.currentTimeMillis();
    }

    public void nouvellePartie() {
        partie = new Partie(this, box2d, recupererNiveau());
    }
}
