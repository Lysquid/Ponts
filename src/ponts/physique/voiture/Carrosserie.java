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

    PolygonShape shape;
    float longueur = 8f;
    float largeur = 3f;

    public Carrosserie(World world, Vec2 pos) {

        creerObjetPhysique(world);
        setPos(pos);

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

    public void dessiner(Graphics g, Box2D box2d) {
        int[] xCoins = new int[4];
        int[] yCoins = new int[4];

        for (int i = 0; i < 4; i++) {
            Vec2 pos = shape.getVertex(i);

            float cos = (float) Math.cos(getAngle());
            float sin = (float) Math.sin(getAngle());
            float x = pos.x * cos - pos.y * sin + getX();
            float y = pos.x * sin + pos.y * cos + getY();

            xCoins[i] = box2d.worldToPixelX(x);
            yCoins[i] = box2d.worldToPixelY(y);
        }

        /*
         * g.setColor(couleurRemplissage);
         * g.fillPolygon(xCoins, yCoins, 4);
         * g.setColor(couleurContour);
         * g.drawPolygon(xCoins, yCoins, 4);
         */

        String chemin = CHEMIN.resolve("Remycar.png").toString();

        try {
            BufferedImage image = ImageIO.read(new File(chemin));
            // Image image2 = image.getScaledInstance((int)(longueur/2), (int)(largeur/2),
            // BufferedImage.SCALE_SMOOTH);

            int w2 = (int) (image.getWidth());
            int h2 = (int) (image.getHeight());
            BufferedImage image2 = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_ARGB);
            AffineTransform at = new AffineTransform();
            at.scale(0.3, 0.6);
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            image2 = scaleOp.filter(image, image2);

            int w3 = (int) (image2.getWidth());
            int h3 = (int) (image2.getHeight());
            int x2 = (int) box2d.worldToPixelX(getPos().x);// (int) x;
            int y2 = (int) box2d.worldToPixelY(getPos().y);

            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(-getAngle(), x2, y2);
            g2d.drawImage(image2, null, x2 - w3 / 7, y2 - h3 / 5);
            g2d.rotate(getAngle(), x2, y2);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
