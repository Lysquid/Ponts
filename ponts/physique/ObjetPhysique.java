package ponts.physique;

import java.awt.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 * Classe abstraite définissant un objet soumis à la physique
 */
public abstract class ObjetPhysique {

    protected Body body;

    public static final float FRICTION = 0.8f; // Coefficient de frottement
    public static final float ELASTICITE = 0.3f; // Determine si l'objet rebondi
    public static final float DENSITE = 1f; // Densitée de l'objet

    /**
     * Méthode à appelée du constructeur, séparant la création de
     * l'objet physique
     * 
     * @param world
     */
    protected abstract void creerObjetPhysique(World world);

    public Vec2 getPos() {
        return body.getTransform().p;
    }

    public float getX() {
        return body.getTransform().p.x;
    }

    public float getY() {
        return body.getTransform().p.y;
    }

    public float getAngle() {
        return body.getAngle();
    }

    public void setPos(Vec2 pos, float angle) {
        body.setTransform(pos.clone(), angle);
    }

    public void setPos(Vec2 pos) {
        setPos(pos, getAngle());
    }

    public Body getBody() {
        return body;
    }

    /**
     * Modifie la transparence d'une couleur
     * 
     * @param color
     * @param alpha
     * @return
     */
    public static Color setColorAlpha(Color color, int alpha) {
        if (color.getAlpha() == alpha) {
            return color;
        } else {
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        }
    }

    /**
     * Créer un objet fixtureDef et définie ses paramètres
     * 
     * @param friction
     * @param elasticite
     * @param densite
     * @param category
     * @param mask
     * @return fixtureDef
     */
    public FixtureDef creerFixtureDef(float friction, float elasticite, float densite, int category,
            int mask) {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.friction = friction;
        fixtureDef.restitution = elasticite;
        fixtureDef.density = densite;
        fixtureDef.filter.categoryBits = category;
        fixtureDef.filter.maskBits = mask;
        return fixtureDef;
    }

}
