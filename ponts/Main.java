package ponts;

import ponts.ihm.Fenetre;

/**
 * Class principale contentant le main
 */
public class Main {

    /**
     * Methode main, qui crée une fenêtre
     */
    public static void main(String[] args) {
        Fenetre.setLookAndFeel(); // Change l'apparence des composants Swing
        new Fenetre();
    }

}
