import org.jbox2d.common.Vec2;

public class Box2D {

    int largeurP, hauteurP;
    float largeur, hauteur;
    private float coeff;

    public Box2D(int largeurP, int hauteurP) {
        this.largeurP = largeurP;
        this.hauteurP = hauteurP;

        largeur = 50f;
        coeff = largeur / largeurP;
        hauteur = hauteurP * coeff;
        // coefficient du nombre de metre par pixel

    }

    public float pixelToWorld(int scalaire) {
        return coeff * scalaire;
    }

    public float pixelToWorldX(int xP) {
        return pixelToWorld(xP);
    }

    public float pixelToWorldY(int yP) {
        return pixelToWorld(hauteurP - yP);
    }

    public Vec2 pixelToWorld(int xP, int yP) {
        return new Vec2(pixelToWorld(xP), pixelToWorldY(yP));
    }

    public Vec2 pixelToWorld(int[] coordP) {
        return pixelToWorld(coordP[0], coordP[1]);
    }

    public int worldToPixel(float scalaire) {
        return Math.round(scalaire / coeff);
    }

    public int worldToPixelX(float x) {
        return worldToPixel(x);
    }

    public int worldToPixelY(float y) {
        return hauteurP - worldToPixel(y);
    }

    public int[] worldToPixel(float x, float y) {
        int[] coord = { worldToPixelX(x), worldToPixelY(y) };
        return coord;
    }

    public int[] worldToPixel(Vec2 coord) {
        return worldToPixel(coord.x, coord.y);
    }

}
