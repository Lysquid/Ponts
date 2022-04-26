package ponts.physique.voiture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import ponts.ihm.Box2D;
import ponts.physique.ObjetPhysique;

/**
 * Classe de la carrosserie de la voiture
 */
public class Carrosserie extends ObjetPhysique {

    public static final int CATEGORY = Voiture.CATEGORY;
    public static final int MASK = Voiture.MASK;
    public static final Path CHEMIN_IMAGES = Paths.get("res", "images");

    private BufferedImage image;
    private Color couleurContour = Color.BLACK;

    private PolygonShape shape;
    private float longueur = 8f;
    private float largeur = 3f;

    /**
     * Constructeur de la carrosserie
     * 
     * @param world
     * @param pos
     */
    public Carrosserie(World world, Vec2 pos) {
        creerObjetPhysique(world);
        setPos(pos);
        chargerImage();

    }

    @Override
    protected void creerObjetPhysique(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        body = world.createBody(bodyDef);

        shape = new PolygonShape();
        shape.setAsBox(longueur / 2, largeur / 2);

        FixtureDef fixtureDef = creerFixtureDef(FRICTION, ELASTICITE, DENSITE, CATEGORY, MASK);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }

    /**
     * Charge l'image de la carrosserie
     */
    private void chargerImage() {
        String chemin = CHEMIN_IMAGES.resolve("voiture.png").toString();
        try {
            image = ImageIO.read(new File(chemin));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dessine la carrosserie
     * 
     * @param g
     * @param box2d
     */
    public void dessiner(Graphics g, Box2D box2d) {
        // Grossissement
        double ratio = 1.2 * box2d.worldToPixel(longueur) / (double) image.getWidth();
        int largeur = (int) Math.ceil(image.getWidth() * ratio);
        int hauteur = (int) Math.ceil(image.getHeight() * ratio);
        BufferedImage imageTournee = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        AffineTransform transform = new AffineTransform();
        transform.scale(ratio, ratio);
        AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        imageTournee = transformOp.filter(image, imageTournee);

        // Rotation
        int x = box2d.worldToPixelX(getPos().x);
        int y = box2d.worldToPixelY(getPos().y);
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(-getAngle(), x, y);
        g2d.drawImage(imageTournee, null, x - largeur / 2, y - hauteur / 2);
        g2d.rotate(getAngle(), x, y);

        // Hitbox (si nécessaire)
        int[] xCoins = new int[4];
        int[] yCoins = new int[4];
        for (int i = 0; i < 4; i++) {
            Vec2 pos = shape.getVertex(i);

            float cos = (float) Math.cos(getAngle());
            float sin = (float) Math.sin(getAngle());
            float x2 = pos.x * cos - pos.y * sin + getX();
            float y2 = pos.x * sin + pos.y * cos + getY();
            xCoins[i] = box2d.worldToPixelX(x2);
            yCoins[i] = box2d.worldToPixelY(y2);
        }
        g.setColor(ObjetPhysique.setColorAlpha(couleurContour, 0)); // pour debug, changer la valeur du alpha
        g.drawPolygon(xCoins, yCoins, 4);

    }

}
