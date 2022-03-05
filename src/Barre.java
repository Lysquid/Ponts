import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class Barre extends ObjetPhysique {

    float longueur, hauteur;
    float angle;

    public Barre(World world, float x, float y, float longueur, float hauteur) {
        this.longueur = longueur;
        this.hauteur = hauteur;

        // Etape 1 : Créer une "body definition"
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(x, y);

        // Etape 2 : Créer un "body"
        body = world.createBody(bd);

        // Etape 3 : Créer une "shape"
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(longueur / 2, hauteur / 2);

        // Etape 4 : Créer une "fixture" qui lie la shape au body
        body.createFixture(shape, 1);

    }

    public void draw(Graphics g, Box2D box2d) {

        g.setColor(Color.CYAN);

        float x = getX();
        float y = getY();
        float angle = getAngle();

        int[] xCoords = new int[4];
        int[] yCoords = new int[4];
        int[][] mult = { { 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };
        for (int i = 0; i < 4; i++) {
            float x2 = (x + longueur / 2 * mult[i][0]);
            float y2 = (y + hauteur / 2 * mult[i][1]);

            double x3 = ((x2 - x) * Math.cos(angle) - (y2 - y) * Math.sin(angle) + x);
            double y3 = ((x2 - x) * Math.sin(angle) + (y2 - y) * Math.cos(angle) + y);

            xCoords[i] = box2d.worldToPixelX((float) x3);
            yCoords[i] = box2d.worldToPixelY((float) y3);
        }

        g.drawPolygon(xCoords, yCoords, 4);

    }

}
