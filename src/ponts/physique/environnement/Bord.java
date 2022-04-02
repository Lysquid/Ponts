package ponts.physique.environnement;

import java.awt.Color;

import java.awt.Graphics2D;
import java.util.LinkedList;

import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import ponts.ihm.Box2D;
import ponts.niveau.Niveau;
import ponts.physique.ObjetPhysique;

public class Bord {

    public static final int CATEGORY = 0b0001;
    public static final int MASK = 0b1111;

    Color couleur_remplissage = Color.decode("#49a03f");
    Color couleur_contour = Color.BLACK;

    LinkedList<Vec2> posCoins;

    public Bord(World world, float frameX, float frameY) {

        posCoins = new LinkedList<>();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position.set(frameX / 2, 0);

        Body body = world.createBody(bodyDef);

        Vec2 gauche = new Vec2(-frameX * 9, 0);
        Vec2 droite = new Vec2(frameX * 10, 0);
        EdgeShape shape = new EdgeShape();
        shape.set(gauche, droite);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

        body.createFixture(fixtureDef);

    }

    public Bord(World world, Niveau niveau) {

        posCoins = niveau.getPosCoins();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.filter.categoryBits = CATEGORY;
        fixtureDef.filter.maskBits = MASK;

        Vec2 prevPosCoin = null;
        for (Vec2 posCoin : posCoins) {

            if (prevPosCoin != null) {
                Body body = world.createBody(bodyDef);
                EdgeShape shape = new EdgeShape();
                shape.set(prevPosCoin, posCoin);
                fixtureDef.shape = shape;
                body.createFixture(fixtureDef);

            }
            prevPosCoin = posCoin;

        }

    }

    public void dessiner(Graphics2D g, Box2D box2d) {

        int n = posCoins.size();
        int[] xCoins = new int[n];
        int[] yCoins = new int[n];

        int i = 0;
        for (Vec2 posCoin : posCoins) {
            xCoins[i] = box2d.worldToPixelX(posCoin.x);
            ;
            yCoins[i] = box2d.worldToPixelY(posCoin.y);
            i++;
        }

        g.setColor(couleur_remplissage);
        g.fillPolygon(xCoins, yCoins, n);
        g.setColor(couleur_contour);
        g.drawPolygon(xCoins, yCoins, n);

    }
}