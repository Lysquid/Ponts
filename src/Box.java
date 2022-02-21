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

        // int[] xCoords = new int[4];
        // int[] yCoords = new int[4];

        // int[][] mult = { { 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };
        // for (int i = 0; i < 4; i++) {
        // float x2 = (x + w / 2 * mult[i][0]);
        // float y2 = (y + h / 2 * mult[i][1]);
        // xCoords[i] = (int) ((x2 - x) * Math.cos(angle) - (y2 - y) * Math.sin(angle) +
        // x);
        // yCoords[i] = (int) ((x2 - x) * Math.sin(angle) + (y2 - y) * Math.cos(angle) +
        // y);
        // }
        // g.drawRect((int) (v.x - w / 2), (int) (v.y - h / 2), (int) w, (int) h);
        // g.drawPolygon(xCoords, yCoords, 4);

        // WORKING VERSION
        // double locationX = bImage.getWidth(frame) / 2;
        // double locationY = bImage.getHeight(frame) / 2;
        // AffineTransform tx = AffineTransform.getRotateInstance(angle, locationX,
        // locationY);
        // AffineTransformOp op = new AffineTransformOp(tx,
        // AffineTransformOp.TYPE_BILINEAR);

        // g.drawImage(op.filter((BufferedImage) bImage, null), (int) (x - w/2), (int)
        // (y - h/2),
        // frame);
        // END OF WORKING VERSION

        // BACKUP
        // int w2 = bImage.getWidth();
        // int h2 = bImage.getHeight();

        // BufferedImage rotated = new BufferedImage(w2*2, h2*2, bImage.getType());
        // Graphics2D graphic = rotated.createGraphics();
        // graphic.rotate(angle, w2 / 2, h2 / 2);
        // graphic.drawImage(bImage, null, 0, 0);
        // graphic.dispose();
        // g.drawImage(rotated, (int) (x - w2/2), (int) (y - h2/2), frame);
        // END OF BACKUP

        int w2 = bImage.getWidth();
        int h2 = bImage.getHeight();

        BufferedImage rotated = new BufferedImage(w2 * 2, h2 * 2, bImage.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(angle, w2, h2);
        graphic.setColor(Color.BLUE);
        graphic.drawImage(bImage, null, w2 / 2, h2 / 2);
        graphic.dispose();
        g.drawImage(rotated, Math.round(x - w2), Math.round(y - h2), frame);

        // img is a BufferedImage instance
        // g.drawImage(texture, tr, frame);

        // g.drawImage(op.filter(texture, null), (int) x, (int) y, frame);

        // g.drawImage(bImage, (int) x, (int) y, frame);

    }

}
