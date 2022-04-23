package ponts.physique.liaisons;

import java.awt.Color;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

public class LiaisonFixe extends Liaison {

    public LiaisonFixe(World world, Vec2 pos) {
        super(world, pos);

        couleurRemplissage = Color.decode("#d33d3d");
        apercu = false;
    }

    public void activerPhysique() {
    }

    @Override
    public void creerObjetPhysique(World world) {
        creerObjetPhysique(world, BodyType.STATIC);

    }
}
