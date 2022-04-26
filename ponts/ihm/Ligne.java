package ponts.ihm;

import javax.swing.JPanel;
import java.awt.FlowLayout;

/**
 * Classe définissant une version modifiée du JPanel souvent utilisée dans l'IHM
 */
public class Ligne extends JPanel {

    /**
     * Constructeur d'une ligne
     */
    public Ligne() {
        setOpaque(false);
    }

    /**
     * Constructeur d'une ligne modifiant l'espacement entre chaque composants
     * 
     * @param espacementX
     * @param espacementY
     */
    public Ligne(int espacementX, int espacementY) {
        this();
        setLayout(
                new FlowLayout(FlowLayout.CENTER, espacementX, espacementY));
    }

}
