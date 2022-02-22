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

public class Box {

    Body body;

    float x, y;
    float w, h;
    float angle;

    Toolkit toolkit;
    Image image;
    BufferedImage bImage;

    public Box(World world, float _x, float _y, float _w, float _h) {
        this.x = _x;
        this.y = _y;
        this.w = _w;
        this.h = _h;

        // Step 1 : Body def
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(x, y);

        // Step 1 : Create body
        body = world.createBody(bd);

        // Step 3 : Create Shape
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(w / 2, h / 2);

        // Step 4 : Create fixture
        body.createFixture(ps, 1);

        toolkit = Toolkit.getDefaultToolkit();
        image = toolkit.getImage("res/box.png");

        // bImage = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_INT_RGB);
        // Graphics g = bImage.getGraphics();
        // g.drawImage(image, 0, 0, bImage);

        try {
            File img = new File("res/box.png");
            bImage = ImageIO.read(img);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void draw(Graphics g, JPanel frame) {
        g.setColor(Color.CYAN);
        Vec2 v = body.getTransform().p;
        x = v.x;
        y = v.y;
        angle = body.getAngle();

        if (w == h) {

            int w2 = bImage.getWidth();
            int h2 = bImage.getHeight();
            int x2 = (int) x;
            int y2 = (int) y;
            Graphics2D g2d = (Graphics2D) g;
            g2d.rotate(angle, x2, y2);
            g2d.drawImage(bImage, null, x2 - w2 / 2, y2 - h2 / 2);
            g2d.rotate(-angle, x2, y2);

        } else {
            int[] xCoords = new int[4];
            int[] yCoords = new int[4];
            int[][] mult = { { 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };
            for (int i = 0; i < 4; i++) {
                float x2 = (x + w / 2 * mult[i][0]);
                float y2 = (y + h / 2 * mult[i][1]);
                xCoords[i] = (int) ((x2 - x) * Math.cos(angle) - (y2 - y) * Math.sin(angle) +
                        x);
                yCoords[i] = (int) ((x2 - x) * Math.sin(angle) + (y2 - y) * Math.cos(angle) +
                        y);
            }
            g.drawPolygon(xCoords, yCoords, 4);
        }

    }

}
