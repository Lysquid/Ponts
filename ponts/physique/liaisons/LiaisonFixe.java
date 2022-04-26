package ponts.physique.liaisons;

import java.awt.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

/**
 * Classe d'une liaison fixe (en rouge) ne pouvant pas bouger
 */
public class LiaisonFixe extends Liaison {

    /**
     * Constructeur d'une liaison fixe
     * 
     * @param world
     * @param pos
     */
    public LiaisonFixe(World world, Vec2 pos) {
        super(world, pos);

        couleurRemplissage = Color.decode("#d33d3d");
        apercu = false;
    }

    @Override
    public void activerPhysique() {
    }

    @Override
    protected void creerObjetPhysique(World world) {
        creerObjetPhysique(world, BodyType.STATIC);

    }
}
