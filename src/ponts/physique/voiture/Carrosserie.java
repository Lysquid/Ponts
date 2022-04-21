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

public class Carrosserie extends ObjetPhysique {

    public static final int CATEGORY = Voiture.CATEGORY;
    public static final int MASK = Voiture.MASK;
    public static final Path CHEMIN = Paths.get("res", "images");

    Color couleurContour = Color.BLACK;
    Color couleurRemplissage = Color.RED;

    BufferedImage image;

    PolygonShape shape;
    float longueur = 8f;
    float largeur = 3f;

    public Carrosserie(World world, Vec2 pos) {
        creerObjetPhysique(world);
        setPos(pos);
        chargerImage();

    }

    @Override
    public void creerObjetPhysique(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        body = world.createBody(bodyDef);

        shape = new PolygonShape();
        shape.setAsBox(longueur / 2, largeur / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;
        body.createFixture(fixtureDef);
    }

    private void chargerImage() {
        String chemin = CHEMIN.resolve("Remycar.png").toString();
        try {
            image = ImageIO.read(new File(chemin));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dessiner(Graphics g, Box2D box2d) {
        // Grossissement
        double ratio = 1.2 * box2d.worldToPixel(longueur) / (double) image.getWidth();
        int w = (int) Math.ceil(image.getWidth() * ratio);
        int h = (int) Math.ceil(image.getHeight() * ratio);
        BufferedImage imageTournee = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform transform = new AffineTransform();
        transform.scale(ratio, ratio);
        AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        imageTournee = transformOp.filter(image, imageTournee);

        // Rotation
        int x = box2d.worldToPixelX(getPos().x);
        int y = box2d.worldToPixelY(getPos().y);
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(-getAngle(), x, y);
        g2d.drawImage(imageTournee, null, x - w / 2, y - h / 2);
        g2d.rotate(getAngle(), x, y);

        // Hitbox
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
        g.setColor(ObjetPhysique.setColorAlpha(couleurContour, 0));
        g.drawPolygon(xCoins, yCoins, 4);

    }

}
