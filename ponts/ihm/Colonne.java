package ponts.ihm;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Classe définissant une version modifiée du JPanel souvent utilisée dans l'IHM
 */
public class Colonne extends JPanel {

    /**
     * Constucteur d'une colonne, définit son layout manager
     */
    public Colonne() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * Ajoute un composant en le centrant
     */
    @Override
    public Component add(Component composant) {
        if (composant instanceof JComponent) {
            ((JComponent) composant).setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        return super.add(composant);
    }

}
