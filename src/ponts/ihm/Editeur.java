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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    static final Path CHEMIN = Paths.get("res", "niveaux");

    Box2D box2d;
    Niveau niveau;

    JLabel textNomFichier;
    JLabel textBudget;
    JTextField champNomFichier;
    JTextField champBudget;
    JButton boutonSauvegarder;
    JButton boutonCharger;
    JButton boutonUndo;

    public Editeur(int largeur, int hauteur) {

        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setSize(largeur, hauteur);
        box2d = new Box2D(getWidth(), getHeight());

        addMouseListener(this);
        addMouseMotionListener(this);

        textBudget = new JLabel("Budget");
        add(textBudget);

        champBudget = new JTextField(4);
        add(champBudget);

        textNomFichier = new JLabel("Nom fichier");
        add(textNomFichier);

        champNomFichier = new JTextField(6);
        add(champNomFichier);

        boutonSauvegarder = new JButton("Sauvegarder");
        add(boutonSauvegarder);
        boutonSauvegarder.addActionListener(this);

        boutonCharger = new JButton("Charger");
        boutonCharger.addActionListener(this);
        add(boutonCharger);

        boutonUndo = new JButton("Undo");
        boutonUndo.addActionListener(this);
        add(boutonUndo);

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
        if (e.getSource() == boutonSauvegarder) {
            sauvegarderNiveau();
        }
        if (e.getSource() == boutonCharger) {
            chargerNiveau();
        }
        if (e.getSource() == boutonUndo) {
            niveau.undo();
            repaint();
        }
    }

    public String recupererChemin() {
        String nomFichier = champNomFichier.getText();
        return CHEMIN.resolve(nomFichier).toString();
    }

    public void sauvegarderNiveau() {

        niveau.ajouterExtremitees(box2d);

        String chemin = recupererChemin();
        int budget;
        try {
            budget = Integer.parseInt(champBudget.getText());
        } catch (NumberFormatException i) {
            budget = 0;
        }
        niveau.setBudget(budget);

        try {

            FileOutputStream fileOut = new FileOutputStream(chemin);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(niveau);
            objectOut.close();
            fileOut.close();
        } catch (FileNotFoundException i) {
            System.out.println("Nom de fichier invalide");
        } catch (IOException i) {
            i.printStackTrace();
        }
        repaint();
    }

    public void chargerNiveau() {
        String chemin = recupererChemin();

        try {
            FileInputStream fileIn = new FileInputStream(chemin);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            niveau = (Niveau) objectIn.readObject();
            objectIn.close();
            fileIn.close();

        } catch (FileNotFoundException i) {
            System.out.println("Fichier introuvable");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException i) {
            i.printStackTrace();
        }
        int budget = niveau.getBudget();
        if (budget != 0) {
            champBudget.setText(Integer.toString(budget));
        }
        repaint();
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
        repaint();
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
