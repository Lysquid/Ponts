package ponts.ihm;

import org.jbox2d.common.Vec2;

public class Box2D {

    Fenetre fenetre;

    float largeur;
    float hauteur;

    public Box2D(Fenetre fenetre) {
        this.fenetre = fenetre;

        largeur = 100f;
        hauteur = getHauteurPixels() * coeff();

    }

    public float coeff() {
        // coefficient du nombre de metre par pixel
        return largeur / getLargeurPixels();
    }

    public float pixelToWorld(int scalaire) {
        return coeff() * scalaire;
    }

    public float pixelToWorldX(int xP) {
        return pixelToWorld(xP);
    }

    public float pixelToWorldY(int yP) {
        return pixelToWorld(getHauteurPixels() - yP);
    }

    public Vec2 pixelToWorld(int xP, int yP) {
        return new Vec2(pixelToWorld(xP), pixelToWorldY(yP));
    }

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