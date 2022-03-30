package ponts.physique.voiture;

import java.awt.Color;
import java.awt.Graphics;

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

    Color couleurContour = Color.BLACK;
    Color couleurRemplissage = Color.RED;

    PolygonShape shape;
    float longueur = 8f;
    float largeur = 3f;

    public Carrosserie(World world, Vec2 pos) {

        creerObjetPhysique(world);
        setPos(pos);

    }

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

        g.setColor(couleurRemplissage);
        g.fillPolygon(xCoins, yCoins, 4);
        g.setColor(couleurContour);
        g.drawPolygon(xCoins, yCoins, 4);

    }

}
