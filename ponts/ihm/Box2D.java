package ponts.ihm;

import org.jbox2d.common.Vec2;

/**
 * Singleton intialisé au début du jeu, qui gère les conversions entre les
 * coordonnées en pixels de l'IHM, aux float (mètres) de la simulation physiques
 */
public class Box2D {

    private Fenetre fenetre;

    private float largeur;
    private float hauteur;

    /**
     * Constructeur de l'objet Box2D
     * 
     * @param fenetre
     */
    public Box2D(Fenetre fenetre) {
        this.fenetre = fenetre;

        largeur = 100f;
        hauteur = getHauteurPixels() * coeff();

    }

    /**
     * Calcul du coefficient du nombre de mètres par pixel
     * 
     * @return coefficient
     */
    private float coeff() {
        return largeur / getLargeurPixels();
    }

    /**
     * Convertit une distance en pixels en mètres
     * Des fonctions spécifiques sont définies pour une coordonnées en X et en Y
     * 
     * @param scalaire
     * @return distance en mètres
     */
    public float pixelToWorld(int scalaire) {
        return coeff() * scalaire;
    }

    public float pixelToWorldX(int xP) {
        return pixelToWorld(xP);
    }

    public float pixelToWorldY(int yP) {
        return pixelToWorld(getHauteurPixels() - yP); // l'axe y de l'IHM et de la simulation sont inversés
    }

    public Vec2 pixelToWorld(int xP, int yP) {
        return new Vec2(pixelToWorld(xP), pixelToWorldY(yP));
    }

    /**
     * Convertit une distance en mètres en pixels
     * Des fonctions spécifiques sont définies pour une coordonnées en X et en Y
     * 
     * @param scalaire
     * @return distance en pixels
     */
    public int worldToPixel(float scalaire) {
        return Math.round(scalaire / coeff());
    }

    public int worldToPixelX(float x) {
        return worldToPixel(x);
    }

    public int worldToPixelY(float y) {
        return getHauteurPixels() - worldToPixel(y);
    }

    public float getLargeur() {
        return largeur;
    }

    public float getHauteur() {
        return hauteur;
    }

    public int getLargeurPixels() {
        return fenetre.getWidth();
    }

    public int getHauteurPixels() {
        return fenetre.getHeight();
    }

}
