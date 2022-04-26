package ponts.physique.liaisons;

import java.awt.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

/**
 * Classe d'une liaison mobile (en jaune)
 */
public class LiaisonMobile extends Liaison {

    /**
     * Constructeur d'une liaison mobile
     * 
     * @param world
     * @param pos
     */
    public LiaisonMobile(World world, Vec2 pos) {
        super(world, pos);

        couleurRemplissage = Color.decode("#e3f069");
        apercu = true;
    }

    /**
     * Supprime une liaison mobile
     * 
     * @param world
     */
    public void supprimer(World world) {
        world.destroyBody(body);
    }

    @Override
    public void activerPhysique() {
        body.setType(BodyType.DYNAMIC);
        apercu = false;
    }

    @Override
    protected void creerObjetPhysique(World world) {
        creerObjetPhysique(world, BodyType.KINEMATIC);

    }

}
