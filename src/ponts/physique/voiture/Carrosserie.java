package ponts.physique.voiture;

import java.util.Locale.Category;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.PolygonAndCircleContact;
import java.awt.Graphics;
import java.awt.Color;

import ponts.ihm.Box2D;
import ponts.physique.ObjetPhysique;
import ponts.physique.barres.Barre;
import ponts.physique.barres.BarreBois;
import ponts.physique.environnement.Bord;

public class Carrosserie extends ObjetPhysique {
    public static final int CATEGORY = 0b1000;
    public static final int MASK = Bord.CATEGORY | BarreBois.CATEGORY;

    FixtureDef fixtureDef;
    Fixture fixture;
    PolygonShape shape;
    Color couleurContour = Color.GREEN;
    Color couleurRemplissage = Color.GREEN;
    float longueur = 8;
    float largeur = 3;

    public Carrosserie(World world, Vec2 pos ) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(pos);

        body = world.createBody(bodyDef);
        shape = new PolygonShape();

        fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;

        shape.setAsBox(longueur/2, largeur/2);
        setPos(pos);
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);

        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

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
        
        g.setColor(couleurRemplissage);
        g.fillPolygon(xCoins, yCoins, 4);
        g.setColor(couleurContour);
        g.drawPolygon(xCoins, yCoins, 4);


    }

}
