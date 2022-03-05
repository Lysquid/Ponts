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

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class liaison extends ObjetPhysique {

    float rayon;

    public liaison(World world, float x, float y, float rayon){
        
        this.rayon = rayon;

        //Créer une "body definition"
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(x,y);
        
        //Créer le body
        body = world.createBody(bd);
        CircleShape shape = new CircleShape();
        shape.setRadius(this.rayon);

        body.createFixture(shape, 1);
    }

    public void draw(Graphics g, Box2D box2d){
        
        g.setColor(Color.CYAN);

        float x = getX();
        float y = getY();

        int Xcentre = box2d.worldToPixelX(x);
        int Ycentre = box2d.worldToPixelX(y);
        int r = box2d.worldToPixelX(this.rayon);

        g.drawOval(Xcentre, Ycentre, r, r);

    }

    
}
